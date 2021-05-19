package com.learn.log.kafka;

import com.alibaba.fastjson.JSONObject;
import com.learn.log.constant.LogConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.skywalking.apm.toolkit.trace.KafkaToolkit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.skywalking.apm.toolkit.trace.KafkaTransferData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/13 18:03
 */
@Aspect
public class KafkaListenerLogAspect {

    private static final String EXECUTION = "@annotation(com.lianjia.lease.log.annotation.KafkaListenerLogAnnotation)";
    private static final String KAFKA_RECEIVED_TEMPLATE = "Kafka message key:{0} and content:{1} from topic:{2}.";
    private static final String KAFKA_RESULT_TEMPLATE = "Kafka message cost:{0}ms, error:{1}.";
    public static ThreadLocal<String> title = new ThreadLocal<>();
    private static Logger log = LoggerFactory.getLogger(LogConstants.MESSAGE_LOG_PACKAGE);


    /**
     * @Around
     */
    @Around(EXECUTION)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        boolean isKafkaListener = true;//targetMethod.getAnnotation(KafkaListener.class) != null;
        if (isKafkaListener) {
            KafkaListenerLogAnnotation annotation = targetMethod.getAnnotation(KafkaListenerLogAnnotation.class);
            String[] onlyAcceptKeys = {};
            if (annotation != null && ArrayUtils.isNotEmpty(annotation.onlyAcceptKeys())) {
                onlyAcceptKeys = annotation.onlyAcceptKeys();
            }
            return enhance(joinPoint, onlyAcceptKeys);
        } else {
            return executeWithErrorMsg(joinPoint);
        }
    }

    private Object executeWithErrorMsg(ProceedingJoinPoint joinPoint) throws Throwable {
        log.error("KafkaListenerLogAnnotation MUST be used With KafkaListener Annotation. ");
        return joinPoint.proceed();
    }

    private Object enhance(ProceedingJoinPoint joinPoint, String[] onlyAcceptKeys) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Object firstArg = args.length > 0 ? args[0] : null;

        if (firstArg == null) {
            firstArg = "";
        }

        String key = "unknown";
        String content = String.valueOf(firstArg);
        String topic = "消息建议使用ConsumerRecord参数.";
        ConsumerRecord<?, ?> record = null;

        if (firstArg instanceof ConsumerRecord) {
            record = (ConsumerRecord<?, ?>) firstArg;
            key = record.key() != null ? record.key().toString() : null;
            content = record.value() != null ? record.value().toString() : null;
            topic = record.topic();
        }

        boolean isRecordLog = isRecordLog(record, onlyAcceptKeys);

        title.set("kafka消息, " + MessageFormat.format(KAFKA_RECEIVED_TEMPLATE, key, content, topic));

        Long start = System.currentTimeMillis();
        Exception proceedException = null;
        try {
            KafkaToolkit.beginEntrySpan(new KafkaTransferData(getTraceId(content)));
            if (isRecordLog) {
                log.info(MessageFormat.format(KAFKA_RECEIVED_TEMPLATE, key, content, topic));
            }
            return joinPoint.proceed();
        } catch (Exception e) {
            KafkaToolkit.recordException(e);
            proceedException = e;
            throw e;
        } finally {
            Long end = System.currentTimeMillis();
            if (proceedException != null) {
                log.error(MessageFormat.format(KAFKA_RESULT_TEMPLATE, end - start,
                        ExceptionUtils.getStackTrace(proceedException)));
            } else {
                if (isRecordLog) {
                    log.info(MessageFormat.format(KAFKA_RESULT_TEMPLATE, end - start, null));
                }
            }
            title.remove();
            KafkaToolkit.afterEntrySpan();
        }
    }

    private boolean isRecordLog(ConsumerRecord<?, ?> record, String[] onlyAcceptKeys) {
        boolean isRecordLog = true;
        if (record != null && record.key() != null && onlyAcceptKeys != null && onlyAcceptKeys.length > 0
                && !ArrayUtils.contains(onlyAcceptKeys, record.key())) {
            isRecordLog = false;
        }
        if (isRecordLog) {
            try {
//                isRecordLog = moduleConfig.isListenerLog();
            } catch (Exception e) {
                // 不会造成问题，因此本处记录INFO级别日志，忽略处理
                log.info("消息切面处理时，获取yml中listenerLog属性出现错误,忽略处理", e);
            }
        }
        return isRecordLog;
    }

    private String getTraceId(String content) {
        String traceId = null;
        if (StringUtils.isNotBlank(content) && content.contains("trace-id")) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(content);
                traceId = jsonObject.getString("trace-id");
            } catch (Exception e) {
                // 不会造成问题，因此本处记录INFO级别日志，并吞掉异常
                log.info("消息切面处理时，收到非json格式信息，忽略处理,消息内容");
            }
        }
        return traceId;
    }

}

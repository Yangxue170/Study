package com.learn.log.kafka;

import java.lang.annotation.*;

/**
 * @author Kafka Listener日志
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface KafkaListenerLogAnnotation {

    /**
     * 配置仅接受的key列表，非指定key消息不会记录日志
     */
    String[] onlyAcceptKeys() default {};
}

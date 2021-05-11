package com.learn.log.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.learn.log.constant.LogConstants;
import com.learn.log.util.ClassUtils;
import org.apache.dubbo.common.extension.Activate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

@Activate(group = {Constants.CONSUMER, Constants.PROVIDER})
public class DubboLoggerFilter implements Filter {
    public static final String TIMEOUT = Constants.TIMEOUT_KEY;
    public static ThreadLocal<String> title = new ThreadLocal<>();
    private final Logger logger = LoggerFactory.getLogger(LogConstants.DUBBO_LOG_PACKAGE);


    /**
     * 是否dubbo超时异常
     *
     * @param e 异常
     * @return 是否
     */
    public static boolean isDubboTimeoutException(Throwable e) {
        if (e != null) {
            if (e instanceof RpcException) {
                //是rpc的异常
                RpcException rpc = (RpcException) e;
                if (rpc.isTimeout()) {
                    //返回超时，超时时间的设置
                    return true;
                }
            }
            if (e instanceof TimeoutException) {
                //超时异常
                return true;
            }
        }
        return false;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();
        String role = context.isProviderSide() ? Constants.PROVIDER : Constants.CONSUMER;
        String argumentJson = toJsonString(invocation.getArguments());
        URL url = context.getUrl();
        String interfaceName = null;
        if (url != null) {
            interfaceName = ClassUtils.getAcronymPackage(url.getServiceInterface());
        }
        String methodName = context.getMethodName();
        String remote = context.getRemoteAddressString();
        Result.CompatibleResult result = null;
        Object resultValue = null;
        Throwable resultException = null;
        long start = System.currentTimeMillis();
        boolean hasException = false;
        title.set(String.format("dubbo接口, remote:{}, interface:{}, method:{}",
                remote, interfaceName, methodName));

        if (logger.isDebugEnabled()) {
            logger.debug("[dubbo-{}] remote:{}, interface:{}, method:{}, param:{}",
                    role, remote, interfaceName, methodName, argumentJson);
        }

        /*
         * AccessUtils.addDubboVisit(interfaceName, methodName, role)
         */

        try {
            //执行具体的方法
            result = (Result.CompatibleResult) invoker.invoke(invocation);
        } catch (Exception e) {
            hasException = true;
            resultException = e;
            throw e;
        } finally {
            if (result != null) {
                //接口执行完毕，返回值不为空
                resultValue = result.getValue();
                resultException = result.getException();
                hasException = result.hasException();
            }
            //方法执行完毕，获取系统时间作为结束时间
            long cost = System.currentTimeMillis() - start;
            if (hasException) {
                //过程中出现异常情况
                if (isDubboTimeoutException(resultException)) {
                    //接口调用超时
                    logger.error("[dubbo-{}-{}], remote:{}, interface:{}, method:{}, param:{}, cost:{}ms",
                            role, TIMEOUT, remote, interfaceName, methodName, argumentJson, cost);
                } else {
                    //其余异常场景打印warn的log
                    String errorMsg = String.format(
                            "[dubbo-%s-%s], remote:%s, interface:%s, method:%s, param:%s, cost:%sms",
                            role, "error", remote, interfaceName, methodName, argumentJson, cost);
                    logger.warn(errorMsg, resultException);
                }
            } else {
                if (logger.isDebugEnabled()) {
                    //级别为debug的数据打印debug级别日志
                    logger.debug("[dubbo-{}] remote:{}, interface:{}, method:{}, result:{}, cost:{}ms",
                            role, remote, interfaceName, methodName, toJsonString(resultValue), cost);
                } else {
                    //无异常、非debug认为是info类型日志
                    logger.info("[dubbo-{}] remote:{}, interface:{}, method:{}, param:{}, cost:{}ms",
                            role, remote, interfaceName, methodName, argumentJson, cost);
                }
            }
            title.remove();
        }
        return result;
    }

    private String toJsonString(Object o) {
        try {
            return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
        } catch (Exception e) {
            logger.warn("trans object to json error");
            if (o == null) {
                return "null";
            }
            return o.toString();
        }
    }


    /**
     * 截取日志中接口信息
     *
     * @param errorMsg 日志
     * @return 接口
     */
    public static String getInterfaceByErrorMsg(String errorMsg) {
        try {
            String[] strs = StringUtils.split(errorMsg, " ");
            assert strs != null;
            return (strs[2].split(":")[1] + "." + strs[3].split(":")[1]).replaceAll(",", "");
        } catch (Exception e) {
            return LogConstants.DUBBO_LOG_PACKAGE;
        }
    }
}

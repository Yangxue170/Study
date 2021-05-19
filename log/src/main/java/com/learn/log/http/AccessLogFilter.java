package com.learn.log.http;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.learn.log.constant.LogConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jdx
 * @version 1.0
 * @description http请求的本系统
 * @date 2021/5/11 12:04
 */

public class AccessLogFilter implements Filter {
    private int warnTimeout = 500;
    private int errorTimeout = 1000;

    public static ThreadLocal<String> title = new ThreadLocal<>();
    private static Logger log = LoggerFactory.getLogger(LogConstants.HTTP_LOG_PACKAGE);
    // 过滤header日志中的key
    private Map<String, String> filterHeaderMap = Maps.newHashMap();


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Web Filter[AccessLogFilter] is Initialised");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // 可重复读取request，解决流读取只能读取一次的问题。避免因为filter处理导致后面的请求中request为空。
        HttpServletRequest request;
        try {
            if (!(req instanceof RepeatedlyReadRequestWrapper) && req.getDispatcherType() == DispatcherType.REQUEST) {
                //不是 请求/可重复读
                request = new RepeatedlyReadRequestWrapper((HttpServletRequest) req);
            } else {
                request = (HttpServletRequest) req;
            }
        } catch (Exception e) {
            log.warn("转化RepeatedlyReadRequestWrapper异常", e);
            request = (HttpServletRequest) req;
        }
        HttpServletResponse response = (HttpServletResponse) resp;

        Long startTime = System.currentTimeMillis();
        startLog(request, response);
        //执行 过滤器链
        chain.doFilter(request, response);
        endLog(request, response, startTime);
    }

    @Override
    public void destroy() {

    }

    private void startLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求的URL
        String url = request.getRequestURL().toString();
        //组装请求头
        String header = getHeaderJson(request);
        //组装参数
        String params = getParamJson(request);
        //设置参数信息
        title.set("http接口请求, url:" + url + ", param:" + params + ", header:" + header);
        //打印请求
        log.info("[http] url:{}, param:{}, method:{}, header:{}, remote:{}",
                url, params, request.getMethod(), header, request.getRemoteAddr());
    }

    private void endLog(HttpServletRequest request, HttpServletResponse response, Long startTime) throws IOException {
        title.remove();
        String url = request.getRequestURL().toString();
        long cost = System.currentTimeMillis() - startTime;

        if (cost <= warnTimeout) {
            log.info("[http] url:{}, cost:{}ms", url, cost);
        } else if (cost > warnTimeout && cost <= errorTimeout) {
            log.warn("[http-timeout] url:{}, cost:{}ms", url, cost);
        } else if (cost > errorTimeout) {
            log.error("[http-timeout] url:{}, cost:{}ms", url, cost);
        } else {
            log.info("[http] url:{}, cost:{}ms", url, cost);
        }
    }

    private String getHeaderJson(HttpServletRequest request) {
        //是否开启了debug模式
        boolean isDebug = log.isDebugEnabled();
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (isDebug) {
                map.put(name, request.getHeader(name));
            } else {
                if (!filterHeaderMap.containsKey(name)) {
                    map.put(name, request.getHeader(name));
                }
            }
        }
        return JSON.toJSONString(map);
    }

    private String getParamJson(HttpServletRequest request) throws IOException {
        StringBuilder param = new StringBuilder();
        if (isJsonParam(request)) {
            ServletInputStream inputStream = request.getInputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                param.append(new String(b, 0, len));
            }
            // 压缩输出json参数
            try {
                String str = param.toString();
                Object obj = JSON.parse(str);
                return JSON.toJSONString(obj);
            } catch (Exception e) {
                return StringUtils.EMPTY;
            }
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            return JSON.toJSONString(parameterMap);
        }
    }

    private boolean isJsonParam(HttpServletRequest request) {
        return StringUtils.equalsIgnoreCase("POST", request.getMethod())
                && StringUtils.containsIgnoreCase(
                request.getHeader(HttpHeaders.CONTENT_TYPE), MediaType.APPLICATION_JSON.toString());
    }

    /**
     * 构造
     *
     * @param warnTimeout  警告日志时间
     * @param errorTimeout 报警日志时间
     */
    public AccessLogFilter(int warnTimeout, int errorTimeout) {
        super();
        if (warnTimeout > errorTimeout) {
            throw new RuntimeException(String.format("warnTimeout:[%s] must be greater than errorTimeout:[%s]",
                    warnTimeout, errorTimeout));
        }
        this.warnTimeout = warnTimeout;
        this.errorTimeout = errorTimeout;
    }

}

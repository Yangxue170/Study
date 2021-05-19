package com.learn.log;

import com.learn.log.http.AccessLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/12 18:34
 */
@Configuration
public class LogConfig {
    @Bean
    public FilterRegistrationBean setAccessLogFilter() {
        // 设置filter，重写RepeatedlyReadRequestWrapper，支持重复读取request.getInputStream
        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new AccessLogFilter(100, 500));
        filterBean.setName("AccessLogFilter");
        filterBean.addUrlPatterns("/*");
        return filterBean;
    }
}

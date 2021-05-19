package com.learn.log.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/11 11:13
 */
public class Provider {
    public static void main(String[] args) throws IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] {"META-INF/spring/spring-dubbo-provider.xml"});
        context.start();
        System.in.read();
    }
}

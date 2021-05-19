package com.learn.log.dubbo.provider;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/11 11:12
 */
public class DemoServiceImpl implements DemoService{
    @Override
    public String sayHello(String name) {
        return "hello"+name;
    }
}
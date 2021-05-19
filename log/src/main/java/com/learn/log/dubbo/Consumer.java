package com.learn.log.dubbo;

import com.learn.log.dubbo.provider.DemoService;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/11 11:27
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"META-INF/spring/spring-dubbo-consumer.xml"});
        context.start();
        // 获取远程服务代理
        DemoService demoService = (DemoService) context.getBean("demoService");
        // 执行远程方法
        String st = demoService.sayHello(" world");
        System.out.println(st);

        throw new RpcException();
        // 显示调用结果
    }
}

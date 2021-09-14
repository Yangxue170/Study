package com.example.demo.thread.executor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/28 16:28
 */
public class MyRejected implements RejectedExecutionHandler {
    public MyRejected(){
    }


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义处理..");
        System.out.println("当前被拒绝任务为：" + r.toString());
    }
}

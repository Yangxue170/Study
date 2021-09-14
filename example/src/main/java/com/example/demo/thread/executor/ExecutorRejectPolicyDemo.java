package com.example.demo.thread.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @author Jdx
 * @version 1.0
 * @description 拒绝策略
 * @date 2021/6/28 17:15
 */
public class ExecutorRejectPolicyDemo {


    private final static Logger logger = LoggerFactory.getLogger(ExecutorRejectPolicyDemo.class);

    /**
     * 基于数组的阻塞队列
     */
    private static final BlockingQueue blockingQueue = new ArrayBlockingQueue(3);
    /**
     * 线程工厂，用于命名线程名称
     */
    private static final CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("my-self-thread");
    /**
     * 无可用线程且队列已满，拒绝策略
     * 自定义拒绝策略，线程任务队列满了的情况下，任务等待入线程队列
     */
    private static final RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException ignored) {
            logger.error("wait input queue error");
        }
    };

    /**
     * 同时运行线程不超过150个，核心线程数150个
     * 最大线程数和核心线程数设置为一样时，设置的时间和时间单位不生效
     * 自定义拒绝策略，线程任务队列满了的情况下，任务等待入线程队列
     * 最大线程数比核心线程数大时，若有空余线程空闲超过设置的时常会被回收
     */
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            1,
            1,
            1,
            TimeUnit.MINUTES,
            blockingQueue,
            threadFactory,
            rejectedExecutionHandler);

    public void addTask(Integer i) {
        //线程池执行
        THREAD_POOL_EXECUTOR.execute(() -> {
            logger.info("当前i的值：" + i);
            logger.info("当前执行任务的线程名字：" + Thread.currentThread().getName());
        });
    }

    public static void main(String[] args) {
        ExecutorRejectPolicyDemo threadPool = new ExecutorRejectPolicyDemo();
        for (int i = 0; i < 10; i++) {
            threadPool.addTask(i);
        }
        //线程池中线程
        THREAD_POOL_EXECUTOR.shutdown();
    }

}


/*
1、线程池提供的拒绝策略：
    ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。: 线程池的默认拒绝策略
    ThreadPoolExecutor.DiscardPolicy：丢弃任务，但是不抛出异常。
    ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务
    ThreadPoolExecutor.CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务

2、自定义拒绝策略
    java.util.concurrent.RejectedExecutionHandler.rejectedExecution(Runnable r, ThreadPoolExecutor executor)
 */
package com.example.demo.thread.executor.self;

import com.example.demo.thread.executor.self.ExecutorDemo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/24 18:48
 */
public class FlashExecutorBo implements ExecutorDemo {
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数 = 核心线程数+非核心线程数 之和最大值
     */
    private int maximumPoolSize;
    /**
     * 非核心线程数的空闲时间
     */
    private long keepAliveTime;
    /**
     * 空闲时间的单位
     */
    private TimeUnit unit;
    /**
     * 任务队列：线程安全的阻塞队列
     */
    private BlockingQueue<Runnable> workQueue;
    /**
     * 线程工厂
     */
    private ThreadFactory threadFactory;
    /**
     * 拒绝策略
     */
    private RejectedExecutionHandler handler;


    public FlashExecutorBo(
            int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
        this.unit = unit;
    }

    /**
     * 第一步：每次调用都会创建新的线程，当调用10000次后，
     * 会产生10000个线程，造成浪费
     *
     * @param r
     */
    @Override
    public void execute(Runnable r) {
        new Thread(r).start();
    }

    /**
     * 第二步：进行控制线程的数量，构造一个队列
     * 把这个任务 r 丢到一个 tasks 队列中，然后只启动一个线程，
     * 就叫它 Worker 线程吧，
     * 不断从 tasks 队列中取任务，执行任务。
     * 这样无论调用者调用多少次，永远就只有一个 Worker 线程在运行
     */


    /**
     * 第三步：
     * 1、只有一个后台的工作线程 Worker 会不会少了点
     * 2、如果这个 tasks 队列满了怎么办
     */

}

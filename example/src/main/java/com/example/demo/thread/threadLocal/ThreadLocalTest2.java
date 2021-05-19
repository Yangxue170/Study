package com.example.demo.thread.threadLocal;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/18 20:18
 */
public class ThreadLocalTest2 {
    private static class MyRunnable implements Runnable {

        private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        @Override
        public void run() {
            threadLocal.set((int) (Math.random() * 100D));
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }
    }


    public static void main(String[] args) {
        //线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        MyRunnable one = new MyRunnable();
        MyRunnable two = new MyRunnable();
        singleThreadPool.execute(one);
        singleThreadPool.execute(two);
        singleThreadPool.shutdown();
    }
    /**
     * A:95
     * B:70
     *
     * 线程A与线程B中ThreadLocal保存的整型变量是各自独立的，互不相干，
     * 只要在每个线程内部使用set方法赋值，然后在线程内部使用get就能取到对应的值。
     *
     */
}
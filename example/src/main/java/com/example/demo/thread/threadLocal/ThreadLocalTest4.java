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
 * @date 2021/5/18 20:27
 */
public class ThreadLocalTest4 {
    private static final InheritableThreadLocal<Integer> threadLocal = new InheritableThreadLocal<Integer>();

    public static class MyRunnable implements Runnable {

        private String _name;

        public MyRunnable(String name) {
            _name = name;
            System.out.println(name + " => " + Thread.currentThread().getName() + ":" + threadLocal.get());
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }
    }


    public static void main(String[] args) {
        threadLocal.set(1);
        System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());

        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        MyRunnable one = new MyRunnable("R-A");
        MyRunnable two = new MyRunnable("R-B");
        singleThreadPool.execute(one);
        singleThreadPool.execute(two);
        singleThreadPool.shutdown();
    }
    /**
     * main:1
     * R-A => main:1
     * R-B => main:1
     * A:1
     * B:1
     *
     *
     * ThreadLocal还有一个派生的子类：
     * InheritableThreadLocal ，可以允许线程及该线程创建的子线程均可以访问同一个变量(有些OOP中的proteced的意味)
     *
     */
}

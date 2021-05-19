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
 * @date 2021/5/18 20:19
 */
public class ThreadLocalTest3 {
    public static class MyRunnable implements Runnable {

        private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        public MyRunnable() {
            threadLocal.set((int) (Math.random() * 100D));
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
        }
    }

    /**
     * 最外层是main主线程
     *
     * @param args
     */
    public static void main(String[] args) {
        //线程池
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        MyRunnable one = new MyRunnable();
        MyRunnable two = new  MyRunnable();
        singleThreadPool.execute(one);
        singleThreadPool.execute(two);
        singleThreadPool.shutdown();
    }
}
/*输出：
 * main:31
 * main:87
 * A:null
 * B:null
 *
 * 原因：MyRunnable的方法是在主线程中进行调用的，所以thread local只会在主线程中生效，
 * t1/t2是新创建的线程，因此其threadLocal数据为空
 *
 */

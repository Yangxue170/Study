package com.example.demo.thread.executor;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

/**
 * @author Jdx
 * @version 1.0
 * @description Executors提供四种线程池
 * @date 2021/6/28 17:49
 */
public class ExecutorsThreadPoolDemo {
    /**
     * 线程工厂，用于命名线程名称
     */
    private static final CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("my-self-thread");

    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
//                60L, TimeUnit.SECONDS,
//                new SynchronousQueue<>(),
//                threadFactory);
        for (int i = 0; i < 1000; i++) {
            final int index = i;
            try {
                Thread.sleep(index * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + ":" + index);
                }
            });
        }
        cachedThreadPool.shutdown();
    }

    /**
     * newCachedThreadPool
     * 实现
     * new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     * 60L, TimeUnit.SECONDS,
     * new SynchronousQueue<Runnable>())
     * 无核心线程数，最大线程数MAX_VALUE，存活时间60s ，队列SynchronousQueue，默认拒绝策略 丢弃
     */

    public static void fixedThreadPool() {
        // 每隔两秒打印3个数
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + ":" + index);
                        //三个线程并发
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * newFixedThreadPool
     * 实现
     * new ThreadPoolExecutor(nThreads, nThreads,
     * 0L, TimeUnit.MILLISECONDS,
     * new LinkedBlockingQueue<Runnable>())
     * 设置nThreads的个数，核心线程数与最大线程数一致，时间设置不生效。  使用 LinkedBlockingQueue队列
     * 队列满之后进行等待处理
     */

    public static void scheduledThreadPool() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ": delay 1 seconds, and excute every 3 seconds");
            }
            // 表示延迟1秒后每3秒执行一次
        }, 1, 3, TimeUnit.SECONDS);
    }

    /**
     * newScheduledThreadPool
     * 创建一个定长线程池，支持定时及周期性任务执行。延迟执行示例代码如下.表示延迟1秒后每3秒执行一次
     * new ThreadPoolExecutor(corePoolSize, Integer.MAX_VALUE, 0, NANOSECONDS,
     * new DelayedWorkQueue())
     */


    public static void singleThreadExecutor() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(Thread.currentThread().getName() + ":" + index);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
/**
 * newSingleThreadExecutor
 * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，
 * 保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
 * new ThreadPoolExecutor(1, 1,
 *                                     0L, TimeUnit.MILLISECONDS,
 *                                     new LinkedBlockingQueue<Runnable>())
 */
}

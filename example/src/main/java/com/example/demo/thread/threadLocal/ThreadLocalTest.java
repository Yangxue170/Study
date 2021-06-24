package com.example.demo.thread.threadLocal;

import java.util.stream.IntStream;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/18 18:50
 */
public class ThreadLocalTest {

    private ThreadLocal<Integer> localInt = new ThreadLocal<>();

    /**
     * 进行设置
     *
     * @return
     */
    public int setAndGet() {
        localInt.set(8);
        return localInt.get();
    }

    public static void main(String[] args) {
        //本线程自己使用的
        ThreadLocal threadLocal = new ThreadLocal();
        //父子间共用的threadLocal
//        InheritableThreadLocal threadLocal = new InheritableThreadLocal();
        IntStream.range(0, 10).forEach(i -> {
            //每个线程的序列号，希望在子线程中能够拿到
            threadLocal.set(i);
            //这里来了一个子线程，我们希望可以访问上面的threadLocal
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + ":" + threadLocal.get());
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

}

/*
 * 1、问题 - 内存泄漏的风险
 * 当前线程使用完 threadlocal 后，通过调用 ThreadLocal 的 remove 方法进行清除从而降低内存泄漏的风险。
 *
 */
































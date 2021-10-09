package com.example.demo.lock;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/9/14 15:57
 */
public class ReentrantLockTest {

    private final Lock lock = new ReentrantLock();
    private int count;

    public void add(int n) {
        //获取锁
        lock.lock();
        try {
            count += n;
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}


class Counter {
    private int count;

    public void add(int n) {
        // synchronized 关键字
        synchronized (this) {
            count += n;
        }
    }
}

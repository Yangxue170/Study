package com.example.demo.thread;

import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/17 18:35
 */
public class ARunnable {
    static final int COUNT = 20;

    public static void main(String[] args) throws Exception {
        new Thread(new Teacher(cdl)).start();
        sleep(1);
        for (int i = 0; i < COUNT; i++) {
            new Thread(new Student(i, cdl)).start();
        }
        synchronized (ThreadCo1.class) {
            ThreadCo1.class.wait();
        }
    }

    static CountDownLatch cdl = new CountDownLatch(COUNT);

    static class Teacher implements Runnable {

        CountDownLatch cdl;

        Teacher(CountDownLatch cdl) {
            this.cdl = cdl;
        }

        @Override
        public void run() {
            System.out.println("老师发卷子。。。");
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("老师收卷子。。。");
        }

    }

    static class Student implements Runnable {

        CountDownLatch cdl;
        int num;

        Student(int num, CountDownLatch cdl) {
            this.num = num;
            this.cdl = cdl;
        }

        @Override
        public void run() {

            System.out.println("学生(%d)写卷子。。。" + num);
            doingLongTime();
            System.out.println("学生(%d)交卷子。。。" + num);
            cdl.countDown();
        }

        private void doingLongTime() {
        }

    }
}



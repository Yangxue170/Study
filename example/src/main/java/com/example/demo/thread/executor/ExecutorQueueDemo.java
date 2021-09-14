package com.example.demo.thread.executor;

import java.util.concurrent.*;

/**
 * @author Jdx
 * @version 1.0
 * @description 线程池三种队列
 * @date 2021/6/28 16:23
 */
public class ExecutorQueueDemo {
    public static void main(String[] args) {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                //coreSize
                1,
                //MaxSize
                2,
                //60
                60,
                TimeUnit.SECONDS,
                //指定一种队列 （有界队列）
                new ArrayBlockingQueue<>(3)
                //new LinkedBlockingQueue<Runnable>()
                , new MyRejected()
                //, new DiscardOldestPolicy()
        );

        MyTask mt1 = new MyTask(1, "任务1");
        MyTask mt2 = new MyTask(2, "任务2");
        MyTask mt3 = new MyTask(3, "任务3");
        MyTask mt4 = new MyTask(4, "任务4");
        MyTask mt5 = new MyTask(5, "任务5");
        MyTask mt6 = new MyTask(6, "任务6");
        pool.execute(mt1);
        pool.execute(mt2);
        pool.execute(mt3);
        pool.execute(mt4);
        pool.execute(mt5);
        pool.execute(mt6);
        Future<?> submit = pool.submit(mt1);

        pool.shutdown();
    }

    /**
     * SynchronousQueue 队列，超过最大线程池的，直接走拒绝策略，队列长度可以认为是0
     * 自定义处理..
     * run taskId =1   Thread11
     * run taskId =2   Thread12
     * 当前被拒绝任务为：com.example.demo.thread.executor.MyTask@7530d0a
     * 自定义处理..
     * 当前被拒绝任务为：com.example.demo.thread.executor.MyTask@27bc2616
     * 自定义处理..
     * 当前被拒绝任务为：com.example.demo.thread.executor.MyTask@3941a79c
     * 自定义处理..
     * 当前被拒绝任务为：com.example.demo.thread.executor.MyTask@506e1b77
     */

    /**
     * LinkedBlockingQueue 队列，只与核心线程数有关，maximumPoolSizes就相当于无效了
     * 只有一个核心线程处理，放入队列中进行等待处理
     * run taskId =1   Thread11
     * run taskId =2   Thread11
     * run taskId =3   Thread11
     * run taskId =4   Thread11
     * run taskId =5   Thread11
     * run taskId =6   Thread11
     */

    /**
     * ArrayBlockingQueue有界缓存等待队列
     *
     *自定义处理..
     * run taskId =5   Thread12
     * run taskId =1   Thread11
     * 当前被拒绝任务为：com.example.demo.thread.executor.MyTask@312b1dae
     * run taskId =2   Thread12
     * run taskId =3   Thread11
     * run taskId =4   Thread11
     *
     *
     * //执行顺序解读
     * 1.task1，2，3，4，5陆续添加到线程池中之行
     * 2.线程中有一个核心线程,task1立即执行
     * 3.task1执行过程中,2,3,4被放入队列中,但是由于队列容量只有3(ArrayBlockingQueue<Runnable>(3)),task5进入不了队列
     * 4.线程池判断核心线程数<最大线程数,又新开了一个线程,立刻执行线程5
     * 5.线程池判断核心线程数>=最大线程数,且队列已满，则执行拒绝策略
     * 6.task1,5执行完成之后,ThreadPool存在2条线程依次执行任务2，3，4
     */


}

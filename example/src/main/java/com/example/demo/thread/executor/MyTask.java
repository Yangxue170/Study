package com.example.demo.thread.executor;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/28 16:24
 */
public class MyTask implements Runnable {
    private int taskId;
    private String taskName;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public void run() {
        try {
            System.out.println("run taskId =" + this.taskId + "   Thread" + Thread.currentThread().getId());
            Thread.sleep(5 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MyTask(int taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }
}

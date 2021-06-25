package com.example.demo.jvm.chapter01;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/6/18 17:59
 */
public class HelloLoader {
    /**
     * /prepare阶段：a=0 , initial阶段  a=1
     */
    private static int a = 1;

    public static void main(String[] args) {
        System.out.println("Thanks classloader");
        System.out.println("Thanks very much");
    }
}

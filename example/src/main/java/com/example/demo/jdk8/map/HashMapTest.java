package com.example.demo.jdk8.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jdx
 * @version 1.0
 * @description currentHashMap 设计
 * @date 2021/6/15 15:37
 */
public class HashMapTest {


    public static void main(String[] args) {
//        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap(16);
//        concurrentHashMap.put("crg", 2);
//        Integer crg = concurrentHashMap.get("crg");
//        System.out.println(crg);
        HashMapTest test = new HashMapTest();
        test.test();
    }


    private void test() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
        System.out.println(String.class.getClassLoader());
    }


}

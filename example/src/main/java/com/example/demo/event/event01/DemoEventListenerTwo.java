package com.example.demo.event.event01;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/1/14 11:09
 */
public class DemoEventListenerTwo implements DemoListener{
    @Override
    public void doorEvent(DemoEvent event) {
        // TODO Auto-generated method stub
        if (event.getDoorState() != null && event.getDoorState().equals("open")) {
            System.out.println("门2打开，同时打开走廊的灯");
        } else {
            System.out.println("门2关闭，同时关闭走廊的灯");
        }

    }
}

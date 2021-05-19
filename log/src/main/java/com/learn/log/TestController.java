package com.learn.log;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jdx
 * @version 1.0
 * @description
 * @date 2021/5/12 18:42
 */
@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }
}

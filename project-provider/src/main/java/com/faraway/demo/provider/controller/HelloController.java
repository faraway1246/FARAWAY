package com.faraway.demo.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:20
 * @version: 1.0
 * @description:
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello(){
        return "Hello!";
    }
}

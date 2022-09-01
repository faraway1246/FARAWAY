package com.faraway.demo.provider.controller;

import com.faraway.demo.provider.common.JsonResult;
import com.faraway.demo.provider.common.JsonResultUtil;
import com.faraway.demo.provider.common.RabbitMqUtils;
import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: 张峰玮
 * @since: 2022/8/31 21:41
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/deadMessage")
public class DeadMessageController {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "normal_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    @GetMapping("/sendMessage")
    public JsonResult sendMessage() throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        //声明普通和死信交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();
        String msg = "info";
        for (int i = 1; i < 11; i++) {
            String s = msg + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,s.getBytes());
        }
        return JsonResultUtil.ok();
    }
}

package com.faraway.demo.provider.controller;

import com.faraway.demo.provider.common.JsonResult;
import com.faraway.demo.provider.common.JsonResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * @author: 张峰玮
 * @since: 2022/9/1 22:27
 * @version: 1.0
 * @description:
 */
@Slf4j
@RestController
@RequestMapping("/delayQueue")
public class DelayMessageController {

    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息
    @GetMapping("/send/{message}")
    public JsonResult sendMessage(@PathVariable String message){

        log.info("当前时间{},发送一条消息{}到两个ttl队列",new Date().toString(),message);

        rabbitTemplate.convertAndSend("X","XA",message);
        rabbitTemplate.convertAndSend("X","XB",message);
        return JsonResultUtil.ok();
    }

    //发送消息
    @GetMapping("/sendWithTtl/{message}/{ttl}")
    public JsonResult sendMessageWithTTL(@PathVariable String message,@PathVariable String ttl){

        log.info("当前时间{},发送一条消息{}到普通队列",new Date().toString(),message);

        rabbitTemplate.convertAndSend("X","XC",message,msg->{
            msg.getMessageProperties().setExpiration(ttl);
            return msg;
        });
        return JsonResultUtil.ok();
    }

    //发送消息
    @GetMapping("/sendByDelayedExchange/{message}/{ttl}")
    public JsonResult sendByDelayedExchange(@PathVariable String message,@PathVariable Integer ttl){

        log.info("当前时间{},发送一条时长为{}的消息{}到延迟队列",new Date(),ttl,message);

        rabbitTemplate.convertAndSend(DELAYED_EXCHANGE_NAME,DELAYED_ROUTING_KEY,message,msg->{
            msg.getMessageProperties().setDelay(ttl);
            return msg;
        });
        return JsonResultUtil.ok();
    }
}

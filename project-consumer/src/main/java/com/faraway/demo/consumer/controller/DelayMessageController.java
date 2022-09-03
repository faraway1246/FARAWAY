package com.faraway.demo.consumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
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
@Component
public class DelayMessageController {

    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message){
        String msg = new String(message.getBody());

        log.info("当前时间{}，收到延迟队列的消息{}",new Date(),msg);
    }

}

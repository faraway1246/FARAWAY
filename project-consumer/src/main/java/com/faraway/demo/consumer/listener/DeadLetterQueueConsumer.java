package com.faraway.demo.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author: 张峰玮
 * @since: 2022/9/1 23:20
 * @version: 1.0
 * @description: 
 */
@Slf4j
@Configuration
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receivedQD(Message message) throws UnsupportedEncodingException {
        String msg = new String(message.getBody(), "UTF-8");
        log.info("当前时间{},收到消息{}:",new Date().toString(),msg);
    }

}

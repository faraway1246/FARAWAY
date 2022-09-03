package com.faraway.demo.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 张峰玮
 * @since: 2022/9/3 0:41
 * @version: 1.0
 * @description:
 */
@Configuration
public class DelayedQueueConfig {

    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //routingkey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    public Queue dealyedQueue(){
        return QueueBuilder.durable(DELAYED_QUEUE_NAME).build();
    }

    @Bean
    public CustomExchange delayedExchange(){
        Map<String,Object> map = new HashMap<>();
        map.put("x-delayed-type","direct");
        return new CustomExchange(
                DELAYED_EXCHANGE_NAME,
                "x-delayed-message",
                true,false,map);
    }

    @Bean
    public Binding dealyedQueueBindingDelayedExchange(){
        return BindingBuilder.bind(dealyedQueue()).to(delayedExchange()).with(DELAYED_ROUTING_KEY).noargs();
    }

}

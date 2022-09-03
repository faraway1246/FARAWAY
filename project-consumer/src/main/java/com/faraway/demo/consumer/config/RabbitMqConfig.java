package com.faraway.demo.consumer.config;

import org.bouncycastle.pqc.crypto.newhope.NHOtherInfoGenerator;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 张峰玮
 * @since: 2022/9/1 22:41
 * @version: 1.0
 * @description:
 */
@Configuration
public class RabbitMqConfig {

    //普通交换机
    public static final String X_CHANGE = "X";
    //普通交换机
    public static final String Y_DEAD_LETTER_CHANGE = "Y";
    //普通队列
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    //死信队列
    public static final String DEAD_LETTER_QUEUE = "QD";

    @Bean
    public DirectExchange xExchange(){
        return new DirectExchange(X_CHANGE);
    }

    @Bean
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_CHANGE);
    }

    /**
     * 10秒的延迟队列
     * @return
     */
    @Bean
    public Queue ququeA(){
        //设置死信交换机,routingKey,TTL
        Map<String,Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_CHANGE);
        map.put("x-dead-letter-routing-key","YD");
        map.put("x-message-ttl",10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(map).build();
    }

    /**
     * 40秒的延迟队列
     * @return
     */
    @Bean
    public Queue ququeB(){
        //设置死信交换机,routingKey,TTL
        Map<String,Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_CHANGE);
        map.put("x-dead-letter-routing-key","YD");
        map.put("x-message-ttl",40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(map).build();
    }

    /**
     * 不设置具体过期时间的延迟队列
     * @return
     */
    @Bean
    public Queue ququeC(){
        //设置死信交换机,routingKey,TTL
        Map<String,Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange",Y_DEAD_LETTER_CHANGE);
        map.put("x-dead-letter-routing-key","YD");
        return QueueBuilder.durable(QUEUE_C).withArguments(map).build();
    }

    @Bean
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    @Bean
    public Binding queueABindingX(){
        return BindingBuilder.bind(ququeA()).to(xExchange()).with("XA");
    }

    @Bean
    public Binding queueBBindingX(){
        return BindingBuilder.bind(ququeB()).to(xExchange()).with("XB");
    }

    @Bean
    public Binding queueCBindingX(){
        return BindingBuilder.bind(ququeC()).to(xExchange()).with("XC");
    }

    @Bean
    public Binding queueDBindingY(){
        return BindingBuilder.bind(queueD()).to(yExchange()).with("YD");
    }



}

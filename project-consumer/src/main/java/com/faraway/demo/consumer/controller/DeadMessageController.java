package com.faraway.demo.consumer.controller;

import com.faraway.demo.consumer.common.JsonResult;
import com.faraway.demo.consumer.common.JsonResultUtil;
import com.faraway.demo.consumer.common.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    @GetMapping("/get1")
    public JsonResult getMessage1() throws IOException, TimeoutException {

        Channel channel = RabbitMqUtils.getChannel();
        //声明普通和死信交换机 类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列和死信队列
        Map<String,Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        args.put("x-dead-letter-routing-key","lisi");
        //10秒后变为死信
//        args.put("x-message-ttl",10000);
        //队列中消息堆积长度超过6个后成为死信
//        args.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,args);
        //死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        //声明队列和交换机的绑定关系
        //普通交换机 binding 普通队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
        //死信交换机 binding 死信队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        System.out.println("等待接收普通队列的消息C1......");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            String s = new String(message.getBody(), "UTF-8");
            if (s.equals("info5")) {
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
                System.out.println("此消息被拒绝:" + new String(message.getBody(),"UTF-8"));
            }else {
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                System.out.println(new String(message.getBody(),"UTF-8"));
            }

        };
        CancelCallback cancelCallback = (consumerTag)->{
        };
        channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }

    @GetMapping("/get2")
    public JsonResult getMessage2() throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("等待接收普通队列的消息C2......");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("死信队列接到的消息:" + new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = (consumerTag)->{
        };

        channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }
}

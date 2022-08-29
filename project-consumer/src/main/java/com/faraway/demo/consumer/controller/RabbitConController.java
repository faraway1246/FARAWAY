package com.faraway.demo.consumer.controller;

import com.faraway.demo.consumer.common.JsonResult;
import com.faraway.demo.consumer.common.JsonResultUtil;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: 张峰玮
 * @since: 2022/8/27 10:37
 * @version: 1.0
 * @description: 消费消息
 */
@RestController
@RequestMapping("rabbit")
public class RabbitConController {

    @Value("${mymq.sleepTime}")
    private int sleepTime;

    @Value("${mymq.prefetchCount}")
    private int prefetchCount;


    public static String QUEUE_NAME = "hello";

    public static String EXCHANGE_NAME = "logs";

    public static Channel getChannel(String gueueName) throws IOException, TimeoutException{
        QUEUE_NAME = gueueName;
        Channel channel = getChannel();
        return channel;
    }

    public static Channel getChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //设置地址
        factory.setHost("127.0.0.1");
        //设置账号密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        //获取连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();

        //声明队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        return channel;
    }

    public static Channel getChannelWithNoQueue() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        //设置地址
        factory.setHost("127.0.0.1");
        //设置账号密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        //获取连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }

    //hello world 模式
    @GetMapping("/get1")
    public JsonResult funOne() throws IOException, TimeoutException {

        Channel channel = getChannel();

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag);
        };
        //消息消费
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }

    //工作队列模式
    @GetMapping("/get2")
    public JsonResult funTwo() throws IOException, TimeoutException {

        Channel channel = getChannel();

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag);
        };
        //消息消费
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }

    //工作队列模式-手动应答
    @GetMapping("/get3")
    public JsonResult funThree() throws IOException, TimeoutException {

        Channel channel = getChannel("NoAck");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            String msg = new String(message.getBody(), "UTF-8");
            /*try {
                System.out.println("sleepTime = " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            System.out.println("获取到消息:" + msg);
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        CancelCallback cancelCallback = (consumerTag)->{
            System.out.println(consumerTag);
        };
        //设置不公平分发
        channel.basicQos(prefetchCount);
        System.out.println("cancelCallback = " + prefetchCount);
        //消息消费
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }

    @GetMapping("/receiveLog")
    public JsonResult receivedLog() throws IOException, TimeoutException {
        Channel channel = getChannelWithNoQueue();
        //声明一个扇出类型交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列
        String queue = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queue,EXCHANGE_NAME,"");
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("控制台打印接收到的消息:" + new String(message.getBody(),"UTF-8"));
        };
        CancelCallback cancelCallback = (consumerTag)->{};

        channel.basicConsume(queue,true,deliverCallback,cancelCallback);
        return JsonResultUtil.ok();
    }

}

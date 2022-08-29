package com.faraway.demo.provider.controller;

import com.faraway.demo.provider.common.JsonResult;
import com.faraway.demo.provider.common.JsonResultUtil;
import com.rabbitmq.client.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * @author: 张峰玮
 * @since: 2022/8/27 10:16
 * @version: 1.0
 * @description: 消息发送
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitProdController {

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
    @GetMapping("/send1")
    public JsonResult funOne() throws IOException, TimeoutException {
        Channel channel = getChannel();
        String msg = "moneymoneymoney";
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println("消息发送完毕");
        return JsonResultUtil.ok();
    }

    //工作队列模式
    @GetMapping("/send2")
    public JsonResult funTwo() throws IOException, TimeoutException {
        Channel channel = getChannel();
        String msg = "work queue mode";
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        System.out.println("消息发送完毕");
        return JsonResultUtil.ok();
    }

    //工作队列模式-手动应答
    @GetMapping("/send3")
    public JsonResult funThree(@RequestParam(required = false,value = "count") Integer count) throws IOException, TimeoutException {
        Channel channel = getChannel("NoAck");
        //开启发布确认(三种:单个确认发布,批量确认发布,一部确认发布)
        channel.confirmSelect();
        if (count != null) {
            for (Integer integer = 1; integer <= count; integer++) {
                String msg = "消息:"+ integer;
                System.out.println("msg = " + msg);
                channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
            }
        }else {
            String msg = "消息:"+ Math.random();
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
        }
        System.out.println("消息发送完毕");
        return JsonResultUtil.ok();
    }


    //开启发布确认
    @GetMapping("/send4")
    public JsonResult funFour(@RequestParam(value = "count") Integer count) throws Exception {
        Channel channel = getChannel("publishConfirm_queue");
        //开启发布确认(三种:单个确认发布,批量确认发布,一部确认发布)
        channel.confirmSelect();
        long startTime = System.currentTimeMillis();
//        singleConfirm(count,channel);
//        batchConfirm(count, channel);
        asyncConfirm(count,channel);
        long endTime = System.currentTimeMillis();
        System.out.println("消息发送完毕,耗时" + (endTime-startTime) + "ms");

        return JsonResultUtil.ok();
    }
    //单个确认发布 1000个消息,测试三次耗时5320ms,耗时4847ms,耗时4891ms
    private void singleConfirm(Integer count, Channel channel) throws IOException, InterruptedException {
        for (Integer integer = 1; integer <= count; integer++) {
            String msg = "消息:"+ integer;
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
            boolean confirmFlag = channel.waitForConfirms();
            if (!confirmFlag) {
                break;
            }
        }
    }
    //批量确认发布,每一百条消息确认一次  三次测试耗时130ms,耗时123ms,耗时132ms
    private void batchConfirm(Integer count, Channel channel) throws IOException, InterruptedException {
        for (Integer integer = 1; integer <= count; integer++) {
            String msg = "消息:"+ integer;
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
            if (integer % 100 == 0) {
                //单个确认和批量确认调的是同一个方法
                boolean confirmFlag = channel.waitForConfirms();
                if (!confirmFlag) {
                    break;
                }
            }
        }
    }
    //异步确认发布,生产者先发布消息,无需等待broker是否确认,broker在确认成功与否后异步通知生产者确认结果(成功回调和失败回调)
    //三次测试,耗时35ms,耗时34ms,耗时38ms
    private void asyncConfirm(Integer count, Channel channel) throws IOException, InterruptedException {

        ConcurrentSkipListMap<Long,String> publishedMessage = new ConcurrentSkipListMap();
        //异步需要准备监听器，监听broker通知的确认结果
        //ConfirmListener addConfirmListener(ConfirmCallback ackCallback, ConfirmCallback nackCallback);
        channel.addConfirmListener(
                //ackCallback
                (deliveryTag,multiple)->{
                    if (multiple) {
                        ConcurrentNavigableMap<Long, String> confirmedMessage = publishedMessage.headMap(deliveryTag);
                        confirmedMessage.clear();
                    }else {
                        publishedMessage.remove(deliveryTag);
                    }
                },
                //nackCallback
                (deliveryTag,multiple)-> System.out.println("失败消息的标记:" + deliveryTag));
        for (Integer integer = 1; integer <= count; integer++) {
            String msg = "消息:"+ integer;
            publishedMessage.put(channel.getNextPublishSeqNo(),msg);
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());

        }
    }

    @GetMapping("/sendLog")
    public JsonResult sendLog() throws IOException, TimeoutException {
        Channel channel = getChannelWithNoQueue();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes("UTF-8"));
        }
        return JsonResultUtil.ok();
    }

}

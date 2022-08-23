package com.faraway.demo.consumer.controller;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:20
 * @version: 1.0
 * @description:
 */
@RestController
public class RocketMqController {

    @GetMapping("/getHello")
    public void getHello() throws MQClientException,InterruptedException {
        //1.创建出来一个消费者对象
        DefaultMQPushConsumer consumer  = new DefaultMQPushConsumer("rocketmq-consumer");
        //2.绑定namesrv的地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //3.监听要消费的消息的主题
        consumer.subscribe("hello","*");//所有tag都消费
        //4.写监听的方法，监听消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            //如果监听到指定的topic就会执行consumeMessage方法
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
                                                            ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //消息就存在list里面
                for (MessageExt messageExt : list) {
                    System.out.println("消息的内容为："+new String(messageExt.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //5.启动消费者
        consumer.start();
    }

    //官网示例
    @GetMapping("/getMessage")
    public void aaa() throws Exception{
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("demo-consumer");

        // Specify name server addresses.
        consumer.setNamesrvAddr("127.0.0.1:9876");

        // Subscribe one more more topics to consume.
        consumer.subscribe("TopicTest", "*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.registerMessageListener(new MessageListenerConcurrently() {

            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }
}

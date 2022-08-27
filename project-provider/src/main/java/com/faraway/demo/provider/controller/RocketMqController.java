package com.faraway.demo.provider.controller;

import org.springframework.web.bind.annotation.RestController;


/**
 * @author: 张峰玮
 * @since: 2022/8/21 23:20
 * @version: 1.0
 * @description:
 */
@RestController
public class RocketMqController {

    /*@GetMapping("/sayHello")
    public void sayHello() throws Exception {
        //1.创建出来一个生产者对象
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("rocketmq-producer");
        //2.绑定namesrv的地址
        defaultMQProducer.setNamesrvAddr("127.0.0.1:9876");
        //3.启动生产者
        defaultMQProducer.start();
        //4.创建一个消息对象，把要发送的的消息放在消息对象中
        Message message = new Message("hello","你好呀".getBytes());
        //5.使用生产着发出消息(默认使用同步方式发送)
        SendResult sendResult = defaultMQProducer.send(message);
        System.out.println(sendResult);
        //6.关闭资源
        defaultMQProducer.shutdown();
    }

    //同步发送消息
    @GetMapping("/sendBySync")
    public void sendBySync() throws Exception {
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new
                DefaultMQProducer("sync-producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("127.0.0.1:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" *//* Topic *//*,
                    "TagA" *//* Tag *//*,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) *//* Message body *//*
            );
            //Call send message to deliver message to one of brokers.
            SendResult sendResult = producer.send(msg);
            System.out.printf("%s%n", sendResult);
        }
        //Shut down once the producer instance is not longer in use.
        producer.shutdown();
    }
    //异步发送消息
    @GetMapping("/sendByAsync")
    public void sendByAsync() throws Exception{
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("async-producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("127.0.0.1:9876");
        //Launch the instance.
        producer.start();
        producer.setRetryTimesWhenSendAsyncFailed(0);

        int messageCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(messageCount);
        for (int i = 0; i < messageCount; i++) {
            try {
                final int index = i;
                Message msg = new Message("TopicTest",
                        "TagA",
                        "OrderID188",
                        "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                producer.send(msg, new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d OK %s %n", index, sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        countDownLatch.countDown();
                        System.out.printf("%-10d Exception %s %n", index, e);
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.await(5, TimeUnit.SECONDS);
        producer.shutdown();
    }
    //单向发送消息
    @GetMapping("/sendByOneway")
    public void sendByOneway() throws Exception{
        //Instantiate with a producer group name.
        DefaultMQProducer producer = new DefaultMQProducer("one-way-producer");
        // Specify name server addresses.
        producer.setNamesrvAddr("127.0.0.1:9876");
        //Launch the instance.
        producer.start();
        for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" *//* Topic *//*,
                    "TagA" *//* Tag *//*,
                    ("Hello RocketMQ " +
                            i).getBytes(RemotingHelper.DEFAULT_CHARSET) *//* Message body *//*
            );
            //Call send message to deliver message to one of brokers.
            producer.sendOneway(msg);
        }
        //Wait for sending to complete
        Thread.sleep(5000);
        producer.shutdown();
    }*/
}

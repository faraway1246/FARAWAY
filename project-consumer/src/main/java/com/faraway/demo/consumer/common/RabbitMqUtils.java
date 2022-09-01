package com.faraway.demo.consumer.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author: 张峰玮
 * @since: 2022/8/31 22:00
 * @version: 1.0
 * @description:
 */
public class RabbitMqUtils {

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
        return channel;
    }

}

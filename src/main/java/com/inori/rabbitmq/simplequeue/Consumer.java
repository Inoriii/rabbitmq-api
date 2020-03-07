package com.inori.rabbitmq.simplequeue;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.inori.rabbitmq.MyConsumer;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/6 00:09
 * @description:
 */
public class Consumer {

    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 定义队列的消费者
        MyConsumer consumer = new MyConsumer(channel);
        // 监听队列
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

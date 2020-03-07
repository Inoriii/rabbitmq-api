package com.inori.rabbitmq.message;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 19:15
 * @description:
 */
public class Consumer {

    public static final String exchangeName = "";
    public static final String queueName = "message_test_properties";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.basicConsume(queueName, true, myConsumer);
    }
}

package com.inori.rabbitmq.exchange.topic;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 17:31
 * @description:
 */
public class Consumer {

    public static final String exchangeName = "test_topic_exchange";
    public static final String queueName = "test_topic_queue";
    public static final BuiltinExchangeType exchangeType = BuiltinExchangeType.TOPIC;
    public static final String routingKey = "user.*";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        channel.exchangeDeclare(exchangeName, exchangeType, true);
        channel.queueBind(queueName, exchangeName, routingKey);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.basicConsume(queueName, true, myConsumer);
    }
}

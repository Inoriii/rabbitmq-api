package com.inori.rabbitmq.apiack;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/8 18:06
 * @description:
 */
public class Consumer {
    public static final String exchangeName = "test_api_ack_exchange";
    public static final String queueName = "test_api_ack_queue";
    public static final String routingKey = "api.ack.#";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.basicConsume(queueName, false, myConsumer);
    }
}

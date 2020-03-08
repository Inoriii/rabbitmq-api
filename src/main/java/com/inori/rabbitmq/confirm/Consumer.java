package com.inori.rabbitmq.confirm;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/8 16:04
 * @description:
 */
public class Consumer {

    public static final String exchangeName = "test_confirm_exchange";
    public static final String queueName = "test_confirm_queue";
    public static final String routKing = "confirm.#";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routKing);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.basicConsume(queueName, true, myConsumer);
    }
}

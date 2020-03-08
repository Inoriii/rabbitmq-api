package com.inori.rabbitmq.dlx;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/8 18:06
 * @description:
 */
public class Consumer {
    public static final String exchangeName = "test_dlx_exchange";
    public static final String queueName = "test_dlx_queue";
    public static final String routingKey = "dlx.#";

    public static final String deathExchangeName = "dlx_exchange";
    public static final String deathQueueName = "dlx_queue";
    public static final String deathRoutingKey = "#";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC, true);
        channel.queueDeclare(queueName, true, false, false, new HashMap<String, Object>() {{
            put("x-dead-letter-exchange", deathExchangeName);
        }});
        channel.queueBind(queueName, exchangeName, routingKey);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.exchangeDeclare(deathExchangeName, BuiltinExchangeType.TOPIC, true, false, null);
        channel.queueDeclare(deathQueueName, true, false, false, null);
        channel.queueBind(deathQueueName, deathExchangeName, deathRoutingKey);

        channel.basicConsume(queueName, false, myConsumer);
    }
}

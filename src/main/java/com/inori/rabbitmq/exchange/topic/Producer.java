package com.inori.rabbitmq.exchange.topic;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 17:31
 * @description:
 */
public class Producer {

    public static final String routingKey1 = "user.save";
    public static final String routingKey2 = "user.update";
    public static final String routingKey3 = "user.delete.something";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String message = "send this message";

        System.out.println("---pre send message routingKey is [" + routingKey1 + "] ---");
        channel.basicPublish(Consumer.exchangeName, routingKey1, null, message.getBytes());
        System.out.println("---message [ " + message + " ]" + " had been send---");
        System.out.println("---pre send message routingKey is [" + routingKey2 + "] ---");
        channel.basicPublish(Consumer.exchangeName, routingKey2, null, message.getBytes());
        System.out.println("---message [ " + message + " ]" + " had been send---");
        System.out.println("---pre send message routingKey is [" + routingKey3 + "] ---");
        channel.basicPublish(Consumer.exchangeName, routingKey3, null, message.getBytes());
        System.out.println("---message [ " + message + " ]" + " had been send---");

        channel.close();
        connection.close();
    }
}

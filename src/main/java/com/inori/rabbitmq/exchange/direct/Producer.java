package com.inori.rabbitmq.exchange.direct;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.inori.rabbitmq.exchange.direct.Consumer.exchangeName;
import static com.inori.rabbitmq.exchange.direct.Consumer.routingKey;

/**
 * @author sakura
 * @date: 2020/3/7 16:23
 * @description:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String message = "this is a message";

        System.out.println("---pre send message---");
        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        System.out.println("---message [ " + message + " ]" + " had been send---");

        channel.close();
        connection.close();
    }
}

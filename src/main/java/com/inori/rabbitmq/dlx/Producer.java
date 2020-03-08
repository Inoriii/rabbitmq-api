package com.inori.rabbitmq.dlx;

import com.inori.rabbitmq.util.CommentString;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.inori.rabbitmq.dlx.Consumer.exchangeName;

/**
 * @author sakura
 * @date: 2020/3/8 18:06
 * @description:
 */
public class Producer {

    public static final String routingKey = "dlx.save";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> headerMap = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            headerMap.put("num", i);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("utf-8")
                    .headers(headerMap)
                    .expiration(10 * 1000 + "")
                    .build();

            String message = CommentString.messageStart + i + CommentString.messageEnd;
            channel.basicPublish(exchangeName, routingKey, true, basicProperties, message.getBytes());
            System.out.println(message);
        }

        channel.close();
        connection.close();
    }
}

package com.inori.rabbitmq.message;

import com.inori.rabbitmq.util.CommentString;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.inori.rabbitmq.message.Consumer.exchangeName;
import static com.inori.rabbitmq.message.Consumer.queueName;

/**
 * @author sakura
 * @date: 2020/3/7 19:15
 * @description:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //创建一个headers,自定义BasicProperties一些信息
        Map<String, Object> headerProperties = new HashMap<>();
        headerProperties.put("p1", "111");
        headerProperties.put("p2", 2);

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                //消息是否持久化 1:非持久化 2:持久化
                .deliveryMode(2)
                .contentEncoding("utf-8")
                //过期时间 毫秒  没被消费的话时间到了会被消除
                .expiration(10 * 1000 + "")
                .headers(headerProperties)
                .build();

        for (int i = 0; i < 10; i++) {
            String message = CommentString.messageStart + i + CommentString.messageEnd;
            channel.basicPublish(exchangeName, queueName, properties, message.getBytes());
            System.out.println(message);
        }

        channel.close();
        connection.close();
    }
}

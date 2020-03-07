package com.inori.rabbitmq.exchange.fanout;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 18:12
 * @description:
 */
public class Producer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        String messageStart = "---   this is NO.";
        String messageEnd = "   ---";
        for (int i = 0; i < 10; i++) {
            String message = messageStart + i + "条消息" + messageEnd;
            channel.basicPublish(Consumer.exchangeName, Consumer.routingKey + "和路由键没关系 随便设置", null, message.getBytes());
            System.out.println(message);
        }

        channel.close();
        connection.close();
    }
}

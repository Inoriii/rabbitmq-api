package com.inori.rabbitmq.returnlistener;

import com.inori.rabbitmq.util.CommentString;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.inori.rabbitmq.returnlistener.Consumer.exchangeName;

/**
 * @author sakura
 * @date: 2020/3/8 16:59
 * @description:
 */
public class Producer {

    public static final String routingKey = "return.save";
    public static final String routingKeyError = "abc.save";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) {
                //replyCode响应码
                //replyText文本
                System.out.println("----------------ReturnListener---------------------");
                System.out.println("replyCode" + replyCode);
                System.out.println("replyText" + replyText);
            }
        });

        for (int i = 0; i < 10; i++) {
            String message = CommentString.messageStart + i + CommentString.messageEnd;
            channel.basicPublish(exchangeName, routingKeyError, true, null, message.getBytes());
            System.out.println(message);
        }

    }
}

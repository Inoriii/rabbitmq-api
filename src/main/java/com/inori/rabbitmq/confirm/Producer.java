package com.inori.rabbitmq.confirm;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/8 16:03
 * @description:
 */
public class Producer {
    public static final String routKing = "confirm.save";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //指定消息确认模式
        channel.confirmSelect();

        String message = "this is a message";

        System.out.println("---pre send message---");
        channel.basicPublish(Consumer.exchangeName, routKing, null, message.getBytes());
        System.out.println("---message [ " + message + " ]" + " had been send---");

        //添加确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("------------ ack success ------------");
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("-------------- ack fail --------------");
            }
        });
    }
}

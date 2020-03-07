package com.inori.rabbitmq.exchange.direct;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 16:23
 * @description:
 */
public class Consumer {

    public static final String queueName = "test_direct_queue";
    public static final BuiltinExchangeType exchangeType = BuiltinExchangeType.DIRECT;
    public static final String exchangeName = "test_direct_exchange";
    public static final String routingKey = "test.direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(exchangeName, exchangeType, true);
        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);
        //绑定
        channel.queueBind(queueName, exchangeName, routingKey);

        MyConsumer myConsumer = new MyConsumer(channel);

        //设置监听队列
        channel.basicConsume(queueName, true, myConsumer);
    }
}

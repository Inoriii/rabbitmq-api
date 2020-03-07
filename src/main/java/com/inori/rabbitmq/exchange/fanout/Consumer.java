package com.inori.rabbitmq.exchange.fanout;

import com.inori.rabbitmq.MyConsumer;
import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author sakura
 * @date: 2020/3/7 18:12
 * @description:
 */
public class Consumer {

    public static final String exchangeName = "test_fanout_exchange";
    public static final String queueName = "test_fanout_queue";
    //不设置路由键
    public static final String routingKey = "";
    public static final BuiltinExchangeType exchangeType = BuiltinExchangeType.FANOUT;

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchangeName, exchangeType, true);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
        MyConsumer myConsumer = new MyConsumer(channel);

        channel.basicConsume(queueName, true, myConsumer);
    }
}

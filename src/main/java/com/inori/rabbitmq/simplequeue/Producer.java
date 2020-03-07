package com.inori.rabbitmq.simplequeue;

import com.inori.rabbitmq.util.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author inori
 * @date: 2020/3/5 18:57
 * @description:
 */
public class Producer {

    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtil.getConnection();

        //通过Connection创建Channel
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 10; i >= 0; i--) {
            // 消息内容
            String message = "生产者的第[[" + i + "]]次发送数据:==>smile happy day";

            System.out.println("-----------   producer message   ----------");
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            System.out.println("-----------producer message finish----------");

        }
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}

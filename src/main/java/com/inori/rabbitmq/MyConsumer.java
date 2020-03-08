package com.inori.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author inori
 * @date: 2020/3/5 18:58
 * @description: 自定义消费者
 */
public class MyConsumer extends DefaultConsumer {

    private Channel channel;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("-----------   consume message   ----------");

        System.out.println("consumerTag: " + consumerTag);
        System.out.println("envelope: " + envelope);
        System.out.println("properties: " + properties);
        System.out.println("body: " + new String(body, StandardCharsets.UTF_8));
        System.out.println("[x] Received '" + new String(body, StandardCharsets.UTF_8) + "'");

        Map<String, Object> headers = properties.getHeaders();
        if (!CollectionUtils.isEmpty(headers)) {
            if ("0".equals(headers.get("num") + "")) {
                //multiple 是否批量
                //requeue 是否重回队列
                System.out.println("------------ 0 equals num ------------");
                channel.basicNack(envelope.getDeliveryTag(), false, true);
            }
        }

        System.out.println("-----------consume message finished----------");

        //@param multiple 是否批量签收
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
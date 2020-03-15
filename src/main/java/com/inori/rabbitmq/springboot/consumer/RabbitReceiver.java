package com.inori.rabbitmq.springboot.consumer;

import com.inori.rabbitmq.springAMQP.entity.Order;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author sakura
 * @date: 2020/3/14 23:06
 * @description:
 */
@Component
public class RabbitReceiver {

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
            key = {"springboot.#"}
    )})

    @RabbitHandler
    public void onMessage(Message<Object> message, Channel channel) throws IOException {
        System.out.println("==========================================");
        System.out.println("消费Payload: " + message.getPayload());
        Object o = message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck((Long) o, false);
    }

    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String, Object> headers, Channel channel) throws IOException {
        System.out.println("==========================================");
        System.out.println("消费Order: " + order);
        channel.basicAck((Long) headers.get(AmqpHeaders.DELIVERY_TAG), false);
    }
}

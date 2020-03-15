package com.inori.rabbitmq.springboot.producer;

import com.inori.rabbitmq.springAMQP.entity.Order;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sakura
 * @date: 2020/3/14 20:09
 * @description:
 */
@Component
public class RabbitSender {

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.out.println("correlationData: " + correlationData);
            System.out.println("ack: " + ack);
            if (!ack) {
                System.out.println("异常处理");
            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
            System.out.println("--------- returnedMessage -----------");
            System.out.println("message: " + message);
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey);
            System.out.println("=====================================");
        }
    };

    public RabbitSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object message, Map<String, Object> properties) {
        MessageHeaders messageHeader = new MessageHeaders(properties);
        Message<Object> msg = MessageBuilder.createMessage(message, messageHeader);
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        //一定要全局唯一
        correlationData.setId("1234");
        rabbitTemplate.convertAndSend("exchange-1", "springboot.hello", msg, correlationData);
    }

    public void sendOrder(Order order) {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);
        CorrelationData correlationData = new CorrelationData();
        //一定要全局唯一
        correlationData.setId("1234");
        rabbitTemplate.convertAndSend("exchange-2", "springboot.hello", order, correlationData);
    }
}

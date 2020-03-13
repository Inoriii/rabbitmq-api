package com.inori.rabbitmq.springAMQP.config;

import com.inori.rabbitmq.springAMQP.MessageDelegate;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author sakura
 * @date: 2020/3/11 19:01
 * @description:
 */
@Configuration
@ComponentScan("com.inori.rabbitmq.springAMQP.*")
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses("192.168.1.5:5672");
        cachingConnectionFactory.setUsername("inori");
        cachingConnectionFactory.setPassword("inori");
        cachingConnectionFactory.setVirtualHost("/");
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public TopicExchange exchange001() {
        return new TopicExchange("topic001", true, false);
    }

    @Bean
    public Queue queue001() {
        return new Queue("queue001", true);
    }

    @Bean
    public Binding binding001() {
        return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }

    @Bean
    public TopicExchange jsonTopicExchange() {
        return new TopicExchange("jsonTopicExchange", true, false);
    }

    @Bean
    public Queue jsonQueue() {
        return new Queue("jsonQueue", true, false, false);
    }

    @Bean
    public Binding jsonBinding() {
        return BindingBuilder.bind(jsonQueue()).to(jsonTopicExchange()).with("json.*");
    }


    @Bean
    public TopicExchange javaObjTopicExchange() {
        return new TopicExchange("javaObjTopicExchange", true, false);
    }


    @Bean
    public Queue javaObjQueue() {
        return new Queue("javaObjQueue", true, false, false);
    }

    @Bean
    public Binding javaObjBinding() {
        return BindingBuilder
                .bind(javaObjQueue())
                .to(javaObjTopicExchange())
                .with("javaObj.*");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("queue001", "test.direct.queue", "jsonQueue", "javaObjQueue");
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(2);
        //是否重回队列
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID();
            }
        });
//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                System.out.println("---------------消费者" + new String(message.getBody(), StandardCharsets.UTF_8));
//            }
//        });

//        //设置监听适配器 如果不setDefaultListenerMethod,则监听到消息会默认使用handleMessage方法执行
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod(MessageDelegate.messageName);
//        //设置监听转换器,将不同contentType请求的内容转换成监听器接收的对象类型
//        adapter.setMessageConverter(new MyMessageConverter());
//        container.setMessageListener(adapter);

//        //设置监听适配器 通过监听的队列名称与监听器绑定
//        HashMap<String, String> queueOrTagToMethodName = new HashMap<>();
//        queueOrTagToMethodName.put("queue001", MessageDelegate.messageName);
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        //设置监听转换器,将不同contentType请求的内容转换成监听器接收的对象类型
//        adapter.setMessageConverter(new MyMessageConverter());
//        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);

//        Map<String, String> queueOrTagToMethodName = new HashMap<>();
//        queueOrTagToMethodName.put("jsonQueue", "handleMessageJson");
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//        adapter.setMessageConverter(new Jackson2JsonMessageConverter());
//        container.setMessageListener(adapter);

        Map<String, String> queueOrTagToMethodName = new HashMap<>();
        queueOrTagToMethodName.put("jsonQueue", "handleMessageJson");
        queueOrTagToMethodName.put("javaObjQueue", "handleMessageJavaObj");
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        adapter.setMessageConverter(new Jackson2JsonMessageConverter());
        container.setMessageListener(adapter);

        return container;
    }
}
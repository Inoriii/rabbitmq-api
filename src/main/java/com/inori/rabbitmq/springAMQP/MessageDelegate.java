package com.inori.rabbitmq.springAMQP;

import com.alibaba.fastjson.JSONObject;
import com.inori.rabbitmq.springAMQP.entity.Order;
import com.inori.rabbitmq.springAMQP.entity.Packaged;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author sakura
 * @date: 2020/3/13 18:45
 * @description:
 */
public class MessageDelegate {

    public static final String messageName = "handleMessageString";

    public void handleMessage(byte[] messageBody) {
        System.out.println("默认方法: " + new String(messageBody, StandardCharsets.UTF_8));
    }

    public void handleMessageString(String messageBody) {
        System.out.println("String的默认方法: " + messageBody);
    }

    public void handleMessageJson(Map<String, Object> messageBody) {
        System.out.println("***** handleMessageJson 打印 *****");
        System.out.println(new JSONObject(messageBody).toJavaObject(Order.class));
        System.out.println("**********************************");
    }

    public void handleMessageJavaObj(Object o) {
        if (o instanceof Order) {
            Order order = (Order) o;
            System.out.println("***** handleMessageJavaObj 打印 *****");
            System.out.println(order);
            System.out.println("*************************************");
        } else if (o instanceof Packaged) {
            Packaged packaged = (Packaged) o;
            System.out.println("***** handleMessageJavaObj 打印 *****");
            System.out.println(packaged);
            System.out.println("*************************************");
        } else {
            System.out.println("***** handleMessageJavaObj 打印 *****");
            System.out.println("未知类别");
            System.out.println(o);
            System.out.println("*************************************");
        }
    }
}

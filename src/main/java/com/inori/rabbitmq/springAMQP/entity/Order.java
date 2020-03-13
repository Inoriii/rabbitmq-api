package com.inori.rabbitmq.springAMQP.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sakura
 * @date: 2020/3/13 20:33
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    private String id;

    private String name;

    private String content;
}

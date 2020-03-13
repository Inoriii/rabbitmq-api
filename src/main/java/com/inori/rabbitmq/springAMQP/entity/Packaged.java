package com.inori.rabbitmq.springAMQP.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sakura
 * @date: 2020/3/13 20:48
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Packaged {
    private String id;
    private String name;
    private String description;
}

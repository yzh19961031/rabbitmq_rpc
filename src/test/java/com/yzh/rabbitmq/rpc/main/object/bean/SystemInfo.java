package com.yzh.rabbitmq.rpc.main.object.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuanzhihao
 * @since 2020/12/29
 */
@Data
@AllArgsConstructor
public class SystemInfo implements Serializable {

    private String os;
    private String version;
}

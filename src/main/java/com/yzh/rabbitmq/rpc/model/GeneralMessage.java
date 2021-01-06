package com.yzh.rabbitmq.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义消息
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneralMessage {
    // 消息ID
    private String messageId;
    // 消息名称
    private String messageName;
    // PRC使用的标识ID
    private String correlationId;
    // 响应队列
    private String replyTo;
    // 源端ID
    private String sourceId;
    // 目标端ID
    private String destinationId;
    // 消息体
    private Serializable content;
    // 指定的超时时间
    private long timeout;
}

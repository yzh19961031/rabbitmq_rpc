package com.yzh.rabbitmq.rpc.model;

import lombok.Getter;

/**
 * 消息类型
 *
 * @author yuanzhihao
 * @since 2020/12/22
 */
@Getter
public enum MessageType {
    CONTENT_TYPE_BYTES("application/octet-stream"),
    CONTENT_TYPE_TEXT_PLAIN("text/plain"),
    CONTENT_TYPE_JSON("application/json"),
    CONTENT_TYPE_XML("application/xml");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }
}

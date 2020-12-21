package com.yzh.rabbitmq.rpc.message.api;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;

import java.io.Serializable;

/**
 * 消息分发器
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
public interface MessageDispatcher {

    /**
     * 请求并获得响应
     *
     * @param destination 目标端
     * @param messageName 请求的消息名称
     * @param content 消息内容
     * @return 响应消息
     */
    GeneralMessage request(String destination, String messageName, Serializable content);

    /**
     * 响应方法 基于rabbitmq实现RPC
     *
     * @param message 消息
     * @param content 响应的内容
     */
    void replay(GeneralMessage message, Serializable content);
}

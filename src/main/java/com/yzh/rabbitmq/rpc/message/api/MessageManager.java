package com.yzh.rabbitmq.rpc.message.api;


import org.springframework.amqp.core.MessageListener;

/**
 * 消息管理器
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
public interface MessageManager {

    /**
     * 消息订阅
     *
     * @param messageName 自定义消息名称
     * @param messageListener 消息监听器
     */
    void subscribe(String messageName, MessageListener messageListener);

    /**
     * 取消消息订阅
     *
     * @param messageName 消息名称
     */
    void unSubscribe(String messageName);
}

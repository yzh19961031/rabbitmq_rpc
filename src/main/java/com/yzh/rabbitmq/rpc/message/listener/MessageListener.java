package com.yzh.rabbitmq.rpc.message.listener;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;

/**
 * 消息监听器
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
public interface MessageListener {

    /**
     * 监听方法
     *
     * @param message 消息
     */
    void onMessage(GeneralMessage message);
}

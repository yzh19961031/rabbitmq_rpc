package com.yzh.rabbitmq.rpc.message.listener.template;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.message.listener.GeneralMessageListener;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 传递对象
 *
 * @author yuanzhihao
 * @since 2020/12/22
 */
@Slf4j
public abstract class ObjectGeneralMessageListener implements GeneralMessageListener {

    public void onMessage(GeneralMessage message) {
        if (message == null) {
            log.error("Message is null");
            return;
        }
        log.info("Receive a message messageName is {}, sourceId is {}", message.getMessageName(), message.getSourceId());
        RpcMaster.reply(message, this.handle(message.getContent()));
    }

    // 核心处理的方法
    public abstract Serializable handle(Serializable document);
}

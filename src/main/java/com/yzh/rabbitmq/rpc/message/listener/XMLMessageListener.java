package com.yzh.rabbitmq.rpc.message.listener;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * XML类型的监听器  使用模板模式
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public abstract class XMLMessageListener implements MessageListener {

    public void onMessage(GeneralMessage message) {
        //
        if (message == null) {
            log.error("message is null");
            return;
        }
        this.handle(message);
    }

    public abstract void handle(GeneralMessage message);
}

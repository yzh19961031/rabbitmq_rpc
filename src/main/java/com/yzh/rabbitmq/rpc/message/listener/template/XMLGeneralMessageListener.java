package com.yzh.rabbitmq.rpc.message.listener.template;

import com.yzh.rabbitmq.rpc.message.listener.GeneralMessageListener;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yuanzhihao
 * @since 2020/12/22
 */
@Slf4j
public abstract class XMLGeneralMessageListener implements GeneralMessageListener {

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

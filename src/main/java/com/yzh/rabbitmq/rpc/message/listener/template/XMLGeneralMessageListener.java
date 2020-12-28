package com.yzh.rabbitmq.rpc.message.listener.template;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.message.listener.GeneralMessageListener;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import java.io.Serializable;

/**
 * 使用模板模式 通用的参数校验提取出来
 *
 * @author yuanzhihao
 * @since 2020/12/22
 */
@Slf4j
public abstract class XMLGeneralMessageListener implements GeneralMessageListener {

    public void onMessage(GeneralMessage message) {
        if (message == null) {
            log.error("Message is null");
            return;
        }

        Serializable content = message.getContent();
        Document document;
        try {
            document = DocumentHelper.parseText((String) content);
        } catch (DocumentException e) {
            log.error("Illegal message format");
            return;
        }
        String response = this.handle(document);
        RpcMaster.reply(message, response);
    }

    // 核心处理的方法
    public abstract String handle(Document document);
}

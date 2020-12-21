package com.yzh.rabbitmq.rpc.message.common;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.io.Serializable;

/**
 * 消息转换器
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public class MessageConvert {

    public static Message convertToAMQPMessage(GeneralMessage generalMessage) {
        MessageProperties messageProperties = new MessageProperties();
        Serializable content = generalMessage.getContent();



        return new Message(null, messageProperties);
    }

    public static GeneralMessage convertToGeneralMessage(Message message) {
        return new GeneralMessage();
    }

}

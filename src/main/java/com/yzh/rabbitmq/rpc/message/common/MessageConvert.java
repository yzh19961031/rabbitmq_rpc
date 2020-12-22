package com.yzh.rabbitmq.rpc.message.common;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.model.MessageType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 消息转换器
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public class MessageConvert {

    /**
     * 自定义消息转换为AMQP消息
     *
     * @param generalMessage 自定义消息
     * @return AMQP消息
     */
    public static Message convertToAMQPMessage(GeneralMessage generalMessage) {
        MessagePropertiesBuilder messagePropertiesBuilder = MessagePropertiesBuilder.newInstance();
        Serializable content = generalMessage.getContent();
        String contentType;
        byte[] body = null;
        if (content instanceof String) {
            contentType = MessageType.CONTENT_TYPE_TEXT_PLAIN.getName();
            body = ((String) content).getBytes(StandardCharsets.UTF_8);
        } else {
            contentType = MessageType.CONTENT_TYPE_BYTES.getName();
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(content);
                body = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                log.error("Conversion of byte array failed");
            }
        }

        MessageProperties messageProperties = messagePropertiesBuilder.setMessageId(generalMessage.getMessageId()).
                setCorrelationIdIfAbsent(generalMessage.getCorrelationId()).
                setExpiration(Long.toString(generalMessage.getTimeout())).
                setHeader("messageName", generalMessage.getMessageName()).
                setHeaderIfAbsent("sourceId", generalMessage.getSourceId()).
                setHeaderIfAbsent("destinationId", generalMessage.getDestinationId()).
                setContentType(contentType).build();

        return new Message(body, messageProperties);
    }

    /**
     * AMQP消息转换为自定义消息
     *
     * @param message AMQP消息
     * @return 自定义消息
     */
    public static GeneralMessage convertToGeneralMessage(Message message) {
        if (message == null) {
            return null;
        }
        MessageProperties messageProperties = message.getMessageProperties();
        if (messageProperties != null && messageProperties.getHeaders() != null) {
            String messageId = messageProperties.getMessageId();
            String replyTo = messageProperties.getReplyTo();
            String correlationId = messageProperties.getCorrelationId();
            String expiration = messageProperties.getExpiration();
            Map<String, Object> headers = messageProperties.getHeaders();
            String messageName = (String) headers.get("messageName");
            String sourceId = (String) headers.get("sourceId");
            String destinationId = (String) headers.get("destinationId");
            Object content = null;

            if (MessageType.CONTENT_TYPE_TEXT_PLAIN.getName().equals(messageProperties.getContentType())) {
                content = new String(message.getBody(), StandardCharsets.UTF_8);
            } else {
                try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(message.getBody());
                     ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                    content = objectInputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Conversion of object array failed");
                }
            }
            return new GeneralMessage(messageId, messageName, correlationId,
                    replyTo, sourceId, destinationId, (Serializable) content, Long.parseLong(expiration));
        }
        return null;
    }

}

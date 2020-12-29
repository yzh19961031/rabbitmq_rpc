package com.yzh.rabbitmq.rpc.instance;


import com.yzh.rabbitmq.rpc.config.CustomPropertyPlaceholder;
import com.yzh.rabbitmq.rpc.constant.BasicConstant;
import com.yzh.rabbitmq.rpc.message.MessageCoreImplementer;
import com.yzh.rabbitmq.rpc.message.common.MessageConvert;
import com.yzh.rabbitmq.rpc.message.listener.GeneralMessageListener;
import com.yzh.rabbitmq.rpc.message.register.SubMessageRegister;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.Serializable;
import java.util.Objects;

/**
 * 具体调度实现  单例实现
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public class RpcInstance implements MessageCoreImplementer, BasicConstant {

    private RabbitTemplate rabbitTemplate;
    /**
     * 这边注入一个配置类
     */
    private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer;

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void setPropertySourcesPlaceholderConfigurer(PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer) {
        this.propertySourcesPlaceholderConfigurer = propertySourcesPlaceholderConfigurer;
    }

    /**
     * 使用单例  保证调度实现与监听器中的subMessageRegister是同一个
     */
    private final SubMessageRegister subMessageRegister = SubMessageRegister.getInstance();


    @Override
    public GeneralMessage request(String destination, String messageName, Serializable content) {
        if (ObjectUtils.anyNull(destination, messageName)) {
            log.error("param is illegal");
            return null;
        }
        String messageId = CommonUtil.generateId();
        String correlationId = CommonUtil.generateId();
        CustomPropertyPlaceholder propertyPlaceholder = (CustomPropertyPlaceholder) propertySourcesPlaceholderConfigurer;
        GeneralMessage generalMessage = new GeneralMessage(messageId, messageName, correlationId, "",
                propertyPlaceholder.getLocalId(), destination, content, DEFAULT_TIMEOUT);
        Message message = this.rabbitTemplate.sendAndReceive(EXCHANGE_NAME, destination, MessageConvert.convertToAMQPMessage(generalMessage));

        if (!correlationId.equals(Objects.requireNonNull(message).getMessageProperties().getCorrelationId())) {
            log.error("The request correlationId not equal to response correlationId");
            return null;
        }
        return MessageConvert.convertToGeneralMessage(message);
    }

    @Override
    public void reply(GeneralMessage message, Serializable content) {
        String replyTo = message.getReplyTo();
        GeneralMessage generalMessage = new GeneralMessage(message.getMessageId(), message.getMessageName(),
                message.getCorrelationId(), replyTo, message.getSourceId(), message.getDestinationId(), content, message.getTimeout());
        rabbitTemplate.send(replyTo, MessageConvert.convertToAMQPMessage(generalMessage));
    }

    @Override
    public void subscribe(String messageName, GeneralMessageListener messageListener) {
        if (StringUtils.isEmpty(messageName) || ObjectUtils.isEmpty(messageListener)) {
            log.error("messageName or messageListener cannot be null");
        } else {
            // 使用匿名内部类对自定义的messageListener进行转换
            this.subMessageRegister.register(messageName, message -> messageListener.onMessage(MessageConvert.convertToGeneralMessage(message)));
        }
    }

    @Override
    public void unSubscribe(String messageName) {
        if (StringUtils.isEmpty(messageName)) {
            log.error("messageName cannot be null");
        } else {
            this.subMessageRegister.unRegister(messageName);
        }
    }
}

package com.yzh.rabbitmq.rpc.instance;


import com.yzh.rabbitmq.rpc.config.CustomPropertyPlaceholder;
import com.yzh.rabbitmq.rpc.constant.BasicConstant;
import com.yzh.rabbitmq.rpc.message.MessageCoreImplementer;
import com.yzh.rabbitmq.rpc.message.common.MessageConvert;
import com.yzh.rabbitmq.rpc.message.register.SubMessageRegister;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.Serializable;

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
        if (StringUtils.isAnyEmpty(destination, messageName, (String) content)) {
            log.error("param is illegal");
            return null;
        }
        CustomPropertyPlaceholder propertyPlaceholder = (CustomPropertyPlaceholder) propertySourcesPlaceholderConfigurer;
        GeneralMessage generalMessage = new GeneralMessage(CommonUtil.generateId(), messageName,
                propertyPlaceholder.getLocalId(), destination, content, DEFAULT_TIMEOUT);
        Message message = this.rabbitTemplate.sendAndReceive(EXCHANGE_NAME, destination, MessageConvert.convertToAMQPMessage(generalMessage));
        return MessageConvert.convertToGeneralMessage(message);
    }

    @Override
    public void replay(GeneralMessage message, Serializable content) {

    }

    @Override
    public void subscribe(String messageName, MessageListener messageListener) {
        if (StringUtils.isEmpty(messageName) || ObjectUtils.isEmpty(messageListener)) {
            log.error("messageName or messageListener cannot be null");
        } else {
            this.subMessageRegister.register(messageName, messageListener);
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

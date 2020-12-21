package com.yzh.rabbitmq.rpc.message.common;

import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 自定义消息构建器
 *
 * @author yuanzhihao
 * @since 2020/12/20
 */
@Slf4j
public class MessageCreator {
    // 默认超时时间
    private static final long DEFAULT_TIME = 180000;

    public static GeneralMessage createMessage(String localId, String destinationId, String messageName, Serializable content, long msgTimeout) {
        String messageId = CommonUtil.generateId();
        return new GeneralMessage();
    }

}

package com.yzh.rabbitmq.rpc.message.register;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 监听消息注册器
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public class SubMessageRegister {
    private static final SubMessageRegister INSTANCE = new SubMessageRegister();

    private SubMessageRegister() {}

    public static SubMessageRegister getInstance() {
        return INSTANCE;
    }

    /**
     * 使用一个map来存储RPC调用的方法名称  以及对应的方法监听
     *
     */
    private final Map<String, MessageListener> registerStore = new ConcurrentHashMap<>();


    /**
     * 注册RPC调用方法名称与监听器
     *
     * @param messageName 消息名称
     * @param messageListener 监听器
     */
    public void register(String messageName, MessageListener messageListener) {
       if (StringUtils.isEmpty(messageName) || ObjectUtils.isEmpty(messageListener)) {
           log.error("messageName or messageListener is null");
           return;
       }
       MessageListener listener = registerStore.putIfAbsent(messageName, messageListener);
       if (listener != null) {
           log.error("message [{}] already exists, please check the messageName", messageName);
       }
    }

    /**
     * 移除某个监听的消息
     *
     * @param messageName 消息名称
     */
    public void unRegister(String messageName) {
        registerStore.remove(messageName);
    }

    /**
     * 清空监听消息
     */
    public void clearAllRegister() {
        registerStore.clear();
    }

    /**
     * 获得某个消息监听器
     *
     * @param messageName 消息名称
     * @return 监听器
     */
    public MessageListener getMessageListener(String messageName) {
        return registerStore.get(messageName);
    }
}

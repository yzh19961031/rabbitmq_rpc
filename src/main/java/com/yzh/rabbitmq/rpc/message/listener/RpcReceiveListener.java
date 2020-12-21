package com.yzh.rabbitmq.rpc.message.listener;

import com.yzh.rabbitmq.rpc.message.register.SubMessageRegister;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 自定义消息监听器
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
@Data
public class RpcReceiveListener implements MessageListener {
    // 使用线程池调度
    private Executor taskExecutor;

    private final SubMessageRegister subMessageRegister = SubMessageRegister.getInstance();

    @Override
    public void onMessage(Message message) {
        if (taskExecutor == null) {
            // 默认设置线程池大小为5
            taskExecutor = Executors.newFixedThreadPool(5);
        }
        taskExecutor.execute(() -> handleMessage(message));
    }

    // 分发到指定的监听器
    private void handleMessage(Message message) {
        if (message == null) {
            log.error("message is null");
        } else {
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            String messageName = (String) headers.get("messageName");
            this.subMessageRegister.getMessageListener(messageName).onMessage(message);
        }
    }
}

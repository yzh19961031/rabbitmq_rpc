package com.yzh.rabbitmq.rpc.constant;

/**
 * 常量配置
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
public interface BasicConstant {
    // 默认交换机名称
    String EXCHANGE_NAME = "rpc_exchange";

    // 绑定队列名称
    String QUEUE_NAME = "queue_%s";

    // 默认消息超时时间
    long DEFAULT_TIMEOUT = 180000;

}

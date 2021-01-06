package com.yzh.rabbitmq.rpc.config;


import com.yzh.rabbitmq.rpc.constant.BasicConstant;
import com.yzh.rabbitmq.rpc.instance.RpcInstance;
import com.yzh.rabbitmq.rpc.message.listener.RpcReceiveListener;
import com.yzh.rabbitmq.rpc.model.RpcBuildParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import java.util.Locale;

/**
 * 核心配置类
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
@Configuration
@ImportResource("classpath:applicationContext.xml")
@Slf4j
public class CoreConfig implements BasicConstant {

    // 构建参数
    private final static RpcBuildParams buildParams = CustomPropertyPlaceholder.getBuildParams();

    // 是否使用bean构建
    private final static boolean parameterized = CustomPropertyPlaceholder.isParameterized();

    @Autowired
    private CustomPropertyPlaceholder customPropertyPlaceholder;

    @Value("${localId}")
    private String localId;

    @Value("${brokerUrl}")
    private String brokerUrl;

    @Value("${brokerUsername}")
    private String brokerUsername;

    @Value("${brokerPassword}")
    private String brokerPassword;

    // 注入connectionFactory对象
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(parameterized ? buildParams.getBrokerUrl() : brokerUrl);
        connectionFactory.setUsername(parameterized ? buildParams.getBrokerUsername() : brokerUsername);
        connectionFactory.setPassword(parameterized ? buildParams.getBrokerPassword() : brokerPassword);
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }

    // 声明交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    // 声明队列
    @Bean
    public Queue directQueue() {
        return new Queue(String.format(Locale.ROOT, QUEUE_NAME, parameterized ? buildParams.getLocalId() : localId),true);
    }

    // 绑定交换机和队列
    @Bean
    public Binding binding() {
        // 这边绑定
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(parameterized ? buildParams.getLocalId() : localId);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    // 创建初始化RabbitAdmin对象
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        // 只有设置为 true，spring 才会加载 RabbitAdmin 这个类
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    // 消息监听器
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        // 监听的队列
        container.setQueues(directQueue());
        // 设置监听器
        RpcReceiveListener listener = new RpcReceiveListener();
        container.setMessageListener(listener);
        return container;
    }

    /**
     * 核心调度类
     *
     * @return rpcInstance实例对象
     */
    @Bean
    public RpcInstance rpcInstance() {
        RpcInstance rpcInstance = new RpcInstance();
        rpcInstance.setPropertySourcesPlaceholderConfigurer(customPropertyPlaceholder);
        rpcInstance.setRabbitTemplate(rabbitTemplate());
        return rpcInstance;
    }
}

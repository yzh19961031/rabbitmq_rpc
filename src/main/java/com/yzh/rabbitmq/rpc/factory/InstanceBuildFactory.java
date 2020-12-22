package com.yzh.rabbitmq.rpc.factory;

import com.yzh.rabbitmq.rpc.config.CoreConfig;
import com.yzh.rabbitmq.rpc.config.CustomPropertyPlaceholder;
import com.yzh.rabbitmq.rpc.instance.RpcInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 实例构建工厂
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
public class InstanceBuildFactory {

    private InstanceBuildFactory() {}


    /**
     * 通过指定配置文件路径构建rpc实例
     *
     * @param propsFile 配置文件路径
     * @return rpc实例
     */
    public synchronized static RpcInstance buildWithPropsFile(String propsFile) {
        CustomPropertyPlaceholder.setPropsFile(propsFile);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CoreConfig.class);
        return (RpcInstance) applicationContext.getBean("rpcInstance");
    }

    /**
     * 通过默认配置文件路径构建rpc实例
     *
     * @return rpc实例
     */
    public synchronized static RpcInstance buildWithDefaultFile() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(CoreConfig.class);
        return (RpcInstance) applicationContext.getBean("rpcInstance");
    }

}
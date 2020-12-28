package com.yzh.rabbitmq.rpc.factory;

import com.yzh.rabbitmq.rpc.config.CoreConfig;
import com.yzh.rabbitmq.rpc.config.CustomPropertyPlaceholder;
import com.yzh.rabbitmq.rpc.instance.RpcInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * 实例构建工厂
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
public class InstanceBuildFactory {
    private static AbstractApplicationContext context;

    public static void setContext(AbstractApplicationContext context) {
        InstanceBuildFactory.context = context;
    }

    private InstanceBuildFactory() {}


    /**
     * 通过指定配置文件路径构建rpc实例
     *
     * @param propsFile 配置文件路径
     * @return rpc实例
     */
    public synchronized static RpcInstance buildWithPropsFile(String propsFile) {
        CustomPropertyPlaceholder.setPropsFile(propsFile);
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(CoreConfig.class);
        setContext(applicationContext);
        return (RpcInstance) context.getBean("rpcInstance");
    }

    /**
     * 通过默认配置文件路径构建rpc实例
     *
     * @return rpc实例
     */
    public synchronized static RpcInstance buildWithDefaultFile() {
        AbstractApplicationContext applicationContext = new AnnotationConfigApplicationContext(CoreConfig.class);
        setContext(applicationContext);
        return (RpcInstance) context.getBean("rpcInstance");
    }

    /**
     * 关闭applicationContext
     */
    public synchronized static void destroy() {
        if (context != null) {
            context.close();
        }
    }

}
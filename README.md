# rabbitmq_rpc
hello rabbitmq_rpc

## 起步

### 环境配置

1. 客户端与服务端都需要安装RabbitMQ。
2. 需要有Java环境。

## 开始使用

### 配置文件信息

这边启动的话，需要用户自己指定一个配置文件的路径，如果不指定的话，默认会使用当前目录的node.properties。后续考虑会增加使用bean类的方式来启动。配置文件的格式如下：

```properties
localId=test_rpc1 # 指定启动的实例名称  这边其实就是对应rabbitmq上面的direct交换机的routingkey
brokerUrl=192.168.1.108:5672 # url信息
brokerUsername=guest # rabbitmq 用户 默认
brokerPassword=guest # rabbitmq 密码 默认
```

### 服务端

项目中，主要的逻辑都在com.yzh.rabbitmq.rpc.RpcMaster这个主类中，包括主程序启动，主程序关闭，订阅监听消息，请求指定的监听方法等等。

下面提供一个简单的示例，去获取服务端机器上os以及version的信息。

- 首先需要注册到rabbitmq服务器，使用com.yzh.rabbitmq.rpc.RpcMaster#register(java.lang.String)方法，参数指定一个配置文件的路径。
- 然后需要订阅监听消息的名称以及名称对应的监听方法，我这边目前提供了两种可以继承的模板监听器：XMLGeneralMessageListener用于处理XML格式的消息，ObjectGeneralMessageListener用于处理对象类型的消息。我们自定义的监听器需要继承指定模板，重写handle方法即可。

代码示例：

```java
/**
 * 服务端
 *
 * @author yuanzhihao
 * @since 2020/12/23
 */
@Slf4j
public class Server {

    public static void main(String[] args) {
        // 指定配置文件路径
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node1.properties";
        // 注册到rabbitmq服务器
        RpcMaster.register(propsFile);
        // 订阅消息使用对于的监听方法
        String messageName = "getSystemInfo";
        RpcMaster.subscribe(messageName, new SystemInfoMessageListener());
    }

    /**
     *
     * 获取操作系统信息
     * 消息名称: getSystemInfo
     * 请求消息体:
     * <getSystemInfo/>
     * 响应消息:
     * <system>
     *  <os>Mac OS X</os>
     *  <version>10.15.6</version>
     * </system>
     */
    private static class SystemInfoMessageListener extends XMLGeneralMessageListener {

        @Override
        public String handle(Document document) {
            log.info("receive a message {}", document.asXML());
            String os = getOs();
            String osVersion = getOsVersion();
            Document responseDocument = DocumentHelper.createDocument();
            Element system = responseDocument.addElement("system");
            system.addElement("os").setText(os);
            system.addElement("version").setText(osVersion);
            return responseDocument.asXML();
        }

        private String getOs() {
            return System.getProperty("os.name");
        }

        private String getOsVersion() {
            return System.getProperty("os.version");
        }
    }
}

```

这边提供了一个XML的消息格式，还有对象格式的消息使用可以参考项目代码中com.yzh.rabbitmq.rpc.main.object.Client以及com.yzh.rabbitmq.rpc.main.object.Server两个类。

### 客户端

客户端的启动与服务端类似，同样需要注册到rabbitmq服务器同样需要注册到rabbitmq服务器。多了一个请求方法的步骤。具体如下

- 注册rabbitmq服务器。
- 使用com.yzh.rabbitmq.rpc.RpcMaster#request方法，方法的三个参数分别是目标端的ID，就是配置文件中localId的信息，需要请求的方法名，请求的参数信息。该方法有一个返回值，就是对应远端调用方法的返回值。

代码示例：

```java
/**
 * @author yuanzhihao
 * @since 2020/12/28
 */
public class Client {

    public static void main(String[] args) {
       	// 指定配置文件路径
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node.properties";
        RpcMaster.register(propsFile);
        // 创建请求的参数信息
        Document document = DocumentHelper.createDocument();
        document.addElement("getSystemInfo");
        // 指定请求方法名称  请求的目标端id
        String messageName = "getSystemInfo";
        String destId = "test_rpc2";
        GeneralMessage message = RpcMaster.request(destId, messageName, document.asXML());
        assert message != null;
        // 打印对应的返回信息
        System.out.println(message.getContent());
        RpcMaster.destroy();
    }
}
```

## 后期优化

目前我也是简单实现了功能，还有很多优化的点，这边暂时罗列一下，等待后续整改。

1. 提供配置文件以及bean类两种方式启动程序。
2. 日志优化，支持打印相关消息详情等。
3. 异常处理优化，目前异常都是简单记录日志。
4. 并发环境下的使用验证。
5. ......
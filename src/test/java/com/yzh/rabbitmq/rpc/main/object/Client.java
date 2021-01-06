package com.yzh.rabbitmq.rpc.main.object;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.main.object.bean.SystemInfo;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.model.RpcBuildParams;

/**
 * 客户端
 *
 * @author yuanzhihao
 * @since 2020/12/29
 */
public class Client {
    // localId=test_rpc1
    // brokerUrl=192.168.1.108:5672
    // brokerUsername=guest
    // brokerPassword=guest
    private static final String localId = "test_rpc1";
    private static final String brokerUrl = "192.168.1.108:5672";
    private static final String brokerUsername = "guest";
    private static final String brokerPassword = "guest";

    public static void main(String[] args) {
        // 这边提供两种方法  可以指定配置文件路径  或者提供配置类信息
        // String propsFile = "/Users/yuanzhihao/Desktop/tmp/node.properties";
        // RpcMaster.register(propsFile);
        RpcBuildParams buildParams = new RpcBuildParams(localId, brokerUrl, brokerUsername, brokerPassword);
        RpcMaster.register(buildParams);
        String messageName = "getSystemInfo.object";
        String destId = "test_rpc2";
        GeneralMessage message = RpcMaster.request(destId, messageName, null);
        assert message != null;
        SystemInfo systemInfo = (SystemInfo) message.getContent();
        System.out.println("systemInfo is " + systemInfo);
        RpcMaster.destroy();
    }
}


package com.yzh.rabbitmq.rpc.main.object;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.main.object.bean.SystemInfo;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;

/**
 * 客户端
 *
 * @author yuanzhihao
 * @since 2020/12/29
 */
public class Client {

    public static void main(String[] args) {
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node.properties";
        RpcMaster.register(propsFile);
        String messageName = "getSystemInfo.object";
        String destId = "test_rpc2";
        GeneralMessage message = RpcMaster.request(destId, messageName, null);
        assert message != null;
        SystemInfo systemInfo = (SystemInfo) message.getContent();
        System.out.println("systemInfo is " + systemInfo);
        RpcMaster.destroy();
    }
}


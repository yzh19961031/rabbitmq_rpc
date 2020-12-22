package com.yzh.rabbitmq.rpc.test;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;

/**
 * 客户端
 *
 * @author yuanzhihao
 * @since 2020/12/23
 */
public class Client {

    public static void main(String[] args) {
        RpcMaster.register();
        String text = "hello world";
        String messageName = "hello";
        String destId = "test_rpc2";
        GeneralMessage message = RpcMaster.request(destId, messageName, text);
        assert message != null;
        System.out.println(message.getContent());
    }
}

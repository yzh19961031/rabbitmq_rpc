package com.yzh.rabbitmq.rpc.test;

import com.yzh.rabbitmq.rpc.RpcMaster;


/**
 * 服务端
 *
 * @author yuanzhihao
 * @since 2020/12/23
 */
public class Server {

    public static void main(String[] args) {
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node1.properties";
        RpcMaster.register(propsFile);
        String messageName = "hello";
        RpcMaster.subscribe(messageName, message -> {
            System.out.println(message);
            String replyMessage = "wocao";
            RpcMaster.reply(message, replyMessage);
        });
    }
}

package com.yzh.rabbitmq.rpc.main.xml;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

/**
 * @author yuanzhihao
 * @since 2020/12/28
 */
public class Client {

    public static void main(String[] args) {
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node.properties";
        RpcMaster.register(propsFile);
        Document document = DocumentHelper.createDocument();
        document.addElement("getSystemInfo");
        String messageName = "getSystemInfo";
        String destId = "test_rpc2";
        GeneralMessage message = RpcMaster.request(destId, messageName, document.asXML());
        assert message != null;
        System.out.println(message.getContent());
        RpcMaster.destroy();
    }

}

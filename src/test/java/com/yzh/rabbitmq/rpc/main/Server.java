package com.yzh.rabbitmq.rpc.main;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.message.listener.template.XMLGeneralMessageListener;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


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
        String messageName = "getSystemInfo";
        RpcMaster.subscribe(messageName, new SystemInfoMessageListener());
    }

    // 获取操作系统信息
    // 消息名称: getSystemInfo
    // 请求消息体:
    // <getSystemInfo/>
    // 响应消息:
    // <system>
    //  <os>Mac OS X</os>
    //  <version>10.15.6</version>
    // </system>
    private static class SystemInfoMessageListener extends XMLGeneralMessageListener {

        @Override
        public String handle(Document document) {
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

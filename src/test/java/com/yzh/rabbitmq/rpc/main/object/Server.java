package com.yzh.rabbitmq.rpc.main.object;

import com.yzh.rabbitmq.rpc.RpcMaster;
import com.yzh.rabbitmq.rpc.main.object.bean.SystemInfo;
import com.yzh.rabbitmq.rpc.message.listener.template.ObjectGeneralMessageListener;
import com.yzh.rabbitmq.rpc.message.listener.template.XMLGeneralMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.Serializable;


/**
 * 服务端
 *
 * @author yuanzhihao
 * @since 2020/12/23
 */
@Slf4j
public class Server {

    public static void main(String[] args) {
        String propsFile = "/Users/yuanzhihao/Desktop/tmp/node1.properties";
        RpcMaster.register(propsFile);
        String messageName = "getSystemInfo.object";
        RpcMaster.subscribe(messageName, new SystemInfoMessageListener());
    }


    private static class SystemInfoMessageListener extends ObjectGeneralMessageListener {

        @Override
        public Serializable handle(Serializable document) {
            // 返回对象
            return new SystemInfo(getOs(), getOsVersion());
        }

        private String getOs() {
            return System.getProperty("os.name");
        }

        private String getOsVersion() {
            return System.getProperty("os.version");
        }
    }

}

package com.yzh.rabbitmq.rpc;

import com.yzh.rabbitmq.rpc.factory.InstanceBuildFactory;
import com.yzh.rabbitmq.rpc.instance.RpcInstance;
import com.yzh.rabbitmq.rpc.message.listener.GeneralMessageListener;
import com.yzh.rabbitmq.rpc.model.GeneralMessage;
import com.yzh.rabbitmq.rpc.model.RpcBuildParams;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 主程序
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
@Slf4j
public class RpcMaster {
    private static RpcInstance rpcInstance;

    private RpcMaster() {}

    public static void register(String propsFile) {
        if (rpcInstance == null) {
            rpcInstance = InstanceBuildFactory.buildWithPropsFile(propsFile);
        } else {
            log.error("RpcMaster already started.");
        }
    }

    public static void register(RpcBuildParams rpcBuildParams) {
        if (rpcInstance == null) {
            rpcInstance = InstanceBuildFactory.buildWithParams(rpcBuildParams);
        } else {
            log.error("RpcMaster already started.");
        }
    }

    public static GeneralMessage request(String destId, String msgName, Serializable msgContent) {
        if (rpcInstance == null) {
            log.error("RpcMaster has not been started.");
            return null;
        }
        return rpcInstance.request(destId, msgName, msgContent);
    }

    public static void subscribe(String msgName, GeneralMessageListener listener) {
        if (rpcInstance == null) {
            log.error("RpcMaster has not been started.");
        } else {
            rpcInstance.subscribe(msgName, listener);
        }
    }

    public static void unSubscribe(String msgName) {
        if (rpcInstance == null) {
            log.error("RpcMaster has not been started.");
        } else {
            rpcInstance.unSubscribe(msgName);
        }
    }

    public static void reply(GeneralMessage requestMessage, Serializable msgContent) {
        if (rpcInstance == null) {
            log.error("RpcMaster has not been started.");
        } else {
            rpcInstance.reply(requestMessage, msgContent);
        }
    }

    public static void destroy() {
        InstanceBuildFactory.destroy();
        rpcInstance = null;
    }

}

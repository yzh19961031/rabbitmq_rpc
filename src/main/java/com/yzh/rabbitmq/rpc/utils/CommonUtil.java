package com.yzh.rabbitmq.rpc.utils;

import java.util.UUID;

/**
 * 工具类
 *
 * @author yuanzhihao
 * @since 2020/12/20
 */
public class CommonUtil {

    /**
     * 生成id
     *
     * @return id
     */
    public static String generateId() {
        return UUID.randomUUID().toString().replace("-","");
    }

}

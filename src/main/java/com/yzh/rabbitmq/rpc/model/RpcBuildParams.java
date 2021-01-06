package com.yzh.rabbitmq.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 构建参数
 *
 * @author yuanzhihao
 * @since 2020/12/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcBuildParams {
    private String localId;

    private String brokerUrl;

    private String brokerUsername;

    private String brokerPassword;
}

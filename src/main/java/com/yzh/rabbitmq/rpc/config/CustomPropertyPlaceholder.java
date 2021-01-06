package com.yzh.rabbitmq.rpc.config;

import com.yzh.rabbitmq.rpc.exception.FailureInfo;
import com.yzh.rabbitmq.rpc.exception.IllegalParamException;
import com.yzh.rabbitmq.rpc.model.RpcBuildParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Objects;

/**
 * 自定义配置类
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@Slf4j
public class CustomPropertyPlaceholder extends PropertySourcesPlaceholderConfigurer implements InitializingBean {
    // 用户自定义配置文件路径
    private static String propsFile;

    // 构建参数
    private static RpcBuildParams buildParams;

    // 是否是参数化的
    private static boolean parameterized = false;


    // 配置文件
    private Resource[] locations;

    public static void setPropsFile(String propsFile) {
        CustomPropertyPlaceholder.propsFile = propsFile;
    }

    public static boolean isParameterized() {
        return parameterized;
    }


    public static RpcBuildParams getBuildParams() {
        return buildParams;
    }

    public static void setBuildParams(RpcBuildParams buildParams) {
        if (checkBuildParamsIllegal(buildParams)) {
            log.error("Params is illegal");
            throw new IllegalParamException(FailureInfo.ILLEGAL_PARAM);
        }
        CustomPropertyPlaceholder.buildParams = buildParams;
        parameterized = true;
    }

    /**
     * 检查参数是否合法
     *
     * @param buildParams 构建参数
     * @return T 合法 F 非法
     */
    private static boolean checkBuildParamsIllegal(RpcBuildParams buildParams) {
        return StringUtils.isAnyEmpty(buildParams.getLocalId(), buildParams.getBrokerUrl(), buildParams.getBrokerUsername(), buildParams.getBrokerPassword());
    }

    // 在bean被加载的时候 判断下用户是否自定定义了配置文件
    @Override
    public void afterPropertiesSet() {
        if (StringUtils.isNotEmpty(propsFile)) {
            if (locations != null && locations.length > 0) {
                // 覆盖默认的配置文件路径
                locations[0] = new FileSystemResource(propsFile);
                // 重新设置一次locations
                super.setLocations(locations);
            }
        }
    }

    @Override
    public void setLocations(Resource... locations) {
        this.locations = locations;
        super.setLocations(locations);
    }

    /**
     * 获取指定属性值
     *
     * @param propertyName 配置属性名称
     * @return 属性值
     */
    public String getPropertyValue(String propertyName) {
        return (String) Objects.requireNonNull(super.getAppliedPropertySources().get("localProperties")).getProperty(propertyName);
    }

    /**
     * 获得localId信息
     *
     * @return localId
     */
    public String getLocalId() {
        return getPropertyValue("localId");
    }

}

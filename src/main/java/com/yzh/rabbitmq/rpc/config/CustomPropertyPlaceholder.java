package com.yzh.rabbitmq.rpc.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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

    // 自定义参数
    private static final Map<String, String> PARAM_STORE = new ConcurrentHashMap<>();

    // 配置文件
    private Resource[] locations;

    public static void setPropsFile(String propsFile) {
        CustomPropertyPlaceholder.propsFile = propsFile;
    }

    // 在bean被加载的时候 判断下用户是否自定定义了配置文件
    @Override
    public void afterPropertiesSet() throws Exception {
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

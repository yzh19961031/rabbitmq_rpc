package com.yzh.rabbitmq.rpc.test;

import com.yzh.rabbitmq.rpc.config.CoreConfig;
import com.yzh.rabbitmq.rpc.config.CustomPropertyPlaceholder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 测试类
 *
 * @author yuanzhihao
 * @since 2020/12/19
 */
@ContextConfiguration(classes = {CoreConfig.class})
@RunWith(SpringRunner.class)
public class MainTest {
    private static final Logger log = LoggerFactory.getLogger(MainTest.class);

    @Autowired
    private CustomPropertyPlaceholder customPropertyPlaceholder;

    @Test
    public void test01() {
        System.out.println(customPropertyPlaceholder);
        System.out.println("");
    }

}

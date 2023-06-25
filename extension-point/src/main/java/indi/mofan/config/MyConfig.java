package indi.mofan.config;

import indi.mofan.pojo.MyAnotherProperties;
import indi.mofan.pojo.MyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2022/10/27 10:51
 */
@Configuration
@EnableConfigurationProperties({MyProperties.class, MyAnotherProperties.class})
public class MyConfig {
}

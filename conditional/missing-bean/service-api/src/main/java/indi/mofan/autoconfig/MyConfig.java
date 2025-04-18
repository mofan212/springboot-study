package indi.mofan.autoconfig;


import indi.mofan.service.registry.MyBeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;

/**
 * @author mofan
 * @date 2025/4/17 14:08
 */
// @Configuration
// @AutoConfigureAfter(DubboAutoConfiguration.class)
public class MyConfig {
    @Bean
    public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor() {
        return new MyBeanDefinitionRegistryPostProcessor();
    }
}

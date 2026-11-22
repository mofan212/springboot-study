package indi.mofan.autoconfigure.helloworld;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author mofan
 * @date 2026/11/15 17:59
 */
@AutoConfiguration
@ConditionalOnClass(HelloWorldService.class)
@ConditionalOnProperty(prefix = "demo.hello-world", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(HelloWorldProperties.class)
public class HelloWorldAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public HelloWorldService helloWorldService(HelloWorldProperties properties) {
        return new HelloWorldService(properties.getPrefix());
    }
}

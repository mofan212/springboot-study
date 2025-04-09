package indi.mofan.config;


import indi.mofan.component.DefaultComponent;
import indi.mofan.service.MyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2025/4/9 16:22
 */
@Configuration
public class DefaultServiceConfig {

    @Bean
    @DubboService
    @ConditionalOnMissingBean(MyService.class)
    public MyService defaultComponent() {
        return new DefaultComponent();
    }
}

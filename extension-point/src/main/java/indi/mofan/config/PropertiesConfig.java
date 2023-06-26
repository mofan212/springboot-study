package indi.mofan.config;

import indi.mofan.convert.WeightConvertor;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2023/6/26 10:52
 */
@Configuration
public class PropertiesConfig {
    @Bean
    @ConfigurationPropertiesBinding
    public WeightConvertor weightConvertor() {
        return new WeightConvertor();
    }
}

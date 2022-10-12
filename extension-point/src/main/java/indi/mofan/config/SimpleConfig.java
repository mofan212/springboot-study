package indi.mofan.config;

import indi.mofan.bean.EmployeeFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2022/10/12 20:24
 */
@Configuration
public class SimpleConfig {

    @Bean
    public EmployeeFactoryBean getEmployeeFactoryBean() {
        return new EmployeeFactoryBean();
    }
}

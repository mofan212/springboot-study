package indi.mofan.config;

import indi.mofan.bean.EmployeeFactoryBean;
import org.springframework.context.annotation.Bean;

/**
 * 测试自动装配，不显式将该类交由 Spring 管理
 *
 * @author mofan
 * @date 2022/10/12 20:24
 */
public class SimpleConfig {

    @Bean
    public EmployeeFactoryBean getEmployeeFactoryBean() {
        return new EmployeeFactoryBean();
    }
}

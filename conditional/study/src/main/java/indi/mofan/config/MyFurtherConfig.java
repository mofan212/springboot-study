package indi.mofan.config;


import indi.mofan.component.MyBean;
import indi.mofan.condition.FurtherCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2024/8/18 17:08
 */
@Configuration
@Conditional(FurtherCondition.class)
public class MyFurtherConfig {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }
}

package indi.mofan.config;

import indi.mofan.life.LifeCycle;
import indi.mofan.pojo.Life;
import org.springframework.context.annotation.Bean;

/**
 * @author mofan
 * @date 2022/10/11 21:19
 */
public class LifeCycleConfig {
    @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")
    public LifeCycle getLifeCycle() {
        return new LifeCycle();
    }

    @Bean
    public Life getLife() {
        return new Life();
    }
}

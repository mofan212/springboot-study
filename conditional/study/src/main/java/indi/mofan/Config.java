package indi.mofan;


import indi.mofan.component.MyDao;
import indi.mofan.component.MyRepository;
import indi.mofan.component.MyService;
import indi.mofan.condition.ServiceCondition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author mofan
 * @date 2024/8/18 17:09
 */
@ComponentScan
@Configuration
@Import(MyDao.class)
public class Config {

    @Bean
    @Conditional(ServiceCondition.class)
    public MyService myService() {
        return new MyService();
    }

    @Bean
    public MyRepository myRepository() {
        return new MyRepository();
    }
}

package indi.mofan.summary.a04;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/5/26 15:09
 */
@SpringBootApplication
public class A04ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A04ExtensionPoint.class, args);
        context.close();
    }

    @Getter
    @Setter
    static class Fish implements InitializingBean {
        private Double weight;
        private Double price;

        public Fish() {
            System.out.println("1. 执行了无参构造");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("3. 执行了 afterPropertiesSet() 方法");
        }

        public void init() {
            System.out.println("4. 执行了 init() 方法");
        }
    }

    @Configuration
    static class A04Configuration {
        @Bean(initMethod = "init")
        public Fish fish() {
            return new Fish();
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if ("fish".equals(beanName)) {
                System.out.println("2. 执行了 postProcessBeforeInitialization() 方法");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if ("fish".equals(beanName)) {
                System.out.println("5. 执行了 postProcessAfterInitialization() 方法");
            }
            return bean;
        }
    }
}

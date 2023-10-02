package indi.mofan.summary.a09;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author mofan
 * @date 2023/5/29 16:31
 */
@SpringBootApplication
public class A09ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A09ExtensionPoint.class, args);
        context.close();
    }

    private static final String EXAMPLE_BEAN_NAME = "a08-example";

    @Component
    static class A08Dependence {
    }

    static class A08Example implements InitializingBean {
        private A08Dependence dependence;

        @Autowired
        public void setDependence(A08Dependence dependence) {
            this.dependence = dependence;
            System.out.println("1. A08Example 注入 dependence");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("4. 执行了 afterPropertiesSet 方法");
        }

        @PostConstruct
        public void init() {
            System.out.println("3. 执行了 init 方法");
        }

        public void initMethod() {
            System.out.println("5. 执行了 init-method");
        }
    }

    @Configuration
    static class MyConfig {
        @Bean(value = EXAMPLE_BEAN_NAME, initMethod = "initMethod")
        public A08Example a08Example() {
            return new A08Example();
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("2. 执行了 postProcessBeforeInitialization 方法");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("6. 执行了 postProcessAfterInitialization 方法");
            }
            return bean;
        }
    }
}

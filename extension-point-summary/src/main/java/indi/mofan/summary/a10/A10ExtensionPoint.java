package indi.mofan.summary.a10;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/5/29 17:08
 */
@SpringBootApplication
public class A10ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A10ExtensionPoint.class, args);
        context.close();
    }

    private static final String BEAN_NAME = "myBean";

    @Component(BEAN_NAME)
    static class MySmartInitializingSingleton implements InitializingBean, SmartInitializingSingleton {
        @Override
        public void afterPropertiesSet() {
            System.out.println("1. 执行了 afterPropertiesSet 方法");
        }

        @Override
        public void afterSingletonsInstantiated() {
            System.out.println("3. 执行了 afterSingletonsInstantiated 方法");
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAME.equals(beanName)) {
                System.out.println("2. 执行了 postProcessAfterInitialization 方法");
            }
            return bean;
        }
    }
}

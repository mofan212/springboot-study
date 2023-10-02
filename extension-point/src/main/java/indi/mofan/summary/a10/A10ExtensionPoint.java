package indi.mofan.summary.a10;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
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
        // context.close(); 不直接关闭容器，而是通过 System.exit() 关闭，此时会触发 shutdown hooks
        System.out.printf("[%s]: main 方法执行完毕，准备关闭容器\n", Thread.currentThread().getName());
        System.exit(0);
    }

    @Component
    static class A09Dependence {
    }

    private static final String BEAN_NAME = "myBean";

    @Component(BEAN_NAME)
    static class MySmartInitializingSingleton implements InitializingBean, SmartInitializingSingleton, DisposableBean {
        private A09Dependence dependence;

        @Autowired
        public void setDependence(A09Dependence dependence) {
            this.dependence = dependence;
            System.out.println("1. MySmartInitializingSingleton 注入 dependence");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("3. 执行了 afterPropertiesSet 方法");
        }

        @Override
        public void afterSingletonsInstantiated() {
            System.out.println("5. 执行了 afterSingletonsInstantiated 方法");
        }

        @Override
        public void destroy() throws Exception {
            System.out.printf("[%s]: 6. 执行了 destroy 方法\n", Thread.currentThread().getName());
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAME.equals(beanName)) {
                System.out.println("2. 执行了 postProcessBeforeInitialization 方法");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAME.equals(beanName)) {
                System.out.println("4. 执行了 postProcessAfterInitialization 方法");
            }
            return bean;
        }
    }
}

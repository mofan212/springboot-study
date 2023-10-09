package indi.mofan.summary.a08;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author mofan
 * @date 2023/5/29 16:18
 */
@SpringBootApplication
public class A08ExtensionPoint {

    private static final String BEAN_NAME = "a07Example";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A08ExtensionPoint.class, args);
        context.close();
    }

    @Component
    static class A07Dependence {
        public A07Dependence() {
            System.out.println("1. 执行了 A07Dependence 的无参构造");
        }
    }

    @Component(BEAN_NAME)
    static class A07Example {
        private A07Dependence dependence;

        public A07Example(A07Dependence dependence) {
            System.out.println("2. 执行了 A07Example 的无参构造");
        }

        @Autowired
        public void setDependence(A07Dependence dependence) {
            this.dependence = dependence;
            System.out.println("3. A07Example 注入 dependence");
        }

        @PostConstruct
        public void init() {
            System.out.println("5. 执行了 @PostConstruct 标记的方法");
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAME.equals(beanName)) {
                System.out.println("4. 执行了 postProcessBeforeInitialization 方法");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAME.equals(beanName)) {
                System.out.println("6. 执行了 postProcessBeforeInitialization 方法");
            }
            return bean;
        }
    }

}

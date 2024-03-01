package indi.mofan.summary.a05;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author mofan
 * @date 2023/5/26 15:48
 */
@SpringBootApplication
public class A05ExtensionPoint {

    private static final String EXAMPLE_BEAN_NAME = "a05-example";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A05ExtensionPoint.class, args);
        Assert.isTrue(context.getBean(Example.class).getStr().equals("str"), "");
        Assert.isTrue(context.getBean(Example.class).getDependence().equals(context.getBean(Dependence.class)), "");
        context.close();
    }

    @Getter
    @Setter
    static class Example {
        private String str = "example";
        private Dependence dependence;

        @Autowired
        public void setDependence(Dependence dependence) {
            System.out.println("6. Example 注入 dependence");
            this.dependence = dependence;
        }

        public void setStr(String str) {
            System.out.println("7. Example 注入 str");
            this.str = str;
        }

        public Example() {
            System.out.println("3. 执行了 Example 的无参构造");
        }

        public void init() {
            System.out.println("9. 执行了 init() 方法");
        }
    }

    @Configuration
    static class A05Configuration {
        @Bean(initMethod = "init", value = EXAMPLE_BEAN_NAME)
        public Example example() {
            return new Example();
        }
    }

    @Component
    static class Dependence {
        public Dependence() {
            System.out.println("1. 执行了 Dependence 的无参构造");
        }
    }

    @Component
    static class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {
        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("2. 执行了 postProcessBeforeInstantiation() 方法");
            }
            // 是否返回其他 Bean 实例？可以返回代理对象，阻止默认实例化
            return null;
        }

        @Override
        public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("4. 执行了 postProcessAfterInstantiation() 方法");
            }
            // 是否需要依赖注入，如果返回 false，则不再执行 postProcessProperties() 方法；
            return true;
        }

        @Override
        public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("5. 执行了 postProcessProperties() 方法");
                MutablePropertyValues propertyValues = (MutablePropertyValues) pvs;
                propertyValues.add("str", "str");
            }
            return pvs;
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("8. 执行了 postProcessBeforeInitialization() 方法");
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (EXAMPLE_BEAN_NAME.equals(beanName)) {
                System.out.println("10. 执行了 postProcessAfterInitialization() 方法");
            }
            return bean;
        }
    }
}

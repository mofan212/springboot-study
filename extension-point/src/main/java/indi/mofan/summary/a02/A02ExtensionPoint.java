package indi.mofan.summary.a02;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/5/22 16:20
 */
@SpringBootApplication
public class A02ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A02ExtensionPoint.class, args);
        Dog dog = context.getBean(Dog.class);
        System.out.println(dog.getName());
        System.out.println(dog.getAge());
        context.close();
    }

    @Getter
    @Setter
    @Component("dog")
    static class Dog {
        private String name = "旺财";
        private Integer age = 2;
    }

    @Component
    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            ScannedGenericBeanDefinition definition = (ScannedGenericBeanDefinition) beanFactory.getBeanDefinition("dog");
            MutablePropertyValues properties = definition.getPropertyValues();
            properties.add("name", "小黑");
        }
    }
}

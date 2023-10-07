package indi.mofan.summary.a02;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

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

    @Component
    static class MyFirstBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

        @Autowired
        private ApplicationContext context;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            Assert.isNull(context, "依赖注入的 ApplicationContext 不是 null");
        }
    }

    @Component
    static class MySecondBeanFactoryPostProcessor implements BeanFactoryPostProcessor, ApplicationContextAware {

        private ApplicationContext context;

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            Assert.notNull(context, "使用 ApplicationContextAware 注入的 ApplicationContext 为 null");
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }
}

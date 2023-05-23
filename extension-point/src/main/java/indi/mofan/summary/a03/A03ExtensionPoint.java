package indi.mofan.summary.a03;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2023/5/23 16:46
 */
@SpringBootApplication
public class A03ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A03ExtensionPoint.class, args);
        Cat cat = context.getBean(Cat.class);
        System.out.println(cat.getName());
        System.out.println(cat.getAge());
        context.close();
    }

    @Getter
    @Setter
    static class Cat {
        private String name;
        private String age;
    }

    @Component
    static class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            beanDefinition.setBeanClass(Cat.class);
            PropertyValue name = new PropertyValue("name", "大橘");
            PropertyValue age = new PropertyValue("age", 1);
            MutablePropertyValues propertyValues = new MutablePropertyValues(Arrays.asList(name, age));
            beanDefinition.setPropertyValues(propertyValues);
            // 手动注册
            beanDefinitionRegistry.registerBeanDefinition("cat", beanDefinition);
        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            RootBeanDefinition beanDefinition = (RootBeanDefinition) beanFactory.getBeanDefinition("cat");
            MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
            System.out.println("原始年龄是: " + propertyValues.get("age"));
            propertyValues.add("age", 2);
        }
    }
}

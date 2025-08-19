package indi.mofan;

import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2023/8/11 10:54
 */
public class BeanFactoryTest implements WithAssertions {

    static class MyBean {
    }

    @Test
    public void testDefaultListableBeanFactory() {
        // 既实现了 BeanFactory，又实现了 BeanDefinitionRegistry
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // ClassPathBeanDefinitionScanner 的一种替代，编程式显式注册 bean
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanFactory);
        reader.registerBean(MyBean.class);
        MyBean bean = beanFactory.getBean(MyBean.class);
        assertThat(bean).isNotNull();
    }

    @Configuration
    static class MyConfig {
        @Bean
        public Children children() {
            return new Children();
        }
    }

    @Getter
    @Setter
    static class Parent {
        private Children children;
    }

    static class Children {
    }

    @Test
    public void testAutowireCapableBeanFactory() {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        // 容器中不存在 Parent 类型的 Bean
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(Parent.class));

        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        // 使用 AutowireCapableBeanFactory 创建一个对象
        Parent parent = (Parent) beanFactory.autowire(Parent.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, false);
        Children children = context.getBean(Children.class);
        // 将 children 注入到了 parent 对象中
        assertThat(parent.getChildren()).isSameAs(children);

        // 容器里还是不存在 parent 类型的对象
        assertThatExceptionOfType(NoSuchBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(Parent.class));
    }
}

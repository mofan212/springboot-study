package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;

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
}

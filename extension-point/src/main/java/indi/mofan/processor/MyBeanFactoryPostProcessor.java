package indi.mofan.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 注册到 Spring 容器后即可禁止循环依赖
 *
 * @author mofan
 * @date 2022/10/12 19:48
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // 禁止循环依赖
        ((DefaultListableBeanFactory) beanFactory).setAllowCircularReferences(false);
    }
}

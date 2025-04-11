package indi.mofan.registry;


import indi.mofan.constant.BeanNameConstant;
import indi.mofan.service.MyService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2025/3/26 16:45
 */
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ConfigurableListableBeanFactory beanFactory = (ConfigurableListableBeanFactory) registry;
        String[] beanNames = beanFactory.getBeanNamesForType(MyService.class);
        long distinctClassCount = Arrays.stream(beanNames)
                // 通过 Bean 定义获取 Class 类型（不触发初始化）
                .map(beanFactory::getBeanDefinition)
                .map(BeanDefinition::getBeanClassName)
                .distinct()
                .count();
        // 有多种类型的 Bean，并且包含默认 Bean
        if (distinctClassCount > 1 && ArrayUtils.contains(beanNames, BeanNameConstant.DEFAULT_BEAN)) {
            registry.removeBeanDefinition(BeanNameConstant.DEFAULT_BEAN);
        }
    }
}

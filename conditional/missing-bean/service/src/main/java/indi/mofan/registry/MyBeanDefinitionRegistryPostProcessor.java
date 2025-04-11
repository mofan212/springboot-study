package indi.mofan.registry;


import indi.mofan.service.MyService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author mofan
 * @date 2025/3/26 16:45
 */
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ListableBeanFactory beanFactory = (ListableBeanFactory) registry;
        Map<String, MyService> beanMap = beanFactory.getBeansOfType(MyService.class);
        // 有多种类型的 Bean，并且包含默认 Bean
        if (beanMap.values().stream().map(MyService::getClass).distinct().count() > 1
            && beanMap.containsKey("defaultComponent")) {
            registry.removeBeanDefinition("defaultComponent");
        }
    }
}

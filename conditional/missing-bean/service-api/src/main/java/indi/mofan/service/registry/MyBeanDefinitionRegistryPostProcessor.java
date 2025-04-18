package indi.mofan.service.registry;


import indi.mofan.service.c.MyComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/4/17 11:24
 */
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        ListableBeanFactory beanFactory = (ListableBeanFactory) registry;
        String[] beanNames = beanFactory.getBeanNamesForType(MyComponent.class);
        if (beanNames.length > 1 && ArrayUtils.contains(beanNames, "defaultComponent")) {
            registry.removeBeanDefinition("defaultComponent");
        }
    }
}

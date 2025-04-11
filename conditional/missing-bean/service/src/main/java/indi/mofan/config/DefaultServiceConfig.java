package indi.mofan.config;


import indi.mofan.constant.BeanNameConstant;
import indi.mofan.service.InjectComponent;
import indi.mofan.service.MyService;
import indi.mofan.service.impl.DefaultServiceImpl;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2025/4/9 16:22
 */
@Configuration
public class DefaultServiceConfig {

    @Bean
    @DubboService
    @Conditional(DefaultComponentCondition.class)
    public MyService defaultServiceImplForDubbo(InjectComponent injectComponent) {
        DefaultServiceImpl service = new DefaultServiceImpl();
        service.setInjectComponent(injectComponent);
        return service;
    }

    public static class DefaultComponentCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return matchCondition(context, metadata, MyService.class, BeanNameConstant.DEFAULT_BEAN);
        }
    }

    static <T> boolean matchCondition(ConditionContext context,
                                      AnnotatedTypeMetadata metadata,
                                      Class<T> clazz,
                                      String defaultBeanName) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        if (beanFactory == null) {
            return false;
        }

        // 获取所有 Bean 名称（不触发初始化）
        String[] beanNames = beanFactory.getBeanNamesForType(clazz);

        // 统计不同的实现类数量
        long distinctClassCount = Arrays.stream(beanNames)
                // 排除默认 Bean
                .filter(name -> !name.equals(defaultBeanName))
                .map(name -> {
                    // 通过 Bean 定义获取 Class 类型（不触发初始化）
                    BeanDefinition definition = beanFactory.getBeanDefinition(name);
                    return definition.getResolvableType().getType();
                })
                .distinct()
                .count();

        // 条件逻辑：当没有其他实现类时，才注册默认 Bean
        return distinctClassCount == 0;
    }
}

package indi.mofan.config.condition;


import indi.mofan.service.MyService;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author mofan
 * @date 2025/3/26 15:35
 */
public class MissingMyServiceBeanCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        if (beanFactory == null) {
            return false;
        }
        return beanFactory.getBeanNamesForType(MyService.class).length == 0;
    }
}

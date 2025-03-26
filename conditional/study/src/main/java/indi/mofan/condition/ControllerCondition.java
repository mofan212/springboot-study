package indi.mofan.condition;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author mofan
 * @date 2024/8/18 17:01
 */
@Slf4j
public class ControllerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        log.info(this.getClass().getName());
        return true;
    }
}

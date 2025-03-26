package indi.mofan.config.component;


import indi.mofan.config.condition.MissingMyServiceBeanCondition;
import indi.mofan.service.MyService;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2025/3/26 15:34
 */
@Component
@Conditional(MissingMyServiceBeanCondition.class)
public class CustomComponent implements MyService {
}

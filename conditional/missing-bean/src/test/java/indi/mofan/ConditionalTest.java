package indi.mofan;


import indi.mofan.config.FirstConfig;
import indi.mofan.registry.SecondConfig;
import indi.mofan.registry.component.CustomComponent;
import indi.mofan.registry.component.MyComponent;
import indi.mofan.service.MyService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mofan
 * @date 2025/3/26 15:37
 */
public class ConditionalTest implements WithAssertions {
    @Test
    public void testFirstImpl() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FirstConfig.class);
        String[] beanNames = context.getBeanNamesForType(MyService.class);
        assertThat(beanNames).hasSize(2);
    }

    @Test
    public void testSecondImpl() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SecondConfig.class);
        String[] beanNames = context.getBeanNamesForType(MyService.class);
        assertThat(beanNames).hasSize(1)
                .contains("customComponent");

        MyComponent component = context.getBean(MyComponent.class);
        assertThat(component).isNotNull()
                .extracting(MyComponent::getMyService)
                .isInstanceOf(CustomComponent.class);
    }
}

package indi.mofan;


import indi.mofan.component.CustomComponent;
import indi.mofan.component.MyComponent;
import indi.mofan.service.MyService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * @author mofan
 * @date 2025/3/26 15:37
 */
public class ConditionalTest implements WithAssertions {

    @Test
    public void testMissingBean() {
        ApplicationContext context = SpringApplication.run(MissingBeanApplication.class);
        String[] beanNames = context.getBeanNamesForType(MyService.class);
        assertThat(beanNames).hasSize(2)
                .contains("customComponent");

        MyComponent component = context.getBean(MyComponent.class);
        assertThat(component).isNotNull()
                .extracting(MyComponent::getCustomComponent)
                .isNotExactlyInstanceOf(CustomComponent.class);
    }
}

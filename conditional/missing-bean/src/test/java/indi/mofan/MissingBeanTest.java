package indi.mofan;


import indi.mofan.component.CustomComponent;
import indi.mofan.component.MyComponent;
import indi.mofan.service.MyService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author mofan
 * @date 2025/3/26 15:37
 */
@SpringBootTest
public class MissingBeanTest implements WithAssertions {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testMissingBean() {
        String[] beanNames = context.getBeanNamesForType(MyService.class);
        assertThat(beanNames).hasSize(2)
                .contains("customComponent");

        MyComponent component = context.getBean(MyComponent.class);
        assertThat(component).isNotNull()
                .extracting(MyComponent::getCustomComponent)
                .isNotExactlyInstanceOf(CustomComponent.class);
    }
}

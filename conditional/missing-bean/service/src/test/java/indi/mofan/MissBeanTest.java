package indi.mofan;


import indi.mofan.component.MySpringComponent;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author mofan
 * @date 2025/4/11 10:47
 */
@SpringBootTest
public class MissBeanTest implements WithAssertions {
    @Autowired
    private ApplicationContext context;

    @Test
    public void testMissingBean() {
        MySpringComponent component = context.getBean(MySpringComponent.class);
        assertThat(component).isNotNull()
                .extracting(MySpringComponent::getStr)
                .isEqualTo("custom Inject");
    }
}

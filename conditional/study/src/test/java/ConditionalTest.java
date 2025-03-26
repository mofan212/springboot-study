import indi.mofan.Config;
import indi.mofan.component.MyRepository;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mofan
 * @date 2024/8/18 17:00
 */
public class ConditionalTest implements WithAssertions {
    @Test
    public void testConditional() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        MyRepository repository = context.getBean(MyRepository.class);
        // 尽管 Condition 返回了 false，但是容器中还是有对应的 Bean
        assertThat(repository).isNotNull();
    }
}

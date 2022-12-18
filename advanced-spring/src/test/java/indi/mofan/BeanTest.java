package indi.mofan;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * @author mofan
 * @date 2022/12/18 16:24
 */
@SpringBootTest
public class BeanTest {
    @Autowired
    private ConfigurableApplicationContext context;

    @Test
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public void testSingletonObjects() {
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        long componentCount = map.entrySet().stream()
                .filter(i -> i.getKey().startsWith("component"))
                .count();
        Assertions.assertEquals(2L, componentCount);
    }

    @Test
    public void testMessageSource() {
        String en = context.getMessage("thanks", null, Locale.ENGLISH);
        Assertions.assertEquals("Thank you", en);

        String zhCN = context.getMessage("thanks", null, Locale.SIMPLIFIED_CHINESE);
        Assertions.assertEquals("谢谢", zhCN);

        String zhTW = context.getMessage("thanks", null, Locale.TRADITIONAL_CHINESE);
        Assertions.assertEquals("謝謝", zhTW);
    }
}

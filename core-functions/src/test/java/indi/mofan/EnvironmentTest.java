package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * @author mofan
 * @date 2023/8/8 11:03
 */
@SpringBootTest(classes = CoreFunctionsTestApplication.class)
public class EnvironmentTest implements WithAssertions {
    @Autowired
    private ApplicationContext context;

    @Test
    public void testGetYamlProperties() {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("name");
        assertThat(property).isEqualTo("mofan");
    }

    @Test
    public void testPropertySource() {
        Map<String, Object> source = Map.of("chinese-name", "默烦");
        PropertySource<Map<String, Object>> propertySource = new MapPropertySource("myPropertySource", source);
        String property = (String) propertySource.getProperty("chinese-name");
        assertThat(property).isEqualTo("默烦");
    }
}

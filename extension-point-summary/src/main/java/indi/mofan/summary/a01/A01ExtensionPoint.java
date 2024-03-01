package indi.mofan.summary.a01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * @author mofan
 * @date 2023/5/21 16:58
 */
@SpringBootApplication
public class A01ExtensionPoint {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(A01ExtensionPoint.class);
        application.addInitializers(new ThirdApplicationContextInitializer());
        ConfigurableApplicationContext context = application.run(args);
        ConfigurableEnvironment environment = context.getEnvironment();
        System.out.println(environment.getProperty("1"));
        System.out.println(environment.getProperty("2"));
        System.out.println(environment.getProperty("3"));
        context.close();
    }

    static class FirstApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            putProperty(context, "1", "ONE");
        }
    }

    static class SecondApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            putProperty(context, "2", "TWO");
        }
    }

    static class ThirdApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            putProperty(context, "3", "THREE");
        }
    }

    private static void putProperty(ConfigurableApplicationContext context, String key, Object value) {
        Map<String, Object> properties = context.getEnvironment().getSystemProperties();
        properties.put(key, value);
    }
}

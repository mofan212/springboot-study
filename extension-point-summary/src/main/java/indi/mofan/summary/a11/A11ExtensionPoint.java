package indi.mofan.summary.a11;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author mofan
 * @date 2023/10/2 22:44
 */
@SpringBootApplication
public class A11ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(A11ExtensionPoint.class);
        context.close();
    }

    private static final String COMMAND_LINE_BEAN = "command-line-bean";
    private static final String APPLICATION_BEAN = "application-bean";
    private static final Set<String> BEAN_NAMES = Set.of(COMMAND_LINE_BEAN, APPLICATION_BEAN);

    @Component(COMMAND_LINE_BEAN)
    static class MyCommandLineRunner implements CommandLineRunner {
        @Override
        public void run(String... args) throws Exception {
            System.out.println("执行 CommandLineRunner");
        }
    }

    @Component(APPLICATION_BEAN)
    static class MyApplicationRunner implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("执行 ApplicationRunner");
        }
    }

    @Component
    static class MyBeanPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (BEAN_NAMES.contains(beanName)) {
                System.out.printf("检测到指定 Bean:[%s] 的 postProcessAfterInitialization 方法%n", beanName);
            }
            return bean;
        }
    }
}

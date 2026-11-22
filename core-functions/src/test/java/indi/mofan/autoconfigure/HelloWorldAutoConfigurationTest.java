package indi.mofan.autoconfigure;

import indi.mofan.autoconfigure.helloworld.HelloWorldAutoConfiguration;
import indi.mofan.autoconfigure.helloworld.HelloWorldService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;

/**
 * @author mofan
 * @date 2026/11/15 17:59
 */
public class HelloWorldAutoConfigurationTest implements WithAssertions {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(HelloWorldAutoConfiguration.class));

    @Test
    public void testDefaultConfigurationCreatesHelloWorldService() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(HelloWorldService.class);
            assertThat(context.getBean(HelloWorldService.class)
                    .sayHello("mofan"))
                    .isEqualTo("Hello, mofan");
        });
    }

    @Test
    public void testPrefixPropertyChangesHelloWorld() {
        this.contextRunner.withPropertyValues("demo.hello-world.prefix=你好，")
                .run(context ->
                        assertThat(context.getBean(HelloWorldService.class)
                                .sayHello("mofan"))
                                .isEqualTo("你好，mofan"));
    }

    @Test
    public void testDisabledHelloWorldDoesNotCreateService() {
        this.contextRunner.withPropertyValues("demo.hello-world.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(HelloWorldAutoConfiguration.class);
                    assertThat(context).doesNotHaveBean(HelloWorldService.class);
                });
    }

    @Test
    public void testUserServiceMakesDefaultServiceBackOff() {
        this.contextRunner.withUserConfiguration(UserHelloWorldConfiguration.class).run(context -> {
            assertThat(context).hasSingleBean(HelloWorldService.class)
                    .doesNotHaveBean("helloWorldService");
            assertThat(context.getBean(HelloWorldService.class))
                    .isSameAs(context.getBean("customHelloWorldService"));
        });
    }

    @Test
    public void testEnableAutoConfigurationDiscoversImportsFile() {
        // 不复用 Runner
        new ApplicationContextRunner().withUserConfiguration(ImportApplication.class)
                .run(context -> assertThat(context).hasSingleBean(HelloWorldService.class));
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class UserHelloWorldConfiguration {
        @Bean
        HelloWorldService customHelloWorldService() {
            return new HelloWorldService("Hi, ");
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    @EnableAutoConfiguration
    static class ImportApplication {
    }

}

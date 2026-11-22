package indi.mofan.autoconfigure;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.servlet.autoconfigure.HttpEncodingAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * @author mofan
 * @date 2026/11/15 17:59
 */
public class HttpEncodingAutoConfigurationTest implements WithAssertions {
    /**
     * 复用加载 HTTP 编码自动配置的基础设置
     * 每次调用 run() 都创建新的容器，并在回调结束后关闭
     */
    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(HttpEncodingAutoConfiguration.class));

    /**
     * 未配置编码属性时，自动配置提供默认的 UTF-8 过滤器
     */
    @Test
    public void testDefaultEncodingCreatesUtf8Filter() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(CharacterEncodingFilter.class);
            assertThat(context.getBean(CharacterEncodingFilter.class).getEncoding()).isEqualTo("UTF-8");
        });
    }

    /**
     * 用户声明同类型过滤器后，自动配置不再注册默认过滤器
     */
    @Test
    public void testUserFilterMakesDefaultFilterBackOff() {
        this.contextRunner.withUserConfiguration(UserFilterConfiguration.class).run(context -> {
            assertThat(context).hasSingleBean(CharacterEncodingFilter.class)
                    .doesNotHaveBean("characterEncodingFilter");
            assertThat(context.getBean(CharacterEncodingFilter.class))
                    .isSameAs(context.getBean("customCharacterEncodingFilter"));
        });
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class UserFilterConfiguration {
        @Bean
        CharacterEncodingFilter customCharacterEncodingFilter() {
            return new CharacterEncodingFilter();
        }
    }
}

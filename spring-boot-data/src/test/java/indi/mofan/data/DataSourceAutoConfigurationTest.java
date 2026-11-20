package indi.mofan.data;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcClientAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 检查 Spring Boot 怎样自动创建数据源、JdbcTemplate 和 JdbcClient
 *
 * <p>通过 ApplicationContextRunner 只加载本类指定的自动配置，不启动完整应用
 * 检查创建了哪些对象，以及连接地址、最大连接数等设置是否生效
 * 同时检查自己定义一个数据源、定义两个数据源时，Boot 分别会创建哪些对象
 * 测试不调用 getConnection()，不连接数据库，也不执行 SQL，因此不需要启动 MySQL
 *
 * @author mofan
 * @date 2026/11/16 13:45
 */
class DataSourceAutoConfigurationTest {
    /**
     * 保存三个测试共用的配置，创建 runner 时还不会启动 Spring 上下文
     */
    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            // 只加载下面三个自动配置，AutoConfigurations.of 会处理它们的先后顺序
            .withConfiguration(AutoConfigurations.of(DataSourceAutoConfiguration.class,
                    JdbcTemplateAutoConfiguration.class, JdbcClientAutoConfiguration.class))
            // 给测试设置连接地址和最大连接数，不需要写进 application.yaml
            .withPropertyValues("spring.datasource.url=jdbc:mysql://127.0.0.1:1/unused",
                    "spring.datasource.hikari.maximum-pool-size=3");

    /**
     * 没有手动定义数据源时，Boot 会创建 HikariCP、JdbcTemplate 和 JdbcClient
     * 再检查 URL 和最大连接数是否采用测试中的配置
     */
    @Test
    void shouldConfigureHikariAndJdbcClients() {
        // run() 新建并启动上下文，下面的回调用它检查 Bean，回调结束后自动关闭上下文
        runner.run(context -> {
            assertThat(context).hasSingleBean(DataSource.class)
                    .hasSingleBean(JdbcTemplate.class)
                    .hasSingleBean(NamedParameterJdbcTemplate.class)
                    .hasSingleBean(JdbcClient.class);
            HikariDataSource pool = context.getBean(HikariDataSource.class);
            assertThat(pool.getMaximumPoolSize()).isEqualTo(3);
            assertThat(pool.getJdbcUrl()).isEqualTo("jdbc:mysql://127.0.0.1:1/unused");
        });
    }

    /**
     * 自己定义数据源后，Boot 不再创建默认数据源，自定义的 URL 和池大小保持不变
     * JdbcClient 仍会自动创建
     */
    @Test
    void shouldBackOffOnlyDefaultDataSourceWhenUserProvidesOne() {
        // withUserConfiguration 把自己的配置类加进这次测试，不会修改公共 runner 的配置
        runner.withUserConfiguration(UserDataSourceConfiguration.class).run(context -> {
            assertThat(context).hasSingleBean(DataSource.class).hasSingleBean(JdbcClient.class);
            HikariDataSource pool = context.getBean(HikariDataSource.class);
            assertThat(pool).isSameAs(context.getBean("customDataSource"));
            assertThat(pool.getMaximumPoolSize()).isEqualTo(2);
            assertThat(pool.getJdbcUrl()).endsWith("/custom");
        });
    }

    /**
     * 有两个数据源且没有指定主要数据源时，Boot 无法选定一个
     * 因此不会创建默认的 JdbcTemplate 和 JdbcClient
     */
    @Test
    void shouldNotConfigureJdbcTemplateWithoutSingleCandidateDataSource() {
        runner.withUserConfiguration(TwoDataSourcesConfiguration.class).run(context -> {
            // 存在两个数据源不会导致上下文启动失败
            assertThat(context).hasNotFailed().hasBean("firstDataSource").hasBean("secondDataSource");
            assertThat(context).doesNotHaveBean(JdbcTemplate.class).doesNotHaveBean(JdbcClient.class);
        });
    }

    @Configuration(proxyBeanMethods = false)
    static class UserDataSourceConfiguration {
        @Bean
        HikariDataSource customDataSource() {
            HikariDataSource pool = new HikariDataSource();
            pool.setJdbcUrl("jdbc:mysql://127.0.0.1:1/custom");
            pool.setMaximumPoolSize(2);
            return pool;
        }
    }

    @Configuration(proxyBeanMethods = false)
    static class TwoDataSourcesConfiguration {
        @Bean
        HikariDataSource firstDataSource() {
            return new HikariDataSource();
        }

        @Bean
        HikariDataSource secondDataSource() {
            return new HikariDataSource();
        }
    }
}

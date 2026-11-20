package indi.mofan.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import indi.mofan.data.dao.AccountDao;
import indi.mofan.data.dao.JdbcAccountDao;
import indi.mofan.data.entity.Account;
import indi.mofan.data.service.TransferService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLTransientConnectionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 在 MySQL 上运行账户查询、转账和连接池测试
 *
 * <p>{@link Sql} 在每个测试方法执行前，先运行 {@code schema.sql}，再运行 {@code data.sql}
 * 前一个脚本删除并重建 {@code tb_account}，后一个脚本插入 Alice 和 Bob 的初始数据
 * 表不存在时会自动创建，表已存在时原有数据会被删除
 *
 * @author mofan
 * @date 2026/11/16 13:45
 */
@SpringBootTest
@Sql({"/schema.sql", "/data.sql"})
class DatabaseIntegrationTest {
    @Autowired
    private Environment environment;
    @Autowired
    private JdbcAccountDao jdbcAccountDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcClient jdbcClient;
    @Autowired
    private TransferService transferService;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MeterRegistry meterRegistry;

    /**
     * 确认连接的是 MySQL，并根据 druid Profile 检查实际使用 HikariCP 还是 Druid
     */
    @Test
    void shouldUseRealMySqlAndConfiguredPool() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.getMetaData().getDatabaseProductName()).isEqualTo("MySQL");
        }
        if (!environment.matchesProfiles("druid")) {
            assertThat(dataSource).isInstanceOf(HikariDataSource.class);
            assertThat(((HikariDataSource) dataSource).getMaximumPoolSize()).isEqualTo(4);
        } else {
            assertThat(dataSource.getClass().getName()).contains("DruidDataSource");
        }
    }

    /**
     * 用 JDBC、MyBatis 注解和 XML 读取同一账户
     */
    @Test
    void shouldMapRecordWithJdbcAndMyBatisAnnotationsAndXml() {
        Account jdbc = jdbcAccountDao.findById(1).orElseThrow();
        Account annotation = accountDao.findById(1);
        Account xml = accountDao.findAll().getFirst();
        assertThat(jdbc).isEqualTo(annotation).isEqualTo(xml);
        assertThat(jdbc.accountName()).isEqualTo("Alice");
        assertThat(jdbc.balance()).isEqualByComparingTo("1000.00");
        assertThat(jdbc.updatedAt()).isNotNull();
    }

    /**
     * 测试 SQL 注入
     */
    @Test
    void shouldBindValuesWithoutTreatingThemAsSql() {
        String name = "O'Reilly'); drop table tb_account; --";
        assertThat(jdbcAccountDao.insert(3, name, new BigDecimal("12.34"))).isEqualTo(1);
        assertThat(jdbcAccountDao.findById(3).orElseThrow().accountName()).isEqualTo(name);
        assertThat(accountDao.findAll()).hasSize(3);
    }

    @Test
    void shouldDistinguishOptionalAndSingleResultContracts() {
        assertThat(jdbcAccountDao.findById(999)).isEmpty();
        assertThatThrownBy(() -> jdbcClient.sql("select id from tb_account where id = 999")
                .query(Long.class).single()).isInstanceOf(EmptyResultDataAccessException.class);
        assertThatThrownBy(() -> jdbcClient.sql("select id from tb_account")
                .query(Long.class).optional()).isInstanceOf(IncorrectResultSizeDataAccessException.class);
    }

    /**
     * 测试主键冲突
     */
    @Test
    void shouldTranslateDuplicateKeyToSpringException() {
        assertThatThrownBy(() -> jdbcAccountDao.insert(1, "duplicate", BigDecimal.ONE))
                .isInstanceOf(DuplicateKeyException.class);
    }

    /**
     * 用 JdbcTemplate 的 ? 参数查询账户余额额
     */
    @Test
    void shouldSupportJdbcTemplateWithPositionalParameters() {
        BigDecimal balance = jdbcTemplate.queryForObject(
                "select balance from tb_account where id = ?", BigDecimal.class, 1L);
        assertThat(balance).isEqualByComparingTo("1000.00");
    }

    @Test
    void shouldCommitJdbcDebitAndMyBatisCreditTogether() {
        transferService.transfer(1, 2, new BigDecimal("100.00"));
        // Alice 1000 向 Bob 500 进行转账
        assertBalances("900.00", "600.00");
    }

    @Test
    void shouldRollBackDebitWhenTargetIsMissing() {
        assertThatThrownBy(() -> transferService.transfer(1, 999, new BigDecimal("100.00")))
                .isInstanceOf(IllegalStateException.class).hasMessage("收款账户不存在");
        assertBalances("1000.00", "500.00");
    }

    @Test
    void shouldRollBackBothAccessTechnologiesAfterBothUpdates() {
        TransactionTemplate transaction = new TransactionTemplate(transactionManager);
        assertThatThrownBy(() -> transaction.executeWithoutResult(status -> {
            transferService.transfer(1, 2, new BigDecimal("100.00"));
            throw new IllegalStateException("模拟后续业务失败");
        })).isInstanceOf(IllegalStateException.class).hasMessage("模拟后续业务失败");
        assertBalances("1000.00", "500.00");
    }

    @Test
    void shouldLeaveBalancesUnchangedWhenFundsAreInsufficient() {
        assertThatThrownBy(() -> transferService.transfer(1, 2, new BigDecimal("1000.01")))
                .isInstanceOf(IllegalStateException.class).hasMessage("付款账户不存在或余额不足");
        assertBalances("1000.00", "500.00");
    }

    @Test
    void shouldRejectInvalidTransferBeforeUpdating() {
        assertThatThrownBy(() -> transferService.transfer(1, 1, BigDecimal.ONE))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> transferService.transfer(1, 2, new BigDecimal("0.001")))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> transferService.transfer(1, 2, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
        assertBalances("1000.00", "500.00");
    }

    /**
     * 检查 HikariCP 的 hikaricp.connections.active 和 jdbc.connections.max 指标是否存在
     * 当前 Druid 配置下，jdbc.connections.active 和 jdbc.connections.max 都没有自动注册
     */
    @Test
    void shouldExposeMetricsForSupportedPoolMetadata() {
        if (environment.matchesProfiles("druid")) {
            // 当前 Druid Starter 没有自动注册下面这两个指标
            assertThat(meterRegistry.find("jdbc.connections.active").gauges()).isEmpty();
            assertThat(meterRegistry.find("jdbc.connections.max").gauges()).isEmpty();
        } else {
            assertThat(meterRegistry.find("hikaricp.connections.active").gauges()).isNotEmpty();
            assertThat(meterRegistry.find("jdbc.connections.max").gauges()).isNotEmpty();
        }
    }

    /**
     * 新建只有 1 个连接的 HikariCP 池，占住连接后再次获取应超时，归还连接后才能获取
     */
    @Test
    void shouldTimeOutWhilePoolIsExhaustedAndRecoverAfterReturn() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getRequiredProperty("spring.datasource.url"));
        config.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        config.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        config.setMaximumPoolSize(1);
        config.setMinimumIdle(0);
        config.setConnectionTimeout(300);
        try (HikariDataSource pool = new HikariDataSource(config)) {
            try (Connection occupied = pool.getConnection()) {
                assertThat(occupied.isValid(1)).isTrue();
                assertThatThrownBy(pool::getConnection).isInstanceOf(SQLTransientConnectionException.class);
            }
            try (Connection returned = pool.getConnection()) {
                assertThat(returned.isValid(1)).isTrue();
            }
        }
    }

    private void assertBalances(String alice, String bob) {
        assertThat(jdbcAccountDao.findById(1).orElseThrow().balance()).isEqualByComparingTo(alice);
        assertThat(jdbcAccountDao.findById(2).orElseThrow().balance()).isEqualByComparingTo(bob);
    }
}

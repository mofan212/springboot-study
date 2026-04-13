package indi.mofan;


import indi.mofan.dao.BizDataDao;
import indi.mofan.dao.RuntimeLogDao;
import indi.mofan.service.BusinessService;
import indi.mofan.service.ExternalCallService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * @author mofan
 * @date 2026/4/13 11:26
 */
@SpringBootTest
public class RuntimeLogTransactionTest implements WithAssertions {
    @Autowired
    private BusinessService businessService;
    @Autowired
    private BizDataDao bizDataDao;
    @Autowired
    private RuntimeLogDao runtimeLogDao;
    @MockitoBean
    private ExternalCallService externalCallService;

    @BeforeEach
    public void setUp() {
        bizDataDao.delete(null);
        runtimeLogDao.delete(null);
    }

    @Test
    @DisplayName("场景 2.1：未设置日志记录的事务传播级别时，业务数据出现异常，日志记录也回滚了")
    public void testScenario1_DefaultPropagation_RollbacksEverything() {
        Mockito.doThrow(new RuntimeException()).when(externalCallService).executeError();
        Assertions.assertThrows(RuntimeException.class, () -> businessService.executeWithDefaultPropagation());

        // 由于日志方法没有走独立事务，故完全跟主线同生共死。两者的数据记录应全都 = 0
        assertThat(bizDataDao.selectCount(null))
                .as("主线业务数据已被事务管理器撤销").isEqualTo(0);
        assertThat(runtimeLogDao.selectCount(null))
                .as("使用默认事务传播，日志数据被一连串回滚影响丢失，条数应为 0").isEqualTo(0);
    }

    @Test
    @DisplayName("场景 2.2：设置日志记录的事务传播级别后，业务抛出异常，由于新环境日志不回滚正常落库")
    public void testScenario2_RequiresNewPropagation_LogCommits() {
        Mockito.doThrow(new RuntimeException()).when(externalCallService).executeError();
        Assertions.assertThrows(RuntimeException.class, () -> businessService.executeWithRequiresNewPropagation());

        assertThat(bizDataDao.selectCount(null))
                .as("发生错误主线数据应保持正确状态被回滚为零").isEqualTo(0);
        assertThat(runtimeLogDao.selectCount(null))
                .as("日志因采用了 PROPAGATION_REQUIRES_NEW，早已单独获得 Connection 提交成功！")
                .isEqualTo(1);
    }

    @Test
    @DisplayName("场景 3：不开启事务，虽然业务抛出异常，但由于 finally 保证了执行，且无外部主事务包裹，日志正常落库")
    public void testScenario3_WithoutTransactional_FinallyGuaranteesExecution() {
        Mockito.doThrow(new RuntimeException()).when(externalCallService).executeError();
        Assertions.assertThrows(RuntimeException.class, () -> businessService.executeWithoutTransactional());

        assertThat(runtimeLogDao.selectCount(null))
                .as("""
                        利用 try-finally 块，就算主线出错了也能一定触发 finally 中的记录日志。
                        由于方法本身没打全局的 @Transactional 被回滚联动，因此该独立的新事务必定保存成功。预期条数为 1
                        """)
                .isEqualTo(1);
    }
}

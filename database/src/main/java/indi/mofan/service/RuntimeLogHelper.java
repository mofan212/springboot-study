package indi.mofan.service;


import indi.mofan.dao.RuntimeLogDao;
import indi.mofan.entity.RuntimeLog;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author mofan
 * @date 2026/4/13 11:18
 */
@Component
public class RuntimeLogHelper {
    @Autowired
    private RuntimeLogDao runtimeLogDao;
    @Autowired
    private PlatformTransactionManager transactionManager;

    /**
     * 独立新事务（修复后）
     */
    private TransactionTemplate requiresNewTransactionTemplate;

    @PostConstruct
    public void init() {
        requiresNewTransactionTemplate = new TransactionTemplate(transactionManager);
        requiresNewTransactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * 场景一：未设置日志记录的独立事务（走当前外层事务）
     */
    public void saveLogDefault(String content) {
        RuntimeLog log = new RuntimeLog();
        log.setLogContent(content);
        runtimeLogDao.insert(log);
    }

    /**
     * 场景二：新开事务保存日志
     */
    public void saveLogRequiresNew(String content) {
        requiresNewTransactionTemplate.executeWithoutResult(_ -> {
            RuntimeLog log = new RuntimeLog();
            log.setLogContent(content);
            runtimeLogDao.insert(log);
        });
    }
}

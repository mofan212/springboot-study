package indi.mofan.data.service;

import indi.mofan.data.dao.AccountDao;
import indi.mofan.data.dao.JdbcAccountDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author mofan
 * @date 2026/11/16 13:45
 */
@Service
public class TransferService {
    private final JdbcAccountDao jdbcAccountDao;
    private final AccountDao accountDao;

    public TransferService(JdbcAccountDao jdbcAccountDao, AccountDao accountDao) {
        this.jdbcAccountDao = jdbcAccountDao;
        this.accountDao = accountDao;
    }

    /**
     * 先用 JDBC 扣款，再用 MyBatis 入账，两步放在同一个事务中
     *
     * @param sourceId 付款账户编号
     * @param targetId 收款账户编号，不能与付款账户相同
     * @param amount 非空正数，小数位数不超过 2
     * @apiNote 调用应经过 Spring 代理，方法抛出运行时异常时整笔转账回滚
     */
    @Transactional
    public void transfer(long sourceId, long targetId, BigDecimal amount) {
        if (sourceId == targetId || amount.signum() <= 0 || amount.scale() > 2) {
            throw new IllegalArgumentException("转账需要不同账户，金额必须为正数且最多两位小数");
        }
        if (jdbcAccountDao.debit(sourceId, amount) != 1) {
            throw new IllegalStateException("付款账户不存在或余额不足");
        }
        if (accountDao.credit(targetId, amount) != 1) {
            throw new IllegalStateException("收款账户不存在");
        }
    }
}

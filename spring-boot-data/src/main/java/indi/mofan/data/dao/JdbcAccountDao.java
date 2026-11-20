package indi.mofan.data.dao;

import indi.mofan.data.entity.Account;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author mofan
 * @date 2026/11/16 13:45
 */
@Repository
public class JdbcAccountDao {
    private final JdbcClient jdbcClient;

    public JdbcAccountDao(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Account> findById(long id) {
        return jdbcClient.sql("""
                        select id, account_name, balance, updated_at 
                        from tb_account 
                        where id = :id
                        """)
                .param("id", id)
                .query(Account.class)
                .optional();
    }

    public int insert(long id, String accountName, BigDecimal balance) {
        return jdbcClient.sql("""
                        insert into tb_account(id, account_name, balance)
                        values (:id, :name, :balance)
                        """)
                .param("id", id)
                .param("name", accountName)
                .param("balance", balance)
                .update();
    }

    /**
     * 在同一条 SQL 中检查余额并扣款，避免查完余额后又被其他请求修改
     */
    public int debit(long id, BigDecimal amount) {
        return jdbcClient.sql("""
                        update tb_account
                        set balance = balance - :amount
                        where id = :id and balance >= :amount
                        """)
                .param("id", id)
                .param("amount", amount)
                .update();
    }
}

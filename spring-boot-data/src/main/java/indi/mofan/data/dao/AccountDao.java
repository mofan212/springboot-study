package indi.mofan.data.dao;

import indi.mofan.data.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mofan
 * @date 2026/11/16 13:45
 */
@Mapper
public interface AccountDao {
    @Select("select id, account_name, balance, updated_at from tb_account where id = #{id}")
    Account findById(@Param("id") long id);

    List<Account> findAll();

    int credit(@Param("id") long id, @Param("amount") BigDecimal amount);
}

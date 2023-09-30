package indi.mofan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.mofan.entity.TestNull;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author mofan
 * @date 2023/9/30 11:07
 */
@Mapper
@Repository
public interface TestNullDao extends BaseMapper<TestNull> {
}

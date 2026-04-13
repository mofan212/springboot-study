package indi.mofan.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.mofan.entity.RuntimeLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author mofan
 * @date 2026/4/13 11:17
 */
@Mapper
@Repository
public interface RuntimeLogDao extends BaseMapper<RuntimeLog> {
}

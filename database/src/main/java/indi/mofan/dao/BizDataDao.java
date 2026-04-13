package indi.mofan.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.mofan.entity.BizData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author mofan
 * @date 2026/4/13 11:17
 */
@Mapper
@Repository
public interface BizDataDao extends BaseMapper<BizData> {
}

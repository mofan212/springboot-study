package indi.mofan.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.mofan.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author mofan
 * @date 2022/11/9 22:21
 */
@Mapper
@Repository
public interface StudentDao extends BaseMapper<Student> {
}

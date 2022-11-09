package indi.mofan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.mofan.dao.StudentDao;
import indi.mofan.entity.Student;
import indi.mofan.enums.Gender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mofan
 * @date 2022/11/9 22:24
 */
@SpringBootTest
public class MyBatisPlusSimpleTest {
    @Autowired
    private StudentDao studentDao;

    @AfterEach
    public void destroy() {
        LambdaQueryWrapper<Student> wrapper = Wrappers.<Student>lambdaQuery()
                .isNotNull(Student::getId);
        studentDao.delete(wrapper);
    }

    @Test
    public void testSave() {
        Student student = new Student();
        student.setAge(20);
        student.setName("mofan");
        student.setGender(Gender.MALE);
        studentDao.insert(student);
    }
}

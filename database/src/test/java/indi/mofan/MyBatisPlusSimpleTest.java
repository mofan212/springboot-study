package indi.mofan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.mofan.dao.StudentDao;
import indi.mofan.dao.TestNullDao;
import indi.mofan.entity.Student;
import indi.mofan.entity.TestNull;
import indi.mofan.enums.Gender;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

/**
 * @author mofan
 * @date 2022/11/9 22:24
 */
@SpringBootTest
public class MyBatisPlusSimpleTest implements WithAssertions {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private TestNullDao testNullDao;

    private static final String UNIQUE_NAME = "mofan";

    @BeforeEach
    public void init() {
        TestNull testNull = new TestNull();
        testNull.setName(UNIQUE_NAME);
        testNull.setAge(21);
        testNull.setEmptyField("");
        testNullDao.insert(testNull);
    }

    @AfterEach
    public void destroy() {
        LambdaQueryWrapper<Student> wrapper = Wrappers.lambdaQuery(Student.class)
                .isNotNull(Student::getId);
        studentDao.delete(wrapper);

        LambdaQueryWrapper<TestNull> testNullWrapper = Wrappers.lambdaQuery(TestNull.class)
                .eq(TestNull::getName, UNIQUE_NAME);
        testNullDao.delete(testNullWrapper);
    }

    @Test
    public void testSave() {
        Student student = new Student();
        student.setAge(20);
        student.setName("mofan");
        student.setGender(Gender.MALE);
        studentDao.insert(student);
    }

    private List<TestNull> getMofanTestNull() {
        LambdaQueryWrapper<TestNull> wrapper = Wrappers.lambdaQuery(TestNull.class)
                .eq(TestNull::getName, UNIQUE_NAME);
        return testNullDao.selectList(wrapper);
    }

    @Test
    public void testLambdaWrapperUpdateNull() {
        Optional<TestNull> optional = getMofanTestNull().stream()
                .filter(i -> UNIQUE_NAME.equals(i.getName()))
                .findFirst();

        var wrapper = Wrappers.lambdaUpdate(TestNull.class)
                .set(TestNull::getEmptyField, null)
                .set(TestNull::getAge, null);

        optional.ifPresent(i -> testNullDao.update(i, wrapper));

        optional = getMofanTestNull().stream()
                .filter(i -> UNIQUE_NAME.equals(i.getName()))
                .findFirst();

        optional.ifPresent(
                entity -> assertThat(entity)
                        .extracting(TestNull::getEmptyField, TestNull::getAge)
                        .containsOnlyNulls()
        );
    }

    @Test
    public void testWrapperUpdateNull() {
        Optional<TestNull> optional = getMofanTestNull().stream()
                .filter(i -> UNIQUE_NAME.equals(i.getName()))
                .findFirst();
        if (optional.isEmpty()) {
            Assertions.fail();
        }

        TestNull entity = optional.get();
        var wrapper = new UpdateWrapper<>(entity);
        wrapper.set("empty_field", null).set("age", null);
        testNullDao.update(entity, wrapper);

        optional = getMofanTestNull().stream()
                .filter(i -> UNIQUE_NAME.equals(i.getName()))
                .findFirst();
        optional.ifPresent(
                i -> assertThat(i)
                        .extracting(TestNull::getEmptyField, TestNull::getAge)
                        .containsOnlyNulls()
        );
    }
}

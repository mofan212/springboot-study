package indi.mofan;

import indi.mofan.entity.Student;
import indi.mofan.enums.Gender;
import indi.mofan.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mofan
 * @date 2022/11/9 22:41
 */
@SpringBootTest
public class InvalidTransactionTest {
    @Autowired
    private StudentService studentService;

    @AfterEach
    public void destroy() {
        studentService.clearStudentInfo();
    }

    private Student init() {
        Student student = new Student();
        student.setName("mofan");
        student.setAge(20);
        student.setGender(Gender.MALE);
        return student;
    }

    @Test
    public void testSaveAndUpdateButHasTransactional() {
        Student student = init();
        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.saveAndUpdateButHasTransactional(student));
        // 事务向下传播，数据不存在，事务回滚
        Assertions.assertNull(studentService.selectById(student.getId()));
    }

    @Test
    public void testSaveAndUpdateButHasTransactional_2() {
        Student student = init();
        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.saveAndUpdateButHasTransactional_2(student));
        // 事务向下传播，数据不存在，事务回滚
        Assertions.assertNull(studentService.selectById(student.getId()));
    }

    @Test
    public void testSaveAndUpdateButNoTransactional() {
        Student student = init();
        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.saveAndUpdateButNoTransactional(student));
        // 数据存在
        Student result = studentService.selectById(student.getId());
        // 甚至更新成功。更新操作中虽然存在异常，但是更新成功
        Assertions.assertEquals(21, result.getAge());
    }

    @Test
    public void testRollBackSuccess() {
        Student student = init();
        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.saveAndUpdateRollBackSuccess(student));

        /*
         * 数据存在，但是没有更新成功，证明更新时的成功回滚。
         * 数据存在的原因是 insert 操作没有被事务管理，也没有报错，索引插入成功。
         */
        Student result = studentService.selectById(student.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(20, result.getAge());
    }

    @Test
    public void testRollBackSuccess_2() {
        Student student = init();
        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.saveAndUpdateRollBackSuccess_2(student));

        Student result = studentService.selectById(student.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(20, result.getAge());
    }

    @Test
    public void testAvoidBigTransaction() {
        Student student = init();

        Assertions.assertThrows(ArithmeticException.class,
                () -> studentService.avoidBigTransaction(student));

        Student result = studentService.selectById(student.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals("mofan", result.getName());
    }

    @Test
    public void testDoAfterComplete() {
        Student student = init();

        try {
            studentService.doAfterComplete(student);
            Assertions.assertNotNull(studentService.selectById(student.getId()));
        } catch (Exception e) {
            Assertions.assertTrue(e instanceof ArithmeticException);
        }
    }
}

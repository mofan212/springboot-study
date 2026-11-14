package indi.mofan;

import indi.mofan.service.MultiThreadTransactionService;
import indi.mofan.service.StudentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * <a href = "https://www.bilibili.com/video/BV1Td4y1b7DQ/">link</a>
 *
 * @author mofan
 * @date 2022/11/14 22:38
 */
@SpringBootTest
public class MultiThreadTransactionServiceTest {

    @Autowired
    private StudentService studentService;
    @Autowired
    private MultiThreadTransactionService service;

    @AfterEach
    public void destroy() {
        studentService.clearStudentInfo();
    }

    @Test
    public void testTransactionAsyncFail() {
        try {
            service.transactionAsyncFail();
            Assertions.fail();
        } catch (Exception e) {
            Assertions.assertInstanceOf(RuntimeException.class, e);
        }
        // 插入两条数据，存在异常，数据没有回滚
        Assertions.assertEquals(1L, studentService.selectByCount());
    }

    @Test
    public void testTransactionAsyncSuccess_1() throws Exception {
        // 等待子线程完成事务，再查询和清理数据
        service.transactionAsyncSuccess_1().get(10, TimeUnit.SECONDS);
        Assertions.assertEquals(0, studentService.selectByCount());
    }

}

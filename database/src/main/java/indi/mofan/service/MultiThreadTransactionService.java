package indi.mofan.service;

import indi.mofan.dao.StudentDao;
import indi.mofan.entity.Student;
import indi.mofan.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author mofan
 * @date 2022/11/14 22:41
 */
@Service
public class MultiThreadTransactionService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    @Autowired
    private TransactionDefinition transactionDefinition;

    @Transactional(rollbackFor = Exception.class)
    public void transactionAsyncFail() {
        // 这条数据不会回滚
        new Thread(() -> addStudent(1)).start();
        // 这条数据会回滚
        addStudent(3);
        throw new RuntimeException("异常信息");
    }

    private int addStudent(int i) {
        Student student = new Student();
        student.setName("mofan_" + i);
        student.setGender(Gender.MALE);
        student.setAge(20 + i);

        int insert = studentDao.insert(student);

        if (i % 2 == 0) {
            return 0;
        }
        return insert;
    }

    public void transactionAsyncSuccess_1() {
        int size = 10;
        CyclicBarrier barrier = new CyclicBarrier(size);
        AtomicBoolean rollback = new AtomicBoolean(false);

        for (int i = 0; i < size; i++) {
            int currentNum = i;
            // 手动开启事务
            new Thread(() -> {
                TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
                try {
                    // 如果插入数据 < 1 就抛出异常
                    if (addStudent(currentNum) < 1) {
                        throw new RuntimeException("插入数据失败");
                    }
                } catch (Exception e) {
                    // 如果当前线程执行异常，则设置回滚标志
                    rollback.set(true);
                }

                // 等待所有线程的事务结果
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                // 如果标志需要回滚，就回滚
                if (rollback.get()) {
                    transactionManager.rollback(transaction);
                    return;
                }
                // 否则提交事务
                transactionManager.commit(transaction);
            }).start();
        }
    }
}

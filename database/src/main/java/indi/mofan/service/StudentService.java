package indi.mofan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.mofan.dao.StudentDao;
import indi.mofan.entity.Student;
import indi.mofan.util.TransactionUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author mofan
 * @date 2022/11/9 22:37
 */
@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TransactionTemplate transactionTemplate;

    public Long saveAndUpdateButNoTransactional(Student student) {
        studentDao.insert(student);
        // 在未被 @Transactional 注解标记的方法中调用被 @Transactional 注解标记的方法
        student.setAge(student.getAge() + 1);
        this.updateButThrowException(student);
        return student.getId();
    }

    public Long saveAndUpdateRollBackSuccess(Student student) {
        studentDao.insert(student);
        student.setAge(student.getAge() + 1);
        StudentService proxy = (StudentService) AopContext.currentProxy();
        proxy.updateButThrowException(student);
        return student.getId();
    }

    public Long saveAndUpdateRollBackSuccess_2(Student student) {
        studentDao.insert(student);
        student.setAge(student.getAge() + 1);
        // SpringBoot 从 2.6 开始默认不允许出现 Bean 循环引用，需要在配置文件中显式配置
        studentService.updateButThrowException(student);
        return student.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveAndUpdateButHasTransactional(Student student) {
        studentDao.insert(student);
        // 在被 @Transactional 注解标记的方法中调用被 @Transactional 注解标记的方法
        student.setAge(student.getAge() + 1);
        updateButThrowException(student);
        return student.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long saveAndUpdateButHasTransactional_2(Student student) {
        studentDao.insert(student);
        // 在被 @Transactional 注解标记的方法中调用未被 @Transactional 注解标记的方法
        student.setAge(student.getAge() + 1);
        updateButThrowExceptionButNoTransactional(student);
        return student.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateButThrowException(Student student) {
        int i = studentDao.updateById(student);
        // 模拟抛出异常
        int a = 100 / 0;
        return i;
    }

    public int updateButThrowExceptionButNoTransactional(Student student) {
        int i = studentDao.updateById(student);
        // 模拟抛出异常
        int a = 100 / 0;
        return i;
    }

    public Integer avoidBigTransaction(Student student) {
        /*
         * 如果事务处理的粒度过大，可能会造成的情况：
         *   - 造成数据库死锁
         *   - 在并发量大的情况下，数据库连接可能会被打满
         *   - 数据回滚时间非常长
         *   - 整个服务的性能大大降低
         * 避免大事务，就是要将不需要被事务管理的操作抽取出来，比如查询，而只将
         * 创建、更新、删除操作交由事务管理，抽取的方式除了可以额外编写方法进行
         * 调用外，还可以使用编程式事务管理。
         */

        //  无需返回值，使用 TransactionCallbackWithoutResult
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                studentDao.insert(student);
            }
        });

        assert studentDao.selectById(student.getId()) != null;

        // 需要返回值，直接使用 Lambda 表达式
        return transactionTemplate.execute(status -> {
            student.setName("aaa");
            // 模拟异常
            int a = 1 / 0;
            return studentDao.updateById(student);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    public void doAfterComplete(Student student) {
        studentDao.insert(student);
        // 模拟随机失败
        int random = (int) (Math.random() * 6);
        if (random == 2) {
            int a = 1 / 0;
        }
        // 事务成功调用
        TransactionUtil.doAfterCompletion(new TransactionUtil.DoSomethingTransactionComplete(() -> {
            System.out.println("事务成功后调用...");
        }));
    }

    @Transactional(rollbackFor = Exception.class)
    public void clearStudentInfo() {
        LambdaQueryWrapper<Student> wrapper = Wrappers.<Student>lambdaQuery().isNotNull(Student::getId);
        studentDao.delete(wrapper);
    }

    public Student selectById(Long id) {
        return studentDao.selectById(id);
    }

}

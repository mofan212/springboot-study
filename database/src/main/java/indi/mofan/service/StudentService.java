package indi.mofan.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import indi.mofan.dao.StudentDao;
import indi.mofan.entity.Student;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author mofan
 * @date 2022/11/9 22:37
 */
@Service
public class StudentService {
    @Autowired
    private StudentDao studentDao;
    @Resource
    private StudentService studentService;

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

    @Transactional(rollbackFor = Exception.class)
    public void clearStudentInfo() {
        LambdaQueryWrapper<Student> wrapper = Wrappers.<Student>lambdaQuery().isNotNull(Student::getId);
        studentDao.delete(wrapper);
    }

    public Student selectById(Long id) {
        return studentDao.selectById(id);
    }

}

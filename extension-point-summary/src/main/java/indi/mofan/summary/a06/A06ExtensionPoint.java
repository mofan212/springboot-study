package indi.mofan.summary.a06;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Constructor;

/**
 * @author mofan
 * @date 2023/10/2 15:34
 */
@SpringBootApplication
public class A06ExtensionPoint {

    private static final String STUDENT_BEAN_NAME = "a06-student";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A06ExtensionPoint.class);
        Teacher teacher = context.getBean(Teacher.class);
        Assert.isInstanceOf(Teacher.class, teacher);
        Student student = context.getBean(Student.class);
        Assert.isInstanceOf(Student.class, student);
        context.close();
    }

    @Component(value = STUDENT_BEAN_NAME)
    static class Student {
        @Getter
        @Setter
        private String name = "mofan";

        @Getter
        @Autowired
        private Teacher teacher;

        public Student() {
            System.out.println("Student 的无参数构造方法被执行");
        }

        public Student(String name) {
            this.name = name;
            System.out.println("Student 的有参数构造方法(name)被执行");
        }

        public Student(Teacher teacher) {
            this.teacher = teacher;
            System.out.println("Student 的有参数构造方法(teacher)被执行");
        }

        public Student(String name, Teacher teacher) {
            this.name = name;
            this.teacher = teacher;
            System.out.println("Student 的有参数构造方法(name,teacher)被执行");
        }

        public void setTeacher(Teacher teacher) {
            System.out.println("----student中的setTeacher方法被调用");
            this.teacher = teacher;
        }
    }

    @Component
    static class Teacher {
        @Getter
        @Setter
        private String name = "Mr.Chen";

        @Getter
        @Autowired
        private Student student;

        public Teacher() {
            System.out.println("Teacher 的无参数构造方法被执行");
        }

        public void setStudent(Student student) {
            System.out.println("Teacher 中的 setStudent 方法被调用");
            this.student = student;
        }
    }

    @Component
    static class MySmartInstantiationAwareBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {
        @Override
        public Class<?> predictBeanType(Class<?> beanClass, String beanName) throws BeansException {
            if (STUDENT_BEAN_NAME.equals(beanName)) {
                System.out.println("predictBeanType 方法被执行");
                return Student.class;
            }
            return null;
        }

        @Override
        public Class<?> determineBeanType(Class<?> beanClass, String beanName) throws BeansException {
            if (STUDENT_BEAN_NAME.equals(beanName)) {
                System.out.println("determineBeanType 方法被执行");
            }
            return beanClass;
        }

        @Override
        public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
            if (STUDENT_BEAN_NAME.equals(beanName)) {
                // 四个构造方法，选默认构造方法
                // 选(name)时会报错，选(teacher)时会有循环依赖，并且是 Spring 无法解决的循环依赖
                // 选(name, teacher)时也会报错
                System.out.println("determineCandidateConstructors 方法被执行");
                return new Constructor[]{beanClass.getConstructors()[0]};
            }
            return null;
        }

        @Override
        public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
            if (STUDENT_BEAN_NAME.equals(beanName)) {
                System.out.println("getEarlyBeanReference 方法被执行");
            }
            return bean;
        }

        @Override
        public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
            if (STUDENT_BEAN_NAME.equals(beanName)) {
                System.out.println("postProcessBeforeInstantiation 方法被执行");
            }
            return null;
        }
    }
}

package indi.mofan.bean;

import indi.mofan.pojo.Person;
import indi.mofan.pojo.Teacher;
import indi.mofan.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * @author mofan
 * @date 2022/10/11 19:50
 */
@SpringBootTest
public class BeanTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testUserFactoryBean() {
        /*
         * 使用 FactoryBean 注册 Bean:
         * 1. 实现 FactoryBean 接口，泛型为需要注册的 Bean 类型
         * 2. 重写 getObject 方法，返回需要注册的 Bean 对象
         * 3. 重写 getObjectType 方法，返回主要注册的 Bean 对象的 Class
         * 4. 将实现类交由 Spring 管理，即：使用 @Component 等类似注解
         */
        System.out.println("获取到的 Bean 为:" + context.getBean(User.class));
    }

    @Test
    public void testImportSelector() {
        /*
         * 使用 ImportSelector 注册 Bean:
         * 1. 实现 ImportSelector 接口，重写 selectImports 方法，方法返回需要注册的 Bean 的全限定类名
         * 2. 将实现类交由 Spring 管理
         * 3. 使用 @Import 注解导入实现类，将被 @Import 注解标记的类交由 Spring 管理
         */
        System.out.println("获取到的 Bean 为:" + context.getBean(Person.class));
    }
    
    @Test
    public void testImportBeanDefinitionRegistrar() {
        /*
         * 使用 ImportBeanDefinitionRegistrar 注册 Bean:
         * 1. 实现 ImportBeanDefinitionRegistrar 接口，重写 registerBeanDefinitions 方法
         * 2. 在 registerBeanDefinitions 方法里构建 BeanDefinition，然后进行注册
         * 3. 将实现类交由 Spring 管理
         * 4. 使用 @Import 注解导入实现类，将被 @Import 注解标记的类交由 Spring 管理
         *
         * 与使用 ImportSelector 注册 Bean 相比，ImportSelector 中重写的方法范围的是全限定类名，
         * 不能对这个 Bean 做其他操作；使用 ImportBeanDefinitionRegistrar 注册 Bean 可以注入自
         * 己构建的 BeanDefinition，然后可以对 Bean 进行额外操作，比如：设置属性值等。
         */
        Teacher teacher = (Teacher) context.getBean("teacher");
        System.out.println("获取到的 Bean 为:" + teacher);
        System.out.println("Teacher 的 name 为:" + context.getBean(Teacher.class).getName());
    }
}

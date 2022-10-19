package indi.mofan.bean;

import indi.mofan.Application;
import indi.mofan.config.LifeCycleConfig;
import indi.mofan.config.SimpleEnableAutoConfiguration;
import indi.mofan.enums.MutationType;
import indi.mofan.event.ChineseHamburger;
import indi.mofan.event.DeleteEvent;
import indi.mofan.event.DeleteEventPublisher;
import indi.mofan.event.MutationEvent;
import indi.mofan.event.Pizza;
import indi.mofan.pojo.Author;
import indi.mofan.pojo.Employee;
import indi.mofan.pojo.NameSpace;
import indi.mofan.pojo.Person;
import indi.mofan.pojo.Student;
import indi.mofan.pojo.Teacher;
import indi.mofan.pojo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author mofan
 * @date 2022/10/11 19:50
 */
@SpringBootTest(classes = Application.class)
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

        /*
         * 使用 FactoryBean 注册的 Bean 的 name 是注册的实现类的 name
         * 如果要获取 FactoryBean 的实现类对应的 Bean，需要在 name 前使用 & 符号
         */
        Assertions.assertTrue(context.getBean("userFactoryBean") instanceof User);
        Assertions.assertTrue(context.getBean("&userFactoryBean") instanceof UserFactoryBean);
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

    @Test
    public void testLifeCycle() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Bean
        context.register(LifeCycleConfig.class);
        context.refresh();
        // 关闭容器，触发销毁操作
        context.close();
    }

    @Test
    public void testBeanPostProcessor() {
        /*
         * 使用  BeanPostProcessor 处理 Bean:
         * 1. 实现 BeanPostProcessor 接口，重写其中的方法对 Bean 进行前置处理或后置处理
         * 2. 处理后的 Bean 记得随方法返回
         * 3. 将实现类交由 Spring 管理
         */
        Student student = context.getBean(Student.class);
        System.out.println("获取到的 Bean 为:" + student);
        System.out.println("Student 的 studentName 为:" + student.getStudentName());
    }

    @Test
    public void testSpringSPI() {
        /*
         * Spring 的 SPI 机制:
         * 1. 配置文件必须在 classpath 路径下的 META-INF 文件夹内，文件名必须为 spring.factories
         * 2. spring.factories 文件内容为键值对，一个键可以对应多个值，使用逗号分割
         */
        List<String> values = SpringFactoriesLoader.loadFactoryNames(
                SimpleEnableAutoConfiguration.class,
                SimpleEnableAutoConfiguration.class.getClassLoader()
        );
        Assertions.assertTrue(values.stream().anyMatch(i -> Employee.class.getName().equals(i)));
    }

    @Test
    public void testAutoConfiguration() {
        /*
         * 自定义自动装配:
         * 1. 新建 org.springframework.boot.autoconfigure.AutoConfiguration.imports 文件
         * 2. 该文件必须位于 classpath 路径下的 META-INF/spring 路径下
         * 3. 文件名和所在路径必须严格要求
         * 4. 在 org.springframework.boot.autoconfigure.AutoConfiguration.imports 文件中
         *    添加需要进行自动装配的类的全限定类名，那么 SpringBoot 就会自行将其注册到 Spring 容器中
         */
        System.out.println("获取到的 Bean 为: " + context.getBean(Employee.class));
    }

    @Test
    public void testLoadJsonSource() {
        /*
         * 自定义配置文件解析器:
         * 1. 实现 PropertySourceLoader 接口，重写其中的两个方法，定义支持的类型与解析方式
         * 2. 以 PropertySourceLoader 的全限定类名为 key，实现类的全限定类名为 value 添加到 spring.factories 中，
         *    通过 Spring SPI 机制来实现自定义配置文件解析器
         * 备注：yaml 作为 JSON 的超集，可以直接在 yaml 文件中书写 JSON 以支持 JSON 作为配置文件。见：application.yml
         */
        Author author = context.getBean(Author.class);
        Assertions.assertEquals("mofan", author.getName());
    }
    
    @Test
    public void testEvent() {
        /*
         * 使用 Spring 的事件监听:
         * 1. 新建事件类，继承 Spring 提供的事件基类 ApplicationEvent
         * 2. 新建事件监听器，实现 Spring 提供的 ApplicationListener 接口，泛型为上一步新建的事件类
         * 3. 将事件监听器交由 Spring 管理
         * 4. 使用 ApplicationContext 发送事件
         * 5. 或者实现 ApplicationEventPublisherAware 接口，重写方法以发送事件，实现类需要交由 Spring 管理
         */
        context.publishEvent(new DeleteEvent("applicationContext 发出的删除事件"));
    }

    @Test
    public void testEventPropagation() {
        AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
        parent.register(DeleteEventPublisher.class);
        parent.refresh();

        AnnotationConfigApplicationContext child = new AnnotationConfigApplicationContext();
        child.refresh();

        child.setParent(parent);

        // 通过子容器发布事件，能够在父容器监听到
        child.publishEvent(new DeleteEvent("子容器发送的事件..."));
    }

    @Test
    public void testCustomNameSpace() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 自定义命名空间成功注册 bean 到 Spring 中，可以通过 getBean() 获取到信息
        NameSpace bean = context.getBean(NameSpace.class);
        Assertions.assertNotNull(bean);

        /*
         * 自定义命名空间的方式:
         * 1. 新建 xsd 文件（位置无要求），自定义其中的 xmlns 和 targetNamespace，结尾即为命名空间
         * 2. 实现 NamespaceHandler 接口解析命名空间，一般不直接实现，而是继承 NamespaceHandlerSupport
         * 3. 创建 spring.handlers 文件，指定命名空间的解析方式
         * 4. 创建 spring.schemas 文件，指定 xsd 文件的路径
         * 5. spring.handlers 和 spring.schemas 都置于 META-INF 目录下
         * 6. 在 Spring 配置文件（如 applicationContext.xml）中使用自定义命名空间
         */
    }

    @Autowired
    private ApplicationEventPublisher publisher;

    @Test
    public void testMutationEvent() {
        ChineseHamburger hamburger = new ChineseHamburger();
        hamburger.setPrice(18);
        hamburger.setSize("M");
        publisher.publishEvent(new MutationEvent<>(hamburger, MutationType.CREATED));

        Pizza pizza = new Pizza();
        pizza.setName("NewYorkPizza");
        pizza.setPrice(25);
        publisher.publishEvent(new MutationEvent<>(pizza, MutationType.UPDATED));

        /*
         * 使用带泛型的事件类型:
         * 1. 定义一个带泛型的事件对象：类继承 ApplicationEvent，表示 Spring Event；但由于泛型擦除，
         *    将无法通过事件真正的内部对象类型来分发事件，为了解决这个问题，需要使类实现 ResolvableTypeProvider。
         * 2. 如果未实现 ResolvableTypeProvider 接口，就算发送的泛型事件的内部对象类型与监听器指定的泛型事件的内部对象一样，
         *    也不会监听成功。
         *
         * 另一种发送事件的方式：使用注入的 ApplicationEventPublisher 发送事件。
         *
         * 另一种监听事件的方式：自定义事件监听类，交由 Spring 管理；编写监听方法，参数为需要监听的事件类型，
         *                   并使用 @EventListener 注解标记。
         */
    }
}

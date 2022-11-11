package indi.mofan.bean;

import indi.mofan.enums.MutationType;
import indi.mofan.event.ChineseHamburger;
import indi.mofan.event.CommonEvent;
import indi.mofan.event.DeleteEvent;
import indi.mofan.event.MutationEvent;
import indi.mofan.event.OrderEvent;
import indi.mofan.event.Pizza;
import indi.mofan.event.listener.DeleteListener;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/11/11 14:15
 */
@SpringBootTest
public class EventTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ApplicationEventPublisher publisher;

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
        applicationContext.publishEvent(new DeleteEvent("applicationContext 发出的删除事件"));
    }

    @Test
    public void testEventPropagation() {
        AnnotationConfigApplicationContext parent = new AnnotationConfigApplicationContext();
        parent.register(DeleteListener.class);
        parent.refresh();

        AnnotationConfigApplicationContext child = new AnnotationConfigApplicationContext();
        child.refresh();

        child.setParent(parent);

        // 通过子容器发布事件，能够在父容器监听到
        child.publishEvent(new DeleteEvent("子容器发送的事件..."));
    }

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

    @Test
    public void testOrderEvent() {
        OrderEvent event = new OrderEvent("有序的消息");
        applicationContext.publishEvent(event);
    }

    @Test
    @SneakyThrows
    public void testAsyncListener() {
        System.out.println("start...");
        CommonEvent event = new CommonEvent("异步监听器的消息");
        applicationContext.publishEvent(event);
        System.out.println("end...");
        // 等待打印事件信息
        TimeUnit.SECONDS.sleep(4);
    }
}

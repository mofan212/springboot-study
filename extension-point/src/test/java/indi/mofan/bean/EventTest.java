package indi.mofan.bean;

import indi.mofan.event.CommonEvent;
import indi.mofan.event.OrderEvent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/11/11 14:15
 */
@SpringBootTest
public class EventTest {

    @Autowired
    private ApplicationContext applicationContext;

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

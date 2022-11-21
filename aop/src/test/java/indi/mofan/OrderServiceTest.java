package indi.mofan;

import indi.mofan.pojo.CreateOrUpdateOrder;
import indi.mofan.pojo.SaveOrder;
import indi.mofan.pojo.UpdateOrder;
import indi.mofan.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/11/18 23:15
 */
@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    @SneakyThrows
    public void testSaveAndUpdate() {
        SaveOrder saveOrder = new SaveOrder();
        saveOrder.setId(1L);
        orderService.saveOrder(saveOrder);

        UpdateOrder updateOrder = new UpdateOrder();
        updateOrder.setOrderId(2L);
        orderService.updateOrder(updateOrder);

        // 睡三秒，打印日志
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    @SneakyThrows
    public void test() {
        CreateOrUpdateOrder order = new CreateOrUpdateOrder();
        order.setIdOfOrder(212L);
        orderService.createOrUpdateOrder(order);

        // 睡一秒，异步打印日志
        TimeUnit.SECONDS.sleep(1);
    }
}

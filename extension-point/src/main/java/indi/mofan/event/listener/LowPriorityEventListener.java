package indi.mofan.event.listener;

import indi.mofan.event.OrderEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/11/11 14:23
 */
@Component
public class LowPriorityEventListener {

    @Order(3)
    @EventListener
    public void handleOrderEvent(OrderEvent orderEvent) {
        System.out.println("低优先级监听器监听到消息: " + orderEvent.getSource());
    }

    @Order(4)
    @EventListener
    public void handleOrderEventAgain(OrderEvent orderEvent) {
        System.out.println("最低优先级监听器监听到消息: " + orderEvent.getSource());
    }
}

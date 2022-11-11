package indi.mofan.event.listener;

import indi.mofan.event.OrderEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/11/11 14:24
 */
@Component
public class MediumPriorityListener implements ApplicationListener<OrderEvent>, Ordered {

    @Override
    public void onApplicationEvent(OrderEvent event) {
        System.out.println("中等优先级监听器监听到消息: " + event.getSource());
    }

    @Override
    public int getOrder() {
        return 2;
    }
}

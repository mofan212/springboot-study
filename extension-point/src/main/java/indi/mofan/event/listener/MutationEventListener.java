package indi.mofan.event.listener;

import indi.mofan.event.MutationEvent;
import indi.mofan.event.Pizza;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/17 21:27
 */
@Component
public class MutationEventListener implements ApplicationListener<MutationEvent<Pizza>> {
    @Override
    public void onApplicationEvent(MutationEvent<Pizza> event) {
        System.out.println("监听到 Pizza...");
        System.out.println("类型是: " + event.getType());
        Pizza pizza = event.getSource();
        System.out.println("Pizza 名称为: " + pizza.getName() + ", 价格为: " + pizza.getPrice());
    }
}

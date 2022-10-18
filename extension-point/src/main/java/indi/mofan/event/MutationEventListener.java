package indi.mofan.event;

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
        /*
         * 如果 MutationEvent 未实现 ResolvableTypeProvider，就算发送的事件类型与泛型类型
         * 和当前的监听器指定的类型相等，当前监听器也无法监听成功。
         */
        System.out.println("监听到 Pizza...");
        System.out.println("类型是: " + event.getType());
        Pizza pizza = event.getSource();
        System.out.println("Pizza 名称为: " + pizza.getName() + ", 价格为: " + pizza.getPrice());
    }
}

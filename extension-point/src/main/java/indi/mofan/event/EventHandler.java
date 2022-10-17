package indi.mofan.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/17 21:48
 */
@Component
public class EventHandler {

    @EventListener
    public void handleChineseHamburger(MutationEvent<ChineseHamburger> event) {
        System.out.println("监听到肉夹馍...");
        System.out.println("类型是: " + event.getType());
        ChineseHamburger hamburger = event.getSource();
        System.out.println("肉夹馍的价格是: " + hamburger.getPrice() + ", 大小是: " + hamburger.getSize());
    }
}

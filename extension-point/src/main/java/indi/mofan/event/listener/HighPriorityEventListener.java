package indi.mofan.event.listener;

import indi.mofan.event.OrderEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/11/11 14:20
 */
@Component
public class HighPriorityEventListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(@NotNull Class<? extends ApplicationEvent> eventType) {
        return eventType == OrderEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == String.class;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("高优先级监听器监听到消息: " + event.getSource());
    }
}

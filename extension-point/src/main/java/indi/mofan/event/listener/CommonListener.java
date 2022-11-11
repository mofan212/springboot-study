package indi.mofan.event.listener;

import indi.mofan.event.CommonEvent;
import lombok.SneakyThrows;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/11/11 14:40
 */
@Component
public class CommonListener {

    @Async
    @EventListener
    @SneakyThrows
    public void asyncListener(CommonEvent commonEvent) {
        TimeUnit.SECONDS.sleep(3);
        System.out.println("异步监听器监听到通用事件: " + commonEvent.getSource());
    }
}

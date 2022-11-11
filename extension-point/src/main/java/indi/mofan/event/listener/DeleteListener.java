package indi.mofan.event.listener;

import indi.mofan.event.DeleteEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/14 15:45
 */
@Component
public class DeleteListener implements ApplicationListener<DeleteEvent> {
    @Override
    public void onApplicationEvent(DeleteEvent event) {
        System.out.println(event.getSource());
        System.out.println("监听到删除事件");
    }
}

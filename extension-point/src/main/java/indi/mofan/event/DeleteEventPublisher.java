package indi.mofan.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/14 16:11
 */
@Component
public class DeleteEventPublisher implements ApplicationEventPublisherAware {
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        applicationEventPublisher.publishEvent(new DeleteEvent("aware 发出的删除事件"));
    }
}

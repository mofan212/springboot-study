package indi.mofan.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mofan
 * @date 2022/11/11 14:16
 */
public class OrderEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2564178739929927142L;

    public OrderEvent(String info) {
        super(info);
    }
}

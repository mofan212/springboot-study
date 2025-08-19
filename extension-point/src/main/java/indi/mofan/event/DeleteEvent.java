package indi.mofan.event;

import org.springframework.context.ApplicationEvent;

import java.io.Serial;

/**
 * @author mofan
 * @date 2022/10/14 15:44
 */
public class DeleteEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 6733924474772699555L;

    public DeleteEvent(String source) {
        super(source);
    }
}

package indi.mofan.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author mofan
 * @date 2022/11/11 14:41
 */
public class CommonEvent extends ApplicationEvent {
    private static final long serialVersionUID = 8680996782174950649L;

    public CommonEvent(String source) {
        super(source);
    }
}

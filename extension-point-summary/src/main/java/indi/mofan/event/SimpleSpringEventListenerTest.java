package indi.mofan.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2024/3/1 18:16
 */
@SpringBootApplication
public class SimpleSpringEventListenerTest {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SimpleSpringEventListenerTest.class, args);
        context.publishEvent(new UserRegisterEvent("成功注册用户: mofan"));
        context.close();
    }

    @Slf4j
    @Component
    private static class UserRegisterEventListener {
        @EventListener
        public EventChain doEvent(UserRegisterEvent event) {
            log.info("监听到用户注册事件: {}", event.info());
            return new EventChain("事件链");
        }

        @EventListener
        public void doChain(EventChain chain) {
            log.info("监听到事件链: {}", chain.info());
        }
    }

    private record UserRegisterEvent(String info) {
    }

    private record EventChain(String info) {
    }
}

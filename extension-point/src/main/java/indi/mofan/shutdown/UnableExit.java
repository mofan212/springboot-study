package indi.mofan.shutdown;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mofan
 * @date 2023/10/15 17:28
 */
public class UnableExit {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        context.registerShutdownHook();
        exitWithLock();
    }

    private static void simpleExit() {
        System.exit(0);
    }

    private static final Lock LOCK = new ReentrantLock();

    private static void exitWithLock() {
        LOCK.lock();
        new Thread(() -> System.exit(0)).start();
        LOCK.unlock();
    }

    @Configuration
    static class Config {
        @Bean
        public UnableExitApi unableExitApi() {
            return new Deadlock();
        }
    }

    interface UnableExitApi {
    }

    @SuppressWarnings("all")
    static class EndlessLoop implements UnableExitApi, DisposableBean {
        @Override
        public void destroy() throws Exception {
            while (true) {
                System.out.println("I'm still alive");
                TimeUnit.SECONDS.sleep(5);
            }
        }
    }

    static class Deadlock implements UnableExitApi, DisposableBean {
        @Override
        public void destroy() {
            LOCK.lock();
            System.out.println("Hey! I'm still alive!");
            LOCK.unlock();
            System.out.println("Oh, no! I'm dead.");
        }
    }
}

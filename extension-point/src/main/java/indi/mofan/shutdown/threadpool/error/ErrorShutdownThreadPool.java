package indi.mofan.shutdown.threadpool.error;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/10/15 19:23
 */
@SpringBootApplication
public class ErrorShutdownThreadPool {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ErrorShutdownThreadPool.class);
        System.exit(0);
    }

    private static Object connection = new Object();

    @Component
    static class RedisService implements DisposableBean {
        public Object getConnection() {
            if (connection == null) {
                throw new RuntimeException("connection is null");
            }
            return connection;
        }

        @Override
        public void destroy() {
            connection = null;
        }
    }

    @Component
    static class MyThreadPool implements ApplicationRunner, DisposableBean {

        @Autowired
        private RedisService redisService;

        ExecutorService service = Executors.newFixedThreadPool(1);

        @Override
        public void run(ApplicationArguments args) throws Exception {
            // 模拟线程池中有未完成的任务
            execute(service, redisService);
        }

        @Override
        public void destroy() {
            destroyThreadPool(service).run();
        }
    }

    private static void executeManually(ApplicationContext context) {
        RedisService redisService = context.getBean(RedisService.class);
        ExecutorService service = Executors.newFixedThreadPool(1);
        execute(service, redisService);

        Runtime.getRuntime().addShutdownHook(new Thread(destroyThreadPool(service)));
    }

    private static Runnable destroyThreadPool(ExecutorService service) {
        return () -> {
            service.shutdown();
            try {
                // noinspection ResultOfMethodCallIgnored
                service.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static void execute(Executor service, RedisService redisService) {
        service.execute(() -> {
            System.out.println("开始执行任务一");
            System.out.println("获取连接: " + redisService.getConnection());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("任务一执行结束");
        });

        service.execute(() -> {
            System.out.println("开始执行任务二");
            System.out.println("获取连接: " + redisService.getConnection());
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("任务二执行结束");
        });
    }
}

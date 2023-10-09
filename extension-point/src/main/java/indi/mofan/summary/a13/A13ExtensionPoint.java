package indi.mofan.summary.a13;

import jakarta.annotation.PreDestroy;
import lombok.SneakyThrows;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/10/3 16:24
 */
@SpringBootApplication
public class A13ExtensionPoint {
    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A13ExtensionPoint.class);
        System.out.printf("2. [%s]: main 方法执行完毕，准备关闭容器\n", getCurrentThreadName());
        TimeUnit.SECONDS.sleep(1);
        context.close();
        /*
         * 可以不直接关闭容器，而是通过 `System.exit(0)` 关闭，此时会触发 shutdown hooks；
         * 或者是在 IDEA 中，手动停止程序，也会触发 shutdown hooks
         */
    }

    @Component
    static class MyRunner implements ApplicationRunner {
        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.printf("1. [%s]: 执行了 ApplicationRunner 方法\n", getCurrentThreadName());
        }
    }

    static class MyDisposableBean implements DisposableBean {

        @Override
        public void destroy() {
            System.out.printf("4. [%s]: 执行了 destroy 方法\n", getCurrentThreadName());
        }

        @PreDestroy
        public void preDestroy() {
            System.out.printf("3. [%s]: 执行了 @preDestroy 方法\n", getCurrentThreadName());
        }

        public void destroyMethod() {
            System.out.printf("5. [%s]: 执行了 destroy-method\n", getCurrentThreadName());
        }
    }

    @Configuration
    static class MyConfig {
        @Bean(destroyMethod = "destroyMethod")
        public MyDisposableBean myDisposableBean() {
            return new MyDisposableBean();
        }
    }

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}

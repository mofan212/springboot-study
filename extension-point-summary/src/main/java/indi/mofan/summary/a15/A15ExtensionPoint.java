package indi.mofan.summary.a15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.Lifecycle;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/10/11 11:05
 */
@SpringBootApplication
public class A15ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(A15ExtensionPoint.class);
        // System.exit(0);
        context.close();
    }

    @Component
    static class FirstSmartLifecycle implements SmartLifecycle {

        private boolean running = false;

        @Override
        public void start() {
            System.out.printf("[%s]: first start\n", getCurrentThreadName());
            this.running = true;
        }

        @Override
        public void stop() {
            // 非 SmartLifecycle 的子类才执行
            System.out.printf("[%s]: first stop\n", getCurrentThreadName());
            this.running = false;
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public void stop(Runnable callback) {
            System.out.printf("[%s]: first stop(Runnable)\n", getCurrentThreadName());
            // 显式调用 callback
            callback.run();
            this.running = false;
        }

        @Override
        public int getPhase() {
            return 1;
        }
    }

    @Component
    static class SecondSmartLifecycle implements SmartLifecycle {

        private boolean running = false;

        @Override
        public void start() {
            System.out.printf("[%s]: second start\n", getCurrentThreadName());
            this.running = true;
        }

        @Override
        public void stop() {
            System.out.printf("[%s]: second stop\n", getCurrentThreadName());
            this.running = false;
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }

        @Override
        public void stop(Runnable callback) {
            System.out.printf("[%s]: second stop(Runnable)\n", getCurrentThreadName());
            callback.run();
            this.running = false;
        }

        @Override
        public int getPhase() {
            return 2;
        }
    }

    @Component
    static class MyLifecycle implements Lifecycle {

        private boolean running = true;

        @Override
        public void start() {
            System.out.printf("[%s]: lifecycle start\n", getCurrentThreadName());
        }

        @Override
        public void stop() {
            System.out.printf("[%s]: lifecycle stop\n", getCurrentThreadName());
            this.running = false;
        }

        @Override
        public boolean isRunning() {
            return this.running;
        }
    }

    public static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}

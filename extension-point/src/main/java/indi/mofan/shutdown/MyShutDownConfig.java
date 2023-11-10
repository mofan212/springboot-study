package indi.mofan.shutdown;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2023/10/7 16:45
 */
@Configuration
public class MyShutDownConfig {
    @Bean
    public MyDisposableBean myDisposableBean() {
        return new MyDisposableBean();
    }

    static class MyDisposableBean implements DisposableBean {
        @Override
        public void destroy() {
            // 不同的关闭方式，导致不同的线程来执行
            System.out.printf("[%s]: 执行 Bean 的销毁方法", Thread.currentThread().getName());
        }
    }
}

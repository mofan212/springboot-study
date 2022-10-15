package indi.mofan.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/10/15 11:21
 */
@Slf4j
@Configuration
public class ThreadConfig {

    @Bean(name = "executorService")
    public ExecutorService downloadExecutorService() {
        return new ThreadPoolExecutor(20, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000),
                Executors.defaultThreadFactory(),
                (r, executor) -> log.error("defaultExecutor pool is full!"));
    }
}

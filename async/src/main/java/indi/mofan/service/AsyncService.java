package indi.mofan.service;

import lombok.SneakyThrows;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/10/15 16:05
 */
@Service
public class AsyncService {

    /**
     * 使用 {@code @Async} 注解时，指定线程池
     * 在项目中，尽量做到线程池隔离，不同的任务使用不同的线程池
     */
    @SneakyThrows
    @Async("executorService")
    public void execute(Integer num) {
        TimeUnit.SECONDS.sleep(1);
        System.out.println("执行线程名称:" + Thread.currentThread().getName() + " , 任务：" + num);
    }
}

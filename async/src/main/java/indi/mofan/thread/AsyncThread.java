package indi.mofan.thread;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/10/15 10:55
 */
public class AsyncThread extends Thread {

    @Override
    @SneakyThrows
    public void run() {
        TimeUnit.SECONDS.sleep(1);
        // 模拟非主要可异步执行的业务
        System.out.println("当前线程名称为: " + this.getName() + ", 执行线程名称:" + Thread.currentThread().getName());
    }
}

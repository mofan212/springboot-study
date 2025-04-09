package indi.mofan;

import indi.mofan.service.AsyncService;
import indi.mofan.thread.AsyncThread;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/10/15 10:48
 */
@SpringBootTest
public class AsyncTest {

    @Autowired
    private ExecutorService executorService;

    @Test
    @SneakyThrows
    public void testAsyncThread() {
        // 模拟业务需求
        System.out.println("主线程开始...");
        AsyncThread asyncThread = new AsyncThread();
        // 开启线程，但不一定会立即执行，得看 CPU 的调度
        asyncThread.start();
        // 主线程中模拟其他主要业务
        for (int i = 0; i < 10; i++) {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("主线程继续执行中...");
        }
        // 主线程结束
        System.out.println("主线程结束...");
        // 运行后，可以看到在主线程执行时，新建的线程被调度执行（如果不行，将循环次数调大）
    }

    @Test
    @SneakyThrows
    public void testThreadPool() {
        // 模拟业务需求
        System.out.println("主线程开始...");

        executorService.execute(() -> {
            sleepMilliseconds(1000);
            System.out.println("执行线程名称:" + Thread.currentThread().getName());
        });

        for (int i = 0; i < 10; i++) {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("主线程继续执行中...");
        }
        // 主线程结束
        System.out.println("主线程结束...");
    }

    @Test
    @SneakyThrows
    public void testFuture() {
        Future<String> future = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(5);
            return "异步处理，Callable 返回结果";
        });

        System.out.println("主线程开始...");

        try {
            // get() 操作获取执行结果，但会产生阻塞，直到任务执行完成
            // Future 其他 api 见 notes.md
            System.out.println(future.get());
        } catch (Exception e) {
            // 异常处理
        }

        System.out.println("主线程结束...");
    }

    @Test
    public void testFutureTask() {
        // FutureTask 的线程安全由 CAS 保证
        FutureTask<Integer> task = new FutureTask<>(() -> {
            System.out.println("子线程开始计算: ");
            int sum = 0;
            for (int i = 0; i <= 100; i++) {
                sum += i;
            }
            return sum;
        });

        // 线程池执行任务，执行结果封装在 FutureTask 中
        executorService.submit(task);

        try {
            System.out.println("计算结果为: " + task.get());
        } catch (Exception e) {
            // 异常处理
        }
    }

    @Test
    public void testCompletableFuture() {
        /*
         * 以泡茶为例：
         * 泡茶需要洗水壶、烧开水、洗茶壶、洗茶杯、拿茶叶，最后再泡茶。
         * 这些事项不一定需要同步执行，可以拆分成三个任务：
         * 1. 任务一：洗水壶、烧开水
         * 2. 任务二：洗茶壶、洗茶杯、拿茶叶
         * 3. 任务三：泡茶
         * 任务之间的关系如下：任务一和任务二可以同时进行，但任务三必须在任务一和任务二执行完成后再执行
         */

        // 任务一
        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("T1:洗水壶...");
                    sleepMilliseconds(100);

                    System.out.println("T1:烧开水...");
                    sleepMilliseconds(1500);
                });

        // 任务二
        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("T2:洗茶壶...");
                    sleepMilliseconds(100);

                    System.out.println("T2:洗茶杯...");
                    sleepMilliseconds(200);

                    System.out.println("T2:拿茶叶...");
                    sleepMilliseconds(100);
                    return "龙井";
                });

        // 任务一、任务二完成后再执行任务三
        CompletableFuture<String> f3 =
                f1.thenCombine(f2, (unused, s) -> {
                    System.out.println("T1:拿到茶叶:" + s);
                    System.out.println("T1:泡茶...");
                    return "上茶:" + s;
                });

        // 任务三的执行结果
        System.out.println(f3.join());
    }

    @SneakyThrows
    private void sleepMilliseconds(long timeout) {
        TimeUnit.MILLISECONDS.sleep(timeout);
    }

    @Autowired
    private AsyncService asyncService;

    @Test
    @SneakyThrows
    public void testAsyncAnnotation() {
        // 模拟业务需求
        System.out.println("主线程开始...");

        asyncService.execute(100);

        for (int i = 0; i < 10; i++) {
            TimeUnit.MILLISECONDS.sleep(200);
            System.out.println("主线程继续执行中...");
        }
        // 主线程结束
        System.out.println("主线程结束...");
    }
}

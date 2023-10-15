package indi.mofan.shutdown.threadpool;

import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/10/15 18:41
 */
public class ShutdownThreadPool {
    public static void main(String[] args) {
        shutdownNowAndAwait();
    }

    private static ExecutorService getExecutorService() {
        ExecutorService service = Executors.newFixedThreadPool(1);
        for (int i = 1; i <= 5; i++) {
            String str = i + "";
            service.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(str);
                }

                @Override
                public String toString() {
                    return "第 " + str + " 个任务";
                }
            });
        }
        return service;
    }

    private static void useShutdownOnly() {
        ExecutorService service = getExecutorService();
        service.shutdown();
        System.out.println("线程池关闭了");
    }

    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void shutdownAndAwait() {
        ExecutorService service = getExecutorService();
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("线程池关闭了");
    }

    private static void shutdownNow() {
        ExecutorService service = getExecutorService();
        List<Runnable> list = service.shutdownNow();
        System.out.println("线程池关闭了");
        list.forEach(System.out::println);
    }

    @SneakyThrows
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void shutdownNowAndAwait() {
        ExecutorService service = getExecutorService();
        List<Runnable> list = service.shutdownNow();
        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("线程池关闭了");
        list.forEach(System.out::println);
    }
}

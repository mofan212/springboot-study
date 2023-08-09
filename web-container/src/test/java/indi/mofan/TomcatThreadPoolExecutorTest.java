package indi.mofan;

import lombok.SneakyThrows;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/8/9 10:35
 * @link <a href="https://mp.weixin.qq.com/s/2nazpk9G6cDWzYe3kkqklg">面试官：一个 SpringBoot 项目能处理多少请求？</a>
 */
public class TomcatThreadPoolExecutorTest {

    /*
     * Tomcat 和 JDK 的线程池有区别：
     * - JDK 的线程池先使用核心线程数配置，然后使用队列长度，最后使用最大线程配置。
     * - Tomcat 的线程池则是先使用核心线程数配置，再使用最大线程配置，最后才使用队列长度。
     */
    @Test
    @SneakyThrows
    public void testThreadPoolExecutor() {
        String namePrefix = "mofan-exec-";
        boolean daemon = true;
        /*
         * TaskQueue 是 Tomcat 基于 LinkedBlockingQueue 设计的阻塞队列
         * 这里设置队列长度为 300
         */
        TaskQueue taskqueue = new TaskQueue(300);
        TaskThreadFactory tf = new TaskThreadFactory(namePrefix, daemon, Thread.NORM_PRIORITY);
        // 线程池核心线程数为 5，最大线程数 150
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                150, 60000, TimeUnit.MILLISECONDS, taskqueue, tf);
        /*
         * parent 就是 Tomcat 的线程池，可以认为在 Tomcat 的环境下，parent 不会为空
         * 也就是说，存在下述代码时，就是 Tomcat 线程池，如果注释掉，就是 JDK 线程池
         * parent 为 null 时，就会将任务入队，与 JDK 线程池的逻辑一致
         */
        taskqueue.setParent(executor);
        for (int i = 0; i < 300; i++) {
            String name = "创建任务" + i;
            executor.execute(() -> {
                logStatus(executor, name);
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    Assertions.fail();
                }
            });
        }
        Thread.currentThread().join();
    }

    private void logStatus(ThreadPoolExecutor executor, String name) {
        TaskQueue queue = (TaskQueue) executor.getQueue();
        System.out.println(Thread.currentThread().getName() + "-" + name + ": " +
                           "核心线程数:" + executor.getCorePoolSize() +
                           "\t活动线程数:" + executor.getActiveCount() +
                           "\t最大线程数:" + executor.getMaximumPoolSize() +
                           "\t总任务数:" + executor.getTaskCount() +
                           "\t当前排队线程数:" + queue.size() +
                           "\t队列剩余大小:" + queue.remainingCapacity());
    }
}

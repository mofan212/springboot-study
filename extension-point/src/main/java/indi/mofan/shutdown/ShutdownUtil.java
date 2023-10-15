package indi.mofan.shutdown;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Random;

/**
 * @author mofan
 * @date 2023/10/7 16:47
 */
public class ShutdownUtil {
    public static void shutdown(ConfigurableApplicationContext context) {
        int randomInt = new Random().nextInt(0, 10);
        if ((randomInt & 0b1) == 0) {
            System.out.printf("[%s]: 即将执行 context.close() 关闭容器\n", Thread.currentThread().getName());
            context.close();
        } else {
            System.out.printf("[%s]: 即将执行 System.exit(0) 退出程序\n", Thread.currentThread().getName());
            System.exit(0);
        }
    }
}

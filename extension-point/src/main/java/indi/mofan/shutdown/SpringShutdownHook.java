package indi.mofan.shutdown;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author mofan
 * @date 2023/10/7 16:30
 */
public class SpringShutdownHook {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(MyConfig.class);
        // 非 SpringBoot 环境下，需要手动注册
        context.registerShutdownHook();
        ShutdownUtil.shutdown(context);
    }
}

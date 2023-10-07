package indi.mofan.shutdown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author mofan
 * @date 2023/10/7 16:16
 */
@SpringBootApplication
public class SpringBootShutdownHook {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringBootShutdownHook.class);
        ShutdownUtil.shutdown(context);
    }
}

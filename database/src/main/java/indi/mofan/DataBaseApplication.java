package indi.mofan;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author mofan
 * @date 2025/4/9 19:40
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class DataBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataBaseApplication.class, args);
    }
}

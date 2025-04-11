package indi.mofan;


import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mofan
 * @date 2025/3/26 16:43
 */
@EnableDubbo
@SpringBootApplication
public class MissingBeanApplication {
    public static void main(String[] args) {
        SpringApplication.run(MissingBeanApplication.class, args);
    }
}

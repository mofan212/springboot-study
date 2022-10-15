package indi.mofan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 多 module 下，SpringBoot 会扫描启动类所在目录及其子目录。
 * 为了能够被扫描到，启动类应当放在所有类的最外层包下。
 */
@EnableAsync
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

package indi.mofan.service;


import indi.mofan.service.c.MyComponent;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

/**
 * @author mofan
 * @date 2025/4/16 16:55
 */
@EnableDubbo
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        System.out.println("当前容器中存在的 MyComponent 类型的 Bean 的名称: "
                           + Arrays.toString(context.getBeanNamesForType(MyComponent.class)));
        context.close();
    }
}

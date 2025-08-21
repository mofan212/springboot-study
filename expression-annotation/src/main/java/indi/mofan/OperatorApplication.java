package indi.mofan;


import indi.mofan.registrar.EnableOperator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author mofan
 * @date 2025/8/21 20:00
 */
@EnableOperator
@SpringBootApplication
public class OperatorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperatorApplication.class, args);
    }
}

package indi.mofan;

import indi.mofan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author mofan
 * @date 2024/5/17 16:05
 */
@Slf4j
@SpringBootApplication
public class StateMachineApplication {
    public static void main(String[] args) {
        Thread.currentThread().setName("主线程");
        ConfigurableApplicationContext context = SpringApplication.run(StateMachineApplication.class, args);
        OrderService orderService = (OrderService) context.getBean("orderService");

        orderService.create();
        orderService.create();
        orderService.pay(1);

        new Thread("客户线程") {
            @Override
            public void run() {
                orderService.deliver(1);
                orderService.receive(1);
            }
        }.start();

        orderService.pay(2);
        orderService.deliver(2);
        orderService.receive(2);

        log.info("全部订单状态：{}", orderService.getOrders());
    }
}

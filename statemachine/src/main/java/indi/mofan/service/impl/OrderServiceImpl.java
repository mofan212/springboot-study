package indi.mofan.service.impl;

import indi.mofan.domain.Order;
import indi.mofan.enums.OrderStatus;
import indi.mofan.enums.OrderStatusChangeEvent;
import indi.mofan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mofan
 * @date 2024/5/17 11:40
 */
@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StateMachine<OrderStatus, OrderStatusChangeEvent> orderStateMachine;

    @Autowired
    private StateMachinePersister<OrderStatus, OrderStatusChangeEvent, Order> persister;

    private int id = 1;
    private final Map<Integer, Order> orders = new HashMap<>();

    /**
     * 模拟创建订单
     */
    @Override
    public Order create() {
        Order order = new Order();
        order.setStatus(OrderStatus.WAIT_PAYMENT);
        order.setId(id++);
        orders.put(order.getId(), order);
        log.info("线程名称：{}，创建了订单号为 {} 的订单", Thread.currentThread().getName(), order.getId());
        return order;
    }

    @Override
    public Order pay(int id) {
        Order order = orders.get(id);
        log.info("线程名称：{} 尝试支付，订单号：{}", Thread.currentThread().getName(), id);
        Message<OrderStatusChangeEvent> message = MessageBuilder
                .withPayload(OrderStatusChangeEvent.PAYED)
                .setHeader("order", order)
                .build();
        if (sendEvent(message, order)) {
            log.info("线程名称：{} 支付失败, 状态异常，订单号：{}", Thread.currentThread().getName(), id);
        }
        return order;
    }

    @Override
    public Order deliver(int id) {
        Order order = orders.get(id);
        log.info("线程名称：{} 尝试发货，订单号：{}", Thread.currentThread().getName(), id);
        Message<OrderStatusChangeEvent> message = MessageBuilder.withPayload(OrderStatusChangeEvent.DELIVERY)
                .setHeader("order", order)
                .build();
        if (sendEvent(message, order)) {
            log.info("线程名称：{} 发货失败，状态异常，订单号：{}", Thread.currentThread().getName(), id);
        }
        return order;
    }

    @Override
    public Order receive(int id) {
        Order order = orders.get(id);
        log.info("线程名称：{} 尝试收货，订单号：{}", Thread.currentThread().getName(), id);
        Message<OrderStatusChangeEvent> message = MessageBuilder.withPayload(OrderStatusChangeEvent.RECEIVED)
                .setHeader("order", order)
                .build();
        if (sendEvent(message, order)) {
            log.info("线程名称：{} 收货失败，状态异常，订单号：{}", Thread.currentThread().getName(), id);
        }
        return order;
    }

    @Override
    public Map<Integer, Order> getOrders() {
        return Map.copyOf(orders);
    }

    /**
     * 发送订单状态转换事件
     */
    private synchronized boolean sendEvent(Message<OrderStatusChangeEvent> message, Order order) {
        boolean sendFailed = false;
        try {
            orderStateMachine.start();
            // 尝试恢复状态机状态
            persister.restore(orderStateMachine, order);
            // 添加延迟用于线程安全测试
            Thread.sleep(1000);
            sendFailed = !orderStateMachine.sendEvent(message);
            // 持久化状态机状态
            persister.persist(orderStateMachine, order);
        } catch (Exception e) {
            // do something
        } finally {
            orderStateMachine.stop();
        }
        return sendFailed;
    }
}

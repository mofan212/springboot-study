package indi.mofan.listener;

import indi.mofan.domain.Order;
import indi.mofan.enums.OrderStatus;
import indi.mofan.enums.OrderStatusChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;

/**
 * <p>
 * {@code @OnTransition} 注解配置了状态从 {@code source} 改变为 {@code target} 时需要执行的动作。
 * </p>
 * <p>
 * {@code @WithStateMachine} 注解将某个 Bean 与对应的状态机实例相关联，以响应状态机中状态的变化并触发
 * 对应的事件，这个注解常常与 {@link org.springframework.statemachine.annotation.OnTransition}、
 * {@link org.springframework.statemachine.annotation.OnStateEntry}、
 * {@link org.springframework.statemachine.annotation.OnStateExit} 等注解一起使用。
 * </p>
 *
 * @author mofan
 * @date 2024/5/17 11:26
 */
@Slf4j
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListenerImpl {
    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payTransaction(Message<OrderStatusChangeEvent> message) {
        changeStatus(message, "支付", OrderStatus.WAIT_DELIVER);
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
        changeStatus(message, "发货", OrderStatus.WAIT_RECEIVE);
        return true;
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveTransition(Message<OrderStatusChangeEvent> message) {
        changeStatus(message, "收货", OrderStatus.FINISH);
        return true;
    }

    private void changeStatus(Message<OrderStatusChangeEvent> message, String currentStatus, OrderStatus status) {
        Order order = (Order) message.getHeaders().get("order");
        if (order != null) {
            order.setStatus(status);
        }
        log.info("{}，状态机反馈信息：{}", currentStatus, message.getHeaders());
    }
}

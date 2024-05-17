package indi.mofan.listener;

import indi.mofan.domain.Order;
import indi.mofan.enums.OrderStatus;
import indi.mofan.enums.OrderStatusChangeEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2024/5/17 11:26
 */
@Slf4j
@Component
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListenerImpl {
    @OnTransition(source = "WAIT_PAYMENT", target = "WAIT_DELIVER")
    public boolean payTransaction(Message<OrderStatusChangeEvent> message) {
        changeStatus(message, OrderStatus.WAIT_DELIVER);
        return true;
    }

    @OnTransition(source = "WAIT_DELIVER", target = "WAIT_RECEIVE")
    public boolean deliverTransition(Message<OrderStatusChangeEvent> message) {
        changeStatus(message, OrderStatus.WAIT_RECEIVE);
        return true;
    }

    @OnTransition(source = "WAIT_RECEIVE", target = "FINISH")
    public boolean receiveTransition(Message<OrderStatusChangeEvent> message){
        changeStatus(message, OrderStatus.FINISH);
        return true;
    }

    private void changeStatus(Message<OrderStatusChangeEvent> message, OrderStatus status) {
        Order order = (Order) message.getHeaders().get("order");
        if (order != null) {
            order.setStatus(status);
        }
        log.info("收货，状态机反馈信息：{}", message.getHeaders());
    }
}

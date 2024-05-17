package indi.mofan.domain;

import indi.mofan.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2024/5/17 10:47
 */
@Getter
@Setter
public class Order {

    private int id;
    private OrderStatus status;

    @Override
    public String toString() {
        return "订单号: " + id + ", 订单状态: " + status;
    }
}

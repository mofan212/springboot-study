package indi.mofan.enums;

/**
 * 订单状态改变事件。订单状态的改变由事件驱动。
 *
 * @author mofan
 * @date 2024/5/17 10:49
 */
public enum OrderStatusChangeEvent {
    /**
     * 支付
     */
    PAYED,
    /**
     * 发货
     */
    DELIVERY,
    /**
     * 确认收货
     */
    RECEIVED;
}

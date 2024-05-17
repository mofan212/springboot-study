package indi.mofan.enums;

/**
 * 订单状态
 *
 * @author mofan
 * @date 2024/5/17 10:47
 */
public enum OrderStatus {
    /**
     * 待支付
     */
    WAIT_PAYMENT,
    /**
     * 待发货
     */
    WAIT_DELIVER,
    /**
     * 待收货
     */
    WAIT_RECEIVE,
    /**
     * 订单完成
     */
    FINISH;
}

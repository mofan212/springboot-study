package indi.mofan.log.convert;

import indi.mofan.log.OperateLogDo;
import indi.mofan.pojo.Order;

/**
 * @author mofan
 * @date 2022/11/21 12:49
 */
public class OrderConvert implements Convert<Order> {
    @Override
    public OperateLogDo convert(Order order) {
        OperateLogDo operateLogDo = new OperateLogDo();
        operateLogDo.setOrderId(order.getId());
        return operateLogDo;
    }
}

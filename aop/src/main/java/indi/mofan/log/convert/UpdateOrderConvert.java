package indi.mofan.log.convert;

import indi.mofan.log.OperateLogDo;
import indi.mofan.pojo.UpdateOrder;

/**
 * @author mofan
 * @date 2022/11/18 23:27
 */
public class UpdateOrderConvert implements Convert<UpdateOrder> {
    @Override
    public OperateLogDo convert(UpdateOrder updateOrder) {
        OperateLogDo operateLogDo = new OperateLogDo();
        operateLogDo.setOrderId(updateOrder.getOrderId());
        return operateLogDo;
    }
}

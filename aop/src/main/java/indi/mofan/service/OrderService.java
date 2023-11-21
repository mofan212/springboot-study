package indi.mofan.service;

import indi.mofan.log.RecordOperate;
import indi.mofan.log.convert.SaveOrderConvert;
import indi.mofan.log.convert.UpdateOrderConvert;
import indi.mofan.pojo.CreateOrUpdateOrder;
import indi.mofan.pojo.SaveOrder;
import indi.mofan.pojo.UpdateOrder;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.springframework.stereotype.Service;

/**
 * @author mofan
 * @date 2022/11/18 23:11
 */
@Service
@CanIgnoreReturnValue
public class OrderService {

    @RecordOperate(desc = "保存订单", convert = SaveOrderConvert.class)
    public Boolean saveOrder(SaveOrder saveOrder) {
        System.out.println("save order, orderId: " + saveOrder.getId());
        return Boolean.TRUE;
    }

    @RecordOperate(desc = "更新订单", convert = UpdateOrderConvert.class)
    public Boolean updateOrder(UpdateOrder updateOrder) {
        System.out.println("update order, orderId: " + updateOrder.getOrderId());
        return Boolean.TRUE;
    }

    @RecordOperate(desc = "创建或更新订单", orderId = "#order.idOfOrder")
    public Boolean createOrUpdateOrder(CreateOrUpdateOrder order) {
        System.out.println("create or update order, orderId: " + order.getIdOfOrder());
        return Boolean.TRUE;
    }
}

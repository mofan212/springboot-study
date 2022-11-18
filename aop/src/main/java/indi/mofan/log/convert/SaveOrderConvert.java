package indi.mofan.log.convert;

import indi.mofan.log.OperateLogDo;
import indi.mofan.pojo.SaveOrder;

/**
 * @author mofan
 * @date 2022/11/18 23:27
 */
public class SaveOrderConvert implements Convert<SaveOrder> {
    @Override
    public OperateLogDo convert(SaveOrder saveOrder) {
        OperateLogDo operateLogDo = new OperateLogDo();
        operateLogDo.setOrderId(saveOrder.getId());
        return operateLogDo;
    }
}

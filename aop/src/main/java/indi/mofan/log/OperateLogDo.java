package indi.mofan.log;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2022/11/18 23:19
 */
@Getter
@Setter
public class OperateLogDo {
    private Long orderId;
    private String desc;
    private String result;
}

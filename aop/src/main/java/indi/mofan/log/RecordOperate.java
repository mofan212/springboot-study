package indi.mofan.log;

import indi.mofan.log.convert.Convert;
import indi.mofan.log.convert.OrderConvert;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mofan
 * @date 2022/11/18 23:19
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RecordOperate {

    String desc() default "";

    /**
     * 支持 SPEL
     *
     * @return 订单 id 信息
     */
    String orderId() default "";

    Class<? extends Convert<?>> convert() default OrderConvert.class;
}

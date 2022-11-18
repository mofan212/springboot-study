package indi.mofan.log.convert;

import indi.mofan.log.OperateLogDo;

/**
 * @author mofan
 * @date 2022/11/18 23:25
 */
public interface Convert<T> {
    /**
     * 将不同的入参传唤为同一个日志对象
     *
     * @param t 方法入参
     * @return 日志对象
     */
    OperateLogDo convert(T t);
}

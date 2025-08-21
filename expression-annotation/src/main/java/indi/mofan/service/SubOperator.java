package indi.mofan.service;


import indi.mofan.registrar.Operator;

/**
 * @author mofan
 * @date 2025/8/21 19:57
 */
@Operator("#a - #b")
public interface SubOperator {
    Object sub(Object a, Object b);
}

package indi.mofan.service;


import indi.mofan.registrar.Operator;

/**
 * @author mofan
 * @date 2025/8/21 19:56
 */
@Operator("#a + #b")
public interface AddOperator {
    Object add(Object a, Object b);
}

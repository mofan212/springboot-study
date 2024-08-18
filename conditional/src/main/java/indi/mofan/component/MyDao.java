package indi.mofan.component;


import indi.mofan.condition.DaoCondition;
import org.springframework.context.annotation.Conditional;

/**
 * @author mofan
 * @date 2024/8/18 17:06
 */
@Conditional(DaoCondition.class)
public class MyDao {
}

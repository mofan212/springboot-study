package indi.mofan.component;


import indi.mofan.condition.RepositoryCondition;
import org.springframework.context.annotation.Conditional;

/**
 * @author mofan
 * @date 2024/8/18 17:07
 */
@Conditional(RepositoryCondition.class)
public class MyRepository {
}

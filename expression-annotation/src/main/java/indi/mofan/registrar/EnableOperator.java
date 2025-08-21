package indi.mofan.registrar;


import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mofan
 * @date 2025/8/21 19:26
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(OperatorComponentRegistrar.class)
public @interface EnableOperator {
    /**
     * @return 扫描路径
     */
    String[] value() default {};
}

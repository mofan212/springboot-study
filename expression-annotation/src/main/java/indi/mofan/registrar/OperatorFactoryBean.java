package indi.mofan.registrar;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Proxy;

/**
 * @author mofan
 * @date 2025/8/21 19:41
 */
@FieldNameConstants
public class OperatorFactoryBean implements FactoryBean<Object>, InitializingBean {
    @Getter
    @Setter
    private Class<?> type;
    @Getter
    @Setter
    private String expression;
    private Expression spelExpression;

    @Override
    public Object getObject() {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            StandardEvaluationContext context = new StandardEvaluationContext();
            char argName = 'a';
            for (Object arg : args) {
                context.setVariable(String.valueOf(argName++), arg);
            }
            return spelExpression.getValue(context);
        });
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public void afterPropertiesSet() {
        SpelExpressionParser parser = new SpelExpressionParser();
        this.spelExpression = parser.parseExpression(expression);
    }
}

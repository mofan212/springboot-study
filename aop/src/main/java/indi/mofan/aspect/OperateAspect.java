package indi.mofan.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.mofan.log.OperateLogDo;
import indi.mofan.log.RecordOperate;
import indi.mofan.log.convert.Convert;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2022/11/18 23:20
 */
@Aspect
@Component
public class OperateAspect {

    @Pointcut("@annotation(indi.mofan.log.RecordOperate)")
    public void pointcut() {
    }

    private final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)
    );

    @Around("pointcut()")
    @SuppressWarnings("unchecked")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
                RecordOperate annotation = signature.getMethod().getAnnotation(RecordOperate.class);

                OperateLogDo operateLogDo;
                String orderId = annotation.orderId();
                // 优先使用 SPEL
                if (StringUtils.isNotEmpty(orderId)) {
                    EvaluationContext context = getContext(proceedingJoinPoint.getArgs(), signature.getMethod());
                    SpelExpressionParser parser = new SpelExpressionParser();
                    Long id = parser.parseExpression(orderId).getValue(context, Long.class);
                    operateLogDo = new OperateLogDo();
                    operateLogDo.setOrderId(id);
                } else {
                    Class<? extends Convert<?>> clazz = annotation.convert();
                    Convert<Object> convert = (Convert<Object>) clazz.newInstance();
                    operateLogDo = convert.convert(proceedingJoinPoint.getArgs()[0]);
                }
                operateLogDo.setDesc(annotation.desc());
                operateLogDo.setResult(result.toString());

                ObjectMapper mapper = new ObjectMapper();
                System.out.println("operate log " + mapper.writeValueAsString(operateLogDo));
            } catch (InstantiationException | IllegalAccessException | JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    private EvaluationContext getContext(Object[] args, Method method) {
        // 获取方法的参数名
        String[] parameterNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
        if (parameterNames == null) {
            throw new RuntimeException("参数列表为 null");
        }

        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return context;
    }
}

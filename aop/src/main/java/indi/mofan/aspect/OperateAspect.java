package indi.mofan.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import indi.mofan.log.OperateLogDo;
import indi.mofan.log.RecordOperate;
import indi.mofan.log.convert.Convert;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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
    public void pointcut(){}

    private final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100)
    );

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object result = proceedingJoinPoint.proceed();
        THREAD_POOL_EXECUTOR.execute(() -> {
            try {
                MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
                RecordOperate annotation = signature.getMethod().getAnnotation(RecordOperate.class);

                Class<? extends Convert> clazz = annotation.convert();
                Convert convert = clazz.newInstance();
                OperateLogDo operateLogDo = convert.convert(proceedingJoinPoint.getArgs()[0]);

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
}

package indi.mofan.original;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mofan
 * @date 2023/10/26 15:06
 * @see org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator
 */
@EnableAspectJAutoProxy
@SpringBootApplication
public class ORIGINALTest {

    private static final String MY_SERVICE_BEAN_NAME = "indi.mofan.original.ORIGINALTest$MyService.ORIGINAL";

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ORIGINALTest.class);
        MyService service = context.getBean(MyService.class);
        // 不会被代理
        Assert.isTrue(service.addOne(1) == 2, "");
        context.close();
    }

    @Aspect
    @Component
    static class MyAspect {
        @Pointcut("@annotation(indi.mofan.original.ORIGINALTest.MyAnnotation)")
        public void pc() {
        }

        @Around("pc()")
        public Object before(ProceedingJoinPoint point) {
            try {
                Object proceed = point.proceed();
                if (proceed instanceof Integer result) {
                    return result + 1;
                }
                return proceed;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {

    }

    @Component(MY_SERVICE_BEAN_NAME)
    static class MyService {
        @MyAnnotation
        public int addOne(int value) {
            return value + 1;
        }
    }
}

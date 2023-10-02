package indi.mofan.summary.a07;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2023/5/29 14:05
 */
@SpringBootApplication
public class A07ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A07ExtensionPoint.class, args);
        context.close();
    }


    /**
     * <p>
     * 类似的接口还有：
     * <ul>
     *     <li>EnvironmentAware</li>
     *     <li>EmbeddedValueResolverAware</li>
     *     <li>ResourceLoaderAware</li>
     *     <li>ApplicationEventPublisherAware</li>
     *     <li>MessageSourceAware</li>
     *     <li>ApplicationStartupAware</li>
     * </ul>
     * </p>
     */
    @Component
    static class MyApplicationContextAware implements ApplicationContextAware {
        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            System.out.println("2. Spring 上下文 ApplicationContext 被注入");
        }
    }

    @Component
    static class MyApplicationContext {
        private ApplicationContext applicationContext;

        @Autowired
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            System.out.println("1. 使用 @Autowired 注入 ApplicationContext");
        }
    }
}

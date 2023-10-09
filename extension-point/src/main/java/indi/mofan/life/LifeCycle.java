package indi.mofan.life;

import indi.mofan.pojo.Life;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author mofan
 * @date 2022/10/11 21:11
 */
public class LifeCycle implements InitializingBean, ApplicationContextAware, DisposableBean {

    @Autowired
    private Life life;

    public LifeCycle() {
        System.out.println("LifeCycle 对象被创建了");
    }

    /**
     * 实现的 Aware 回调接口
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Aware 接口起作用，setApplicationContext 被调用了，此时 life =" + life);
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("@PostConstruct 注解起作用，postConstruct 方法被调用了");
    }

    /**
     * 实现 InitializingBean 接口
     */
    @Override
    public void afterPropertiesSet() {
        System.out.println("InitializingBean 接口起作用，afterPropertiesSet 方法被调用了");
    }

    /**
     * 通过 {@link @Bean#initMethod()}来指定
     */
    public void initMethod() {
        System.out.println("@Bean#initMethod() 起作用，initMethod 方法被调用了");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy 注解起作用，preDestroy 方法被调用了");
    }

    /**
     * 通过 {@link @Bean#destroyMethod()}来指定
     */
    public void destroyMethod() {
        System.out.println("@Bean#destroyMethod() 起作用，destroyMethod 方法被调用了");
    }

    /**
     * 实现 DisposableBean 注解
     */
    @Override
    public void destroy() {
        System.out.println("DisposableBean 接口起作用，destroy 方法被调用了");
    }

}

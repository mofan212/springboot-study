package indi.mofan.summary.a08;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author mofan
 * @date 2023/5/29 16:18
 */
@SpringBootApplication
public class A08ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A08ExtensionPoint.class, args);
        context.close();
    }

    @Component
    static class A07Dependence {
        public A07Dependence() {
            System.out.println("1. 执行了 A07Dependence 的无参构造");
        }
    }

    @Component
    static class A07Example implements InitializingBean {
        private A07Dependence dependence;

        public A07Example(A07Dependence dependence) {
            System.out.println("2. 执行了 A07Example 的无参构造");
        }

        @Autowired
        public void setDependence(A07Dependence dependence) {
            this.dependence = dependence;
            System.out.println("3. A07Example 注入 dependence");
        }

        @PostConstruct
        public void init() {
            System.out.println("4. 执行了 @PostConstruct 标记的方法");
        }

        @Override
        public void afterPropertiesSet() throws Exception {
            System.out.println("5. 执行了 afterPropertiesSet 标记的方法");
        }
    }
}

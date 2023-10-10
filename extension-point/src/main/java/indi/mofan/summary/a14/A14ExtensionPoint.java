package indi.mofan.summary.a14;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author mofan
 * @date 2023/10/9 11:22
 * @see indi.mofan.summary.a07.A07ExtensionPoint
 */
@SpringBootApplication
public class A14ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(A14ExtensionPoint.class);
        MyBean myBean = context.getBean(MyBean.class);
        Assert.isTrue(BEAN_NAME.equals(myBean.beanName), "Bean 的 name 不相等");
        context.close();
    }

    private static final String BEAN_NAME = "A Yi A Yi A";

    static class MyBean implements BeanNameAware {

        private String beanName;

        @Override
        public void setBeanName(String name) {
            this.beanName = name;
        }
    }

    @Configuration
    static class MyConfig {
        @Bean(BEAN_NAME)
        public MyBean myBean() {
            return new MyBean();
        }
    }
}

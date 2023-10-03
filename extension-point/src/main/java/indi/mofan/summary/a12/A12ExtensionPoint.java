package indi.mofan.summary.a12;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author mofan
 * @date 2023/10/3 13:28
 */
@SpringBootApplication
public class A12ExtensionPoint {
    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(A12ExtensionPoint.class);
        // 通过 bean 的 name 获取
        Object bean = context.getBean(OWL_BEAN_NAME);
        Assert.isInstanceOf(Owl.class, bean);
        bean = context.getBean(BeanFactory.FACTORY_BEAN_PREFIX + OWL_BEAN_NAME);
        Assert.isInstanceOf(OwlFactoryBean.class, bean);
        Object sameBean = context.getBean(OWL_BEAN_NAME);
        Assert.isTrue(bean == sameBean, "");
        context.close();
    }

    static class Owl {
        @Getter
        @Setter
        private String name;

        public Owl() {
            System.out.println("执行了无参构造");
        }

        public Owl(String name) {
            this.name = name;
            System.out.println("执行了有参(name)构造");
        }
    }

    private static final String OWL_BEAN_NAME = "owl";

    @Component(OWL_BEAN_NAME)
    static class OwlFactoryBean implements FactoryBean<Owl> {

        public OwlFactoryBean() {
            System.out.println("执行了 OwlFactoryBean 的无参构造");
        }

        @Override
        public Owl getObject() throws Exception {
            System.out.println("执行 getObject--start");
            Owl owl = new Owl();
            System.out.println("执行 getObject--end");
            return owl;
        }

        @Override
        public Class<?> getObjectType() {
            return Owl.class;
        }

        @Override
        public boolean isSingleton() {
            return true;
        }
    }

}

package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mofan
 * @date 2023/6/14 10:59
 */
public class InjectMultiIdenticalTypeBeansTest implements WithAssertions {

    interface MyService {
    }

    static class FirstServiceImpl implements MyService, Ordered {
        @Override
        public int getOrder() {
            return 3;
        }
    }

    static class SecondServiceImpl implements MyService, Ordered {
        @Override
        public int getOrder() {
            return 1;
        }
    }

    static class ThirdServiceImpl implements MyService, Ordered {
        @Override
        public int getOrder() {
            return 2;
        }
    }

    @Configuration
    static class MyConfiguration {
        @Bean
//         @Primary
        public FirstServiceImpl firstService() {
            return new FirstServiceImpl();
        }

        @Bean
        public SecondServiceImpl secondService() {
            return new SecondServiceImpl();
        }

        @Bean
        public ThirdServiceImpl thirdService() {
            return new ThirdServiceImpl();
        }

        @Bean
        public Component component() {
            return new Component();
        }
    }

    static class Component {

        /**
         * 使用 @Autowired 按接口类型注入时，如果接口的实现类有多个，
         * 将按照名称注入，如果没有匹配的 Bean 名称，抛出
         * NoUniqueBeanDefinitionException 异常
         */
//        @Autowired
//        private MyService myService;

        @Autowired
        private MyService secondService;

        @Autowired
        @Qualifier("thirdService")
        private MyService service;

        @Autowired
        private MyService[] serviceArrays;

        @Autowired
        private List<MyService> serviceList;

        @Autowired
        private Set<MyService> serviceSet;

        @Autowired
        private Map<String, MyService> serviceMap;
    }

    private static Component component;

    @BeforeAll
    public static void init() {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfiguration.class);
        component = context.getBean(Component.class);
    }

    @Test
    public void testInjectMultiIdenticalTypeBeans() {
        // 使用 @Primary 后，以此注解标记的实现类作为注入的数据
//        assertThat(component.myService.getClass()).isEqualTo(FirstServiceImpl.class);

        // @Autowired 可以按名称注入（使用 @Primary 后，按名称注入失效）
        assertThat(component.secondService.getClass()).isEqualTo(SecondServiceImpl.class);

        // 使用 @Qualifier 指定注入的 Bean 名称
        assertThat(component.service.getClass()).isEqualTo(ThirdServiceImpl.class);
    }

    interface OtherService {
    }

    @Order(3)
    static class FirstOtherServiceImpl implements OtherService {
    }

    static class SecondOtherServiceImpl implements OtherService {
    }

    static class ThirdOtherServiceImpl implements OtherService {
    }

    @Configuration
    static class OtherConfig {
        @Bean
        public FirstOtherServiceImpl firstService() {
            return new FirstOtherServiceImpl();
        }

        @Bean
        @Order(1)
        public SecondOtherServiceImpl secondService() {
            return new SecondOtherServiceImpl();
        }

        @Bean
        @Order(2)
        public ThirdOtherServiceImpl thirdService() {
            return new ThirdOtherServiceImpl();
        }

        @Bean
        public OtherComponent component() {
            return new OtherComponent();
        }
    }

    static class OtherComponent {
        @Autowired
        private List<OtherService> otherServiceList;
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void testInjectCollection() {
        List<Class> serviceClazzList = List.of(SecondServiceImpl.class, ThirdServiceImpl.class, FirstServiceImpl.class);
        // 在 Configuration 类中组合使用 @Bean 和 @Order 无效，在实现类上使用 @Order 也无效，可以实现 Ordered 接口
        assertThat(Arrays.stream(component.serviceArrays))
                .map(i -> (Class) i.getClass())
                .containsExactlyElementsOf(serviceClazzList);
        assertThat(component.serviceList)
                .map(i -> (Class) i.getClass())
                .containsExactlyElementsOf(serviceClazzList);
        assertThat(component.serviceSet)
                .map(i -> (Class) i.getClass())
                .containsExactlyInAnyOrderElementsOf(serviceClazzList);

        // @Order 无效
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OtherConfig.class);
        assertThat(context.getBean(OtherComponent.class).otherServiceList)
                .map(i -> (Class) i.getClass())
                .containsExactlyElementsOf(List.of(
                        FirstOtherServiceImpl.class,
                        SecondOtherServiceImpl.class,
                        ThirdOtherServiceImpl.class
                ));
    }

    @Test
    public void testInjectMap() {
        assertThat(component.serviceMap)
                .hasSize(3)
                .containsKeys("firstService", "secondService", "thirdService");
    }
}

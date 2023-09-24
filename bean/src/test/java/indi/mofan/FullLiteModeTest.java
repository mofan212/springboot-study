package indi.mofan;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author mofan
 * @date 2023/9/20 11:31
 */
public class FullLiteModeTest implements WithAssertions {

    @Setter
    @Getter
    static class Person {
        private Book book;
        private Book anotherBook;
    }

    static class Book {
    }

    @Configuration
    static class FullConfig {
        @Bean
        public Person person() {
            Person person = new Person();
            person.setBook(book());
            return person;
        }

        @Bean
        public Book book() {
            return new Book();
        }
    }

    //    @Configuration(proxyBeanMethods = false)
    @Component
    static class LiteConfig {

        @Autowired
        @Qualifier("liteBook")
        private Book book;

        @Bean
        public Person litePerson() {
            Person person = new Person();
            person.setBook(liteBook());
            // 也可以通过方法参数传进来
            person.setAnotherBook(book);
            return person;
        }

        @Bean
        public Book liteBook() {
            return new Book();
        }
    }

    @Configuration
    static class EmptyConfig {
    }

    private static final String LABEL_VALUE;

    static {
        try {
            Field label = SpringNamingPolicy.class.getDeclaredField("LABEL");
            label.setAccessible(true);
            LABEL_VALUE = (String) label.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @SneakyThrows
    public void testFullConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(FullConfig.class);
        FullConfig config = context.getBean(FullConfig.class);
        // 生成的是 config 代理对象
        assertThat(config.getClass().getName()).contains(LABEL_VALUE);

        // person 里的 book 和 Spring 容器中的是同一个
        Book bookInSpringContainer = (Book) context.getBean("book");
        Book bookInPerson = ((Person) (context.getBean("person"))).getBook();
        assertThat(bookInPerson).isSameAs(bookInSpringContainer);
    }

    @Test
    public void testLiteConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(LiteConfig.class);
        LiteConfig config = context.getBean(LiteConfig.class);
        // 生成的是原始对象
        assertThat(config.getClass().getName())
                .doesNotContain(LABEL_VALUE)
                .isEqualTo(LiteConfig.class.getName());

        // person 中的 book 与 Spring 容器中的不是同一个
        Person person = (Person) context.getBean("litePerson");
        Book liteBook = (Book) context.getBean("liteBook");
        assertThat(person.getBook()).isNotSameAs(liteBook);
        assertThat(person.getAnotherBook()).isSameAs(liteBook);
    }

    @Test
    public void testEmptyConfig() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EmptyConfig.class);
        EmptyConfig config = context.getBean(EmptyConfig.class);
        // 被 @Configuration 注解标记就会生成代理对象
        assertThat(config.getClass().getName()).contains(LABEL_VALUE);
    }
}

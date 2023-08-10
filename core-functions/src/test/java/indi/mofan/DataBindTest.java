package indi.mofan;

import lombok.Getter;
import lombok.Setter;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyValue;

/**
 * @author mofan
 * @date 2023/8/10 11:06
 */
public class DataBindTest implements WithAssertions {

    @Getter
    @Setter
    static class User {
        private Integer integer;
    }

    @Getter
    static class Person {
        private String personName;
    }

    @Test
    public void testBeanWrapper() {
        User user = new User();
        assertThat(user).extracting(User::getInteger).isNull();
        BeanWrapper wrapper = new BeanWrapperImpl(user);
        // 数据绑定过程还可以进行类型转换
        wrapper.setPropertyValue(new PropertyValue("integer", "123"));
        assertThat(user).extracting(User::getInteger).isEqualTo(123);

        // 数据绑定要求提供 Setter，否则无法绑定成功，@Value 注解的属性注入是通过反射，不要求有 Setter
        Person person = new Person();
        assertThat(person).extracting(Person::getPersonName).isNull();
        BeanWrapper personWrapper = new BeanWrapperImpl(person);
        assertThatExceptionOfType(NotWritablePropertyException.class)
                .isThrownBy(() -> personWrapper.setPropertyValue(new PropertyValue("personName", "mofan")));
    }
}

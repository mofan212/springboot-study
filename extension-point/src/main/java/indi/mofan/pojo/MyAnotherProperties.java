package indi.mofan.pojo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * @author mofan
 * @date 2023/6/25 10:36
 */
@Getter
@ConfigurationProperties("another-properties")
public class MyAnotherProperties {
    private final String myName;

    private final Integer age;

    @Value("${person.gender}")
    private String gender;

    private final InnerProperties innerProperties;

    @ConstructorBinding
    public MyAnotherProperties(String myName, Integer age, InnerProperties innerProperties) {
        this.myName = myName;
        this.age = age;
        // 不能直接 new 一个对象，必须通过构造方法传入，否则无法绑定配置文件中的信息
        // this.innerProperties = new InnerProperties();
        this.innerProperties = innerProperties;
    }
}

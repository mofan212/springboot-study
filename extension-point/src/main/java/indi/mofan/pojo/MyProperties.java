package indi.mofan.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mofan
 * @date 2022/10/27 10:24
 */
@Setter
@Getter
@ConfigurationProperties("properties")
public class MyProperties {

    private String name;

    private Integer age;

    @Value("${person.gender}")
    private String gender;
}

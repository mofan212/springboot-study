package indi.mofan.pojo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author mofan
 * @date 2022/10/12 21:03
 */
@Getter
@Component
@PropertySource({"classpath:application.json"})
public class Author {
    @Value("${name}")
    private String name;
}

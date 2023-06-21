package indi.mofan.pojo;

import indi.mofan.factory.YamlPropertySourceFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author mofan
 * @date 2023/6/21 20:05
 */
@Getter
@Configuration
@PropertySource(value = "classpath:my.yaml", encoding = "utf-8", factory = YamlPropertySourceFactory.class)
public class MyYamlProperties {
    @Value("${name}")
    private String name;
}

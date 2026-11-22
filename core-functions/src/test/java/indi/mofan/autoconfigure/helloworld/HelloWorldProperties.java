package indi.mofan.autoconfigure.helloworld;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author mofan
 * @date 2026/11/15 17:59
 */
@Setter
@Getter
@ConfigurationProperties("demo.hello-world")
public class HelloWorldProperties {
    private String prefix = "Hello, ";
}

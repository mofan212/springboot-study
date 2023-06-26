package indi.mofan.pojo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * @author mofan
 * @date 2022/10/27 10:24
 */
@Setter
@Getter
@ConfigurationProperties("properties")
public class MyProperties {

    private String myName;

    private Integer age;

    @Value("${person.gender}")
    private String gender;

    private InnerProperties innerProperties = new InnerProperties();

    private Duration defaultDuration;

    private Duration durationWithUnit;

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration duration;

    private Weight weight;
}

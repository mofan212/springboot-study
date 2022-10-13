package indi.mofan.pojo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * SpringBoot 默认只能读取到文件名为 application 的配置文件
 * 如果需要读取其他名称自定义文件，需要使用 @PropertySource 注解指定文件的位置
 *
 * @author mofan
 * @date 2022/10/12 21:03
 */
@Getter
@Component
public class Author {
    @Value("${author.name}")
    private String name;
}

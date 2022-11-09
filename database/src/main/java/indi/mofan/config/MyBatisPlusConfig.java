package indi.mofan.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author mofan
 * @date 2022/11/9 22:23
 */
@Configuration
@MapperScan("indi.mofan.dao")
public class MyBatisPlusConfig {
}

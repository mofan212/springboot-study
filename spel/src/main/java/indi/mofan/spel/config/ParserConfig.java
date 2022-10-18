package indi.mofan.spel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * @author mofan
 * @date 2022/10/18 16:50
 */
@Configuration
public class ParserConfig {

    @Bean
    public SpelExpressionParser spelExpressionParser() {
        return new SpelExpressionParser();
    }
}

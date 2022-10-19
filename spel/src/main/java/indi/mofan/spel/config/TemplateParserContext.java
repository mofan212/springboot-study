package indi.mofan.spel.config;

import org.springframework.expression.ParserContext;

/**
 * @author mofan
 * @date 2022/10/20 10:15
 */
public class TemplateParserContext implements ParserContext {
    @Override
    public boolean isTemplate() {
        return true;
    }

    @Override
    public String getExpressionPrefix() {
        return "${";
    }

    @Override
    public String getExpressionSuffix() {
        return "}";
    }
}

package indi.mofan.spel;

import indi.mofan.SPELApplication;
import indi.mofan.spel.pojo.Inventor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * @author mofan
 * @date 2022/10/18 16:45
 */
@SpringBootTest(classes = SPELApplication.class)
public class SimpleTest {

    @Autowired
    private SpelExpressionParser parser;

    @Test
    public void testGetStringValue() {
        Expression expression = parser.parseExpression("'Hello World'");
        Assertions.assertEquals("Hello World", expression.getValue());
    }

    @Test
    public void testConcatString() {
        Expression expression = parser.parseExpression("'Hello World'.concat('!')");
        Assertions.assertEquals("Hello World!", expression.getValue());
    }

    @Test
    public void testGetProperty() {
        Expression expression = parser.parseExpression("'Hello World'.bytes");
        byte[] value = (byte[]) expression.getValue();
        Assertions.assertTrue(value != null && value.length > 0);
    }

    @Test
    public void testGetNestedProperties() {
        Expression expression = parser.parseExpression("'Hello World'.bytes.length");
        Integer length = Optional.of(expression)
                .map(Expression::getValue)
                .map(i -> (Integer) i)
                .orElse(0);
        Assertions.assertTrue(length > 0);
    }

    @Test
    public void testUseConstructor () {
        // 使用构造方法，而不是字面量（注意引号的使用）
        Expression expression = parser.parseExpression("new String('Hello World').toUpperCase()");
        Assertions.assertEquals("HELLO WORLD", expression.getValue(String.class));
    }

    @Test
    public void testGetPropertyValueOfCustomObject() {
        Instant instant = LocalDate.of(1856, 7, 9).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date birth = Date.from(instant);

        Inventor tesla = new Inventor("Nikola Tesla", birth, "Serbian");
        Expression exp = parser.parseExpression("name");
        // get name of tesla
        String name = exp.getValue(tesla, String.class);
        Assertions.assertEquals("Nikola Tesla", name);

        exp = parser.parseExpression("'Nikola Tesla' == name");
        Boolean result = exp.getValue(tesla, Boolean.class);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result);
    }
}

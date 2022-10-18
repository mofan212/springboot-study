package indi.mofan.spel;

import indi.mofan.Application;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mofan
 * @date 2022/10/18 17:44
 */
@SpringBootTest(classes = Application.class)
public class EvaluationTest {

    @Autowired
    private SpelExpressionParser parser;

    @Test
    public void testTypeConversion() {
        Simple simple = new Simple();
        simple.booleanList.add(true);

        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        // 给 simple 对象中的 booleanList 列表的第一个元素设置值为 false
        parser.parseExpression("booleanList[0]").setValue(context, simple, "false");

        Boolean b = simple.booleanList.get(0);
        Assertions.assertFalse(b);
    }

    private static class Simple {
        public List<Boolean> booleanList = new ArrayList<>();
    }


    @Test
    public void testParserConfiguration() {

        /*
         * 如果获取索引处的值为 null，自动调用对象的构造方法进行创建；
         * 如果指定的索引超出的最大索引，自动增长数组或列表以适配指定的索引。
         *
         * 上述两个操作将调用默认构造方法，如果没有默认构造方法，将添加 null 到数组或列表中。
         */
        SpelParserConfiguration config = new SpelParserConfiguration(true, true);
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser(config);

        Expression expression = spelExpressionParser.parseExpression("list[3]");
        Demo demo = new Demo();
        Object value = expression.getValue(demo);
        Assertions.assertEquals("", value);
    }

    private static class Demo {
        public List<String> list;
    }

    @Test
    public void testCompilerConfig() {
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE, Thread.currentThread().getContextClassLoader());
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser(config);

        Expression expression = spelExpressionParser.parseExpression("payload");
        MyMessage message = new MyMessage();
        message.setPayload("xx");
        String value = expression.getValue(message, String.class);
        Assertions.assertEquals("xx", value);
    }

    @Getter
    @Setter
    private static class MyMessage {
        public String payload;
    }
}

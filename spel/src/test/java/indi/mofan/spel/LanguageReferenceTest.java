package indi.mofan.spel;

import indi.mofan.Application;
import indi.mofan.spel.pojo.Inventor;
import indi.mofan.spel.pojo.PlaceOfBirth;
import indi.mofan.spel.pojo.Society;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author mofan
 * @date 2022/10/18 20:33
 */
@SpringBootTest(classes = Application.class)
public class LanguageReferenceTest {

    @Autowired
    private SpelExpressionParser parser;

    @Test
    public void testLiteralExpressions() {
        String str = parser.parseExpression("'Hello World'").getValue(String.class);
        Assertions.assertEquals("Hello World", str);

        // 单个单引号，需要使用 4 个单引号
        String singleQuotationMark = parser.parseExpression("''''").getValue(String.class);
        Assertions.assertEquals("'", singleQuotationMark);

        Integer maxIntValue = parser.parseExpression("0x7FFFFFFF").getValue(Integer.class);
        Assertions.assertEquals(Integer.MAX_VALUE, maxIntValue);

        Boolean trueValue = parser.parseExpression("true").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        Object nullValue = parser.parseExpression("null").getValue();
        Assertions.assertNull(nullValue);
    }

    @Test
    public void testEvalNestedPropertyValue() {
        Inventor tesla = getTeslaInfo();

        Integer year = (Integer) parser.parseExpression("birthdate.year + 1900").getValue(tesla);
        Assertions.assertNotNull(year);
        Assertions.assertEquals(1856, year);
        // 属性的首字母不区分大小写
        year = (Integer) parser.parseExpression("Birthdate.year + 1900").getValue(tesla);
        Assertions.assertNotNull(year);
        Assertions.assertEquals(1856, year);

        String city = parser.parseExpression("placeOfBirth.city").getValue(tesla, String.class);
        Assertions.assertEquals("Lika", city);
        city = parser.parseExpression("PlaceOfBirth.City").getValue(tesla, String.class);
        Assertions.assertEquals("Lika", city);
        // 也可以直接调用方法
        city = parser.parseExpression("getPlaceOfBirth().getCity()").getValue(tesla, String.class);
        Assertions.assertEquals("Lika", city);
    }

    @Test
    public void testGetValueOfArrayOrList() {
        Inventor tesla = getTeslaInfo();

        SpelExpressionParser parser = new SpelExpressionParser();
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        // tesla.inventions[0]
        String invention = parser.parseExpression("inventions[0]").getValue(context, tesla, String.class);
        Assertions.assertEquals("Induction motor", invention);

        Society society = new Society();
        society.getMembers().add(tesla);
        // society.members[0].name
        String inventorName = parser.parseExpression("members[0].name").getValue(context, society, String.class);
        Assertions.assertEquals("Nikola Tesla", inventorName);
        // society.members[0].inventions[0]
        String inventionName = parser.parseExpression("members[0].inventions[0]").getValue(context, society, String.class);
        Assertions.assertEquals("Induction motor", inventionName);
    }

    private Inventor getTeslaInfo() {
        Instant instant = LocalDate.of(1856, 7, 9).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
        Date birth = Date.from(instant);
        Inventor tesla = new Inventor("Nikola Tesla", birth, "Serbian");

        PlaceOfBirth placeOfBirth = new PlaceOfBirth("Lika", "Croatia");
        tesla.setPlaceOfBirth(placeOfBirth);

        tesla.setInventions(new String[]{"Induction motor"});

        return tesla;
    }
}

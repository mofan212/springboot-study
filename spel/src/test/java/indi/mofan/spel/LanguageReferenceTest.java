package indi.mofan.spel;

import indi.mofan.Application;
import indi.mofan.spel.config.SimpleBeanFactoryBean;
import indi.mofan.spel.pojo.Inventor;
import indi.mofan.spel.pojo.PlaceOfBirth;
import indi.mofan.spel.pojo.SimpleBean;
import indi.mofan.spel.pojo.Society;
import lombok.Getter;
import lombok.SneakyThrows;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        PlaceOfBirth placeOfBirth = new PlaceOfBirth("Lika");
        tesla.setPlaceOfBirth(placeOfBirth);

        tesla.setInventions(new String[]{"Induction motor"});

        return tesla;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetMapValue() {
        Society society = new Society();
        Inventor tesla = getTeslaInfo();
        society.getOfficers().put(Society.President, tesla);

        Inventor inventor = parser.parseExpression("officers['president']").getValue(society, Inventor.class);
        Assertions.assertNotNull(inventor);

        String city = parser.parseExpression("officers['president'].placeOfBirth.city").getValue(society, String.class);
        Assertions.assertEquals("Lika", city);

        society.getOfficers().clear();
        society.getOfficers().put(Society.Advisors, new ArrayList<>(Collections.singletonList(tesla)));
        // set value
        parser.parseExpression("officers['advisors'][0].placeOfBirth.country").setValue(society, "Croatia");
        Object mapValue = society.getOfficers().get(Society.Advisors);
        Assertions.assertNotNull(mapValue);
        Assertions.assertTrue(mapValue instanceof List);
        Assertions.assertEquals("Croatia", ((List<Inventor>) mapValue).get(0).getPlaceOfBirth().getCountry());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetList() {
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        List<Integer> intList = (List<Integer>) parser.parseExpression("{1, 2, 3, 4}").getValue(context);
        Assertions.assertNotNull(intList);
        MatcherAssert.assertThat(intList, CoreMatchers.is(Arrays.asList(1, 2, 3, 4)));

        List<List<String>> strListList = (List<List<String>>) parser.parseExpression("{{'a', 'b'}, {'c', 'd'}}").getValue(context);
        Assertions.assertNotNull(strListList);
        MatcherAssert.assertThat(strListList, CoreMatchers.is(Arrays.asList(Arrays.asList("a", "b"), Arrays.asList("c", "d"))));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetMap() {
        // 键中的单引号可以省略
        Map<String, Object> simpleMap = (Map<String, Object>) parser.parseExpression("{name: 'Nikola', 'dob': '10-July-1856'}").getValue();
        Assertions.assertNotNull(simpleMap);
        Assertions.assertEquals("Nikola", simpleMap.get("name"));
        Assertions.assertEquals("10-July-1856", simpleMap.get("dob"));

        // 如果键中存在点（DOT），那么单引号不能省略
        String exp = "{name: {first: 'Nikola', last: 'Tesla'}, dob: {day: 10, month: 'July', year: 1856}, 'placeOfBirth.country': 'Croatia'}";
        Map<String, Object> nestedMap = (Map<String, Object>) parser.parseExpression(exp).getValue();
        Assertions.assertNotNull(nestedMap);
        Assertions.assertEquals("Croatia", nestedMap.get("placeOfBirth.country"));
        Map<String, Object> nameMap = (Map<String, Object>) nestedMap.get("name");
        Assertions.assertNotNull(nameMap);
        Assertions.assertEquals("Nikola", nameMap.get("first"));
    }

    @Test
    public void testGetArray() {
        int[] emptyIntArray = (int[]) parser.parseExpression("new int[4]").getValue();
        Assertions.assertArrayEquals(new int[4], emptyIntArray);

        int[] intInitArray = (int[]) parser.parseExpression("new int[]{1, 2, 3}").getValue();
        Assertions.assertArrayEquals(new int[]{1, 2, 3}, intInitArray);

        // 多维数组不可初始化
        int[][] simpleMultiArray = (int[][]) parser.parseExpression("new int[4][5]").getValue();
        Assertions.assertArrayEquals(new int[4][5], simpleMultiArray);
    }

    @Test
    public void testInvokeMethod() {
        String substring = parser.parseExpression("'abc'.substring(1, 3)").getValue(String.class);
        Assertions.assertEquals("bc", substring);

        Inventor tesla = getTeslaInfo();
        String name = parser.parseExpression("getName()").getValue(tesla, String.class);
        Assertions.assertEquals(tesla.getName(), name);
    }

    @Test
    public void testRelationalOperators() {
        Boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        trueValue = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        Boolean falseValue = parser.parseExpression("2 < -0.5").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);

        Boolean value = parser.parseExpression("null < 0").getValue(Boolean.class);
        Assertions.assertNotNull(value);
        Assertions.assertTrue(value);
        value = parser.parseExpression("null < -1").getValue(Boolean.class);
        Assertions.assertNotNull(value);
        Assertions.assertTrue(value);

        // 除了标准的关系操作符，还支持基于实例和正则表达式的比较操作符
        falseValue = parser.parseExpression("'xyz' instanceof T(Integer)").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);

        trueValue = parser.parseExpression("'5.00' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        falseValue = parser.parseExpression("'5.0067' matches '^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);

        // 基本类型的装箱与拆箱
        falseValue = parser.parseExpression("12 instanceof T(int)").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);
        trueValue = parser.parseExpression("12 instanceof T(Integer)").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        /*
         * 每个操作符都对应着一些字母，这在某些情况下很有用（比如 XML 文档），这些字母不区分大小写
         * lt <
         * gt >
         * le <=
         * ge >=
         * eq ==
         * ne !=
         * div /
         * mod %
         * not !
         */
        trueValue = parser.parseExpression("3 gt 2").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);
        falseValue = parser.parseExpression("5 LE 4").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);
    }

    @Test
    public void testLogicalOperators() {
        Society society = new Society();
        society.getMembers().add(getTeslaInfo());

        // -- AND --
        Boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);
        String exp = "isMember('Nikola Tesla') and isMember('Albert Einstein')";
        falseValue = parser.parseExpression(exp).getValue(society, Boolean.class);
        Assertions.assertNotNull(falseValue);
        Assertions.assertFalse(falseValue);

        // -- OR --
        Boolean trueValue = parser.parseExpression("true or false").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);
        exp = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
        trueValue = parser.parseExpression(exp).getValue(society, Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        // -- NOT --
        trueValue = parser.parseExpression("!false").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);

        // -- AND and NOT --
        exp = "isMember('Nikola Tesla') and !isMember('Albert Einstein')";
        trueValue = parser.parseExpression(exp).getValue(society, Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);
    }

    @Test
    public void testMathematicalOperators() {
        // + 号可作用于数值与字符串
        Integer two = parser.parseExpression("1 + 1").getValue(Integer.class);
        Assertions.assertNotNull(two);
        Assertions.assertEquals(2, two);
        String testString = parser.parseExpression("'test' + ' ' + 'string'").getValue(String.class);
        Assertions.assertEquals("test string", testString);

        // 其余数字操作符只支持数值
        Integer four = parser.parseExpression("1 - -3").getValue(Integer.class);
        Assertions.assertNotNull(four);
        Assertions.assertEquals(4, four);
        Double d = parser.parseExpression("1000.00 - 1e4").getValue(Double.class);
        Assertions.assertNotNull(d);
        Assertions.assertEquals(-9000, d);

        Integer six = parser.parseExpression("-2 * -3").getValue(Integer.class);
        Assertions.assertNotNull(six);
        Assertions.assertEquals(6, six);
        Double twentyFour = parser.parseExpression("2.0 * 3e0 * 4").getValue(Double.class);
        Assertions.assertNotNull(twentyFour);
        Assertions.assertEquals(24, twentyFour);

        Integer minusTwo = parser.parseExpression("6 / -3").getValue(Integer.class);
        Assertions.assertNotNull(minusTwo);
        Assertions.assertEquals(-2, minusTwo);
        Double one = parser.parseExpression("8.0 / 4e0 / 2").getValue(Double.class);
        Assertions.assertNotNull(one);
        Assertions.assertEquals(1, one);

        Integer three = parser.parseExpression("7 % 4").getValue(Integer.class);
        Assertions.assertNotNull(three);
        Assertions.assertEquals(3, three);
        Integer intOne = parser.parseExpression("8 / 5 % 2").getValue(Integer.class);
        Assertions.assertNotNull(intOne);
        // 8 / 5 = 1, 1 % 2 = 1
        Assertions.assertEquals(1, intOne);

        Integer minusTwentyOne = parser.parseExpression("1 + 2 - 3 * 8").getValue(Integer.class);
        Assertions.assertNotNull(minusTwentyOne);
        Assertions.assertEquals(-21, minusTwentyOne);
    }

    @Test
    public void testAssignmentOperator() {
        EvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        Inventor tesla = getTeslaInfo();
        parser.parseExpression("name").setValue(context, tesla, "Aleksandar Seovic");
        Assertions.assertEquals("Aleksandar Seovic", tesla.getName());
        // 给 tesla 对象中的 name 字段赋值
        String value = parser.parseExpression("name = 'Nikola Tesla'").getValue(context, tesla, String.class);
        Assertions.assertEquals("Nikola Tesla", tesla.getName());
        Assertions.assertEquals("Nikola Tesla", value);
    }

    @Test
    public void testUseTypes() {
        // java.lang 包下的类无需使用类全限定名
        Class<?> strClass = parser.parseExpression("T(String)").getValue(Class.class);
        Assertions.assertEquals(String.class, strClass);
        // 非 java.lang 包下的类需要使用类全限定名
        Class<?> dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);
        Assertions.assertEquals(Date.class, dateClass);
        Boolean trueValue = parser.parseExpression("T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR").getValue(Boolean.class);
        Assertions.assertNotNull(trueValue);
        Assertions.assertTrue(trueValue);
    }

    @Test
    public void testUseConstructors() {
        Inventor inventor = parser.parseExpression("new indi.mofan.spel.pojo.Inventor('Albert Einstein', 'German')").getValue(Inventor.class);
        Assertions.assertNotNull(inventor);
        Assertions.assertEquals("Albert Einstein", inventor.getName());

        Society society = new Society();
        // 属性名不区分大小写，因此 Members.add() 或 members.add() 都可以
        parser.parseExpression("members.add(new indi.mofan.spel.pojo.Inventor('Albert Einstein', 'German'))").getValue(society);
        Assertions.assertTrue(society.getMembers().size() > 0);
        Inventor member = society.getMembers().get(0);
        Assertions.assertNotNull(member);
        Assertions.assertEquals("German", inventor.getNationality());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testVariables() {
        Inventor tesla = getTeslaInfo();

        SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding().build();
        context.setVariable("newName", "Mike Tesla");

        // 给 tesla 对象的 name 字段赋值为变量 newName 的值
        parser.parseExpression("name = #newName").getValue(context, tesla);
        Assertions.assertEquals("Mike Tesla", tesla.getName());

        /*
         * 有效的变量名必须是由以下一个或多个字符组成：
         * 1. 英文字符： 大写 A 到 Z，小写 a 到 z
         * 2. 数字：0 到 9
         * 3. 下划线：_
         * 4. 美元符号：$
         */

        List<Integer> primes = new ArrayList<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17));
        context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context.setVariable("primes", primes);

        // #this 引用当前对象，#root 引用根对象
        List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression(
                "#primes.?[#this>10]").getValue(context);
        MatcherAssert.assertThat(primesGreaterThanTen, CoreMatchers.is(Arrays.asList(11, 13, 17)));
    }

    @Test
    @SneakyThrows
    public void testFunctions() {
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Method method = StringUtils.class.getMethod("reverseString", String.class);
        context.setVariable("reverseString", method);
        String olleh = parser.parseExpression("#reverseString('hello')").getValue(context, String.class);
        Assertions.assertEquals("olleh", olleh);

        // 明确调用的方法
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.registerFunction("reverseString", method);
        olleh = parser.parseExpression("#reverseString('hello')").getValue(standardEvaluationContext, String.class);
        Assertions.assertEquals("olleh", olleh);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testBeanReferences() {
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        // 使用 @ 符号获取 Bean
        SimpleBean bean = parser.parseExpression("@simpleBean").getValue(context, SimpleBean.class);
        Assertions.assertNotNull(bean);

        // 使用 & 符号获取 FactoryBean
        SimpleBeanFactoryBean factoryBean = parser.parseExpression("&simpleBean").getValue(context, SimpleBeanFactoryBean.class);
        Assertions.assertNotNull(factoryBean);
    }

    @Test
    public void testTernaryOperator() {
        String falseExp = parser.parseExpression("false ? 'trueExp' : 'falseExp'").getValue(String.class);
        Assertions.assertEquals("falseExp", falseExp);

        Society society = new Society();
        society.getMembers().add(getTeslaInfo());
        // 设置根对象为 Society
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadWriteDataBinding()
                .withRootObject(society)
                // 实例方法也注册进去，否则执行表达式时将找不到 isMember
                .withInstanceMethods()
                .build();
        parser.parseExpression("name").setValue(context, "IEEE");
        context.setVariable("queryName", "Nikola Tesla");
        String exp = "isMember(#queryName) ? #queryName + ' is a member of the ' " +
                "+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";
        String queryResult = parser.parseExpression(exp).getValue(context, String.class);
        Assertions.assertEquals("Nikola Tesla is a member of the IEEE Society", queryResult);
    }

    @Test
    public void testElvisOperator() {
        // 不使用 Elvis 操作符
        String exp = "'Elvis Presley' != null ? 'Elvis Presley' : 'Unknown'";
        String value = parser.parseExpression(exp).getValue(String.class);
        Assertions.assertEquals("Elvis Presley", value);
        /*
         * 不使用 Elvis 操作符时，需要写两遍变量
         * 使用类似 Groovy 中的 Elvis 操作符，将简化书写。
         * PS: Elvis 名称的来源是这种符号很像 Elvis（猫王）的发型
         */
        String defaultName = parser.parseExpression("name?:'Unknown'").getValue(new Inventor(), String.class);
        Assertions.assertEquals("Unknown", defaultName);

        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        Inventor tesla = getTeslaInfo();
        String realName = parser.parseExpression("name?:'Elvis Presley'").getValue(context, tesla, String.class);
        Assertions.assertEquals("Nikola Tesla", realName);

        tesla.setName(null);
        defaultName = parser.parseExpression("name?:'Elvis Presley'").getValue(context, tesla, String.class);
        Assertions.assertEquals("Elvis Presley", defaultName);
    }

    @Test
    public void testSafeNavigationOperator() {
        Inventor tesla = getTeslaInfo();

        String city = parser.parseExpression("placeOfBirth?.city").getValue(tesla, String.class);
        Assertions.assertEquals("Lika", city);

        tesla.getPlaceOfBirth().setCity(null);
        city = parser.parseExpression("placeOfBirth?.city").getValue(tesla, String.class);
        Assertions.assertNull(city);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCollectionSelection() {
        Society society = getSociety();

        // 基本语法为：.?[selectionExpression]
        List<Inventor> list = (List<Inventor>) parser.parseExpression("members.?[placeOfBirth.country == 'Croatia']").getValue(society);
        Assertions.assertNotNull(list);
        Assertions.assertEquals(list.size(), 1);

        /*
         * 数组、实现了 java.lang.Iterable 或 java.util.Map 接口的数据都适用该语法
         * 对于数组和实现了 java.lang.Iterable 的数据，选择器作用于每一项
         * 对于实现了 java.util.Map 的数据，选择器作用于 Map.Entry
         */
        HasMapProperty obj = new HasMapProperty();
        Map<String, Integer> filteredMap = (Map<String, Integer>) parser.parseExpression("map.?[value >= 2]").getValue(obj);
        Assertions.assertNotNull(filteredMap);
        Assertions.assertEquals(3, filteredMap.size());

        // 使用 .^[selectionExpression] 获取符合条件的第一个
        filteredMap = (Map<String, Integer>) parser.parseExpression("map.^[value >= 2]").getValue(obj);
        Assertions.assertNotNull(filteredMap);
        Assertions.assertTrue(filteredMap.containsKey("2"));

        // 使用 .$[selectionExpression] 获取符合条件的最后一个
        filteredMap = (Map<String, Integer>) parser.parseExpression("map.$[value >= 2]").getValue(obj);
        Assertions.assertNotNull(filteredMap);
        Assertions.assertTrue(filteredMap.containsKey("4"));
    }

    private Society getSociety() {
        Society society = new Society();
        Inventor tesla = getTeslaInfo();
        tesla.getPlaceOfBirth().setCountry("Croatia");
        society.getMembers().add(tesla);
        return society;
    }

    @Getter
    private static class HasMapProperty {
        public Map<String, Integer> map = new LinkedHashMap<>();

        public HasMapProperty() {
            this.map.put("1", 1);
            this.map.put("2", 2);
            this.map.put("3", 3);
            this.map.put("4", 4);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCollectionProjection() {
        Society society = getSociety();
        // 基本语法为：.![projectionExpression]，转换得到的结果是 List
        List<String> list = (List<String>) parser.parseExpression("members.![placeOfBirth.country]").getValue(society);
        MatcherAssert.assertThat(list, CoreMatchers.is(Collections.singletonList("Croatia")));

        HasMapProperty obj = new HasMapProperty();
        List<Integer> intList = (List<Integer>) parser.parseExpression("map.![value + 1]").getValue(obj);
        MatcherAssert.assertThat(intList, CoreMatchers.is(Arrays.asList(2, 3, 4, 5)));

        List<List<String>> nestedList = (List<List<String>>) parser.parseExpression("map.![{key + '1', value + 2}]").getValue(obj);
        Assertions.assertNotNull(nestedList);

        List<Map<String, Integer>> resultMap = (List<Map<String, Integer>>) parser.parseExpression("map.![{key + '1' : value + 2}]").getValue(obj);
        Assertions.assertNotNull(resultMap);
    }

    @Test
    public void testExpressionTemplating() {
        // 使用 Spring 提供的模板解析器
        String str = parser.parseExpression("random number is #{T(java.lang.Math).random()}",
                new org.springframework.expression.common.TemplateParserContext()).getValue(String.class);
        System.out.println(str);

        str = parser.parseExpression("random number is ${T(Math).random()}",
                new indi.mofan.spel.config.TemplateParserContext()).getValue(String.class);
        System.out.println(str);
    }
}

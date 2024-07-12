package indi.mofan;

import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

import java.io.Serial;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author mofan
 * @date 2023/8/10 13:46
 */
public class ResolvableTypeTest implements WithAssertions {

    static class MyMap extends HashMap<String, List<Integer>> {
        @Serial
        private static final long serialVersionUID = -9026643848761552207L;
    }

    @Test
    public void testResolvableType() {
        ResolvableType myMapType = ResolvableType.forClass(MyMap.class);
        // 泛型参数在父类的 HashMap 中，因此先获取到父类的 ResolvableType
        ResolvableType hashMapType = myMapType.getSuperType();
        // 根据索引获取泛型信息 String
        ResolvableType firstGenericType = hashMapType.getGeneric(0);
        Class<?> firstGenericClazz = firstGenericType.resolve();
        assertThat(firstGenericClazz).isEqualTo(String.class);

        // 再获取第二个泛型信息 List<Integer>
        ResolvableType secondGenericType = hashMapType.getGeneric(1);
        ResolvableType secondFirstGenericType = secondGenericType.getGeneric(0);
        Class<?> secondFirstGenericClazz = secondFirstGenericType.resolve();
        assertThat(secondFirstGenericClazz).isEqualTo(Integer.class);

        /*
         * 除此 forClass() 之外，创建 ResolvableType 的方式有很多，比如：
         * - forField(): 通过字段获取
         * - forMethodReturnType(): 通过方法的返回值获取
         * - forMethodParameter(): 通过方法参数获取
         * - forConstructorParameter(): 通过构造方法参数获取
         */
    }

    @Test
    public void testForRawClass() {
        ResolvableType type = ResolvableType.forClass(List.class);
        // 包含泛型信息
        assertThat(type.toString()).isEqualTo("java.util.List<?>");

        type = ResolvableType.forRawClass(List.class);
        // 不包含泛型信息
        assertThat(type.toString()).isEqualTo("java.util.List");
    }

    static class Foo<T> {
    }

    static class Bar extends Foo<String> {
    }

    @Test
    public void testForClass() {
        ResolvableType type = ResolvableType.forClass(Bar.class);
        /*
         * 把当前 ResolvableType 转换成给定的 ResolvableType
         * Bar 是 Foo 的子类，将 Bar 转成 Foo
         */
        ResolvableType fooType = type.as(Foo.class);
        assertThat(fooType.toString()).contains("Foo<java.lang.String>");


        // 类似于 Foo<String> foo = new Bar();
        fooType = ResolvableType.forClass(Foo.class, Bar.class);
        assertThat(fooType.toString()).contains("Foo<java.lang.String>");
    }

    static class GenericClass<T> {
        public GenericClass(T value) {
        }
    }

    static class IntegerGenericClass extends GenericClass<Integer> {
        public IntegerGenericClass(Integer value) {
            super(value);
        }
    }

    @Test
    public void testForConstructorParameter() {
        ResolvableType type = ResolvableType.forConstructorParameter(GenericClass.class.getConstructors()[0], 0);
        assertThat(type.toString()).isEqualTo("?");
        type = ResolvableType.forConstructorParameter(GenericClass.class.getConstructors()[0], 0, IntegerGenericClass.class);
        assertThat(type.toString()).isEqualTo("java.lang.Integer");
    }

    static class BaseClass<T> {
        protected T value;
    }

    static class StringClass extends BaseClass<String> {
    }

    @Test
    @SneakyThrows
    public void testForField_1() {
        Field field = BaseClass.class.getDeclaredField("value");
        ResolvableType type = ResolvableType.forField(field);
        assertThat(type.toString()).isEqualTo("?");

        type = ResolvableType.forField(field, StringClass.class);
        assertThat(type.toString()).isEqualTo("java.lang.String");
    }

    static class NestedFieldClass {
        private List<List<Integer>> list;
    }

    @Test
    @SneakyThrows
    public void testForField_2() {
        Field field = NestedFieldClass.class.getDeclaredField("list");
        ResolvableType type = ResolvableType.forField(field);
        assertThat(type.toString()).isEqualTo("java.util.List<java.util.List<java.lang.Integer>>");

        // 1 表示外层，2 及其以后才是嵌套泛型
        type = ResolvableType.forField(field, 1);
        assertThat(type.toString()).isEqualTo("java.util.List<java.util.List<java.lang.Integer>>");

        type = ResolvableType.forField(field, 2);
        assertThat(type.toString()).isEqualTo("java.util.List<java.lang.Integer>");

        type = ResolvableType.forField(field, 3);
        assertThat(type.toString()).isEqualTo("java.lang.Integer");
    }

    static class Sample<T> {
        List<String> list;
        T[] array;
        List<? extends Number> wildcardList;
    }

    @Test
    @SneakyThrows
    public void testTypeSystem() {
        // 获取类的类型
        Class<?> clazz = Class.forName("java.util.List");
        assertThat(clazz.toString()).isEqualTo("interface java.util.List");

        // 获取参数化类型
        Field field = Sample.class.getDeclaredField("list");
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType parameterizedType) {
            // 原始类型
            assertThat(parameterizedType.getRawType().toString())
                    .isEqualTo("interface java.util.List");
            // 实际类型参数
            assertThat(parameterizedType.getActualTypeArguments())
                    .extracting(Object::toString)
                    .containsOnly("class java.lang.String");
        }

        // 获取泛型数组类型
        Field arrayField = Sample.class.getDeclaredField("array");
        Type arrayType = arrayField.getGenericType();
        if (arrayType instanceof GenericArrayType genericArrayType) {
            assertThat(genericArrayType.getGenericComponentType().toString())
                    .isEqualTo("T");
        }

        // 获取类型变量
        TypeVariable<Class<Sample>>[] typeParameters = Sample.class.getTypeParameters();
        assertThat(typeParameters.length).isEqualTo(1);
        TypeVariable<Class<Sample>> typeParam = typeParameters[0];
        assertThat(typeParam.getName()).isEqualTo("T");
        // 获取类型变量的上界
        assertThat(typeParam.getBounds())
                .singleElement()
                .extracting(Objects::toString)
                .isEqualTo("class java.lang.Object");

        // 获取通配符类型
        Field wildcardField = Sample.class.getDeclaredField("wildcardList");
        Type wildcardType = wildcardField.getGenericType();
        if (wildcardType instanceof ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            assertThat(actualTypeArguments.length).isEqualTo(1);
            Type typeArg = actualTypeArguments[0];
            if (typeArg instanceof WildcardType wildcard) {
                assertThat(wildcard.getUpperBounds())
                        .singleElement()
                        .extracting(Objects::toString)
                        .isEqualTo("class java.lang.Number");

                assertThat(wildcard.getLowerBounds()).isEmpty();
            }
        }
    }

    static class Outer<T> {
        public T genericMethod() {
            return null;
        }
    }

    @Test
    @SneakyThrows
    public void testForType() {
        ResolvableType type = ResolvableType.forType(Bar.class);
        assertThat(type.toString())
                .contains("Bar")
                .doesNotContain("Foo");

        ParameterizedTypeReference<List<String>> typeReference = new ParameterizedTypeReference<>() {
        };
        type = ResolvableType.forType(typeReference);
        assertThat(type.toString()).isEqualTo("java.util.List<java.lang.String>");

        Method method = Outer.class.getMethod("genericMethod");
        type = ResolvableType.forMethodReturnType(method);
        assertThat(type.toString()).isEqualTo("?");
        ResolvableType genericClassType = ResolvableType.forClassWithGenerics(Outer.class, String.class);
        type = ResolvableType.forType(method.getGenericReturnType(), genericClassType);
        assertThat(type.toString()).isEqualTo("java.lang.String");
    }

    @Test
    public void testGetNested() {
        ParameterizedTypeReference<List<Map<String, List<Integer>>>> typeReference = new ParameterizedTypeReference<>() {
        };
        ResolvableType type = ResolvableType.forType(typeReference);
        ResolvableType nested = type.getNested(1);
        // 第一层就是最外面的
        assertThat(nested.toString()).isEqualTo("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>>");

        nested = type.getNested(2);
        assertThat(nested.toString()).isEqualTo("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");

        /*
         * nestingLevel: 第三层嵌套
         * 2 -> 0: 第 2 层嵌套取 index = 0 的泛型，即: Map<String, List<Integer>>>
         * 3 -> 1: 第 3 层嵌套取 index = 1 的泛型，即: List<Integer>
         */
        nested = type.getNested(3, Map.of(2, 0, 3, 1));
        assertThat(nested.toString()).isEqualTo("java.util.List<java.lang.Integer>");
    }
}

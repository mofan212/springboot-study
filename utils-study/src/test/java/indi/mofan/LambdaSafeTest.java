package indi.mofan;

import org.junit.jupiter.api.Test;
import org.springframework.boot.util.LambdaSafe;
import org.springframework.core.ResolvableType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author mofan
 * @date 2023/3/11 15:31
 */
public class LambdaSafeTest {
    private int sum(int a, int b) {
        return a + b;
    }

    private int subtract(int a, int b) {
        return a - b;
    }

    private void print(int result) {
        System.out.println("计算结果是: " + result);
    }

    interface Calculator {
        double calculator(int a, int b);
    }

    static class Multiply implements Calculator {
        @Override
        public double calculator(int a, int b) {
            return a * b;
        }
    }

    private void print(Calculator calculator, int a, int b) {
        System.out.println("计算结果是: " + calculator.calculator(a, b));
    }

    @Test
    public void testCallbackFunction() {
        print(sum(1, 2));
        print(subtract(2, 1));

        // 如果要乘法运算呢？再提供一个乘法运算方法？
        print(new Multiply(), 2, 3);

        // 这样还不够，每多一种运算就多一个类，这不太行！
        print(new Calculator() {
            @Override
            public double calculator(int a, int b) {
                return (a + b) / 2.0;
            }
        }, 2, 2);

        // 匿名内部类太丑了！还要 new 一下，我其实只想要函数的实现，Java 中函数是一等公民吗？
        Calculator calculator = (a, b) -> (double) a / b;
        print(calculator, 6, 2);

        // 将 A 函数作为 B 函数的参数，并在 B 函数内部调用 A 函数，此时的 A 函数称为回调函数
    }

    private void printLog(String log) {
        int i = new Random().nextInt(10);
        // 随机数大于 5 才打印
        if (i > 5) {
            System.out.println(log);
        }
    }

    private String aInfo() {
        return "A";
    }

    private String bInfo() {
        return "B";
    }

    private void printLog(Supplier<String> supplier) {
        int i = new Random().nextInt(10);
        if (i > 5) {
            System.out.println(supplier.get());
        }
    }

    private int printLog(Supplier<String> supplier, Set<String> pool) {
        int i = new Random().nextInt(10);
        if (i > 5) {
            String log = supplier.get();
            pool.add(log);
            System.out.println(log);
        }
        return i;
    }

    @Test
    public void testDelayExecute() {
        // 对信息进行拼接作为日志信息
        printLog(aInfo() + "-" + bInfo());

        // 无论是否需要打印，都会进行字符串的拼接，这又何尝不是一种性能损耗？
        printLog(() -> aInfo() + "<->" + bInfo());

        Set<String> pool = new HashSet<>();
        int random = printLog(() -> bInfo() + "<==>" + aInfo(), pool);
        if (random > 5) {
            assertThat(pool).isNotEmpty();
        } else {
            assertThat(pool).isEmpty();
        }
    }

    @Test
    public void testSimplyUseLambdaSafe() {
        BiFunction<Integer, Integer, Integer> sum = Integer::sum;
        @SuppressWarnings("unchecked")
        Integer result = (Integer) LambdaSafe.callback(BiFunction.class, sum, 1, 2)
                .invokeAnd(biFunction -> biFunction.apply(1, 2))
                .get(0);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void testNestedConsumer() {
        Consumer<String> consumer = System.out::println;
        Consumer<Consumer<String>> nestedConsumer = c -> c.accept("hello lambda safe");

        nestedConsumer.accept(consumer);
    }

    abstract static class MyList implements List<String> {

    }

    @Test
    public void testSimplyUseResolvableType() {
        ResolvableType type = ResolvableType.forClass(List.class, ArrayList.class);
        assertThat(type.getGenerics().length).isEqualTo(1);
        assertThat(type.resolveGeneric()).isNull();

        type = ResolvableType.forClass(List.class, MyList.class);
        assertThat(type.getGenerics().length).isEqualTo(1);
        Class<?> clazz = type.resolveGeneric();
        assertThat(clazz).isNotNull().isAssignableFrom(String.class);
    }

    interface FirstConsumer extends Consumer<String> {

    }

    interface SecondConsumer extends Consumer<Integer> {

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMatch() {
        FirstConsumer consumer = System.out::println;
        LambdaSafe.callback(
                Consumer.class,
                consumer,
                2
        ).invoke(i -> i.accept("hello lambda safe"));

        LambdaSafe.callback(
                Consumer.class,
                consumer,
                2
        ).invoke(i -> i.accept(2));

        SecondConsumer consumer2nd = integer -> System.out.println(1 + integer);
        LambdaSafe.callback(
                Consumer.class,
                consumer2nd,
                2
        ).invoke(i -> i.accept(2));

        // argument 与回调函数真正执行时使用的参数无关，仅用于过滤和是否抛出 ClassCastException 的判断
        LambdaSafe.callback(
                Consumer.class,
                consumer2nd,
                10000000
        ).invoke(i -> i.accept(2));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testClassCastException() {
        // 需要使用的回调函数实例
        FirstConsumer consumer = System.out::println;
        // 回调函数需要的参数
        String str = "hello lambda safe";
        assertThatThrownBy(() -> {
            LambdaSafe.callback(
                    Consumer.class,
                    consumer,
                    str
            ).invoke(i -> i.accept(2));
        }).isInstanceOf(ClassCastException.class);
    }

    interface FirstFunction extends Function<Integer, Integer> {
    }

    interface SecondFunction extends Function<String, String> {
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNoException() {
        // 回调函数有多个泛型
        FirstFunction function = i -> i + 1;
        String strParam = "lambda";
        assertThatNoException().isThrownBy(() -> {
            LambdaSafe.callback(
                    Function.class,
                    function,
                    strParam
            ).invokeAnd(i -> i.apply(strParam));
        });

        // 回调函数是单个泛型，但是是泛型参数
        Consumer<String> consumer = System.out::println;
        int intParam = 1;
        assertThatNoException().isThrownBy(() -> {
            LambdaSafe.callback(
                    Consumer.class,
                    consumer,
                    intParam
            ).invoke(i -> i.accept(intParam));
        });
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testHasTwoGenericParam() {
        // 1 -> 2
        FirstFunction fun1st = i -> i + 1;
        // 1 -> 12
        SecondFunction fun2nd = i -> i + 2;
        List<Function<?, ?>> functions = Arrays.asList(fun1st, fun2nd);

        int param = 1;
        // 不抛异常，但是有日志输出，能体现 safe
        assertThatNoException().isThrownBy(() -> {
            LambdaSafe.callbacks(
                    Function.class,
                    functions,
                    param
            ).invoke(i -> System.out.println(i.apply(param)));
        });

        assertThatThrownBy(() -> {
            LambdaSafe.callbacks(
                    Function.class,
                    functions,
                    param
            ).invoke(i -> System.out.println(i.apply(String.valueOf(param))));
        }).isInstanceOf(ClassCastException.class);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCallBacks() {
        FirstConsumer consumer1st = System.out::println;
        SecondConsumer consumer2nd = (i) -> System.out.println(i + 1);
        List<Consumer<?>> consumers = Arrays.asList(consumer1st, consumer2nd);

        int hundred = 100;
        assertThatCode(() -> {
            LambdaSafe.callbacks(
                    Consumer.class,
                    consumers,
                    hundred
            ).invoke(i -> i.accept(hundred));
        }).doesNotThrowAnyException();
    }

    // ------------------------------------------------------------------------

    enum Type {
        ONE, TWO
    }

    interface MyConsumer<T> {
        Type getType();

        void accept(T t);
    }

    static class FirstMyConsumer implements MyConsumer<String> {

        @Override
        public Type getType() {
            return Type.ONE;
        }

        @Override
        public void accept(String s) {
            System.out.println("->" + s + "<-");
        }
    }

    static class SecondMyConsumer implements MyConsumer<Integer> {

        @Override
        public Type getType() {
            return Type.TWO;
        }

        @Override
        public void accept(Integer integer) {
            System.out.println(integer);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testNotOnlyLambda() {
        List<MyConsumer<?>> consumers = Arrays.asList(new FirstMyConsumer(), new SecondMyConsumer());
        String param = "callback safe";
        assertThatNoException().isThrownBy(() -> {
            LambdaSafe.callbacks(
                    MyConsumer.class,
                    consumers,
                    param
            ).invoke(i -> i.accept(param));
        });
        // lambda safe? callback safe!
    }
}

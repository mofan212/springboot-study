package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;

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
}

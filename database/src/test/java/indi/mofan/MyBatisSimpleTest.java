package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static indi.mofan.parse.MapVariablesParser.parse;

/**
 * @author mofan
 * @date 2023/6/8 14:23
 */
public class MyBatisSimpleTest implements WithAssertions {
    @Test
    public void testMapVariablesParser() {
        String stringTemplate = "name:${name}, age:${age}, nickname:${name}";
        Map<String, Object> paramMap = Map.of("name", "mofan", "age", 21);
        assertThat(parse(stringTemplate, paramMap))
                .isNotBlank()
                .isEqualTo("name:mofan, age:21, nickname:mofan");
    }
}

package indi.mofan.parse;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;

import java.util.Map;

/**
 * @author mofan
 * @date 2023/6/8 14:21
 */
public class MapVariablesParser {

    public static String parse(String string, Map<String, Object> variables) {
        MapVariableTokenHandler handler = new MapVariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    private record MapVariableTokenHandler(Map<String, Object> variables) implements TokenHandler {
        @Override
        public String handleToken(String content) {
            if (variables != null && variables.containsKey(content)) {
                return variables.get(content).toString();
            }
            return StringUtils.EMPTY;
        }
    }
}

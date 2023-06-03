package indi.mofan.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mofan
 * @date 2022/10/12 20:39
 */
public class JsonPropertySourceLoader implements PropertySourceLoader {

    @Override
    public String[] getFileExtensions() {
        // 支持解析以 json 结尾的配置文件
        return new String[]{"json"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        byte[] bytes = IOUtils.toByteArray(resource.getInputStream());
        // 将读出来的字节转换成字符串
        String content = new String(bytes);
        // 将字符串转换成 JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(content);

        Map<String, Object> map = new HashMap<>(16);
        Iterator<String> iterator = jsonNode.fieldNames();
        while (iterator.hasNext()) {
            String next = iterator.next();
            //将 JSON 的键值对读出来，放入到 map 中
            map.put(next, jsonNode.get(next).asText());
        }
        return Collections.singletonList(new MapPropertySource("jsonPropertySource", map));
    }
}

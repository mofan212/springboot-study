package indi.mofan;


import indi.mofan.controller.SseController;
import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

/**
 * @author mofan
 * @date 2025/8/9 21:49
 */
@WebMvcTest(SseController.class)
public class SseControllerTest implements WithAssertions {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testSse() {
        MvcResult result = mockMvc.perform(get("/sse"))
                // 验证异步启动
                .andExpect(request().asyncStarted())
                .andReturn();
        // 等待异步处理完成
        result.getAsyncResult();

        StringBuilder builder = new StringBuilder();
        // 每次发送的 data 会以 `data:` 开头
        Pattern pattern = Pattern.compile("data:(.+)");
        mockMvc.perform(asyncDispatch(result))
                .andDo(i -> {
                    String str = i.getResponse().getContentAsString(StandardCharsets.UTF_8);
                    Matcher matcher = pattern.matcher(str);
                    while (matcher.find()) {
                        builder.append(matcher.group(1));
                    }
                });

        String content = builder.toString();
        assertThat(content).contains("床前明月光")
                .contains("疑是地上霜")
                .contains("举头望明月")
                .contains("低头思故乡");
    }
}

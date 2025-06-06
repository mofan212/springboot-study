package indi.mofan.controller;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mofan
 * @date 2025/6/6 23:13
 */
@WebMvcTest(value = {FuzzyMatchingController.class, AccurateMatchingController.class})
public class FuzzyMatchingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testMatching() {
        // 模糊匹配
        mockMvc.perform(get("/fuzzy/get"))
                .andExpect(status().isOk())
                .andExpect(content().string("fuzzy matching"));

        // 精准匹配
        mockMvc.perform(get("/accurate/get"))
                .andExpect(status().isOk())
                .andExpect(content().string("accurate matching"));
    }
}

package indi.mofan.controller;


import lombok.SneakyThrows;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author mofan
 * @date 2025/8/12 17:29
 */
@WebMvcTest(controllers = ResponseStatusController.class)
public class ResponseStatusControllerTest implements WithAssertions {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    public void testGet404() {
        mockMvc.perform(get("/code/{code}", "404"))
                .andExpect(status().isNotFound())
                .andExpect(i -> assertThat(i.getResolvedException()).isInstanceOf(ResponseStatusException.class))
                .andExpect(i -> {
                    ResponseStatusException ex = (ResponseStatusException) i.getResolvedException();
                    assertThat(ex).isNotNull();
                    assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(ex.getReason()).isEqualTo("NOT FOUND");
                });

        mockMvc.perform(get("/code/{code}", "200"))
                .andExpect(status().isOk())
                .andExpect(content().string("200"));
    }
}

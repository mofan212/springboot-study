package indi.mofan;


import indi.mofan.component.RefDubboService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author mofan
 * @date 2025/4/11 17:54
 */
@SpringBootTest
public class RpcTest implements WithAssertions {

    @Autowired
    private RefDubboService service;

    @Test
    public void testRpc() {
        assertThat(service.getStr()).isEqualTo("custom Inject");
    }
}

package indi.mofan;

import feign.Feign;
import feign.Request;
import feign.RetryableException;
import indi.mofan.api.UserApi;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author mofan
 * @date 2023/8/25 10:39
 */
public class OpenFeignTest implements WithAssertions {
    @Test
    public void testSimplyUse() {
        UserApi client = Feign.builder()
                // 设置连接和读超时间都是 5s
                .options(new Request.Options(5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true))
                // http://localhost:8088/user/123 是不存在的
                .target(UserApi.class, "http://localhost:8088");

        // 在 feign.Client.Default.execute() 方法断点时，可以观察到超时时间与前面设置的一致
        assertThatExceptionOfType(RetryableException.class).isThrownBy(() -> client.queryUser(123));
    }

    @Test
    public void testUseOptions() {
        UserApi client = Feign.builder()
                // 在 Builder 中设置超时时间为 5s
                .options(new Request.Options(5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true))
                .target(UserApi.class, "http://localhost:8088");
        // 方法参数中也设置了超时时间为 3s
        Request.Options options = new Request.Options(3, TimeUnit.SECONDS, 3, TimeUnit.SECONDS, true);
        // 断点时，方法参数中设置的超时时间优先级更高
        assertThatExceptionOfType(RetryableException.class)
                .isThrownBy(() -> client.queryUserByName("mofan", options));
    }
}

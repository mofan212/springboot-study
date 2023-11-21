package indi.mofan;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author mofan
 * @date 2023/8/8 10:29
 */
public class ResourceManagementTest implements WithAssertions {

    private static final String BAIDU_WEB_SITE = "https://www.baidu.com";

    @Test
    @SneakyThrows
    public void testGetBaiduHomePageInfo() {
        // 构建 URL
        URL url = new URI(BAIDU_WEB_SITE).toURL();
        // 打开资源连接
        URLConnection urlConnection = url.openConnection();
        // 获取资源输入流
        InputStream inputStream = urlConnection.getInputStream();
        // 读取输入流信息
        String content = IOUtils.toString(new InputStreamReader(inputStream));
        assertThat(content).isNotBlank().contains("百度一下，你就知道");
    }

    @Test
    @SneakyThrows
    public void testGetBaiduHomePageInfoWithSpring() {
        // 直接使用 Resource 获取
        Resource resource = new UrlResource(BAIDU_WEB_SITE);
        InputStream inputStream = resource.getInputStream();
        String content = IOUtils.toString(new InputStreamReader(inputStream));
        assertThat(content).isNotBlank().contains("百度一下，你就知道");

        // 使用 ResourceLoader 获取
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        resource = resourceLoader.getResource(BAIDU_WEB_SITE);
        content = IOUtils.toString(new InputStreamReader(resource.getInputStream()));
        assertThat(content).isNotBlank().contains("百度一下，你就知道");
    }
}

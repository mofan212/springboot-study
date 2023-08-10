package indi.mofan;

import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author mofan
 * @date 2023/8/10 14:22
 */
public class I18nTest implements WithAssertions {

    @Test
    public void testResourceBundle() {
        // 国际化文件要按照 basename_lang_country.properties 的格式命名
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n.message", Locale.CHINA);
        String name = resourceBundle.getString("name");
        assertThat(name).isEqualTo("默烦");

        resourceBundle = ResourceBundle.getBundle("i18n/message", Locale.ENGLISH);
        name = resourceBundle.getString("name");
        assertThat(name).isEqualTo("mofan");
    }

    @Test
    public void testMessageFormat() {
        String string = MessageFormat.format("Hello, {0}", "World");
        assertThat(string).isEqualTo("Hello, World");
    }

    @Test
    public void testMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // Spring 扩展了 ResourceBundle 的 Control，支持资源文件的不同编码方式
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        // 设置 basename，支持设置多个 basename
        messageSource.setBasename("i18n.message");

        // 英文国际化信息
        String englishMessage = messageSource.getMessage("welcome", new Object[]{"mofan"}, Locale.ENGLISH);
        assertThat(englishMessage).isEqualTo("Hello: mofan");

        // 中文国际化信息
        String chineseMessage = messageSource.getMessage("welcome", new Object[]{"默烦"}, Locale.CHINA);
        assertThat(chineseMessage).isEqualTo("你好: 默烦");
    }
}

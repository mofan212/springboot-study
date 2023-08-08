package indi.mofan;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mofan
 * @date 2023/8/8 11:31
 */
@SpringBootTest(classes = CoreFunctionsTestApplication.class)
public class TypeConvertTest implements WithAssertions {
    @Value("https://www.baidu.com")
    private Resource resource;

    @Test
    @SneakyThrows
    public void testAtValue() {
        assertThat(resource).isNotNull();
        InputStream inputStream = resource.getInputStream();
        String content = IOUtils.toString(new InputStreamReader(inputStream));
        assertThat(content).contains("百度一下，你就知道");
    }

    static class StringToIntConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String source) {
            try {
                return Integer.valueOf(source);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }

    @Test
    public void testListConverter() {
        StringToIntConverter converter = new StringToIntConverter();
        assertThat(converter.convert("123")).isEqualTo(123);
        assertThat(converter.convert("mofan")).isEqualTo(0);
    }

    // 除此之外，还有 GenericConverter，用于处理带泛型的转换

    @Test
    public void testGenericConversionService() {
        // 为了便于使用，Spring 提供了 GenericConversionService，默认添加了很多转换器
        DefaultConversionService service = new DefaultConversionService();
        assertThat(service.convert(123, String.class)).isEqualTo("123");
        // 也可以直接使用静态方法获取
        assertThat(DefaultConversionService.getSharedInstance().convert(123, String.class))
                .isEqualTo("123");

        // SpringBoot 环境下，还可以使用 ApplicationConversionService
        ApplicationConversionService conversionService = new ApplicationConversionService();
        assertThat(conversionService.convert("123", Integer.class)).isEqualTo(123);
    }

    static class StringToDateConverter implements Converter<String, Date> {
        @Override
        public Date convert(String source) {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(source);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void testTypeConverter() {
        // Spring 又将 PropertyEditor 和 ConversionService 进行整合，形成 TypeConverter
        SimpleTypeConverter converter = new SimpleTypeConverter();
        assertThat(converter.convertIfNecessary("123", Integer.class)).isEqualTo(123);

        String dateStr = "2023-01-01";
        assertThatExceptionOfType(ConversionNotSupportedException.class)
                .isThrownBy(() -> converter.convertIfNecessary(dateStr, Date.class));
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new StringToDateConverter());
        // 自行添加 ConversionService
        converter.setConversionService(service);
        Date date = converter.convertIfNecessary(dateStr, Date.class);
        assertThat(date).isNotNull()
                .hasYear(2023)
                .hasMonth(1)
                .hasDayOfMonth(1);
    }
}

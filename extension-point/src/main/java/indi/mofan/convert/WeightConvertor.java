package indi.mofan.convert;

import indi.mofan.pojo.Weight;
import org.springframework.core.convert.converter.Converter;

import java.math.BigDecimal;

/**
 * @author mofan
 * @date 2023/6/26 10:49
 */
public class WeightConvertor implements Converter<String, Weight> {
    private static final BigDecimal MAX_WEIGHT = new BigDecimal(1000);

    @Override
    public Weight convert(String source) {
        BigDecimal value = new BigDecimal(source);
        return new Weight(value, MAX_WEIGHT.compareTo(value) < 0);
    }
}

package indi.mofan;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author mofan
 * @date 2023/2/20 22:36
 */
@Getter
@Setter
public class Car {
    private String name;
    private BigDecimal price;

    @Override
    public String toString() {
        return "Car{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

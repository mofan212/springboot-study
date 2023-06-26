package indi.mofan.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author mofan
 * @date 2023/6/26 10:46
 */
@Getter
@AllArgsConstructor
public class Weight {
    private BigDecimal value;
    private boolean overWight;
}

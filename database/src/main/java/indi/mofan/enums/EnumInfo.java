package indi.mofan.enums;

import java.io.Serializable;

/**
 * @author mofan
 * @date 2022/11/9 22:16
 */
public interface EnumInfo extends Serializable {
    /**
     * 枚举 code
     *
     * @return code
     */
    int code();

    /**
     * 枚举信息
     *
     * @return 枚举信息
     */
    String message();
}

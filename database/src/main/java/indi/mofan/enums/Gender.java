package indi.mofan.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author mofan
 * @date 2022/11/9 22:16
 */
public enum Gender implements EnumInfo {
    /**
     * 男
     */
    MALE(1, "男"),

    /**
     * 女
     */
    FEMALE(2, "女"),
    ;

    Gender(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @EnumValue
    private final int code;
    @JsonValue
    private final String message;

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }
}

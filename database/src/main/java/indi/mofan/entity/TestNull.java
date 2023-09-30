package indi.mofan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author mofan
 * @date 2023/9/30 11:03
 */
@Getter
@Setter
@TableName("tb_test_null")
public class TestNull extends BaseEntity {
    @Serial
    private static final long serialVersionUID = -1332169884425770306L;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("empty_field")
    private String emptyField;
}

package indi.mofan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import indi.mofan.enums.Gender;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mofan
 * @date 2022/11/9 22:09
 */
@Setter
@Getter
@TableName("tb_student")
public class Student extends BaseEntity {
    private static final long serialVersionUID = 4072478525857967932L;

    @TableField("name")
    private String name;

    @TableField("age")
    private Integer age;

    @TableField("gender")
    private Gender gender;
}

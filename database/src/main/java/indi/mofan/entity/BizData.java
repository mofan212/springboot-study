package indi.mofan.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author mofan
 * @date 2026/4/13 11:14
 */
@Getter
@Setter
@TableName("biz_data")
public class BizData extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 6007104005643198292L;

    @TableField("business_code")
    private String businessCode;
}

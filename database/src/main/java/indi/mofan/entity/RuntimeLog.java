package indi.mofan.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author mofan
 * @date 2026/4/13 11:15
 */
@Getter
@Setter
@TableName("rt_log")
public class RuntimeLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 9033372340379304255L;

    @TableField("log_content")
    private String logContent;
}

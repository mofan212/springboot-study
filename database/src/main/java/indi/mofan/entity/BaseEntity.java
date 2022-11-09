package indi.mofan.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author mofan
 * @date 2022/11/9 22:12
 */
@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 8219268788524935950L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}

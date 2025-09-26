package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 关注关系实体类
 * </p>
 */

@Data
@TableName(value = "follow")
public class Follow {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer itemId;

    // 关联用户信息字段（非数据库字段）
    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String avatarUrl;

    @TableField(exist = false)
    private String info;
}

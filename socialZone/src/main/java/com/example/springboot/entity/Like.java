package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 点赞实体类
 * </p>
 */

@Data
@TableName(value = "`like`")
public class Like {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer itemId;

    private String createTime;

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String avatarUrl;

}
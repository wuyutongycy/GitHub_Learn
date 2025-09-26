package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * 实体类
 * </p>
 */

@Data
@TableName(value = "comment")
public class Comment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer userId;
    @TableField(exist = false)
    private String nickname;
    @TableField(exist = false)
    private String avatarUrl;

    private String time;

    private Integer pid;
    @TableField(exist = false)
    private String pNickname;
    @TableField(exist = false)
    private Integer pUserId;

    private Integer originId;

    private Integer itemId;

    @TableField(exist = false)
    private List<Comment> children;


}

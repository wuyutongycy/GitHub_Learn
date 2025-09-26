package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 私信消息实体类
 * </p>
 */

@Data
@TableName(value = "private_message")
public class PrivateMessage {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private String content;

    private Boolean isRead;

    private String sendTime;

    @TableField(exist = false)
    private String fromNickname;

    @TableField(exist = false)
    private String fromAvatarUrl;

    @TableField(exist = false)
    private String toNickname;

    @TableField(exist = false)
    private String toAvatarUrl;

    // 用于会话列表的虚拟字段
    @TableField(exist = false)
    private Integer userId;

    @TableField(exist = false)
    private String nickname;

    @TableField(exist = false)
    private String avatarUrl;

    @TableField(exist = false)
    private String lastMessage;

    @TableField(exist = false)
    private String lastTime;

    @TableField(exist = false)
    private Integer unreadCount;

}
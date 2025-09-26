package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 好友申请实体类
 * </p>
 */

@Data
@TableName(value = "friend_request")
public class FriendRequest {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fromUserId;

    private Integer toUserId;

    private Integer status; // 0:待处理, 1:已同意, 2:已拒绝

    private String message;

    private String createTime;

    private String handleTime;

    @TableField(exist = false)
    private String fromNickname;

    @TableField(exist = false)
    private String fromAvatarUrl;

    @TableField(exist = false)
    private String toNickname;

    @TableField(exist = false)
    private String toAvatarUrl;

}
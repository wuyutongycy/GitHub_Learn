package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * <p>
 * 消息通知实体类
 * </p>
 */

@Data
@TableName(value = "message")
public class Message {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String text;

    private String type;

    private String time;

    private Integer fromUserId;

    private Integer toUserId;

    private Integer itemId;

    private Boolean isRead;

    private String readTime;

    private String category;

    @TableField(exist = false)
    private String fromNickname;

    @TableField(exist = false)
    private String fromAvatarUrl;

}

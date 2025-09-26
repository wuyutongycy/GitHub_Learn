package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 通知设置实体类
 * </p>
 */

@Data
@TableName(value = "notification_setting")
public class NotificationSetting {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Boolean likeNotify;

    private Boolean followNotify;

    private Boolean commentNotify;

    private Boolean collectNotify;

    private Boolean mentionNotify;

    private Boolean privateNotify;

    private Boolean friendRequestNotify;

}
package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Message;
import com.example.springboot.entity.NotificationSetting;

import java.util.Map;

/**
 * <p>
 *  消息通知服务类
 * </p>
 */
public interface IMessageService extends IService<Message> {

    /**
     * 获取消息列表（支持分类筛选和分页）
     */
    Page<Message> getMessages(Integer userId, String category, Integer pageNum, Integer pageSize);

    /**
     * 标记单个消息为已读
     */
    void markAsRead(Integer messageId);

    /**
     * 标记所有消息为已读
     */
    void markAllAsRead(Integer userId, String category);

    /**
     * 获取未读消息数量（按分类统计）
     */
    Map<String, Object> getUnreadCount(Integer userId);

    /**
     * 获取通知设置
     */
    NotificationSetting getNotificationSettings(Integer userId);

    /**
     * 更新通知设置
     */
    void updateNotificationSettings(NotificationSetting setting);

    /**
     * 发送点赞通知
     */
    void sendLikeNotification(Integer fromUserId, Integer toUserId, Integer itemId);

    /**
     * 发送关注通知
     */
    void sendFollowNotification(Integer fromUserId, Integer toUserId);

    /**
     * 发送评论通知
     */
    void sendCommentNotification(Integer fromUserId, Integer toUserId, Integer itemId);

    /**
     * 发送收藏通知
     */
    void sendCollectNotification(Integer fromUserId, Integer toUserId, Integer itemId);

    /**
     * 发送好友申请通知
     */
    void sendFriendRequestNotification(Integer fromUserId, Integer toUserId);

    /**
     * 发送好友申请被同意通知
     */
    void sendFriendAcceptNotification(Integer fromUserId, Integer toUserId);

}

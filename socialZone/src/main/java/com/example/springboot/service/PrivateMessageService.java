package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.PrivateMessage;

import java.util.List;

public interface PrivateMessageService extends IService<PrivateMessage> {
    
    /**
     * 发送私信
     */
    boolean sendMessage(Integer fromUserId, Integer toUserId, String content);
    
    /**
     * 获取会话消息列表
     */
    Page<PrivateMessage> getConversationMessages(Integer userId1, Integer userId2, Integer pageNum, Integer pageSize);
    
    /**
     * 获取用户的所有会话列表
     */
    List<PrivateMessage> getUserSessions(Integer userId);
    
    /**
     * 标记消息已读
     */
    boolean markAsRead(Integer fromUserId, Integer toUserId);
    
    /**
     * 获取未读消息数量
     */
    Integer getUnreadCount(Integer userId);
}
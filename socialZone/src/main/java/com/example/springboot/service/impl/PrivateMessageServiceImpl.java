package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.PrivateMessage;
import com.example.springboot.mapper.PrivateMessageMapper;
import com.example.springboot.service.PrivateMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

    @Override
    public boolean sendMessage(Integer fromUserId, Integer toUserId, String content) {
        try {
            PrivateMessage message = new PrivateMessage();
            message.setFromUserId(fromUserId);
            message.setToUserId(toUserId);
            message.setContent(content);
            message.setIsRead(false);
            message.setSendTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            
            return save(message);
        } catch (Exception e) {
            log.error("发送私信失败", e);
            return false;
        }
    }

    @Override
    public Page<PrivateMessage> getConversationMessages(Integer userId1, Integer userId2, Integer pageNum, Integer pageSize) {
        Page<PrivateMessage> page = new Page<>(pageNum, pageSize);
        List<PrivateMessage> records = baseMapper.getConversationMessages(page, userId1, userId2);
        page.setRecords(records);
        return page;
    }

    @Override
    public List<PrivateMessage> getUserSessions(Integer userId) {
        return baseMapper.getUserSessions(userId);
    }

    @Override
    public boolean markAsRead(Integer fromUserId, Integer toUserId) {
        try {
            int count = baseMapper.markMessagesAsRead(fromUserId, toUserId);
            return count >= 0;
        } catch (Exception e) {
            log.error("标记消息已读失败", e);
            return false;
        }
    }

    @Override
    public Integer getUnreadCount(Integer userId) {
        try {
            return baseMapper.getUnreadCount(userId);
        } catch (Exception e) {
            log.error("获取未读消息数量失败", e);
            return 0;
        }
    }
}
package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Message;
import com.example.springboot.entity.NotificationSetting;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.MessageMapper;
import com.example.springboot.mapper.NotificationSettingMapper;
import com.example.springboot.service.IMessageService;
import com.example.springboot.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    
    @Resource
    private IUserService userService;
    
    @Autowired
    private NotificationSettingMapper notificationSettingMapper;

    @Override
    public void sendLikeNotification(Integer fromUserId, Integer toUserId, Integer itemId) {
        sendNotification("点赞了你的笔记", "like", fromUserId, toUserId, itemId);
    }

    @Override
    public void sendFollowNotification(Integer fromUserId, Integer toUserId) {
        sendNotification("关注了你", "follow", fromUserId, toUserId, null);
    }

    @Override
    public void sendCommentNotification(Integer fromUserId, Integer toUserId, Integer itemId) {
        sendNotification("评论了你的笔记", "comment", fromUserId, toUserId, itemId);
    }

    @Override
    public void sendCollectNotification(Integer fromUserId, Integer toUserId, Integer itemId) {
        sendNotification("收藏了你的笔记", "collect", fromUserId, toUserId, itemId);
    }

    @Override
    public void sendFriendRequestNotification(Integer fromUserId, Integer toUserId) {
        sendNotification("请求添加你为好友", "friend_request", fromUserId, toUserId, null);
    }

    @Override
    public void sendFriendAcceptNotification(Integer fromUserId, Integer toUserId) {
        sendNotification("同意了你的好友申请", "friend_accept", fromUserId, toUserId, null);
    }

    @Override
    public Page<Message> getMessages(Integer userId, String category, Integer pageNum, Integer pageSize) {
        Page<Message> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectMessagesWithUserInfo(page, userId, category);
    }

    @Override
    public void markAsRead(Integer messageId) {
        try {
            UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", messageId);
            updateWrapper.set("is_read", true);
            updateWrapper.set("read_time", DateUtil.now());
            this.update(updateWrapper);
        } catch (Exception e) {
            log.error("标记消息已读失败", e);
        }
    }

    @Override
    public void markAllAsRead(Integer userId, String category) {
        try {
            UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("to_user_id", userId);
            updateWrapper.eq("is_read", false);
            if (category != null && !category.isEmpty()) {
                updateWrapper.eq("category", category);
            }
            updateWrapper.set("is_read", true);
            updateWrapper.set("read_time", DateUtil.now());
            this.update(updateWrapper);
        } catch (Exception e) {
            log.error("批量标记消息已读失败", e);
        }
    }

    @Override
    public Map<String, Object> getUnreadCount(Integer userId) {
        try {
            Map<String, Object> counts = baseMapper.countUnreadMessagesByCategory(userId);
            if (counts == null) {
                counts = new HashMap<>();
                counts.put("systemCount", 0);
                counts.put("socialCount", 0);
                counts.put("privateCount", 0);
            }
            return counts;
        } catch (Exception e) {
            log.error("获取未读消息数量失败", e);
            Map<String, Object> defaultCounts = new HashMap<>();
            defaultCounts.put("systemCount", 0);
            defaultCounts.put("socialCount", 0);
            defaultCounts.put("privateCount", 0);
            return defaultCounts;
        }
    }

    @Override
    public NotificationSetting getNotificationSettings(Integer userId) {
        try {
            QueryWrapper<NotificationSetting> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            NotificationSetting setting = notificationSettingMapper.selectOne(queryWrapper);
            
            // 如果没有设置记录，创建默认设置
            if (setting == null) {
                setting = new NotificationSetting();
                setting.setUserId(userId);
                setting.setLikeNotify(true);
                setting.setFollowNotify(true);
                setting.setCommentNotify(true);
                setting.setCollectNotify(true);
                setting.setMentionNotify(true);
                setting.setPrivateNotify(true);
                setting.setFriendRequestNotify(true);
                notificationSettingMapper.insert(setting);
            }
            
            return setting;
        } catch (Exception e) {
            log.error("获取通知设置失败", e);
            return null;
        }
    }

    @Override
    public void updateNotificationSettings(NotificationSetting setting) {
        try {
            notificationSettingMapper.updateById(setting);
        } catch (Exception e) {
            log.error("更新通知设置失败", e);
        }
    }

    /**
     * 通用发送通知方法
     */
    private void sendNotification(String text, String type, Integer fromUserId, Integer toUserId, Integer itemId) {
        System.out.println("开始发送通知：" + text + ", type: " + type + ", from: " + fromUserId + ", to: " + toUserId);
        
        try {
            // 检查接收者的通知设置
            if (!shouldSendNotification(toUserId, type)) {
                System.out.println("用户通知设置不允许发送此类通知，跳过");
                return;
            }

            // 获取发送者信息
            User fromUser = userService.getById(fromUserId);
            if (fromUser == null) {
                System.err.println("发送者用户不存在: " + fromUserId);
                return;
            }
            System.out.println("发送者信息: " + fromUser.getNickname());

            // 确定消息分类
            String category = getCategoryByType(type);
            System.out.println("消息分类: " + category);

            // 创建通知消息
            Message message = new Message();
            message.setText(text);
            message.setType(type);
            message.setTime(DateUtil.now());
            message.setFromUserId(fromUserId);
            message.setToUserId(toUserId);
            message.setItemId(itemId);
            message.setIsRead(false);
            message.setCategory(category);

            System.out.println("即将保存消息: " + message.toString());
            
            // 保存通知
            boolean saveResult = this.save(message);
            System.out.println("消息保存结果: " + saveResult + ", 消息ID: " + message.getId());
            
        } catch (Exception e) {
            // 通知发送失败不影响主要功能
            log.error("发送通知失败: {}", e.getMessage());
            System.err.println("发送通知异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 检查是否应该发送通知
     */
    private boolean shouldSendNotification(Integer toUserId, String type) {
        try {
            NotificationSetting settings = getNotificationSettings(toUserId);
            System.out.println("获取用户 " + toUserId + " 的通知设置: " + (settings != null ? "存在" : "不存在"));
            
            if (settings == null) {
                System.out.println("通知设置为空，默认允许发送");
                return true; // 默认发送
            }

            boolean shouldSend = false;
            switch (type) {
                case "like":
                    shouldSend = Boolean.TRUE.equals(settings.getLikeNotify());
                    break;
                case "follow":
                    shouldSend = Boolean.TRUE.equals(settings.getFollowNotify());
                    break;
                case "comment":
                    shouldSend = Boolean.TRUE.equals(settings.getCommentNotify());
                    break;
                case "collect":
                    shouldSend = Boolean.TRUE.equals(settings.getCollectNotify());
                    break;
                case "mention":
                    shouldSend = Boolean.TRUE.equals(settings.getMentionNotify());
                    break;
                case "friend_request":
                case "friend_accept":
                    shouldSend = Boolean.TRUE.equals(settings.getFriendRequestNotify());
                    break;
                case "private":
                    shouldSend = Boolean.TRUE.equals(settings.getPrivateNotify());
                    break;
                default:
                    shouldSend = true;
            }
            
            System.out.println("通知类型 " + type + " 是否允许发送: " + shouldSend);
            return shouldSend;
            
        } catch (Exception e) {
            log.error("检查通知设置失败", e);
            System.err.println("检查通知设置异常: " + e.getMessage());
            e.printStackTrace();
            return true; // 默认发送
        }
    }

    /**
     * 根据类型确定消息分类
     */
    private String getCategoryByType(String type) {
        switch (type) {
            case "like":
            case "follow":
            case "comment":
            case "collect":
            case "friend_request":
            case "friend_accept":
                return "social";
            case "private":
                return "private";
            default:
                return "system";
        }
    }
}

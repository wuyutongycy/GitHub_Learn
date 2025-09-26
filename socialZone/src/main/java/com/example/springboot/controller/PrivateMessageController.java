package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.PrivateMessage;
import com.example.springboot.service.PrivateMessageService;
import com.example.springboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 私信管理控制器
 */
@RestController
@RequestMapping("/privateMessage")
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    /**
     * 发送私信
     */
    @PostMapping("/send")
    public Result sendMessage(@RequestBody Map<String, Object> params) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        Integer toUserId = (Integer) params.get("toUserId");
        String content = (String) params.get("content");

        if (toUserId == null || content == null || content.trim().isEmpty()) {
            return Result.error("400", "参数不能为空");
        }

        boolean success = privateMessageService.sendMessage(currentUser.getId(), toUserId, content.trim());
        if (success) {
            return Result.success("发送成功");
        } else {
            return Result.error("500", "发送失败");
        }
    }

    /**
     * 获取与指定用户的对话消息
     */
    @GetMapping("/conversation/{userId}")
    public Result getConversationMessages(@PathVariable Integer userId,
                                        @RequestParam(defaultValue = "1") Integer pageNum,
                                        @RequestParam(defaultValue = "20") Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        Page<PrivateMessage> page = privateMessageService.getConversationMessages(
                currentUser.getId(), userId, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 测试API - 检查是否有私信数据
     */
    @GetMapping("/test")
    public Result testPrivateMessages() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        try {
            // 检查是否有任何私信记录
            List<PrivateMessage> allMessages = privateMessageService.list();
            System.out.println("数据库中总的私信数量: " + allMessages.size());
            
            // 检查当前用户相关的私信
            List<PrivateMessage> userMessages = privateMessageService.lambdaQuery()
                .eq(PrivateMessage::getFromUserId, currentUser.getId())
                .or()
                .eq(PrivateMessage::getToUserId, currentUser.getId())
                .list();
            System.out.println("用户 " + currentUser.getId() + " 相关的私信数量: " + userMessages.size());
            
            for (PrivateMessage msg : userMessages) {
                System.out.println("私信: from=" + msg.getFromUserId() + " to=" + msg.getToUserId() + " content=" + msg.getContent());
            }
            
            return Result.success("总私信数: " + allMessages.size() + ", 用户相关: " + userMessages.size());
        } catch (Exception e) {
            System.err.println("测试私信失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("500", "测试失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户的会话列表
     */
    @GetMapping("/sessions")
    public Result getUserSessions() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        try {
            List<PrivateMessage> sessions = privateMessageService.getUserSessions(currentUser.getId());
            System.out.println("用户 " + currentUser.getId() + " 的会话数量: " + sessions.size());
            for (PrivateMessage session : sessions) {
                System.out.println("会话: userId=" + session.getUserId() + ", nickname=" + session.getNickname() + ", lastMessage=" + session.getLastMessage());
            }
            return Result.success(sessions);
        } catch (Exception e) {
            System.err.println("获取会话列表失败: " + e.getMessage());
            e.printStackTrace();
            return Result.error("500", "获取会话列表失败: " + e.getMessage());
        }
    }

    /**
     * 标记与指定用户的消息为已读
     */
    @PutMapping("/markRead/{fromUserId}")
    public Result markAsRead(@PathVariable Integer fromUserId) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        boolean success = privateMessageService.markAsRead(fromUserId, currentUser.getId());
        if (success) {
            return Result.success("标记成功");
        } else {
            return Result.error("500", "标记失败");
        }
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unreadCount")
    public Result getUnreadCount() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }

        Integer count = privateMessageService.getUnreadCount(currentUser.getId());
        return Result.success(count);
    }
}
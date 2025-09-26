package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Blog;
import com.example.springboot.entity.Message;
import com.example.springboot.entity.NotificationSetting;
import com.example.springboot.service.IMessageService;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private IMessageService messageService;

    @PostMapping
    public Result save(@RequestBody Message message) {
        return Result.success(messageService.saveOrUpdate(message));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(messageService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(messageService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        Account account = TokenUtils.getCurrentUser();
        LambdaQueryWrapper<Message> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Message::getTime);
        if (!StrUtil.equals(account.getRole(),"ROLE_ADMIN")){
            wrapper.eq(Message::getToUserId,account.getId());
        }
        return Result.success(messageService.list(wrapper));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(messageService.getById(id));
    }

    /**
     * 获取消息列表（支持分类和分页）
     */
    @GetMapping("/list")
    public Result getMessageList(@RequestParam(defaultValue = "") String category,
                                @RequestParam(defaultValue = "1") Integer pageNum,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        Page<Message> page = messageService.getMessages(currentUser.getId(), category, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 标记单个消息为已读
     */
    @PutMapping("/read/{messageId}")
    public Result markAsRead(@PathVariable Integer messageId) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        messageService.markAsRead(messageId);
        return Result.success("标记成功");
    }

    /**
     * 批量标记消息为已读
     */
    @PutMapping("/readAll")
    public Result markAllAsRead(@RequestParam(defaultValue = "") String category) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        messageService.markAllAsRead(currentUser.getId(), category);
        return Result.success("操作成功");
    }

    /**
     * 获取未读消息数量统计
     */
    @GetMapping("/unreadCount")
    public Result getUnreadCount() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        Map<String, Object> counts = messageService.getUnreadCount(currentUser.getId());
        return Result.success(counts);
    }

    /**
     * 获取通知设置
     */
    @GetMapping("/settings")
    public Result getNotificationSettings() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        NotificationSetting settings = messageService.getNotificationSettings(currentUser.getId());
        return Result.success(settings);
    }

    /**
     * 更新通知设置
     */
    @PutMapping("/settings")
    public Result updateNotificationSettings(@RequestBody NotificationSetting settings) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "请登录后操作");
        }
        
        settings.setUserId(currentUser.getId());
        messageService.updateNotificationSettings(settings);
        return Result.success("设置已保存");
    }
}


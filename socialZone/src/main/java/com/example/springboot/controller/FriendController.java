package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.FriendRequest;
import com.example.springboot.entity.RelationStatus;
import com.example.springboot.entity.User;
import com.example.springboot.service.IFriendService;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 好友管理控制器
 * </p>
 */
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private IFriendService friendService;

    /**
     * 测试好友服务
     */
    @GetMapping("/test")
    public Result testFriendService() {
        try {
            if (TokenUtils.getCurrentUser() == null) {
                return Result.error("401", "未登录");
            }
            
            Integer currentUserId = TokenUtils.getCurrentUser().getId();
            if (currentUserId == null) {
                return Result.error("401", "用户ID为空");
            }
            
            // 测试服务是否可用
            RelationStatus status = friendService.getRelationStatus(currentUserId, 5);
            return Result.success("服务正常，当前用户与用户5的关系：" + status.getCode());
        } catch (Exception e) {
            return Result.error("500", "测试失败: " + e.getMessage());
        }
    }

    /**
     * 简化版发送好友申请（用于调试）
     */
    @PostMapping("/request-simple")
    public Result sendFriendRequestSimple(@RequestBody Map<String, Object> params) {
        try {
            // 基础参数检查
            if (TokenUtils.getCurrentUser() == null) {
                return Result.error("401", "未登录");
            }
            
            Integer currentUserId = TokenUtils.getCurrentUser().getId();
            Object toUserIdObj = params.get("toUserId");
            
            if (toUserIdObj == null) {
                return Result.error("400", "缺少toUserId参数");
            }
            
            Integer toUserId;
            try {
                if (toUserIdObj instanceof String) {
                    toUserId = Integer.parseInt((String) toUserIdObj);
                } else {
                    toUserId = (Integer) toUserIdObj;
                }
            } catch (Exception e) {
                return Result.error("400", "toUserId格式错误: " + e.getMessage());
            }
            
            String message = (String) params.getOrDefault("message", "申请添加好友");
            
            // 直接创建好友申请记录，不做复杂逻辑
            FriendRequest request = new FriendRequest();
            request.setFromUserId(currentUserId);
            request.setToUserId(toUserId);
            request.setMessage(message);
            request.setStatus(0);
            request.setCreateTime(cn.hutool.core.date.DateUtil.now());
            
            boolean saved = friendService.save(request);
            
            if (saved) {
                return Result.success("好友申请已发送");
            } else {
                return Result.error("500", "保存申请记录失败");
            }
            
        } catch (Exception e) {
            return Result.error("500", "发送失败: " + e.getMessage() + " | " + e.getClass().getSimpleName());
        }
    }

    /**
     * 发送好友申请
     */
    @PostMapping("/request")
    public Result sendFriendRequest(@RequestBody Map<String, Object> params) {
        // 检查用户登录状态
        if (TokenUtils.getCurrentUser() == null) {
            return Result.error("401", "请登录后操作");
        }
        
        Integer currentUserId = TokenUtils.getCurrentUser().getId();
        if (currentUserId == null) {
            return Result.error("401", "用户信息异常");
        }
        
        // 参数验证和类型转换
        Object toUserIdObj = params.get("toUserId");
        if (toUserIdObj == null) {
            return Result.error("400", "目标用户ID不能为空");
        }
        
        Integer toUserId;
        try {
            if (toUserIdObj instanceof String) {
                toUserId = Integer.parseInt((String) toUserIdObj);
            } else if (toUserIdObj instanceof Integer) {
                toUserId = (Integer) toUserIdObj;
            } else {
                return Result.error("400", "目标用户ID格式错误");
            }
        } catch (NumberFormatException e) {
            return Result.error("400", "目标用户ID格式错误");
        }
        
        String message = (String) params.getOrDefault("message", "");
        
        try {
            friendService.sendFriendRequest(currentUserId, toUserId, message);
            return Result.success("好友申请已发送");
        } catch (RuntimeException e) {
            return Result.error("500", e.getMessage());
        } catch (Exception e) {
            return Result.error("500", "发送好友申请失败: " + e.getMessage());
        }
    }

    /**
     * 处理好友申请
     */
    @PutMapping("/request/{requestId}")
    public Result handleFriendRequest(@PathVariable Integer requestId,
                                     @RequestParam Integer action) { // 1:同意, 2:拒绝
        try {
            if (action == 1) {
                friendService.acceptFriendRequest(requestId);
                return Result.success("已同意好友申请");
            } else if (action == 2) {
                friendService.rejectFriendRequest(requestId);
                return Result.success("已拒绝好友申请");
            } else {
                return Result.error("400", "无效的操作");
            }
        } catch (RuntimeException e) {
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 获取好友申请列表
     */
    @GetMapping("/requests")
    public Result getFriendRequests(@RequestParam(defaultValue = "received") String type) {
        Integer userId = TokenUtils.getCurrentUser().getId();
        
        List<FriendRequest> requests;
        if ("sent".equals(type)) {
            requests = friendService.getSentRequests(userId);
        } else {
            requests = friendService.getReceivedRequests(userId);
        }
        
        return Result.success(requests);
    }

    /**
     * 获取好友列表
     */
    @GetMapping("/list")
    public Result getFriendList() {
        Integer userId = TokenUtils.getCurrentUser().getId();
        List<User> friends = friendService.getFriendList(userId);
        return Result.success(friends);
    }

    /**
     * 获取与指定用户的关系状态
     */
    @GetMapping("/relationship/{userId}")
    public Result getFriendStatus(@PathVariable Integer userId) {
        Integer currentUserId = TokenUtils.getCurrentUser().getId();
        RelationStatus status = friendService.getRelationStatus(currentUserId, userId);
        
        // 直接返回关系状态代码，前端期望的是字符串
        return Result.success(status.getCode());
    }

    /**
     * 撤回好友申请
     */
    @DeleteMapping("/request/{requestId}")
    public Result withdrawFriendRequest(@PathVariable Integer requestId) {
        try {
            friendService.withdrawFriendRequest(requestId);
            return Result.success("已撤回申请");
        } catch (RuntimeException e) {
            return Result.error("500", e.getMessage());
        }
    }

    /**
     * 解除好友关系
     */
    @DeleteMapping("/{friendId}")
    public Result removeFriend(@PathVariable Integer friendId) {
        Integer userId = TokenUtils.getCurrentUser().getId();
        
        try {
            friendService.removeFriend(userId, friendId);
            return Result.success("已解除好友关系");
        } catch (RuntimeException e) {
            return Result.error("500", e.getMessage());
        }
    }

}
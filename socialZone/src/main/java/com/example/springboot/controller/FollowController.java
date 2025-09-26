package com.example.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Blog;
import com.example.springboot.entity.Collect;
import com.example.springboot.entity.Follow;
import com.example.springboot.service.IBlogService;
import com.example.springboot.service.IFollowService;
import com.example.springboot.service.IMessageService;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private IFollowService followService;
    @Resource
    private IBlogService blogService;
    @Resource
    private IMessageService messageService;

    @PostMapping
    public Result save(@RequestBody Follow follow) {
        Account currentUser = TokenUtils.getCurrentUser();
        System.out.println("关注请求：用户 " + currentUser.getId() + " 想要关注用户 " + follow.getItemId());
        
        if (follow.getItemId().equals(currentUser.getId())) {
            return Result.error("605","不可以关注自己");
        }
        
        // 检查是否已经关注
        LambdaQueryWrapper<Follow> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(Follow::getUserId, currentUser.getId());
        checkWrapper.eq(Follow::getItemId, follow.getItemId());
        Follow existingFollow = followService.getOne(checkWrapper);
        
        if (existingFollow != null) {
            // 已经关注，执行取消关注
            System.out.println("用户已关注，执行取消关注操作");
            followService.remove(checkWrapper);
            return Result.success("取消关注成功");
        } else {
            // 执行关注操作
            try {
                follow.setUserId(currentUser.getId());
                boolean saveResult = followService.saveOrUpdate(follow);
                System.out.println("关注操作结果: " + saveResult);
                
                if (saveResult) {
                    // 发送关注通知
                    try {
                        messageService.sendFollowNotification(currentUser.getId(), follow.getItemId());
                        System.out.println("关注通知已发送：用户 " + currentUser.getId() + " 关注了用户 " + follow.getItemId());
                    } catch (Exception e) {
                        System.err.println("发送关注通知失败: " + e.getMessage());
                        e.printStackTrace();
                    }
                    
                    return Result.success("关注成功");
                } else {
                    return Result.error("500", "关注失败");
                }
            } catch (Exception e) {
                System.err.println("关注操作异常: " + e.getMessage());
                e.printStackTrace();
                return Result.error("500", "关注操作失败");
            }
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(followService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(followService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll() {
        return Result.success(followService.list());
    }

    @GetMapping("/check/{userId}")
    public Result check(@PathVariable Integer userId) {
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, TokenUtils.getCurrentUser().getId());
        wrapper.eq(Follow::getItemId, userId);
        return followService.count(wrapper) > 0 ? Result.success() : Result.error("605", "尚未关注");
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(followService.getById(id));
    }

    /**
     * 获取当前用户的关注列表
     */
    @GetMapping("/following")
    public Result getFollowingList() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null || currentUser.getId() == null) {
            return Result.error("401", "请登录后操作");
        }
        
        try {
            List<Follow> followingList = followService.getFollowingWithUserInfo(currentUser.getId());
            return Result.success(followingList);
        } catch (Exception e) {
            return Result.error("500", "获取关注列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户的粉丝列表
     */
    @GetMapping("/followers")
    public Result getFollowersList() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null || currentUser.getId() == null) {
            return Result.error("401", "请登录后操作");
        }
        
        try {
            List<Follow> followersList = followService.getFollowersWithUserInfo(currentUser.getId());
            return Result.success(followersList);
        } catch (Exception e) {
            return Result.error("500", "获取粉丝列表失败: " + e.getMessage());
        }
    }

    /**
     * 测试当前用户信息
     */
    @GetMapping("/test")
    public Result testCurrentUser() {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null) {
            return Result.error("401", "未登录或token无效");
        }
        return Result.success(currentUser);
    }

    /**
     * 取消关注
     */
    @DeleteMapping("/unfollow/{userId}")
    public Result unfollow(@PathVariable Integer userId) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (currentUser == null || currentUser.getId() == null) {
            return Result.error("401", "请登录后操作");
        }
        
        try {
            LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Follow::getUserId, currentUser.getId());
            wrapper.eq(Follow::getItemId, userId);
            boolean result = followService.remove(wrapper);
            
            if (result) {
                return Result.success("取消关注成功");
            } else {
                return Result.error("500", "取消关注失败");
            }
        } catch (Exception e) {
            return Result.error("500", e.getMessage());
        }
    }

}


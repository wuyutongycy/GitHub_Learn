package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Like;
import com.example.springboot.service.ILikeService;
import com.example.springboot.utils.TokenUtils;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 点赞控制器
 * </p>
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    @Resource
    private ILikeService likeService;

    /**
     * 点赞/取消点赞（切换状态）
     */
    @PostMapping("/toggle/{blogId}")
    public Result toggleLike(@PathVariable Integer blogId) {
        Integer userId = TokenUtils.getCurrentUser().getId();
        boolean isLiked = likeService.toggleLike(userId, blogId);
        return Result.success(isLiked);
    }

    /**
     * 获取当前用户对某博文的点赞状态
     */
    @GetMapping("/status/{blogId}")
    public Result getLikeStatus(@PathVariable Integer blogId) {
        Integer userId = TokenUtils.getCurrentUser().getId();
        boolean isLiked = likeService.getLikeStatus(userId, blogId);
        return Result.success(isLiked);
    }

    /**
     * 获取博文点赞数
     */
    @GetMapping("/count/{blogId}")
    public Result getLikeCount(@PathVariable Integer blogId) {
        long count = likeService.getLikeCount(blogId);
        return Result.success(count);
    }

    /**
     * 获取点赞用户列表
     */
    @GetMapping("/users/{blogId}")
    public Result getLikeUsers(@PathVariable Integer blogId) {
        List<Like> likeUsers = likeService.getLikeUsersWithInfo(blogId);
        return Result.success(likeUsers);
    }

}
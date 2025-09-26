package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Like;

import java.util.List;

/**
 * <p>
 * 点赞服务接口
 * </p>
 */
public interface ILikeService extends IService<Like> {

    /**
     * 切换点赞状态（点赞/取消点赞）
     */
    boolean toggleLike(Integer userId, Integer itemId);

    /**
     * 获取用户对某博文的点赞状态
     */
    boolean getLikeStatus(Integer userId, Integer itemId);

    /**
     * 获取博文点赞数
     */
    long getLikeCount(Integer itemId);

    /**
     * 获取点赞用户列表（带用户信息）
     */
    List<Like> getLikeUsersWithInfo(Integer itemId);

}
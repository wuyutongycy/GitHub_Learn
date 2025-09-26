package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Follow;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 */
public interface IFollowService extends IService<Follow> {

    /**
     * 获取用户的关注列表（包含用户信息）
     */
    List<Follow> getFollowingWithUserInfo(Integer userId);

    /**
     * 获取用户的粉丝列表（包含用户信息）
     */
    List<Follow> getFollowersWithUserInfo(Integer userId);

    /**
     * 检查用户是否关注了目标用户
     */
    boolean isFollowing(Integer userId, Integer targetUserId);
}

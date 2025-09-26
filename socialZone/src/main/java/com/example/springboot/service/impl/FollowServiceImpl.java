package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Follow;
import com.example.springboot.mapper.FollowMapper;
import com.example.springboot.service.IFollowService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    
    @Override
    public List<Follow> getFollowingWithUserInfo(Integer userId) {
        return baseMapper.getFollowingWithUserInfo(userId);
    }

    @Override
    public List<Follow> getFollowersWithUserInfo(Integer userId) {
        return baseMapper.getFollowersWithUserInfo(userId);
    }

    @Override
    public boolean isFollowing(Integer userId, Integer targetUserId) {
        if (userId == null || targetUserId == null) {
            return false;
        }
        
        LambdaQueryWrapper<Follow> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Follow::getUserId, userId)
               .eq(Follow::getItemId, targetUserId);
        
        return count(wrapper) > 0;
    }
}

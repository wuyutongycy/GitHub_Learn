package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Blog;
import com.example.springboot.entity.Like;
import com.example.springboot.mapper.LikeMapper;
import com.example.springboot.service.IBlogService;
import com.example.springboot.service.ILikeService;
import com.example.springboot.service.IMessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 点赞服务实现类
 * </p>
 */
@Slf4j
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {

    @Resource
    private IBlogService blogService;
    
    @Resource
    private IMessageService messageService;

    @Override
    @Transactional
    public boolean toggleLike(Integer userId, Integer itemId) {
        LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getUserId, userId);
        queryWrapper.eq(Like::getItemId, itemId);
        
        Like existingLike = this.getOne(queryWrapper);
        
        if (existingLike != null) {
            // 取消点赞
            this.removeById(existingLike.getId());
            updateBlogLikeCount(itemId, -1);
            return false;
        } else {
            // 点赞
            Like like = new Like();
            like.setUserId(userId);
            like.setItemId(itemId);
            like.setCreateTime(DateUtil.now());
            this.save(like);
            updateBlogLikeCount(itemId, 1);
            
            // 发送点赞通知
            sendLikeNotification(userId, itemId);
            return true;
        }
    }

    @Override
    public boolean getLikeStatus(Integer userId, Integer itemId) {
        LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getUserId, userId);
        queryWrapper.eq(Like::getItemId, itemId);
        return this.count(queryWrapper) > 0;
    }

    @Override
    public long getLikeCount(Integer itemId) {
        LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getItemId, itemId);
        return this.count(queryWrapper);
    }

    @Override
    public List<Like> getLikeUsersWithInfo(Integer itemId) {
        return baseMapper.selectLikeUsersWithInfo(itemId);
    }

    /**
     * 更新博文点赞数缓存
     */
    private void updateBlogLikeCount(Integer itemId, int delta) {
        Blog blog = blogService.getById(itemId);
        if (blog != null) {
            int currentCount = blog.getLikeCount() != null ? blog.getLikeCount() : 0;
            blog.setLikeCount(Math.max(0, currentCount + delta));
            blogService.updateById(blog);
        }
    }

    /**
     * 发送点赞通知
     */
    private void sendLikeNotification(Integer fromUserId, Integer itemId) {
        try {
            Blog blog = blogService.getById(itemId);
            if (blog != null && !blog.getUserId().equals(fromUserId)) {
                // 不给自己发通知
                messageService.sendLikeNotification(fromUserId, blog.getUserId(), itemId);
            }
        } catch (Exception e) {
            // 通知发送失败不影响点赞功能
            log.warn("发送点赞通知失败: {}", e.getMessage());
        }
    }
}
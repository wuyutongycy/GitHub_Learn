package com.example.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Follow;
import com.example.springboot.entity.FriendRequest;
import com.example.springboot.entity.RelationStatus;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.FriendRequestMapper;
import com.example.springboot.service.IFollowService;
import com.example.springboot.service.IFriendService;
import com.example.springboot.service.IMessageService;
import com.example.springboot.service.IUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 好友管理服务实现类
 * </p>
 */
@Slf4j
@Service
public class FriendServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest> implements IFriendService {

    @Resource
    private IFollowService followService;
    
    @Resource
    private IUserService userService;
    
    @Resource
    private IMessageService messageService;

    @Override
    public RelationStatus getRelationStatus(Integer userId, Integer targetUserId) {
        if (userId.equals(targetUserId)) {
            return RelationStatus.STRANGER;
        }

        // 首先检查是否已经是确认的好友关系（通过好友申请流程）
        if (isConfirmedFriend(userId, targetUserId)) {
            return RelationStatus.FRIEND;
        }

        // 检查关注状态
        boolean iFollowTarget = isFollowing(userId, targetUserId);
        boolean targetFollowsMe = isFollowing(targetUserId, userId);

        if (iFollowTarget && targetFollowsMe) {
            // 双向关注但不是确认好友，仍然是关注状态
            return RelationStatus.FOLLOWING;
        } else if (iFollowTarget) {
            return RelationStatus.FOLLOWING;
        } else if (targetFollowsMe) {
            return RelationStatus.FOLLOWER;
        }

        // 检查好友申请状态
        LambdaQueryWrapper<FriendRequest> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FriendRequest::getFromUserId, userId)
                   .eq(FriendRequest::getToUserId, targetUserId)
                   .eq(FriendRequest::getStatus, 0);
        
        if (this.count(queryWrapper) > 0) {
            return RelationStatus.REQUESTING;
        }

        // 检查是否有待处理的申请
        queryWrapper.clear();
        queryWrapper.eq(FriendRequest::getFromUserId, targetUserId)
                   .eq(FriendRequest::getToUserId, userId)
                   .eq(FriendRequest::getStatus, 0);
        
        if (this.count(queryWrapper) > 0) {
            return RelationStatus.PENDING;
        }

        return RelationStatus.STRANGER;
    }

    @Override
    @Transactional
    public void sendFriendRequest(Integer fromUserId, Integer toUserId, String message) {
        // 检查是否已经是好友或已有申请
        RelationStatus status = getRelationStatus(fromUserId, toUserId);
        if (status == RelationStatus.FRIEND || status == RelationStatus.REQUESTING) {
            throw new RuntimeException("已经是好友或申请已存在");
        }

        // 如果对方已经申请过，直接同意对方的申请
        if (status == RelationStatus.PENDING) {
            LambdaQueryWrapper<FriendRequest> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(FriendRequest::getFromUserId, toUserId)
                       .eq(FriendRequest::getToUserId, fromUserId)
                       .eq(FriendRequest::getStatus, 0);
            
            FriendRequest existingRequest = this.getOne(queryWrapper);
            if (existingRequest != null) {
                acceptFriendRequest(existingRequest.getId());
                return;
            }
        }

        // 创建新的好友申请
        FriendRequest request = new FriendRequest();
        request.setFromUserId(fromUserId);
        request.setToUserId(toUserId);
        request.setMessage(message);
        request.setStatus(0);
        request.setCreateTime(DateUtil.now());

        this.save(request);

        // 发送好友申请通知
        try {
            messageService.sendFriendRequestNotification(fromUserId, toUserId);
        } catch (Exception e) {
            log.warn("发送好友申请通知失败: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void acceptFriendRequest(Integer requestId) {
        FriendRequest request = this.getById(requestId);
        if (request == null || request.getStatus() != 0) {
            throw new RuntimeException("申请不存在或已处理");
        }

        // 更新申请状态
        request.setStatus(1);
        request.setHandleTime(DateUtil.now());
        this.updateById(request);

        // 建立双向关注关系
        createFollowRelation(request.getFromUserId(), request.getToUserId());
        createFollowRelation(request.getToUserId(), request.getFromUserId());

        // 发送同意通知
        try {
            messageService.sendFriendAcceptNotification(request.getToUserId(), request.getFromUserId());
        } catch (Exception e) {
            log.warn("发送好友同意通知失败: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void rejectFriendRequest(Integer requestId) {
        FriendRequest request = this.getById(requestId);
        if (request == null || request.getStatus() != 0) {
            throw new RuntimeException("申请不存在或已处理");
        }

        request.setStatus(2);
        request.setHandleTime(DateUtil.now());
        this.updateById(request);
    }

    @Override
    @Transactional
    public void withdrawFriendRequest(Integer requestId) {
        FriendRequest request = this.getById(requestId);
        if (request == null || request.getStatus() != 0) {
            throw new RuntimeException("申请不存在或已处理");
        }

        this.removeById(requestId);
    }

    @Override
    public List<User> getFriendList(Integer userId) {
        // 查询所有已同意的好友申请
        LambdaQueryWrapper<FriendRequest> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(FriendRequest::getFromUserId, userId)
                    .eq(FriendRequest::getStatus, 1);
        List<FriendRequest> sentRequests = this.list(queryWrapper1);
        
        LambdaQueryWrapper<FriendRequest> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(FriendRequest::getToUserId, userId)
                    .eq(FriendRequest::getStatus, 1);
        List<FriendRequest> receivedRequests = this.list(queryWrapper2);

        List<User> friends = new ArrayList<>();
        
        // 添加我发出申请被同意的好友
        for (FriendRequest request : sentRequests) {
            User friend = userService.getById(request.getToUserId());
            if (friend != null && friends.stream().noneMatch(f -> f.getId().equals(friend.getId()))) {
                friends.add(friend);
            }
        }
        
        // 添加对方发出申请我同意的好友
        for (FriendRequest request : receivedRequests) {
            User friend = userService.getById(request.getFromUserId());
            if (friend != null && friends.stream().noneMatch(f -> f.getId().equals(friend.getId()))) {
                friends.add(friend);
            }
        }

        return friends;
    }

    @Override
    public boolean isFriend(Integer userId, Integer targetUserId) {
        return isConfirmedFriend(userId, targetUserId);
    }

    @Override
    public List<FriendRequest> getReceivedRequests(Integer userId) {
        return baseMapper.selectReceivedRequests(userId);
    }

    @Override
    public List<FriendRequest> getSentRequests(Integer userId) {
        return baseMapper.selectSentRequests(userId);
    }

    @Override
    @Transactional
    public void removeFriend(Integer userId, Integer friendId) {
        if (!isFriend(userId, friendId)) {
            throw new RuntimeException("不是好友关系");
        }

        // 删除双向关注关系
        removeFollowRelation(userId, friendId);
        removeFollowRelation(friendId, userId);
    }

    /**
     * 检查是否关注
     */
    private boolean isFollowing(Integer userId, Integer targetId) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId, userId)
                   .eq(Follow::getItemId, targetId);
        return followService.count(queryWrapper) > 0;
    }

    /**
     * 检查是否是确认的好友关系（通过好友申请流程建立的）
     */
    private boolean isConfirmedFriend(Integer userId, Integer targetUserId) {
        // 检查是否存在已同意的好友申请记录（双向检查）
        LambdaQueryWrapper<FriendRequest> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(FriendRequest::getFromUserId, userId)
                    .eq(FriendRequest::getToUserId, targetUserId)
                    .eq(FriendRequest::getStatus, 1);
        
        LambdaQueryWrapper<FriendRequest> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(FriendRequest::getFromUserId, targetUserId)
                    .eq(FriendRequest::getToUserId, userId)
                    .eq(FriendRequest::getStatus, 1);
        
        return this.count(queryWrapper1) > 0 || this.count(queryWrapper2) > 0;
    }

    /**
     * 创建关注关系
     */
    private void createFollowRelation(Integer userId, Integer targetId) {
        if (!isFollowing(userId, targetId)) {
            Follow follow = new Follow();
            follow.setUserId(userId);
            follow.setItemId(targetId);
            followService.save(follow);
        }
    }

    /**
     * 删除关注关系
     */
    private void removeFollowRelation(Integer userId, Integer targetId) {
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId, userId)
                   .eq(Follow::getItemId, targetId);
        followService.remove(queryWrapper);
    }
}
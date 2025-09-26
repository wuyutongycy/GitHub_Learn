package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.FriendRequest;
import com.example.springboot.entity.RelationStatus;
import com.example.springboot.entity.User;

import java.util.List;

/**
 * <p>
 * 好友管理服务接口
 * </p>
 */
public interface IFriendService extends IService<FriendRequest> {

    /**
     * 获取用户关系状态
     */
    RelationStatus getRelationStatus(Integer userId, Integer targetUserId);

    /**
     * 发送好友申请
     */
    void sendFriendRequest(Integer fromUserId, Integer toUserId, String message);

    /**
     * 同意好友申请
     */
    void acceptFriendRequest(Integer requestId);

    /**
     * 拒绝好友申请
     */
    void rejectFriendRequest(Integer requestId);

    /**
     * 撤回好友申请
     */
    void withdrawFriendRequest(Integer requestId);

    /**
     * 获取好友列表（双向关注的用户）
     */
    List<User> getFriendList(Integer userId);

    /**
     * 检查是否为好友关系
     */
    boolean isFriend(Integer userId, Integer targetUserId);

    /**
     * 获取收到的好友申请
     */
    List<FriendRequest> getReceivedRequests(Integer userId);

    /**
     * 获取发出的好友申请
     */
    List<FriendRequest> getSentRequests(Integer userId);

    /**
     * 解除好友关系
     */
    void removeFriend(Integer userId, Integer friendId);

}
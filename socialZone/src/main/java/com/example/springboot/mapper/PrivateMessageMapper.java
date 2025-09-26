package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PrivateMessageMapper extends BaseMapper<PrivateMessage> {

    @Select("SELECT pm.*, u1.nickname as fromNickname, u1.avatar_url as fromAvatarUrl, " +
            "u2.nickname as toNickname, u2.avatar_url as toAvatarUrl " +
            "FROM private_message pm " +
            "LEFT JOIN sys_user u1 ON pm.from_user_id = u1.id " +
            "LEFT JOIN sys_user u2 ON pm.to_user_id = u2.id " +
            "WHERE (pm.from_user_id = #{userId1} AND pm.to_user_id = #{userId2}) " +
            "OR (pm.from_user_id = #{userId2} AND pm.to_user_id = #{userId1}) " +
            "ORDER BY pm.send_time ASC")
    List<PrivateMessage> getConversationMessages(Page<PrivateMessage> page, @Param("userId1") Integer userId1, @Param("userId2") Integer userId2);

    // MySQL 5.7兼容版本 - 获取每个用户的最新消息
    @Select("SELECT " +
            "pm1.other_user_id as userId, " +
            "pm1.nickname, " +
            "pm1.avatarUrl, " +
            "pm1.content as lastMessage, " +
            "pm1.send_time as lastTime, " +
            "0 as unreadCount " +
            "FROM ( " +
            "  SELECT " +
            "    CASE WHEN pm.from_user_id = #{userId} THEN pm.to_user_id ELSE pm.from_user_id END as other_user_id, " +
            "    CASE WHEN pm.from_user_id = #{userId} " +
            "      THEN (SELECT nickname FROM sys_user WHERE id = pm.to_user_id) " +
            "      ELSE (SELECT nickname FROM sys_user WHERE id = pm.from_user_id) " +
            "    END as nickname, " +
            "    CASE WHEN pm.from_user_id = #{userId} " +
            "      THEN (SELECT avatar_url FROM sys_user WHERE id = pm.to_user_id) " +
            "      ELSE (SELECT avatar_url FROM sys_user WHERE id = pm.from_user_id) " +
            "    END as avatarUrl, " +
            "    pm.content, " +
            "    pm.send_time " +
            "  FROM private_message pm " +
            "  WHERE pm.from_user_id = #{userId} OR pm.to_user_id = #{userId} " +
            ") pm1 " +
            "WHERE pm1.send_time = ( " +
            "  SELECT MAX(pm2.send_time) " +
            "  FROM private_message pm2 " +
            "  WHERE (pm2.from_user_id = #{userId} AND pm2.to_user_id = pm1.other_user_id) " +
            "     OR (pm2.to_user_id = #{userId} AND pm2.from_user_id = pm1.other_user_id) " +
            ") " +
            "GROUP BY pm1.other_user_id " +
            "ORDER BY pm1.send_time DESC")
    List<PrivateMessage> getUserSessions(@Param("userId") Integer userId);

    @Update("UPDATE private_message SET is_read = 1 WHERE from_user_id = #{fromUserId} AND to_user_id = #{toUserId} AND is_read = 0")
    int markMessagesAsRead(@Param("fromUserId") Integer fromUserId, @Param("toUserId") Integer toUserId);

    @Select("SELECT COUNT(*) FROM private_message WHERE to_user_id = #{userId} AND is_read = 0")
    Integer getUnreadCount(@Param("userId") Integer userId);
}
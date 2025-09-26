package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.FriendRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 好友申请Mapper接口
 * </p>
 */
@Mapper
public interface FriendRequestMapper extends BaseMapper<FriendRequest> {

    @Select("SELECT fr.*, fu.nickname as from_nickname, fu.avatar_url as from_avatar_url, " +
            "tu.nickname as to_nickname, tu.avatar_url as to_avatar_url " +
            "FROM friend_request fr " +
            "LEFT JOIN sys_user fu ON fr.from_user_id = fu.id " +
            "LEFT JOIN sys_user tu ON fr.to_user_id = tu.id " +
            "WHERE fr.to_user_id = #{userId} AND fr.status = 0 " +
            "ORDER BY fr.create_time DESC")
    List<FriendRequest> selectReceivedRequests(Integer userId);

    @Select("SELECT fr.*, fu.nickname as from_nickname, fu.avatar_url as from_avatar_url, " +
            "tu.nickname as to_nickname, tu.avatar_url as to_avatar_url " +
            "FROM friend_request fr " +
            "LEFT JOIN sys_user fu ON fr.from_user_id = fu.id " +
            "LEFT JOIN sys_user tu ON fr.to_user_id = tu.id " +
            "WHERE fr.from_user_id = #{userId} " +
            "ORDER BY fr.create_time DESC")
    List<FriendRequest> selectSentRequests(Integer userId);

}
package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {

    /**
     * 获取用户的关注列表（包含被关注用户信息）
     */
    @Select("SELECT f.*, u.nickname, u.avatar_url as avatarUrl, u.info " +
            "FROM follow f " +
            "LEFT JOIN sys_user u ON f.item_id = u.id " +
            "WHERE f.user_id = #{userId} " +
            "ORDER BY f.id DESC")
    List<Follow> getFollowingWithUserInfo(@Param("userId") Integer userId);

    /**
     * 获取用户的粉丝列表（包含粉丝用户信息）
     */
    @Select("SELECT f.*, u.nickname, u.avatar_url as avatarUrl, u.info " +
            "FROM follow f " +
            "LEFT JOIN sys_user u ON f.user_id = u.id " +
            "WHERE f.item_id = #{userId} " +
            "ORDER BY f.id DESC")
    List<Follow> getFollowersWithUserInfo(@Param("userId") Integer userId);
}

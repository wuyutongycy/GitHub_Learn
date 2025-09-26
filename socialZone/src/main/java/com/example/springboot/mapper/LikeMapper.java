package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Like;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 点赞Mapper接口
 * </p>
 */
public interface LikeMapper extends BaseMapper<Like> {

    @Select("SELECT l.*, u.nickname, u.avatar_url FROM `like` l " +
            "LEFT JOIN sys_user u ON l.user_id = u.id " +
            "WHERE l.item_id = #{itemId} ORDER BY l.create_time DESC")
    List<Like> selectLikeUsersWithInfo(Integer itemId);

}
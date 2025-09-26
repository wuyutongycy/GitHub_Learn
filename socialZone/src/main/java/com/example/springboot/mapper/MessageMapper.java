package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  消息Mapper接口
 * </p>
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    @Select("<script>" +
            "SELECT m.*, u.nickname as from_nickname, u.avatar_url as from_avatar_url " +
            "FROM message m " +
            "LEFT JOIN sys_user u ON m.from_user_id = u.id " +
            "WHERE m.to_user_id = #{userId} " +
            "<if test='category != null and category != \"\"'> AND m.category = #{category} </if>" +
            "ORDER BY m.time DESC" +
            "</script>")
    Page<Message> selectMessagesWithUserInfo(Page<Message> page, @Param("userId") Integer userId, @Param("category") String category);

    @Select("SELECT COUNT(*) FROM message WHERE to_user_id = #{userId} AND is_read = 0")
    Long countUnreadMessages(@Param("userId") Integer userId);

    @Select("<script>" +
            "SELECT " +
            "COALESCE(SUM(CASE WHEN category = 'system' AND is_read = 0 THEN 1 ELSE 0 END), 0) as systemCount," +
            "COALESCE(SUM(CASE WHEN category = 'social' AND is_read = 0 THEN 1 ELSE 0 END), 0) as socialCount," +
            "COALESCE(SUM(CASE WHEN category = 'private' AND is_read = 0 THEN 1 ELSE 0 END), 0) as privateCount " +
            "FROM message WHERE to_user_id = #{userId}" +
            "</script>")
    java.util.Map<String, Object> countUnreadMessagesByCategory(@Param("userId") Integer userId);

}

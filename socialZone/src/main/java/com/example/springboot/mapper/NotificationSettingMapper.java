package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.NotificationSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通知设置Mapper接口
 * </p>
 */
@Mapper
public interface NotificationSettingMapper extends BaseMapper<NotificationSetting> {

}
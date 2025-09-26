package com.example.springboot.entity;

/**
 * <p>
 * 用户关系状态枚举
 * </p>
 */
public enum RelationStatus {
    STRANGER("stranger", "陌生人"),
    FOLLOWING("following", "我关注的"), 
    FOLLOWER("follower", "关注我的"),
    FRIEND("friend", "好友"),
    REQUESTING("requesting", "申请中"),
    PENDING("pending", "待处理");

    private final String code;
    private final String desc;

    RelationStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
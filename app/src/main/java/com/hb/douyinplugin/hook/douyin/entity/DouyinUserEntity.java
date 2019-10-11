package com.hb.douyinplugin.hook.douyin.entity;

/**
 * createTime: 2019/10/11.16:17
 * updateTime: 2019/10/11.16:17
 * author: singleMan.
 * desc:
 */

public class DouyinUserEntity {

    private String uid;
    private String nickname;
    private String secUid;
    private String thirdName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSecUid() {
        return secUid;
    }

    public void setSecUid(String secUid) {
        this.secUid = secUid;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }
}

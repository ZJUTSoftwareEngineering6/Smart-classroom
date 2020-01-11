package com.example.updownload.sdk_demo;

public class User {
    public String userName,userId,userPaw, userType, userCondition;
    public final String create = "TestXyy";
    public User(){}
    public User(String userName, String userId, String userPaw,String userType,String userCondition) {
        this.userName = userName;
        this.userId = userId;
        this.userPaw = userPaw;
        this.userType = userType;
        this.userCondition = userCondition;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public void setUserPaw(String userPaw) {
        this.userPaw = userPaw;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserCondition(String userCondition) {
        this.userCondition = userCondition;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPaw() {
        return userPaw;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserCondition() {
        return userCondition;
    }
}

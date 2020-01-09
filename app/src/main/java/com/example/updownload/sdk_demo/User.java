package com.example.updownload.sdk_demo;

public class User {
    public String userName, userId, userPaw, userType;
    public final String create = "TestXyy";
    public User(){}
    public User(String userName, String userId, String userPaw) {
        this.userName = userName;
        this.userId = userId;
        this.userPaw = userPaw;
        this.userType = "stu";
    }
    public void init(){
        this.userId = "1";
        this.userName = "TestXyy";
        this.userPaw = "1";
    }
}

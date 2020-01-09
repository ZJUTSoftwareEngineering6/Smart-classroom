package com.example.updownload.sdk_demo;

public class User {
    private String userName, userId;
    public final String create = "TestXyy";
    public User(){}
    public User(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }
    public void init(){
        this.userId = "001";
        this.userName = "TestXyy";
    }
}

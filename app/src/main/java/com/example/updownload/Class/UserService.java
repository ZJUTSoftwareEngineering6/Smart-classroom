package com.example.updownload.Class;

import com.example.updownload.LoginActivity;
import com.example.updownload.sdk_demo.User;
import com.example.updownload.sql.DatabaseHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class UserService {
    private DatabaseHelper dbHelper;
    private String s = null;

    /**
     * 查询数据库
     */
    public static int check(String id,String password) {
        String sql = "select * from login where id='"+id+"' and pwd='"+password+"'" ;
        try (Connection conn = DatabaseHelper.getSQLConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            if(rs.next()) {
                LoginActivity.curUser.userId = id;
                LoginActivity.curUser.userPaw = password;
                if(rs.getString("username") != null)
                    LoginActivity.curUser.userName = rs.getString("username");
                if(rs.getString("userStatus") != null)
                    LoginActivity.curUser.userType = rs.getString("userStatus");
                //如果是老师
                if(rs.getString("userStatus").trim().equals("教师"))
                    return 1;
                //如果是学生
                else return 2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public List<String> getList() {
        List<String> listdata = new ArrayList<String>();
        String sql = "select * from login where userStatus = '学生'";
        try (Connection conn = DatabaseHelper.getSQLConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Student student = new Student();
                student.setUsername(rs.getString("username").trim());
                student.setUserCondition(rs.getString("userCondition").trim());
                String text = student.getUsername()+"  "+student.getUserCondition();
                listdata.add(text);
            }
            return listdata;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update(String name){
        String sql = "update login set userCondition = '已签到' where username = '" + name + "'";
        try {
            Connection conn = DatabaseHelper.getSQLConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

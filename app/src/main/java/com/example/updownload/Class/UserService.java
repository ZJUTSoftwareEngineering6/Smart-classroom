package com.example.updownload.Class;

import com.example.updownload.LoginActivity;
import com.example.updownload.sql.DatabaseHelper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class UserService {
    private DatabaseHelper dbHelper;
    private String s = null;

    /**
     * 查询数据库
     */
    public static boolean check(String name,String password) {

        try {
            Connection conn = DatabaseHelper.getSQLConnection();
            String sql = "select * from login where id='"+name+"' and pwd='"+password+"'" ;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                LoginActivity.curUser.userId = name;
                LoginActivity.curUser.userPaw = password;
                if(rs.getString("name") != null)
                    LoginActivity.curUser.userName = rs.getString("name");
                if(rs.getString("type") != null)
                    LoginActivity.curUser.userType = rs.getString("type");

                rs.close();
                stmt.close();
                conn.close();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

}

package com.example.updownload.sql;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static String IP = "jiangxjcn.xyz";
    private static String DBName = "kxl";
    private static String USER = "jiangxj";
    private static String PWD = "Jiangxj201706061813";

    /**
     * 创建数据库对象
     */
    public static Connection getSQLConnection() {
        Connection con = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //加上 useunicode=true;characterEncoding=UTF-8 防止中文乱码
            con = DriverManager.getConnection("jdbc:jtds:sqlserver://" + IP + ":1433/" + DBName + ";useunicode=true;characterEncoding=UTF-8", USER, PWD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }


}
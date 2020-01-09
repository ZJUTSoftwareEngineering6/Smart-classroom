package com.example.updownload.Util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RequestListUtils {
    //请求服务器文件夹文件目录
    public List<String> requestList(String txt,List<String> listdata){
        String baseUrl = "http://47.101.150.40:80/SoftwareDesign/ListServlet.do";
        String[] fileName = new String[10];    //请求服务器文件夹下的文件名目录数组
        try {
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            String context = String.valueOf(txt);
            dos.writeBytes(context);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                InputStreamReader isr = new InputStreamReader(urlConn.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(isr);
                String readLine = bufferedReader.readLine();
                String [] a = readLine.split("\\ ");
                listdata.clear();
                for(int i=0;i<a.length;i++){
                    fileName[i] = a[i];
                    listdata.add(fileName[i]);
                }

            } else {
                System.out.println("出错！ ");
            }
            // 关闭连接
            urlConn.disconnect();
            return listdata;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

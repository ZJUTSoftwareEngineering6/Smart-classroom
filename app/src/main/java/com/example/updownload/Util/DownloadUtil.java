package com.example.updownload.Util;

import android.os.Looper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DownloadUtil {
    public boolean download(final String path, final String FileName) {
        try {
            URL url = new URL(getUtf8Url(path));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setReadTimeout(50000);
            con.setConnectTimeout(50000);
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 200) {
                InputStream is = con.getInputStream();//获取输入流
                FileOutputStream fileOutputStream = null;//文件输出流
                if (is != null) {
                    FileUtils fileUtils = new FileUtils();
                    fileOutputStream = new FileOutputStream(fileUtils.createFile(FileName));//指定文件保存路径
                    byte[] buf = new byte[1024];
                    int ch;
                    while ((ch = is.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                    }
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  false;
        }
        return  false;
    }

    //将url转化为utf-8字符，解决url带中文tomcat无法编码问题
    private static String getUtf8Url(String url) {
        char[] chars = url.toCharArray();
        StringBuilder utf8Url = new StringBuilder();
        final int charCount = chars.length;
        for (int i = 0; i < charCount; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 1) {
                utf8Url.append(chars[i]);
            }else{
                try {
                    utf8Url.append(URLEncoder.encode(String.valueOf(chars[i]), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return utf8Url.toString();
    }

}

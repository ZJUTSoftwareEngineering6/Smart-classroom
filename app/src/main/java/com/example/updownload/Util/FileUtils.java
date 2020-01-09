package com.example.updownload.Util;


import android.os.Environment;

import java.io.File;

public class FileUtils {
    private String path = Environment.getExternalStorageDirectory().toString() + "/image";

    public FileUtils() {
        File file = new File(path);
        /*
         *如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //创建文件
    public File createFile(String FileName) {
        return new File(path, FileName);
    }
}
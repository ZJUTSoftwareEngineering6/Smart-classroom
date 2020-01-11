package com.example.updownload.scan;

import android.app.Application;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class ZXingApplication extends Application {
    //onCreate方法
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ZXING包
        ZXingLibrary.initDisplayOpinion(this);
    }
}

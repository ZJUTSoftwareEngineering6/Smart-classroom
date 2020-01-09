package com.example.shangchuan;
import android.content.Intent;
import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
}


//    传输数据至服务器端
//    private EditText name;
//    private EditText age;
//    private Button button;
//
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            // TODO Auto-generated method stub
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0x123://成功上传数据提示
//                    Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
//                    break;
//                case 0x234://未成功上传提示
//                    Toast.makeText(MainActivity.this, "fail", Toast.LENGTH_LONG).show();
//                    break;
//                case 0x345://错误提示
//                    Toast.makeText(MainActivity.this, "fail and error", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //根据id得到控件
//        name = (EditText) findViewById(R.id.name);
//        age = (EditText) findViewById(R.id.age);
//        button = (Button) findViewById(R.id.button);
//        //给button设置监听事件
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //开启线程
//                new Thread(mRunnable).start();
//            }
//        });
//    }
//    public Runnable mRunnable = new Runnable() {
//
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//            //给线程创建消息循环
//            Looper.prepare();
//            //获得输入的name和age的值
//            String nameText = name.getText().toString();
//            String ageText = age.getText().toString();
//            try {
//                boolean result=false;
//                //调用UserInformationService的save（）方法并传入所获得的name和age的值
//                result = UserInformationService.save(nameText, ageText);
//                //发送空消息进行Toast提示成功与否
//                if (result) {
//                    handler.sendEmptyMessage(0x123);
//                } else {
//                    handler.sendEmptyMessage(0x234);
//                }
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//                handler.sendEmptyMessage(0x345);
//            }
//        }
//    };


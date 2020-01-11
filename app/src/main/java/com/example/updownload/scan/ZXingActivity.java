package com.example.updownload.scan;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.updownload.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class ZXingActivity extends AppCompatActivity {

    //定义一个全局的静态常量
    private static final int REQUEST_CODE = 001;

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui);

        //运动时权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        //找控件
        TextView tvSys = (TextView) findViewById(R.id.tvSys);
        //点击事件
        tvSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用系统相机功能，就是跳转到摄像头的界面
                Intent intent = new Intent(ZXingActivity.this, CaptureActivity.class);
                //用此方法跳转的原因是：为了回调下面onActivityResult的方法
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

     */

    private Button btn_cs,mBtnPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_ui);

        //mBtnPhoto = findViewById(R.id.btn_photo);
        /*mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ZXingActivity.this,PictureActivity.class);
                startActivity(intent);
            }
        });*/

        //隐藏系统默认的标题
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        //初始化相机权限
        ZXingLibrary.initDisplayOpinion(this);
        TextView tvSys = (TextView) findViewById(R.id.tvSys);
        tvSys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断手机版本是否在6.0以上，如果在6.0以上则需要动态申请权限
                if (Build.VERSION.SDK_INT > 22) {
                    if (ContextCompat.checkSelfPermission(ZXingActivity.this,
                            android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        //先判断有没有权限 ，没有就在这里进行权限的申请
                        ActivityCompat.requestPermissions(ZXingActivity.this,
                                new String[]{android.Manifest.permission.CAMERA}, 1);
                    } else {
                        //说明已经获取到摄像头权限了 想干嘛干嘛
                        Intent intent = new Intent(ZXingActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, 1);
                    }
                } else {
                    //这个说明系统版本在6.0之下，不需要动态获取权限。
                    Intent intent = new Intent(ZXingActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });
    }
    //获取手机相机权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(ZXingActivity.this, "请打开相机权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //扫描回传值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                //这是拿到解析扫描到的信息，并转成字符串
                String result = bundle.getString(CodeUtils.RESULT_STRING);

                Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                //解析扫到的二维码后就跳转页面
                Intent intent = new Intent(ZXingActivity.this, ZXingDealActivity.class);
                //把扫到并解析到的信息(既:字符串)带到详情页面
                intent.putExtra("path", result);
                startActivity(intent);
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                //否则土司解析二维码失败
                Toast.makeText(this, "解析二维码失败:", Toast.LENGTH_LONG).show();
            }
        }

    }
}

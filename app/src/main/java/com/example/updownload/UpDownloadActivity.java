package com.example.updownload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;

import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.updownload.Util.DownloadUtil;
import com.example.updownload.Util.FileChooseUtil;
import com.example.updownload.Util.RequestListUtils;
import com.example.updownload.Util.UploadUtil;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class UpDownloadActivity extends AppCompatActivity {
    TextView tv;
    ListView lv;
    List<String> listdata = new ArrayList<String>();   //保存文件名目录数组的链表

    //权限
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog alertDialog;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局
        setContentView(R.layout.updownload);
        myRequestPermission();
        final Button upload = (Button) findViewById(R.id.btn1);
        final Button download = (Button) findViewById(R.id.btn2);
        final Button search = (Button) findViewById(R.id.btn3);
        final EditText et = (EditText) findViewById(R.id.et);
        final RadioButton home = (RadioButton)findViewById(R.id.main_home);
        final RadioButton course = (RadioButton)findViewById(R.id.main_course);
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.list_view);

        //主页跳转
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpDownloadActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        //签到页跳转

        //课表页跳转
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpDownloadActivity.this,CourseActivity.class);
                startActivity(intent);
            }
        });

        //按钮点击事件
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = et.getText().toString();
                final String targetPath = "http://47.101.150.40:80/image/"+file;
                int i = targetPath.lastIndexOf("/");
                final String FileName= targetPath.substring(i + 1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DownloadUtil downloadUtil = new DownloadUtil();
                        if(downloadUtil.download(targetPath,FileName)) {
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(), "下载成功", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                        else{
                            Looper.prepare();
                            Toast.makeText(getApplicationContext(),"下载失败",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                    }
                }).start();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RequestListUtils requestListUtils = new RequestListUtils();
                        listdata = requestListUtils.requestList("请求服务器文件夹目录！",listdata);
                    }
                }).start();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.textview,listdata);
                arrayAdapter.notifyDataSetChanged();
                lv.setAdapter(arrayAdapter);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==NOT_NOTICE) {
            myRequestPermission();//由于不知道是否选择了允许所以需要再次判断
        }
        final String path;
        if(resultCode==RESULT_OK) {
            Uri uri = data.getData();
            path = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
            int i = path.lastIndexOf("/");
            final String FileName= path.substring(i + 1);
            tv.setText(FileName);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    UploadUtil uploadUtil = new UploadUtil();
                    if(uploadUtil.upload(path)){
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            });
            thread.start();
        }
    }

    //安卓版本6后需要动态获取权限
    private void myRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
//            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])){//用户选择了禁止不再询问

                        AlertDialog.Builder builder = new AlertDialog.Builder(UpDownloadActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (mDialog != null && mDialog.isShowing()) {
                                            mDialog.dismiss();
                                        }
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                                        intent.setData(uri);
                                        startActivityForResult(intent, NOT_NOTICE);
                                    }
                                });
                        mDialog = builder.create();
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                    }else {//选择禁止
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpDownloadActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(UpDownloadActivity.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }
                                });
                        alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.show();
                    }

                }
            }
        }
    }


}

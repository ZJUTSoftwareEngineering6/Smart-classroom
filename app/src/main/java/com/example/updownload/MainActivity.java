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
import android.widget.TextView;
import android.widget.Toast;

import com.example.updownload.Util.DownloadUtil;
import com.example.updownload.Util.FileChooseUtil;
import com.example.updownload.Util.UploadUtil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class MainActivity extends AppCompatActivity {
    TextView tv;
    ListView lv;
    String[] fileName = new String[10];    //请求服务器文件夹下的文件名目录数组
    final List<String> listdata = new ArrayList<String>();   //保存文件名目录数组的链表

    //权限
    private static final int NOT_NOTICE = 2;//如果勾选了不再询问
    private AlertDialog alertDialog;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局
        setContentView(R.layout.activity_main);
        myRequestPermission();
        final Button upload = (Button) findViewById(R.id.btn1);
        final Button download = (Button) findViewById(R.id.btn2);
        final Button search = (Button) findViewById(R.id.btn3);
        final EditText et = (EditText) findViewById(R.id.et);
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.list_view);

        //点击事件
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
                final String targetPath = "http://47.100.139.52:8080/image/"+file;
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
                        requestPost("请求服务器文件夹目录！");
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

    public void requestPost(String txt){
        String baseUrl = "http://47.100.139.52:8080/demo_war/Servlet.do";
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
//                    System.out.println(fileName[i]);
                }

            } else {
                System.out.println("出错！ ");
            }
            // 关闭连接
            urlConn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //动态获取权限
    private void myRequestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }else {
            Toast.makeText(this,"您已经申请了权限!",Toast.LENGTH_SHORT).show();
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("permission")
                                .setMessage("点击允许才可以使用我们的app哦")
                                .setPositiveButton("去允许", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (alertDialog != null && alertDialog.isShowing()) {
                                            alertDialog.dismiss();
                                        }
                                        ActivityCompat.requestPermissions(MainActivity.this,
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

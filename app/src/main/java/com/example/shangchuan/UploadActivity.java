package com.example.shangchuan;

import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button upload = (Button) findViewById(R.id.btn1);
        tv = (TextView) findViewById(R.id.tv);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//无类型限制
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String path;
        if(resultCode==RESULT_OK) {
            Uri uri = data.getData();
            path = FileChooseUtil.getInstance(this).getChooseFileResultPath(uri);
            System.out.println(path);
            System.out.println("哈哈");
            tv.setText(path);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Upload a = new Upload();
                    a.upload(path);
                }
            });
            thread.start();
        }
    }
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            //布局
//            setContentView(R.layout.upload);
//            Button upload = (Button) findViewById(R.id.btn1);
//            Button download = (Button) findViewById(R.id.btn2);
//            tv = (TextView) findViewById(R.id.tv);
//
//            upload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                    intent.setType("*/*");//无类型限制
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    startActivityForResult(intent, 1);
//                }
//            });
//            final String targetPath = "http://47.100.139.52:8080/image/picture.jpg";
//            int i = targetPath.lastIndexOf("/");
//            final String FileName= targetPath.substring(i + 1);
//            download.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            download(targetPath,FileName);
//                        }
//                    }).start();
//                }
//            });
//        }
//        String path;
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = data.getData();
//                if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
//                    path = uri.getPath();
//                    tv.setText(path);
//                    Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
//                    path = getPath(this, uri);
//                    tv.setText(path);
//                    Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
//                } else {//4.4以下下系统调用方法
//                    path = getRealPathFromURI(uri);
//                    tv.setText(path);
    //                    Toast.makeText(this,"上传成功",Toast.LENGTH_SHORT).show();
//                }
//            }
//            System.out.println("哈哈哈");
//            System.out.println(path);
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    upload(path);
//                }
//            });
//            thread.start();
//        }


        public void upload(String newpath){
            try {
                String actionUrl = "http://47.100.139.52:8080/android_war/Servlet.do";
                Map<String, String> params = new HashMap<String, String>();
                String fileName = "";
                params.put("name", "");
                params.put("type", "");

                int start = newpath.lastIndexOf("/");
                fileName = newpath.substring(start+1);

                Map<String, File> files = new HashMap<>();
                files.put(fileName, new File(newpath));

                String BOUNDARY = java.util.UUID.randomUUID().toString();
                String PREFIX = "--", LINEND = "\r\n";
                String MULTIPART_FROM_DATA = "multipart/form-data";
                String CHARSET = "UTF-8";
                URL uri = new URL(actionUrl);
                HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
                conn.setReadTimeout(5 * 1000); // 缓存的最长时间
                conn.setDoInput(true);// 允许输入
                conn.setDoOutput(true);// 允许输出
                conn.setUseCaches(false); // 不允许使用缓存
                conn.setRequestMethod("POST");
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("charsert", "UTF-8");
                conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA+ ";boundary=" + BOUNDARY);
                // 首先组拼文本类型的参数
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"" + LINEND);
                    sb.append("Content-Type: text/plain; charset=" + CHARSET+ LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue());
                    sb.append(LINEND);
                }
                DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
                outStream.write(sb.toString().getBytes());
                // 发送文件数据
                if (files != null)
                    for (Map.Entry<String, File> file : files.entrySet()) {
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append(PREFIX);
                        sb1.append(BOUNDARY);
                        sb1.append(LINEND);
                        sb1.append("Content-Disposition: form-data; name=\"j5eQkZqZlpOa\"; filename=\""+ file.getKey() + "\"" + LINEND);
                        sb1.append("Content-Type: application/octet-stream; charset="+ CHARSET + LINEND);
                        sb1.append(LINEND);
                        outStream.write(sb1.toString().getBytes());
                        InputStream is = new FileInputStream(file.getValue());
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }
                        is.close();
                        outStream.write(LINEND.getBytes());
                    }


                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();


                // 得到响应*
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream in = conn.getInputStream();
                    int ch;
                    StringBuilder sb2 = new StringBuilder();
                    while ((ch = in.read()) != -1) {
                        sb2.append((char) ch);
                    }
                }

                BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while (reader.read()!=-1) {
                    System.out.println(reader.readLine());
                }

                outStream.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//
//        public String getRealPathFromURI(Uri contentUri) {
//            String res = null;
//            String[] proj = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
//            if(null!=cursor&&cursor.moveToFirst()){;
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                res = cursor.getString(column_index);
//                cursor.close();
//            }
//            return res;
//        }
//
//        /**
//         * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
//         */
//        @SuppressLint("NewApi")
//        public String getPath(final Context context, final Uri uri) {
//
//            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
//
//            // DocumentProvider
//            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//                // ExternalStorageProvider
//                if (isExternalStorageDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    if ("primary".equalsIgnoreCase(type)) {
//                        return Environment.getExternalStorageDirectory() + "/" + split[1];
//                    }
//                }
//                // DownloadsProvider
//                else if (isDownloadsDocument(uri)) {
//
//                    final String id = DocumentsContract.getDocumentId(uri);
//                    final Uri contentUri = ContentUris.withAppendedId(
//                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
//
//                    return getDataColumn(context, contentUri, null, null);
//                }
//                // MediaProvider
//                else if (isMediaDocument(uri)) {
//                    final String docId = DocumentsContract.getDocumentId(uri);
//                    final String[] split = docId.split(":");
//                    final String type = split[0];
//
//                    Uri contentUri = null;
//                    if ("image".equals(type)) {
//                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("video".equals(type)) {
//                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
//                    } else if ("audio".equals(type)) {
//                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//                    }
//
//                    final String selection = "_id=?";
//                    final String[] selectionArgs = new String[]{split[1]};
//
//                    return getDataColumn(context, contentUri, selection, selectionArgs);
//                }
//            }
//            // MediaStore (and general)
//            else if ("content".equalsIgnoreCase(uri.getScheme())) {
//                return getDataColumn(context, uri, null, null);
//            }
//            // File
//            else if ("file".equalsIgnoreCase(uri.getScheme())) {
//                return uri.getPath();
//            }
//            return null;
//        }
//
//        public String getDataColumn(Context context, Uri uri, String selection,
//                                    String[] selectionArgs) {
//
//            Cursor cursor = null;
//            final String column = "_data";
//            final String[] projection = {column};
//
//            try {
//                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
//                        null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    final int column_index = cursor.getColumnIndexOrThrow(column);
//                    return cursor.getString(column_index);
//                }
//            } finally {
//                if (cursor != null)
//                    cursor.close();
//            }
//            return null;
//        }
//
//        public boolean isExternalStorageDocument(Uri uri) {
//            return "com.android.externalstorage.documents".equals(uri.getAuthority());
//        }
//
//        public boolean isDownloadsDocument(Uri uri) {
//            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
//        }
//
//        public boolean isMediaDocument(Uri uri) {
//            return "com.android.providers.media.documents".equals(uri.getAuthority());
//        }
//
//        //下载
        public void download(final String path, final String FileName) {
            try {
                URL url = new URL(path);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
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
                    Looper.prepare();
                    Toast.makeText(this,"下载成功",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}


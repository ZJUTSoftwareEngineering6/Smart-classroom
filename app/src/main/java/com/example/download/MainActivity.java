package com.example.download;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    EditText et;
    ListView lv;
    String[] course = new String[10];
    final List<String> listdata = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        et = (EditText) findViewById(R.id.et);
        lv = (ListView) findViewById(R.id.list_view);
        Button bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        requestPost("请求服务器文件夹目录！");
                    }
                }).start();
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.item,listdata);//listdata和str均可
//                arrayAdapter.notifyDataSetChanged();
//                lv.setAdapter(arrayAdapter);
            }
        });

    }

    public void requestPost(String txt){
        String baseUrl = "http://47.100.139.52:8080/demo_war/Servlet2.do";
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
                String str = bufferedReader.readLine();
                String[] a =  str.split("\\ ");
                for(int i=0;i<a.length;i++){
                    System.out.println(a[i]);
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

}

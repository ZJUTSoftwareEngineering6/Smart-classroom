package com.example.coursetable;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private int flag = 1;

    private int length;
    private String[] course = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_main);
        Button button = findViewById(R.id.main_course);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestPost("请求服务器发送课表数据");
            }
        }).start();
        tmpLayout = (RelativeLayout) findViewById(R.id.Monday);
    }

    //在Activity加载后获取组件的高度和宽度；
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(isFirst) {
//            isFirst = false;
            gridWidth = tmpLayout.getWidth();
            gridHeight = tmpLayout.getHeight()/12;
//        }
    }

    private TextView createTv(int start, int end, String text){
        TextView tv = new TextView(this);

        //设定高度和宽度
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridWidth,gridHeight*(end-start+1));
        //设定位置
        tv.setY(gridHeight*(start-1));
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        tv.setText(text);
        return tv;
    }

    private void addView(int day,int start,int end,String text){
        TextView tv;
        switch (day){
            case 1:
                layout = (RelativeLayout) findViewById(R.id.Monday);
                break;
            case 2:
                layout = (RelativeLayout) findViewById(R.id.Tuesday);
                break;
            case 3:
                layout = (RelativeLayout) findViewById(R.id.Wednesday);
                break;
            case 4:
                layout = (RelativeLayout) findViewById(R.id.Thursday);
                break;
            case 5:
                layout = (RelativeLayout) findViewById(R.id.Friday);
                break;
            case 6:
                layout = (RelativeLayout) findViewById(R.id.Saturday);
                break;
            case 7:
                layout = (RelativeLayout) findViewById(R.id.Sunday);
                break;
        }
        tv = createTv(start,end,text);
        Random random = new Random();
        int red = random.nextInt(255);
        int green = random.nextInt(255);
        tv.setBackgroundColor(Color.argb(100,red,green,0));
        layout.addView(tv);
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(flag>0) {
            flag--;
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                for (int i = 0; i < length; i++) {
                    int day = checkDay(course[i]);
                    int startTime = Integer.parseInt(String.valueOf(course[i].charAt(2)));
                    int endTime = Integer.parseInt(String.valueOf(course[i].charAt(3)));
                    String courseName = course[i].substring(4);
                    if (course[i].indexOf("10") != -1) {
                        startTime = 10;
                        courseName = course[i].substring(6);
                        if (course[i].indexOf("11") != -1)
                            endTime = 11;
                        else if (course[i].indexOf("12") != -1)
                            endTime = 12;
                        else
                            Toast.makeText(this, "课表错误！", Toast.LENGTH_SHORT).show();
                    }
                    if (endTime - startTime < 0)
                        Toast.makeText(this, "课表错误！", Toast.LENGTH_SHORT).show();
                    addView(day, startTime, endTime, courseName);
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void requestPost(String txt){
        String baseUrl = "http://47.101.150.40:80/SoftwareDesign/CourseServlet.do";
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
                course = a;
                length = a.length;
            } else {
                System.out.println("出错！ ");
            }
            // 关闭连接
            urlConn.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int checkDay(String str){
        if((str.indexOf("周一")!=-1)) return 1;
        else if(str.indexOf("周二")!=-1) return 2;
        else if(str.indexOf("周三")!=-1) return 3;
        else if(str.indexOf("周四")!=-1) return 4;
        else if(str.indexOf("周五")!=-1) return 5;
        else if(str.indexOf("周六")!=-1) return 6;
        else if(str.indexOf("周七")!=-1) return 7;
        else return 0;
    }


}

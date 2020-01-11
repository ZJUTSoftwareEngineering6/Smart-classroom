package com.example.updownload;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import com.example.updownload.sdk_demo.MainFaceSignInActivity;
import com.example.updownload.sdk_demo.MineActivity;

public class CourseActivity extends AppCompatActivity {
    private int gridHeight,gridWidth;
    private RelativeLayout layout;
    private RelativeLayout tmpLayout;
    private static boolean isFirst = true;
    private int isFlag = 1;

    private int length;
    private String[] course = new String[100];

    private String notificationId = "channelId";
    private String notificationName = "channelName";
    private String notificationId2 = "channelId2";
    private String notificationName2 = "channelName2";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        tmpLayout = (RelativeLayout) findViewById(R.id.Monday);
        new Thread(new Runnable() {
            @Override
            public void run() {
                requestPost(LoginActivity.curUser.userId + "_请求服务器发送课表数据");
            }
        }).start();

        //首页跳转
        RadioButton home = findViewById(R.id.main_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //上传页跳转
        RadioButton up = findViewById(R.id.main_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, UpDownloadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //签到跳转
        RadioButton signIn = findViewById(R.id.main_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, MainFaceSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //我的页跳转
        RadioButton mine = findViewById(R.id.main_mine);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseActivity.this, MineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        remindCourse();

    }

    //在Activity加载后获取组件的高度和宽度；
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if(isFirst) {
        isFirst = false;
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
    protected void onRestart() {
        isFlag = 1;
        super.onRestart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(isFlag>0) {
            isFlag--;
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
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
                    NotificationChannel notificationChannel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_MIN);
                    manager.createNotificationChannel(notificationChannel);
                }
                Notification notification=getTodayCourse();
                manager.notify(1, notification);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Notification getTodayCourse() {
        int flag = 0;
        String week =" ";
        Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        switch (rightNow.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                week = "周日";
                break;
            case Calendar.MONDAY:
                week = "周一";
                break;
            case Calendar.TUESDAY:
                week = "周二";
                break;
            case Calendar.WEDNESDAY:
                week = "周三";
                break;
            case Calendar.THURSDAY:
                week = "周四";
                break;
            case Calendar.FRIDAY:
                week = "周五";
                break;
            case Calendar.SATURDAY:
                week = "周六";
                break;
            default:
                break;
        }

        Intent in = new Intent(CourseActivity.this, CourseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, in, 0);
        Notification notify;
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();//向通知栏添加课表
        style.setBigContentTitle("今日课表");
        /*course[0] = "周一第12节c#程序设计";//测试
        course[1] = "周一第34节Linux操作系统";//测试
         */
        for (int i = 0; i <length; i++) {
            if (course[i].indexOf(week) != -1) {
                style.addLine(course[i]);
                flag++;
            }
        }
        //如果今天没课
        if (flag == 0) {
            notify = new NotificationCompat.Builder(getApplication())
                    .setSmallIcon(R.mipmap.timetable)
                    .setContentTitle("今日无课")
                    .setContentIntent(pendingIntent)//设置后可以通过通知栏进入课表
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .build();
        }
        //今天有课
        else {
            style.setSummaryText("以上为今日课表");
            notify = new NotificationCompat.Builder(getApplication(), notificationId)
                    .setSmallIcon(R.mipmap.timetable)
                    .setContentTitle("今日课表")
                    .setStyle(style)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .build();
        }
        return notify;
    }
    /*  private boolean checkTime() {
          String week =" ";
          Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
          switch (rightNow.get(Calendar.DAY_OF_WEEK)) {
              case Calendar.SUNDAY:
                  week = "周日";
                  break;
              case Calendar.MONDAY:
                  week = "周一";
                  break;
              case Calendar.TUESDAY:
                  week = "周二";
                  break;
              case Calendar.WEDNESDAY:
                  week = "周三";
                  break;
              case Calendar.THURSDAY:
                  week = "周四";
                  break;
              case Calendar.FRIDAY:
                  week = "周五";
                  break;
              case Calendar.SATURDAY:
                  week = "周六";
                  break;
              default:
                  break;
          }
          Vector<String> course_temp=new Vector<>();
          for (int i = 0; i < 2length; i++) {
              if (course[i].indexOf("周一"week) != -1) {

              }
          }
          if(course_temp.size()==0)
              return false;
          else{
              int hour=rightNow.get(Calendar.HOUR_OF_DAY);
              int minute=rightNow.get(Calendar.MINUTE);
              for (int i=0;i<course_temp.size();i++){
              switch (checkOrder(course_temp.get(i))) {
                  case 1:
                      if(hour==0&&minute==59)
                          return true;
                  case 2:
                      if(hour==9&&minute==30)
                          return true;
                  case 3:
                      if(hour==13&&minute==0)
                          return true;
                  case 4:
                      if(hour==15&&minute==0)
                          return true;
                  case 5:
                      if(hour==18&&minute==0)
                          return true;
                  default:
                      return false;
              }
  }
          }
          return false;
      }
      public int checkOrder(String str) {
          if ((str.indexOf("12") != -1)) return 1;
          else if (str.indexOf("34") != -1) return 2;
          else if (str.indexOf("67") != -1) return 3;
          else if (str.indexOf("89") != -1) return 4;
          else if (str.indexOf("1011") != -1) return 5;
          else return 0;
      }*/
    private void remindCourse(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update_time_action");
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("update_time_action")) {
                    Calendar rightNow = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
                    int hour=rightNow.get(Calendar.HOUR_OF_DAY);
                    int minute=rightNow.get(Calendar.MINUTE);
                    if((hour==7&&minute==30)||(hour==9&&minute==30)||(hour==13&&minute==0)||(hour==15&&minute==0||(hour==18&&minute==0))){
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            //只在Android O之上需要渠道，这里的第一个参数要和下面的channelId一样
                            NotificationChannel notificationChannel = new NotificationChannel(notificationId2, notificationName2, NotificationManager.IMPORTANCE_MIN);
                            manager.createNotificationChannel(notificationChannel);
                        }
                        Notification notify=new NotificationCompat.Builder(getApplication(), notificationId)
                                .setSmallIcon(R.mipmap.timetable)
                                .setContentTitle("上课时间到了")
                                .setAutoCancel(false)
                                .build();
                        manager.notify(2,  notify);
                    }
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
                    long nextTime = getNextTime();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        am.setExact(AlarmManager.RTC_WAKEUP, nextTime, pi);
                    } else {
                        am.set(AlarmManager.RTC_WAKEUP, nextTime, pi);
                    }
                }
            }
        }, intentFilter);
        sendBroadcast(new Intent("update_time_action"));
    }
    private long getNextTime() {
        long now = System.currentTimeMillis();
        return now + 60 * 1000 - now % (60 * 1000);
    }
}

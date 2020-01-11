package com.example.updownload;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.updownload.classroom.MainClassroomActivity;
import com.example.updownload.music.MusicActivity;
import com.example.updownload.qrcode.QrcodeActivity;
import com.example.updownload.sdk_demo.MainFaceSignInActivity;
import com.example.updownload.sdk_demo.MineActivity;
import com.example.updownload.weather.TemperatureShowActivity;
import com.example.updownload.scan.CameraScanningActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeActivity extends AppCompatActivity {
    public static Handler handler;
    private GridView gridView;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter simpleAdapter;
    //图片封装为数组
    private int[] icon = {R.drawable._1,R.drawable._2,R.drawable._3,R.drawable._1,R.drawable._2,R.drawable._3};
    private String[] iconName = {"备忘录","教室情况","天气查询","语音屋","发二维码","添加功能"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gridView = (GridView)findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        String [] from = {"image","text"};
        int [] to = {R.id.image,R.id.text};
        simpleAdapter = new SimpleAdapter(this,data_list,R.layout.item,from,to);
        gridView.setAdapter(simpleAdapter);
        //主页功能版跳转
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                switch(position){
                    case 0://备忘录页跳转
                    {
                        Intent intent = new Intent(HomeActivity.this, CourseActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    }
                    case 1://教室情况
                    {
                        Intent intent = new Intent(HomeActivity.this, MainClassroomActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    }
                    case 2://查看当前天气
                    {
                        Intent intent = new Intent(HomeActivity.this, TemperatureShowActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    }
                    case 3://语言播报页跳转
                    {
                        Intent intent = new Intent(HomeActivity.this, MusicActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    }
                    case 4://发布二维码跳转
                    {
                        Intent intent = new Intent(HomeActivity.this, QrcodeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                        break;
                    }
                    default:break;
                }
            }
        });

        //课表页跳转
        RadioButton course = findViewById(R.id.main_course);
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CourseActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //上传页跳转
        RadioButton up = findViewById(R.id.main_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UpDownloadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //签到跳转
        RadioButton signIn = findViewById(R.id.main_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainFaceSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        //我的页跳转
        RadioButton mine = findViewById(R.id.main_mine);
        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MineActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        //扫一扫
        Button ZXing = findViewById(R.id.sao_yi_sao);
        ZXing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CameraScanningActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    public List<Map<String,Object>> getData(){
        for(int i=0;i<icon.length;i++){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            data_list.add(map);
        }
        return data_list;
    }
}

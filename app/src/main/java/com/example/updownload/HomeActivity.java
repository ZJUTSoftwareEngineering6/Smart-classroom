package com.example.updownload;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.updownload.sdk_demo.MainActivity;
import com.example.updownload.sdk_demo.PermissionAcitivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private GridView gridView;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter simpleAdapter;
    //图片封装为数组
    private int[] icon = {R.drawable._1,R.drawable._2,R.drawable._3,R.drawable._1,R.drawable._2,R.drawable._3,R.drawable._1,R.drawable._2,R.drawable._3,R.drawable._1,R.drawable._2,R.drawable._3};
    private String[] iconName = {"功能","功能","功能","功能","功能","功能","功能","功能","功能","功能","功能","功能"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_main);
        gridView = (GridView)findViewById(R.id.gridView);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        String [] from = {"image","text"};
        int [] to = {R.id.image,R.id.text};
        simpleAdapter = new SimpleAdapter(this,data_list,R.layout.item,from,to);
        gridView.setAdapter(simpleAdapter);
        //上传文件跳转
        RadioButton up = findViewById(R.id.main_up);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UpDownloadActivity.class);
                startActivity(intent);
            }
        });
        //课表跳转
        RadioButton course = findViewById(R.id.main_course);
        course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,CourseActivity.class);
                startActivity(intent);
            }
        });
        //签到跳转
        RadioButton signIn = findViewById(R.id.main_signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
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

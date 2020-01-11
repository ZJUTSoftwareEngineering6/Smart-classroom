package com.example.updownload;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.updownload.Class.Student;
import com.example.updownload.Class.UserService;
import com.example.updownload.sdk_demo.User;

import java.util.ArrayList;
import java.util.List;

public class TeacherActivity extends AppCompatActivity {
    ListView lv;
    List<String> listdata = new ArrayList<String>();
    private UserService uService = new UserService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        //查看签到情况
        Button search = findViewById(R.id.search);
        lv = findViewById(R.id.sign_list);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listdata = uService.getList();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.textview, listdata);
                arrayAdapter.notifyDataSetChanged();
                lv.setAdapter(arrayAdapter);
            }
        });
        //修改签到
        Button update = findViewById(R.id.update);
        final EditText editText = findViewById(R.id.sign);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().trim() != null) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            uService.update(editText.getText().toString().trim());
                        }
                    }).start();
                }
//                listdata = uService.getList();
//                ArrayAdapter<Student> arrayAdapter = new ArrayAdapter<Student>(getApplicationContext(),R.layout.textview,listdata);
//                arrayAdapter.notifyDataSetChanged();
//                lv.setAdapter(arrayAdapter);
            }
        });
    }

}

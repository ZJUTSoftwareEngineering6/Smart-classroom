package com.example.updownload.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.updownload.R;

import java.util.Timer;
import java.util.TimerTask;

public class SignActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etInputTime;
    private Button btnGet, btnStop;
    private TextView tvTime;
    private int i = 0;
    private Timer timer = null;
    private TimerTask task = null;

    private ImageView imageView = null;
    private boolean flag = true;
    private boolean temp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        initView();
    }

    private void initView() {
        etInputTime = findViewById(R.id.et_input);
        btnGet = findViewById(R.id.btn_get_time);
        btnStop = findViewById(R.id.btn_stop_time);
        tvTime = findViewById(R.id.tv_time);
        btnGet.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        imageView = findViewById(R.id.iv_photo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_time:
                tvTime.setText(etInputTime.getText().toString());
                i = Integer.parseInt(etInputTime.getText().toString());
                startTime();

                if(flag){
                    imageView.setVisibility(View.VISIBLE);
                    tvTime.setVisibility(View.VISIBLE);
                    flag = false;
                }

                break;

            case R.id.btn_stop_time:
                if (temp) {
                    stopTime();
                    temp = false;
                } else {
                    temp = true;
                    startTime();
                }

            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            tvTime.setText(msg.arg1 + "");
            startTime();
        };
    };

    public void startTime() {
        timer = new Timer();
        task = new TimerTask() {

            @Override
            public void run() {
                if (i > 0) {   //加入判断不能小于0
                    i--;
                    Message message = mHandler.obtainMessage();
                    message.arg1 = i;
                    mHandler.sendMessage(message);
                }
//                if (i == 0) {
//                    Intent intent = new Intent(SignActivity.this,QrcodeActivity.class);
//                    startActivity(intent);
//                }
            }
        };
        timer.schedule(task, 1000);
        if (i == 0) {
            Intent intent = new Intent(SignActivity.this,QrcodeActivity.class);
            startActivity(intent);
        }
    }

    public void stopTime(){
        timer.cancel();
    }
}

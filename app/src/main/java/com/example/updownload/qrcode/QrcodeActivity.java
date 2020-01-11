package com.example.updownload.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.updownload.HomeActivity;
import com.example.updownload.R;

public class QrcodeActivity extends AppCompatActivity {

    private Button btnQrcode, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        btnQrcode = findViewById(R.id.btn_qrcode);
        btnBack = findViewById(R.id.btn_back);

        btnQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrcodeActivity.this,SignActivity.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QrcodeActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}

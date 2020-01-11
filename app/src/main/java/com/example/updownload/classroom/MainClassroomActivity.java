package com.example.updownload.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.updownload.R;

public class MainClassroomActivity extends AppCompatActivity {

    private Button btnLight, btnTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classroom);

        btnLight = findViewById(R.id.btn_light_control);
        btnTemperature = findViewById(R.id.btn_temperature_control);

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainClassroomActivity.this,LightControlActivity.class);
                startActivity(intent);
            }
        });

        btnTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainClassroomActivity.this, TemperatureControlActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.example.updownload.classroom;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.updownload.HomeActivity;
import com.example.updownload.R;
import com.example.updownload.Utils.FlashlightUtils;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class LightControlActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor ligthSensor;
    private StringBuffer stringBuffer1,stringBuffer2;
    private EditText etAccuracy,etLux;
    private Button btnExit;
    FlashlightUtils flashlight = new FlashlightUtils();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_control);

        stringBuffer1 = new StringBuffer();
        stringBuffer2 = new StringBuffer();
        etAccuracy = findViewById(R.id.et_accuracy);
        etLux = findViewById(R.id.et_lux);
        btnExit = findViewById(R.id.btn_exit);

        //获取SensorManager对象
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //获取Sensor对象
        ligthSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        sensorManager.registerListener((SensorEventListener) new MySensorListener(), ligthSensor, SensorManager.SENSOR_DELAY_NORMAL);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LightControlActivity.this, HomeActivity.class);
                flashlight.lightsOff();
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            }
        });
    }

    public class MySensorListener implements SensorEventListener {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void onSensorChanged(SensorEvent event) {
            //获取精度
            float acc = event.accuracy;
            //获取光线强度
            float lux = event.values[0];

            if (lux <= 5) {
                flashlight.lightsOn(LightControlActivity.this);
            } else {
                flashlight.lightsOff();
            }

            stringBuffer1.append("当前测光精度 ----> " + acc);
            //sb1.append("\n");
            stringBuffer2.append("当前光线强度 ----> " + lux);
            //sb2.append("\n");

            //tvValue.setText(sb.toString());
            etAccuracy.setText(stringBuffer1);
            etLux.setText(stringBuffer2);
            stringBuffer1.delete(0,stringBuffer1.length());
            stringBuffer2.delete(0,stringBuffer2.length());
        }

    }
}

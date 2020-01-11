package com.example.updownload.classroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.updownload.HomeActivity;
import com.example.updownload.R;
import com.example.updownload.Utils.GetString;
import com.example.updownload.Utils.StreamTool;
import com.example.updownload.Utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemperatureControlActivity extends Activity implements View.OnClickListener {


    private Button btnSearch;
    private Button btnControl;
    private Button btnReturn;
    private EditText edCity;
    private TextView tvCityResult1;
    private TextView tvCityResult2;
    private TextView tvCity;
    private TextView tvHighTemp,tvLowTemp,tvWeather;
    private TextView tvDate;
    private String highTemp="null",lowTemp="null",weather="null",week="null";

    GetString getString1 = new GetString();

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date();
    String s = simpleDateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_control);
        btnSearch=(Button) findViewById(R.id.btn_search);
        btnControl = findViewById(R.id.btn_control);
        btnReturn = findViewById(R.id.btn_return);
        btnSearch.setOnClickListener(this);


        btnControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (highTemp.equals("null") || lowTemp.equals("null")) {

                    ToastUtil.showMsg(getApplicationContext(),"请先查询天气信息");
                } else {
                    String string1 = getString1.GetTemp(highTemp);
                    String string2 = getString1.GetTemp(lowTemp);
                    int highTemp1 = Integer.parseInt(string1);
                    int lowTemp1 = Integer.parseInt(string2);

                    if (highTemp1 > 30 || highTemp1 == 30) {

                        Toast toastCustom = new Toast(getApplicationContext());
                        LayoutInflater inflater = LayoutInflater.from(TemperatureControlActivity.this);
                        View view = inflater.inflate(R.layout.layout_toast,null);
                        ImageView imageView = view.findViewById(R.id.iv_toast);
                        TextView textView = view.findViewById(R.id.tv_toast);
                        toastCustom.setGravity(Gravity.CENTER,0,0);
                        imageView.setImageResource(R.drawable.cold);
                        textView.setText("冷气开启中");
                        toastCustom.setView(view);
                        toastCustom.setDuration(Toast.LENGTH_SHORT);
                        toastCustom.show();

//                        ToastUtil.showMsg(getApplicationContext(),"打开冷气，调节室温");

                    } else if (lowTemp1 < 10 || lowTemp1 == 10) {

                        Toast toastCustom = new Toast(getApplicationContext());
                        LayoutInflater inflater = LayoutInflater.from(TemperatureControlActivity.this);
                        View view = inflater.inflate(R.layout.layout_toast,null);
                        ImageView imageView = view.findViewById(R.id.iv_toast);
                        TextView textView = view.findViewById(R.id.tv_toast);
                        toastCustom.setGravity(Gravity.CENTER,0,0);
                        imageView.setImageResource(R.drawable.hot);
                        textView.setText("暖气开启中");
                        toastCustom.setView(view);
                        toastCustom.setDuration(Toast.LENGTH_SHORT);
                        toastCustom.show();

//                        ToastUtil.showMsg(getApplicationContext(),"打开暖气，调节室温");

                    } else {
                        ToastUtil.showMsg(getApplicationContext(),"当前温度适宜");
                    }
                }
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemperatureControlActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        edCity=(EditText) findViewById(R.id.ed_city);
        tvCityResult1=(TextView) findViewById(R.id.city_result1);
        tvCityResult2=(TextView) findViewById(R.id.city_result2);

        tvHighTemp = findViewById(R.id.tv_high_temp);
        tvLowTemp = findViewById(R.id.tv_low_temp);
        tvWeather = findViewById(R.id.tv_weather_type);
        tvCity = findViewById(R.id.tv_city);
        tvDate = findViewById(R.id.tv_date);

        {//进来直接查询天气
            // TODO Auto-generated method stub
            city=edCity.getText().toString().trim();
//        if(TextUtils.isEmpty(city)){
//            Toast.makeText(Weather3Activity.this, "路径错误", Toast.LENGTH_SHORT).show();
//            return ;
//        }
            dialog=new ProgressDialog(this);
            dialog.setMessage("正在玩命加载中");
            dialog.show();
            //发起请求给那个网站
            new Thread(){
                public void run() {
                    try {
                        ul=PATH+ URLEncoder.encode(city,"UTF-8");

                        URL url=new URL(ul);

                        //设置必要的参数信息
                        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                        conn.setConnectTimeout(5000);
                        conn.setRequestMethod("GET");

                        //判断响应码
                        int code = conn.getResponseCode();
                        if(code==200){
                            //连接网络成功
                            InputStream in = conn.getInputStream();
                            String data = StreamTool.decodeStream(in);


                            //解析json格式的数据
                            JSONObject jsonObj=new JSONObject(data);
                            //获得desc的值
                            String result = jsonObj.getString("desc");
                            if("OK".equals(result)){
                                //城市有效，返回了需要的数据
                                JSONObject dataObj = jsonObj.getJSONObject("data");

                                JSONArray jsonArray = dataObj.getJSONArray("forecast");
                                //通知更新ui
                                Message msg = Message.obtain();
                                msg.obj=jsonArray;
                                msg.what=SUCCESS;
                                mhandler.sendMessage(msg);
                            }else{
                                //城市无效
                                Message msg=Message.obtain();
                                msg.what=INVALID_CITY;
                                mhandler.sendMessage(msg);
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Message msg = Message.obtain();
                        msg.what=ERROR;
                        mhandler.sendMessage(msg);
                    }
                };
            }.start();
        }
    }

    private final static String PATH="http://wthrcdn.etouch.cn/weather_mini?city=杭州";
    protected static final int SUCCESS = 0;
    protected static final int INVALID_CITY = 1;
    protected static final int ERROR = 2;
    private String city;
    String ul;

    private Handler mhandler=new Handler(){
        public void handleMessage(Message msg) {
            dialog.dismiss();
            switch (msg.what) {
                case SUCCESS:

                    JSONArray data=(JSONArray) msg.obj;
                    try {
                        String day01= data.getString(0);
//                        String day02= data.getString(1);
//                        city_result1.setText(day01);
//                        city_result2.setText(day02);

                        highTemp = getString1.HighTemp(day01);
                        lowTemp = getString1.LowTemp(day01);
                        weather = getString1.Weather(day01);
                        week = getString1.Week(day01);

                        int pos=0;
                        Integer lt = new Integer(0);
                        while(highTemp.charAt(pos) >= '0' && highTemp.charAt(pos) <= '9'){
                            lt = lt * 10 + (highTemp.charAt(pos) - '0');
                            pos++;
                        }
                        String tmp = highTemp.substring(pos);
                        pos = 0;
                        Integer ut = new Integer(0);
                        while(lowTemp.charAt(pos) >= '0' && lowTemp.charAt(pos) <= '9'){
                            ut = ut * 10 + (lowTemp.charAt(pos) - '0');
                            pos++;
                        }
                        tvCity.setText("当前教室：仁和108");
                        tvDate.setText("当前室温：" + (lt + ut) / 2 + tmp);
                        tvHighTemp.setText("室外最高温度：" + highTemp);
                        tvLowTemp.setText("室外最低温度：" + lowTemp);
                        tvWeather.setText("当前天气：" + weather);



                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;



                case INVALID_CITY:
                    Toast.makeText(TemperatureControlActivity.this, "城市无效", Toast.LENGTH_SHORT).show();
                    break;
                case ERROR:
                    Toast.makeText(TemperatureControlActivity.this, "网络无效", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        };
    };

    ProgressDialog dialog=null;
    public void onClick(View v) {
        // TODO Auto-generated method stub
        city=edCity.getText().toString().trim();
//        if(TextUtils.isEmpty(city)){
//            Toast.makeText(Weather3Activity.this, "路径错误", Toast.LENGTH_SHORT).show();
//            return ;
//        }
        dialog=new ProgressDialog(this);
        dialog.setMessage("正在玩命加载中");
        dialog.show();
        //发起请求给那个网站
        new Thread(){
            public void run() {
                try {
                    ul=PATH+ URLEncoder.encode(city,"UTF-8");

                    URL url=new URL(ul);

                    //设置必要的参数信息
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");

                    //判断响应码
                    int code = conn.getResponseCode();
                    if(code==200){
                        //连接网络成功
                        InputStream in = conn.getInputStream();
                        String data = StreamTool.decodeStream(in);


                        //解析json格式的数据
                        JSONObject jsonObj=new JSONObject(data);
                        //获得desc的值
                        String result = jsonObj.getString("desc");
                        if("OK".equals(result)){
                            //城市有效，返回了需要的数据
                            JSONObject dataObj = jsonObj.getJSONObject("data");

                            JSONArray jsonArray = dataObj.getJSONArray("forecast");
                            //通知更新ui
                            Message msg = Message.obtain();
                            msg.obj=jsonArray;
                            msg.what=SUCCESS;
                            mhandler.sendMessage(msg);
                        }else{
                            //城市无效
                            Message msg=Message.obtain();
                            msg.what=INVALID_CITY;
                            mhandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what=ERROR;
                    mhandler.sendMessage(msg);
                }
            };
        }.start();
    }

}

package com.example.updownload.scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.updownload.HomeActivity;
import com.example.updownload.R;

public class ZXingDealActivity extends AppCompatActivity {

    private Button mBtnSuccess,mBtnFail;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent intent = getIntent();
        String jxString = intent.getStringExtra("path");
        TextView result = new TextView(ZXingDealActivity.this);
        result.setText(jxString);

        if (result.getText().equals("扫码成功")) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_zxing_deal);

            mBtnSuccess = findViewById(R.id.btn_success);
            mBtnSuccess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ZXingDealActivity.this, HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        } else {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_zxing_fail);

            mBtnFail = findViewById(R.id.btn_fail);
            mBtnFail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ZXingDealActivity.this,HomeActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        }

    }
/*
    //控件设了个全局
    private WebView wb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_deal);
        //找控件
        //wb = findViewById(R.id.wb);
        //TextView receptionTv = findViewById(R.id.receptionTv);
        //支持js语言
        wb.getSettings().setJavaScriptEnabled(true);
        // 缩放至屏幕的大小
        wb.getSettings().setLoadWithOverviewMode(true);
        //支持缩放
        wb.getSettings().setSupportZoom(true);
        //声明一个Intent意图，用来接受MainActivity传过来的值
        Intent intent = getIntent();
        //拿到MainActivity传过来的值并返回一个字符串，
        //口令要一致
        String jxString = intent.getStringExtra("path");
        //把字符串赋值给输入框
     //   receptionTv.setText(jxString);

        wb.getSettings().setJavaScriptEnabled(true);
        wb.setWebViewClient(new MyWebViewClient());
        wb.setWebChromeClient(new MyWebChromeClient());
        wb.loadUrl(jxString);
    }


    class MyWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("WebView","onPageStarted...");
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wb.canGoBack()) {
            wb.canGoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }
    }

 */
}

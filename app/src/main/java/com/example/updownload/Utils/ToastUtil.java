package com.example.updownload.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
    public static Toast mToast;
    public static void showMsg(Context context, String msg) {
        if(mToast == null) {
            mToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER,0,0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}

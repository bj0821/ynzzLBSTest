package com.ynzz.lbstest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.IllegalFormatCodePointException;
import com.ynzz.lbstest.firstlogo.GuideActivity;

public class Screen extends AppCompatActivity{
    private Handler mHandler = new Handler();
    ImageView imageview;
    //TextView textview;
    int alpha = 255;
    int b = 0;
    public static final int VERSION = 1;
    public static SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screen);
        imageview = (ImageView) this.findViewById(R.id.ImageView01);
        //textview = (TextView) this.findViewById(R.id.TextView01);
        imageview.setAlpha(alpha);
        //读取sp保存
        sp = getSharedPreferences("config", MODE_PRIVATE);
        new Thread(new Runnable() {
            public void run() {
                while (b < 2) {
                    try {
                        if (b == 0) {
                            Thread.sleep(1000);
                            b = 1;
                        } else {
                            Thread.sleep(50);
                        }
                        updateApp();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                imageview.setAlpha(alpha);
               // textview.invalidate();
                imageview.invalidate();
            }
        };

    }

    public void updateApp() {
        alpha -= 5;
        if (alpha <= 0) {
            b = 2;
            boolean isFrist = sp.getBoolean("isFrist", true);
            if (isFrist) {//第一进入
                Intent intent = new Intent(Screen.this, GuideActivity.class);
                startActivity(intent);
            } else {
                Intent intent2 = new Intent(Screen.this, LoginActivity.class);
                startActivity(intent2);
//                    finish();
            }
//                Intent in = new Intent(this, LoginActivity.class);
//                startActivity(in);
            finish();
        }
        mHandler.sendMessage(mHandler.obtainMessage());
    }

}

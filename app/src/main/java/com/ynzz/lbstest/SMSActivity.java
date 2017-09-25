package com.ynzz.lbstest;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ynzz.util.CreateSeal;


public class SMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ImageView view = (ImageView)findViewById(R.id.imageView1);
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        CreateSeal seal = new CreateSeal(bitmap);
        view.setImageBitmap(seal.getBitmap());
    }
}

package com.ynzz.lbstest;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zxing.BarCodeUtil;
import com.zxing.activity.CaptureActivity;

public class Scanobject2 extends AppCompatActivity {
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private TextView tv1;
    private TextView tv2;
    private EditText et1;
    private BarCodeUtil util;
    private ImageView iv1;
    private RadioButton button1;
    private RadioButton first;
    private RadioButton button2;
    private RadioButton baidumap;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanobject2);
        WindowManager wm = getWindowManager();
        //设置一维码和二维码宽高
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        util = new BarCodeUtil(width * 2 / 3, height / 4);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu2);
        }
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        findViews();
        initViews();
        button1=(RadioButton) findViewById(R.id.button_1);
        button2=(RadioButton) findViewById(R.id.button_2);
        baidumap=(RadioButton)findViewById(R.id.badidu);
        first = (RadioButton)findViewById(R.id.button_0);
        first.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Scanobject2.this,lbsActivity.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Scanobject2.this,CameraAlbumActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Scanobject2.this,MyLocation.class);
                startActivity(intent);
            }
        });
        baidumap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("geo:25.053482,102.732821"));
                Intent choose = Intent.createChooser(intent,"百度地图");
                startActivity(choose);
            }
        });
    }

    private void findViews() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        et1 = (EditText) findViewById(R.id.editText1);
        iv1 = (ImageView) findViewById(R.id.iv1);
    }

    private void initViews() {
        /**
         * 扫码
         */
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Scanobject2.this, CaptureActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        /**
         * 生成一维码
         */
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et1.getText().toString().trim();
                try {
                    iv1.setImageBitmap(util.bitmap1(text));
                    tv2.setText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("barCoder", "draw bar code failed." + e.toString());
                }
            }
        });

        /**
         * 生成二维码
         */
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = et1.getText().toString().trim();
                try {
                    iv1.setImageBitmap(util.bitmap2(text));
                    tv2.setText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("barCoder", "draw bar code failed." + e.toString());
                }
            }
        });
    }

    //获取扫码结果
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0) {
            String result = data.getExtras().getString("result");
            tv1.setText(result);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.json, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}

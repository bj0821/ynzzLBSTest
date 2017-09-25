package com.ynzz.lbstest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.ynzz.carmanager.ui.CarMainActivity;
import com.ynzz.lbstest.carmera.CarmeraMainActivity;
import com.ynzz.lbstest.login.ContactActivity;
import com.ynzz.lbstest.login.ShareActivity;
import com.ynzz.util.HttpUtil;
import com.ynzz.util.SIMCardInfo;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class lbsActivity extends AppCompatActivity {

    public LocationClient mLocationClient;
    private TextView positionText;
    private StringBuilder currentPosition;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton baidumap;
    private RadioButton scan;
    private DrawerLayout mDrawerLayout;
    Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.lbs_layout);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu2);
        }
        navView.setCheckedItem(R.id.nav_myinfo);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
               // mDrawerLayout.closeDrawers();
                updatePosition(item);
                return true;
            }
        });
        positionText = (TextView) findViewById(R.id.position_text_view);
        button1=(RadioButton) findViewById(R.id.button_1);
        button2=(RadioButton) findViewById(R.id.button_2);
        baidumap=(RadioButton)findViewById(R.id.badidu);
        scan=(RadioButton)findViewById(R.id.message);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(lbsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(lbsActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(lbsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(lbsActivity.this,permissions,1);
        }else {
            requestLocation();
        }
        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(lbsActivity.this,CameraAlbumActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(lbsActivity.this,MyLocation.class);
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
        scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(lbsActivity.this,Scanobject2.class);
                startActivity(intent);
            }
        });

    }

    private void updatePosition(final MenuItem menuItem) {
        runnable = null;
        switch (menuItem.getItemId()) {
            //用户信息
            case R.id.nav_myinfo:
                runnable = navigateMyInfo;
                break;
            //我的位置
            case R.id.nav_myorders:
                runnable = navigateMyLocation;
                break;
            //用户车辆
            case R.id.nav_mycars:
                runnable = navigateMyCars;
                break;
            case R.id.nav_myregulations:
                runnable = navigateMyCarmera;
                break;
            case R.id.nav_settings:
                runnable = navigateSetting;
                break;
            case R.id.nav_about:
                runnable = navigateAbout;
                break;
            case R.id.nav_help:
                runnable = navigateHelp;
                break;
            case R.id.nav_update:
                runnable = navigateUpdate;
                break;
        }
        if (runnable != null) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 350);
        }
    }

    //我的信息
    Runnable navigateMyInfo = new Runnable() {
        @Override
        public void run() {
            Intent mIntent = new Intent(lbsActivity.this, CarmeraMainActivity.class);
            startActivity(mIntent);
        }
    };
    //我的位置
    Runnable navigateMyLocation = new Runnable() {
        @Override
        public void run() {
            Intent mIntent = new Intent(lbsActivity.this, MyLocation.class);
            startActivity(mIntent);
        }
    };

    //我的汽车
    Runnable navigateMyCars = new Runnable() {
        @Override
        public void run() {

                Intent mIntent = new Intent(lbsActivity.this, CarMainActivity.class);

                startActivity(mIntent);

        }
    };
    //我的相机
    Runnable navigateMyCarmera = new Runnable() {
        @Override
        public void run() {

            Intent mIntent = new Intent(lbsActivity.this, CameraAlbumActivity.class);

            startActivity(mIntent);

        }
    };

    //设置
    Runnable navigateSetting = new Runnable() {
        @Override
        public void run() {

            Intent mIntent = new Intent(lbsActivity.this, HotImgActivity.class);

            startActivity(mIntent);
        }
    };
    //关于
    Runnable navigateAbout = new Runnable() {
        @Override
        public void run() {

            Intent mIntent = new Intent(lbsActivity.this, NaviAboutActivity.class);

            startActivity(mIntent);
        }
    };
    //评价
    Runnable navigateHelp = new Runnable() {
        @Override
        public void run() {

            Intent mIntent = new Intent(lbsActivity.this, Ratingbardemo.class);

            startActivity(mIntent);
        }
    };
    //更新
    Runnable navigateUpdate = new Runnable() {
        @Override
        public void run() {

            Intent mIntent = new Intent(lbsActivity.this, ContactActivity.class);

            startActivity(mIntent);
        }
    };

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        mLocationClient.setLocOption(option);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    for (int result : grantResults){
                        if (result!= PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public class MyLocationListener implements BDLocationListener  {
        @Override
        public void onReceiveLocation(BDLocation location){
            currentPosition = new StringBuilder();
            currentPosition.append("--------------------------------------------------------------\n");
            currentPosition.append("纬度：").append(location.getLatitude()).
                    append("\n");
            currentPosition.append("经度：").append(location.getLongitude()).
                    append("\n");
            currentPosition.append("海拔：").append(location.getAltitude()).
                    append("\n");
            currentPosition.append("速度：").append(location.getSpeed()).
                    append("\n");
            currentPosition.append("方向：").append(location.getRadius()).
                    append("\n");
//            currentPosition.append("国家：").append(location.getCountry()).append("\n");
//            currentPosition.append("    省：").append(location.getProvince()).append("\n");
//            currentPosition.append("    市：").append(location.getCity()).append("\n");
//            currentPosition.append("    区：").append(location.getDistrict()).append("\n");
//            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            currentPosition.append("地址：").append(location.getAddrStr()).append("\n");
            currentPosition.append("区域码：").append(location.getCityCode()).append("\n");
            currentPosition.append("定位方式：");
            if (location.getLocType()==BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if (location.getLocType()==BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
            currentPosition.append("").append("\n");
           // currentPosition.append("--------------------------------------------------------------").append("\n");
            //long time=System.currentTimeMillis();
            try {
                URL url = new URL("http://www.pool.ntp.org/zh/");
                URLConnection uc=url.openConnection();//生成连接对象
                uc.connect(); //发出连接
                long time =uc.getDate(); //取得网站日期时间
                 SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                String newtime = formatter.format(time);
                currentPosition.append("当前时间：").append(time).append("\n");
                currentPosition.append("转换时间：").append(newtime).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentPosition.append("--------------------------------------------------------------").append("\n");
            String srvcName = Context.TELEPHONY_SERVICE;
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(srvcName);
            String imei = telephonyManager.getDeviceId();
            String imsi = telephonyManager.getSubscriberId();
            String number = telephonyManager.getLine1Number();
            String simOperator = telephonyManager.getSimOperator();//返回SIM卡运营商的单个核细胞数+冶
            String simSerialNumber = telephonyManager.getSimSerialNumber();//返回SIM卡的序列号
            currentPosition.append("IMEI：").append(imei).append("\n");
            currentPosition.append("IMSI：").append(imsi).append("\n");
            SIMCardInfo siminfo = new SIMCardInfo(lbsActivity.this);
            currentPosition.append("手机号：").append(siminfo.getNativePhoneNumber()).append("\n");
            currentPosition.append("运营商：").append(siminfo.getProvidersName()).append("\n");
            currentPosition.append("SIM卡序：").append(simSerialNumber).append("\n");
            currentPosition.append("--------------------------------------------------------------");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    positionText.setText(currentPosition);
                }
            });
            try {
                Map<String,String> map = new HashMap<String, String>();
                map.put("longitude", location.getLongitude()+"");
                map.put("latitude", location.getLatitude()+"");
                try {
                    URL url = new URL("http://www.pool.ntp.org/zh/");
                    URLConnection uc=url.openConnection();//生成连接对象
                    uc.connect(); //发出连接
                    long time =uc.getDate(); //取得网站日期时间
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String newtime = formatter.format(time);
                    map.put("time",newtime+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                map.put("mac", imei+"");
                map.put("remark",imsi+"");
                map.put("serialnumber", simSerialNumber);
                if (location.getLongitude()!=4.9E-324 && location.getLatitude()!=4.9E-324){
                    String url = HttpUtil.BASE_URL+"processing";
                    HttpUtil.postRequest(url, map);
                }else{

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

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
            case R.id.json:
                Intent intent = new Intent(lbsActivity.this,JsonActivity.class);
                startActivity(intent);
                break;
            case R.id.sms:
                Intent intent1 = new Intent(lbsActivity.this,SMSActivity.class);
                startActivity(intent1);
                break;
            case R.id.Email:
                Intent intent2 = new Intent(lbsActivity.this,ShareActivity.class);
                startActivity(intent2);
                break;
            default:
        }
        return true;
    }
}

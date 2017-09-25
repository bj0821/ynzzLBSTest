package com.ynzz.lbstest;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class WifiActivity extends AppCompatActivity {
    private TextView varTxt;
    private Button varBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        varTxt=(TextView)findViewById(R.id.wifi);
        varBtn=(Button)findViewById(R.id.idBtn);
    }
    public void getWifiInformation(View v){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String macAddress = wifiInfo.getMacAddress();
        String bssid = wifiInfo.getBSSID();
        int rssi = wifiInfo.getRssi();
        int linkSpeed = wifiInfo.getLinkSpeed();
        String ssid = wifiInfo.getSSID();
        int networkid = wifiInfo.getNetworkId();
        String ipAddress = Formatter.formatIpAddress(ip);
        String info = "IP地址="+ipAddress+"\n"+"Mac地址="+macAddress+"\n"+"BSSID="+bssid
                +"\n"+"RSSI="+rssi+"\n"+"连接速度="+linkSpeed+"\n"+
                "SSID="+ssid+"\n"+"NetworkId="+networkid;
        varTxt.setText(info);

    }
}

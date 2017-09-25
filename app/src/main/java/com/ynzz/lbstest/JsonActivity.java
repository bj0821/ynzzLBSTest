package com.ynzz.lbstest;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ynzz.util.JSONutil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class JsonActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private Button button3;
    private String URL_JSONARRAY = "http://192.168.1.117:8080/AndroidJson/aa.json";
    private String URL_JSONOBJECT = "http://192.168.1.117:8080/AndroidJson/bb.json";
    private String URL_JSON = "http://192.168.1.117:8080/AndroidJson/cc.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        findViews();
        setListeners();
    }

    private void findViews() {
        button1 = (Button) this.findViewById(R.id.button1);
        button2 = (Button) this.findViewById(R.id.button2);
        button3 = (Button) this.findViewById(R.id.button3);
    }

    private void setListeners() {
        button1.setOnClickListener(onClickListener);
        button2.setOnClickListener(onClickListener);
        button3.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button1:
                    try {
                        showResult(JSONutil.getJSONArray(URL_JSONARRAY));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button2:
                    try {
                        showResult(JSONutil.getJSONObject(URL_JSONOBJECT));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.button3:
                    try {
                        showResult(JSONutil.getJSON(URL_JSON));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private void showResult(List<Map<String, String>> list) {
        StringBuffer sb=new StringBuffer();
        try {
            for (Map<String, String> map : list) {
                String id = map.get("id");
                String name = map.get("name");
                sb.append(id).append("--").append(name);
            }
            new AlertDialog.Builder(this).setTitle("json").setMessage(sb.toString()).setPositiveButton("ok", null).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

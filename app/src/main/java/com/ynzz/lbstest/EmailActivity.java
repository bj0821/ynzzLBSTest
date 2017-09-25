package com.ynzz.lbstest;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

public class EmailActivity extends AppCompatActivity {

    private TextView uploadInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        uploadInfo = (TextView) findViewById(R.id.upload_info);

        uploadFile();
    }

    public void uploadFile()
    {
        //服务器端地址
        String url = "http://192.168.1.117:8080/uploadFile/upload";
        //手机端要上传的文件，首先要保存你手机上存在该文件
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + "20170803150817.jpg";

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        try
        {
            param.put("file", new File(filePath));
            param.put("content", "zjp");

            httpClient.post(url, param, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    super.onStart();

                    uploadInfo.setText("正在上传...");
                }
                @Override
            public void onSuccess(int i, org.apache.http.Header[] headers, byte[] bytes) {
                Toast.makeText(EmailActivity.this,"上传成功",Toast.LENGTH_LONG).show();
                    uploadInfo.setText("上传成功");
                }
            @Override
            public void onFailure(int i, org.apache.http.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(EmailActivity.this,"上传失败",Toast.LENGTH_LONG).show();
                uploadInfo.setText("上传失败");
            }
//                @Override
//                public void onStart()
//                {
//                    super.onStart();
//
//                    uploadInfo.setText("正在上传...");
//                }
//
//                @Override
//                public void onSuccess(String arg0)
//                {
//                    super.onSuccess(arg0);
//
//                    Log.i("ck", "success>" + arg0);
//
//                    if(arg0.equals("success"))
//                    {
//                        Toast.makeText(EmailActivity.this, "上传成功！", Toast.LENGTH_LONG).show();
//                    }
//
//                    uploadInfo.setText(arg0);
//                }
//
//                @Override
//                public void onFailure(Throwable arg0, String arg1)
//                {
//                    super.onFailure(arg0, arg1);
//
//                    uploadInfo.setText("上传失败！");
//                }
            });

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(EmailActivity.this, "上传文件不存在！", Toast.LENGTH_LONG).show();
        }
    }
}

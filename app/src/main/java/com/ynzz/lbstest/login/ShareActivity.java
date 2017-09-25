package com.ynzz.lbstest.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ynzz.lbstest.R;

public class ShareActivity extends AppCompatActivity {
    private Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mButton = (Button) findViewById(R.id.btn);

        mButton.setOnClickListener(new OnBtnClickListener());
    }
    /**
     * 按钮做监听 点击一键分享
     *
     * @author Administrator
     */
    private final class OnBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            Intent intent = new Intent(Intent.ACTION_SEND);

//			intent.setType("image/*");//分享图片   一般传一个url来加载图片

            intent.setType("text/plain");

            intent.putExtra(Intent.EXTRA_SUBJECT, "好友分享");

            intent.putExtra(Intent.EXTRA_TEXT, "我正在使用一键分享...");

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(Intent.createChooser(intent, "好友分享"));
        }
    }
}

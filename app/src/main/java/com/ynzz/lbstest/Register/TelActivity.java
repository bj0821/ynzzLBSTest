package com.ynzz.lbstest.Register;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ynzz.lbstest.R;
import com.ynzz.lbstest.login.ForgetPasswordActivity;
import com.ynzz.util.FormatUtils;

public class TelActivity extends AppCompatActivity {
    private TextInputEditText mText;
    private Button mSend;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel);
        initTooblar();

        initView();
    }
    private void initView() {

        mText = (TextInputEditText) findViewById(R.id.activity_tel_text);
        mSend = (Button) findViewById(R.id.activity_tel_send);
        progressBar = (ProgressBar) findViewById(R.id.activity_tel_progressbar);
        linearLayout = (LinearLayout) findViewById(R.id.activity_tel_linearlayout);

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tel = mText.getText().toString().trim();

                if (FormatUtils.isTel(tel)) {

                    showProgressBar();
                    //跳转
                    Intent mIntent = new Intent(TelActivity.this, ForgetPasswordActivity.class);

                    mIntent.putExtra("Tel", tel);

                    startActivity(mIntent);

                    TelActivity.this.finish();
                } else {

                    Toast.makeText(TelActivity.this, "不是手机号", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initTooblar() {

        View rootView = findViewById(R.id.activity_tel);

        if (rootView != null) {

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            toolbar.setTitle("验证手机");

            setSupportActionBar(toolbar);


            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TelActivity.this.finish();
                }
            });
        }
    }

    private void showProgressBar() {

        linearLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showLinearLayout() {
        linearLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

}

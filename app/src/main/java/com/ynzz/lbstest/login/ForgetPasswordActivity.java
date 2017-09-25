package com.ynzz.lbstest.login;

import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ynzz.lbstest.R;
import com.ynzz.lbstest.UserDbAdapter;

public class ForgetPasswordActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    private TextInputEditText mPass;
    private TextInputEditText mConfPass;
    private TextInputEditText mNum;
    private Button mSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initTooblar();

        initView();
    }
    private void initView() {

        mPass = (TextInputEditText) findViewById(R.id.activity_edit_password_pass);
        mConfPass = (TextInputEditText) findViewById(R.id.activity_edit_password_confirm_pass);
        mNum = (TextInputEditText) findViewById(R.id.activity_edit_password_num);
        mSubmit = (Button) findViewById(R.id.activity_edit_password_submit);

        progressBar = (ProgressBar) findViewById(R.id.activity_edit_password_progressbar);
        linearLayout = (LinearLayout) findViewById(R.id.activity_edit_password_linearlayout);

        //
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pass = mPass.getText().toString().trim();
                String confPass = mConfPass.getText().toString().trim();
                String code = mNum.getText().toString().trim();

                if (pass.equals(confPass)) {
                    mConfPass.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.length() == 0) {

                                mConfPass.setTextColor(Color.BLACK);
                            }

                            String t = mPass.getText().toString().trim();

                            if (s.length() > t.length()) {

                                mConfPass.setTextColor(Color.RED);

                                return;
                            }

                            for (int i = 0; i < s.length(); i++) {

                                if (t.charAt(i) != s.charAt(i)) {

                                    mConfPass.setTextColor(Color.RED);

                                    return;
                                }
                            }

                            mConfPass.setTextColor(Color.BLACK);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });

                }
            }});
    }

            private void initTooblar() {

                View rootView = findViewById(R.id.activity_edit_password_toolbar);

                if (rootView != null) {

                    Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

                    toolbar.setTitle("修改密码");

                    setSupportActionBar(toolbar);


                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ForgetPasswordActivity.this.finish();
                        }
                    });
                }
            }

            private void showLayout() {

                linearLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            private void showProgressBar() {

                linearLayout.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }

    }


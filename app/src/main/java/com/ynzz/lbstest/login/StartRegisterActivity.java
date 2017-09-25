package com.ynzz.lbstest.login;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ynzz.lbstest.LoginActivity;
import com.ynzz.lbstest.R;
import com.ynzz.lbstest.beans.User;
import com.ynzz.util.FormatUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartRegisterActivity extends AppCompatActivity {
    public static StartRegisterActivity mInstant;
    private TextInputEditText mNickNameView;
    private TextInputEditText mPasswordView;
    private TextInputEditText mConfirmPasswordView;
    private RadioButton mManView;
    private RadioButton mWomenView;
    private TextView mBirthdayView;
    private TextInputEditText mCodeView;
    private Button mSendView;
    //layout
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private User mDbHelper;
    //Tel
    private String mTel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mDbHelper = new User(this);
//        mDbHelper.open();
        setContentView(R.layout.activity_start_register);
        //拿到号码
        mTel = getIntent().getStringExtra("Tel");

        initView();

        intiToolbar();
    }
    //initToolbar
    private void intiToolbar() {

        View rootView = findViewById(R.id.activity_start_register_toolbar);

        if (rootView != null) {

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            toolbar.setTitle("详细信息");

            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    StartRegisterActivity.this.finish();
                }
            });
        }
    }
    private void initView() {

        findView();

        setListener();

        mInstant = this;
    }
    private void setListener() {
        mSendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkOut()) {

                    showProgressBar();

                    String tel = mCodeView.getText().toString().trim();
                    String User_Password = mPasswordView.getText().toString().trim();
                    String User_NickName = mNickNameView.getText().toString().trim();
                    final User mUser = initData();
                    if (mUser != null) {
//                        Cursor cursor = mDbHelper.getDiary(User_NickName);
//                        if(cursor.moveToFirst()){
//                            Toast.makeText(StartRegisterActivity.this, "用户名已经存在",
//                                    Toast.LENGTH_SHORT).show();
//                        }else {
//                            mDbHelper.createUser(User_NickName, User_Password);
                            showDialog();
                            showLayout();
//                        }

                    }
                }
            }
        });
        mConfirmPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 0) {

                    mConfirmPasswordView.setTextColor(Color.BLACK);
                }

                String t = mPasswordView.getText().toString().trim();

                if (s.length() > t.length()) {

                    mConfirmPasswordView.setTextColor(Color.RED);

                    return;
                }

                for (int i = 0; i < s.length(); i++) {

                    if (t.charAt(i) != s.charAt(i)) {

                        mConfirmPasswordView.setTextColor(Color.RED);

                        return;
                    }
                }

                mConfirmPasswordView.setTextColor(Color.BLACK);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    if (!FormatUtils.isPassword(mPasswordView.getText().toString().trim())) {

                        mPasswordView.setError("密码为6-16位字符数字或则字母，至少包括两项");
                    }
                }
            }
        });


        //设置时间选择
        mBirthdayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(StartRegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mBirthdayView.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay).show();
            }
        });
    }
    //dialog
    private void showDialog() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        //设置标题
        mBuilder.setTitle("注册成功");

        //setIcon
        mBuilder.setIcon(R.drawable.zhongzhi3);

        //设置类容
        mBuilder.setMessage("亲，你已经成功注册！");

        //设置跳转
        mBuilder.setPositiveButton("现在去主页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent mIntent = new Intent(StartRegisterActivity.this, LoginActivity.class);

                StartRegisterActivity.this.startActivity(mIntent);

                StartRegisterActivity.this.finish();
            }
        });

        Dialog mDialog = mBuilder.create();

        //设置点击不消失
        mDialog.setCanceledOnTouchOutside(false);

        mDialog.show();
    }

    //initData
    private User initData() {

        String User_Tel = mTel;
        String User_Password = mPasswordView.getText().toString().trim();
        String User_NickName = mNickNameView.getText().toString().trim();

        Boolean User_Sex;
        if (mManView.isChecked()) {

            User_Sex = true;
        } else {

            User_Sex = false;
        }

        String User_Birthday = mBirthdayView.getText().toString().trim();

        return new User(User_Tel, User_Password, User_NickName,
                User_Sex, User_Birthday);
    }
    private void findView() {

        mNickNameView = (TextInputEditText) findViewById(R.id.userNameAuto);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mConfirmPasswordView = (TextInputEditText) findViewById(
                R.id.conpasswordET);
        mManView = (RadioButton) findViewById(R.id.activity_start_register_radiobutton_sexman);
        mWomenView = (RadioButton) findViewById(R.id.activity_start_register_radiobutton_sexwoman);
        mBirthdayView = (TextView) findViewById(R.id.activity_start_register_textview_birthday);

        String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        mBirthdayView.setText(time);

        mSendView = (Button) findViewById(R.id.regBT);

        mCodeView = (TextInputEditText) findViewById(R.id.activity_start_register_text_code);

        linearLayout = (LinearLayout) findViewById(R.id.activity_start_register_linearlayout);
        progressBar = (ProgressBar) findViewById(R.id.activity_start_register_progressbar);
    }

    //验证
    private boolean checkOut() {

        String nick = mNickNameView.getText().toString().trim();

        if (!FormatUtils.isNick(nick)) {

            mNickNameView.setError("昵称为3-8位字母数字则下划线或者中文");

            return false;
        }

        String code = mCodeView.getText().toString().trim();
        if (!FormatUtils.isCode(code)) {

            mCodeView.setError("验证码为6为数字");
        }

        return true;
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

package com.ynzz.lbstest;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class driver extends AppCompatActivity {
    private EditText mUserText;
    private EditText mPasswordText;
    private EditText mConPasswordText;
    private UserDbAdapter mDbHelper;
    private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        setContentView(R.layout.activity_driver);
        mUserText = (EditText) findViewById(R.id.userNameAuto);
        mPasswordText = (EditText) findViewById(R.id.password);
        mPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mConPasswordText = (EditText) findViewById(R.id.conpasswordET);
        mConPasswordText.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        linearLayout=(LinearLayout)findViewById(R.id.driverback);
        linearLayout.getBackground().setAlpha(100);
        Button confirmButton = (Button) findViewById(R.id.regBT);
        Button cancelButton = (Button) findViewById(R.id.canBT);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String username = mUserText.getText().toString();
                String password = mPasswordText.getText().toString();
                String conPassword = mConPasswordText.getText().toString();
                if((username == null||username.equalsIgnoreCase("")) || (password == null||password.equalsIgnoreCase(""))
                        || (conPassword == null||conPassword.equalsIgnoreCase(""))){
                    Toast.makeText(driver.this, "必须输入用户名和密码",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = mDbHelper.getDiary(username);
                    if(cursor.moveToFirst()){
                        Toast.makeText(driver.this, "用户名已经存在",
                                Toast.LENGTH_SHORT).show();
                    }else if (!password.equals(conPassword)) {
                        Toast.makeText(driver.this, "两次密码不一致，请重新输入",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        mDbHelper.createUser(username, password);
                        Toast.makeText(driver.this, "注册成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(driver.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }

        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(driver.this, LoginActivity.class);
                startActivity(intent);
            }

        });
    }
}

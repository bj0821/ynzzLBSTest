package com.ynzz.lbstest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ynzz.lbstest.Register.RegisterActivity;
import com.ynzz.lbstest.Register.TelActivity;
import com.ynzz.lbstest.login.ContactActivity;
import com.ynzz.lbstest.login.ForgetPasswordActivity;
import com.ynzz.lbstest.login.StartRegisterActivity;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView cardNumAuto;
    private ImageView loginImage;
    private TextView topText;
    private TextPaint tp;
    private Drawable mIconPerson;
    private Drawable mIconLock;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private EditText passwordEdit;
    private Button login;
    private Switch rememberPass;
    private String cardNumStr;
    private String passwordStr;
    private UserDbAdapter mDbHelper;
    private LinearLayout linearLayout;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);//不随屏幕旋转
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        sp.edit().putBoolean("isFrist", false).commit();//不是第一次进入   （别忘了提交）
        topText=(TextView)findViewById(R.id.topname);
//        topText.setTextColor(Color.MAGENTA);
        topText.setTextSize(16.0f);
        topText.setTypeface(Typeface.MONOSPACE,Typeface.BOLD_ITALIC);
        //使用TextPaint的仿“粗体”设置setFakeBoldText为true。目前还无法支持仿“斜体”方法
        tp=topText.getPaint();
        tp.setFakeBoldText(true);
        loginImage=(ImageView)findViewById(R.id.loginImage);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.miniman);
        loginImage.setImageBitmap(toRoundCorner(b, 360));
        linearLayout=(LinearLayout)findViewById(R.id.backg);
        linearLayout.getBackground().setAlpha(130);
//        loginImage.getBackground().setAlpha(80);
        mIconPerson=getResources().getDrawable(R.drawable.txt_person_icon);
        mIconPerson.setBounds(5, 1, 60, 50);
        mIconLock=getResources().getDrawable(R.drawable.txt_lock_icon);
        mIconLock.setBounds(5, 1, 60, 50);
        cardNumAuto = (AutoCompleteTextView)findViewById(R.id.account);
        //accountEdit = (EditText)findViewById(R.id.account);
        cardNumAuto.setCompoundDrawables(mIconPerson, null, null, null);
        passwordEdit = (EditText)findViewById(R.id.password);
        passwordEdit.setCompoundDrawables(mIconLock, null, null, null);
        pref = this.getSharedPreferences("passwordFile", MODE_PRIVATE);
        rememberPass = (Switch) findViewById(R.id.remember_pass);
        rememberPass.setChecked(true);// 默认为记住密码
        cardNumAuto.setThreshold(1);// 输入1个字母就开始自动提示
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 隐藏密码为InputType.TYPE_TEXT_VARIATION_PASSWORD，也就是0x81
        // 显示密码为InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD，也就是0x91
        login = (Button)findViewById(R.id.login);
        cardNumAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String[] allUserName = new String[pref.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
                allUserName = pref.getAll().keySet().toArray(new String[0]);
                // sp.getAll()返回一张hash map
                // keySet()得到的是a set of the keys.
                // hash map是由key-value组成的
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line,
                        allUserName);
                cardNumAuto.setAdapter(adapter);// 设置数据适配器

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                passwordEdit.setText(pref.getString(cardNumAuto.getText()
                        .toString(), ""));// 自动输入密码
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cardNumStr = cardNumAuto.getText().toString();
                passwordStr = passwordEdit.getText().toString();
                if((cardNumStr == null||cardNumStr.equalsIgnoreCase("")) || (passwordStr == null||passwordStr.equalsIgnoreCase(""))){
                    Toast.makeText(LoginActivity.this, "必须输入用户名和密码",
                            Toast.LENGTH_SHORT).show();
                }else{
                    Cursor cursor = mDbHelper.getDiary(cardNumStr);

                    if(!cursor.moveToFirst()){
                        Toast.makeText(LoginActivity.this, "用户名不存在",
                                Toast.LENGTH_SHORT).show();
                    }else if (!passwordStr.equals(cursor.getString(2))) {
                        Toast.makeText(LoginActivity.this, "密码错误，请重新输入",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (rememberPass.isChecked()) {// 登陆成功才保存密码
                            pref.edit().putString(cardNumStr, passwordStr).commit();
                        }
                        Toast.makeText(LoginActivity.this, "登录成功",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("username", cardNumStr);
                        intent.setClass(LoginActivity.this, lbsActivity.class);
                        //intent.setClass(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                }
            }
        });
        mDbHelper = new UserDbAdapter(this);
        mDbHelper.open();
        TextView about = (TextView)findViewById(R.id.about);
        TextView contact = (TextView)findViewById(R.id.contact);
        TextView regist = (TextView)findViewById(R.id.regist);
        TextView forget = (TextView)findViewById(R.id.forget_password);
        about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
        contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,ContactActivity.class);
                startActivity(intent);
            }
        });
        regist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        forget.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,TelActivity.class);
                startActivity(intent);
            }
        });
    }
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(LoginActivity.this,"请选择 我是司机 或 我是乘客 进行注册",Toast.LENGTH_LONG).show();
                break;
            case R.id.driver:
                Intent intent = new Intent(LoginActivity.this,driver.class);
                startActivity(intent);
                break;
            case R.id.passenger:
                Intent intent1 = new Intent(LoginActivity.this,passenger.class);
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }
}

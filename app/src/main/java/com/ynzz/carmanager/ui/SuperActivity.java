package com.ynzz.carmanager.ui;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ynzz.carmanager.ui.consts.ApplicationGlobal;
import com.ynzz.carmanager.ui.consts.MyApplication;
import com.ynzz.carmanager.ui.util.CommonUtil;
import com.ynzz.lbstest.R;


/*
 * 基础普通activity
 */
public abstract class SuperActivity extends Activity {
    //退出程序的广播标识
    public final static String EXIT_BROADCAST = "com.ynzz.carmanager.ui.exit";
    public final static String CHANGE_BG_BROADCAST = "com.ynzz.carmanager.ui.change.bg";//切换皮肤广播
    public CommonUtil commonUtil;
    public Resources resources;
    public SharedPreferences sp;
    //类标识
    protected String TAG = "SuperActivity";
    //返回按钮
    protected ImageButton back;
    //右侧按钮
    protected TextView rightActionBarBtn;
    //文字按钮
    protected TextView rightActionBarBtn2;
    //页面标题
    protected TextView title;
    //背景view
    protected View bgView;

    Context context;
    String token = "";//token标识
    private OnReciverLinsener onReciverLinsener;//父类监听广播回调监听器
    //退出程序的广播接收器
    private BroadcastReceiver superReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(EXIT_BROADCAST)) {
                finish();
            } else if (intent.getAction() != null && intent.getAction().equals(CHANGE_BG_BROADCAST)) {
                changeBg();
            } else if (onReciverLinsener != null) {
                onReciverLinsener.onReciver(context, intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		//判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
//		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////		            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
        context = this;
        resources = getResources();
        commonUtil = new CommonUtil(this);
        sp = MyApplication.getSharePre();
        //添加广播监听器
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXIT_BROADCAST);
        filter.addAction(CHANGE_BG_BROADCAST);
        registerReceiver(superReciver, filter);
        keyBoardHide();
        initHeader();
    }

    @Override
    protected void onResume() {
        if(bgView == null)
        {
            //实例化背景组件
            try {
                bgView = findViewById(R.id.bg_view);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        changeBg();
        super.onResume();
    }

    /**
     * 初始化页面标题栏
     */
    @SuppressLint("NewApi")
    protected void initHeader() {
        ActionBar actionbar = getActionBar();
        if (actionbar != null) {
            actionbar.setDisplayUseLogoEnabled(false);
            actionbar.setDisplayShowHomeEnabled(false);
            actionbar.setDisplayHomeAsUpEnabled(false);
            actionbar.setDisplayShowTitleEnabled(false);
            actionbar.setDisplayShowCustomEnabled(true);
            actionbar.setCustomView(R.layout.actionbar_commont);
            View actionBarView = actionbar.getCustomView();
            back = (ImageButton) actionBarView.findViewById(R.id.common_actionbar_leftbtn);
            rightActionBarBtn = (TextView) actionBarView.findViewById(R.id.common_actionbar_rightbtn);
            rightActionBarBtn2 = (TextView) actionBarView.findViewById(R.id.common_actionbar_rightbtn_2);
            title = (TextView) actionBarView.findViewById(R.id.common_actionbar_title);
            back.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 重启监听器
     *
     * @param actions 添加的监听事件
     */
    protected void reRegisterReciver(String[] actions) {
        if (superReciver != null) {
            unregisterReceiver(superReciver);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXIT_BROADCAST);
        if (actions != null && actions.length > 0) {
            for (int i = 0; i < actions.length; i++) {
                filter.addAction(actions[i]);
            }
        }
        registerReceiver(superReciver, filter);
    }

    protected void showActionBar() {
        if (!getActionBar().isShowing()) {
            getActionBar().show();
        }
    }

    protected void hiddenActionBar() {
        if (getActionBar().isShowing()) {
            getActionBar().hide();
        }
    }

    /**
     * 隐藏返回按钮
     */
    protected void enableBackBtn() {
        if (back != null) {
            back.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏右侧按钮
     */
    protected void enableRightActionBarBtn() {
        if (rightActionBarBtn != null) {
            rightActionBarBtn.clearAnimation();
            rightActionBarBtn.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 隐藏右侧2按钮
     */
    protected void enableRightActionBarBtn2() {
        if (rightActionBarBtn2 != null) {
            rightActionBarBtn2.clearAnimation();
            rightActionBarBtn2.setVisibility(View.GONE);
        }
    }

    /**
     * 设置标题
     */
    protected void setActivityTitle(String titleStr) {
        if (title != null) {
            title.setText(titleStr);
        }
    }

    /**
     * 显示右侧按钮并制定显示图案和点击事件
     */
    protected void showRightActionBarBtn(int res, String text, OnClickListener l) {
        if (rightActionBarBtn != null) {
            rightActionBarBtn.setVisibility(View.VISIBLE);
            rightActionBarBtn.setBackgroundResource(res);
            rightActionBarBtn.setText(text);
            rightActionBarBtn.setOnClickListener(l);
        }
    }

    /**
     * 显示右侧按钮并制定显示图案和点击事件
     */
    protected void showRightActionBarBtn2(int res, String text, OnClickListener l) {
        if (rightActionBarBtn2 != null) {
            rightActionBarBtn2.setVisibility(View.VISIBLE);
            rightActionBarBtn2.setBackgroundResource(res);
            rightActionBarBtn2.setText(text);
            rightActionBarBtn2.setOnClickListener(l);
        }
    }

    /**
     * 显示右侧按钮并制定显示图案和点击事件
     */
    protected void showRightActionBarBtn(int res, OnClickListener l) {
        if (rightActionBarBtn != null) {
            rightActionBarBtn.setVisibility(View.VISIBLE);
            rightActionBarBtn.setBackgroundResource(res);
            rightActionBarBtn.setOnClickListener(l);
        }
    }

    /**
     * 显示右侧按钮并制定显示图案和点击事件
     */
    protected void showRightActionBarBtn2(int res, OnClickListener l) {
        if (rightActionBarBtn2 != null) {
            rightActionBarBtn2.setVisibility(View.VISIBLE);
            rightActionBarBtn2.setBackgroundResource(res);
            rightActionBarBtn2.setOnClickListener(l);
        }
    }

    /**
     * 切换背景
     */
    public void changeBg() {
        if (bgView != null) {
            SharedPreferences sp = MyApplication.getSharePre();
            int defaultSkinRes = sp.getInt(ApplicationGlobal.SKIN_KEY, R.drawable.background);
            bgView.setBackgroundResource(defaultSkinRes);
        }
    }

    @Override
    public void finish() {
        if (title != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(title.getWindowToken(), 0); //强制隐藏键盘
        }
        super.finish();
        overridePendingTransition(0, R.anim.slide_out);
    }

    @Override
    public void startActivity(Intent intent) {
        if (title != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(title.getWindowToken(), 0); //强制隐藏键盘
        }
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
    }

    @Override
    protected void onDestroy() {
        if (superReciver != null) {
            unregisterReceiver(superReciver);
        }
        super.onDestroy();
    }

    public void setOnReciverLinsener(OnReciverLinsener onReciverLinsener) {
        this.onReciverLinsener = onReciverLinsener;
    }

    void keyBoardHide() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    /**
     * 监听广播方法回调
     *
     * @author jinzufan
     */
    protected interface OnReciverLinsener {
        public void onReciver(Context context, Intent intent);
    }
}

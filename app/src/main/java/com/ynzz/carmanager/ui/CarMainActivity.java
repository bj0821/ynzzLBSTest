package com.ynzz.carmanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.ynzz.carmanager.ui.consts.ApplicationGlobal;
import com.ynzz.carmanager.ui.util.CommonUtils;
import com.ynzz.carmanager.ui.util.DeviceUtil;
import com.ynzz.carmanager.ui.util.ProgressDialogUtil;
import com.ynzz.carmanager.ui.view.MyDialog;
import com.ynzz.lbstest.R;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;


import java.util.ArrayList;
import java.util.HashMap;


public class CarMainActivity extends SuperActivity implements View.OnClickListener {
    private LinearLayout queryCar_ll;
    private LinearLayout addCar_ll;
//        List<CarManagerItem> userChannelList = new ArrayList<CarManagerItem>();
    private ArrayList<ImageView> skinsView;
    ArrayList<HashMap<String, Integer>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_main);
        findView();
        initSkinData();
    }
    public void findView(){
        queryCar_ll = (LinearLayout) findViewById(R.id.menu_query_layout);
        addCar_ll = (LinearLayout) findViewById(R.id.menu_addcar_layout);
        queryCar_ll.setOnClickListener(this);
        addCar_ll.setOnClickListener(this);
        setActivityTitle("车辆管理");
        back.setVisibility(View.GONE);
        showRightActionBarBtn(R.mipmap.ic_skin, "", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeSkinDialog();
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.menu_query_layout:
                Intent i1=new Intent(CarMainActivity.this,QueryCarPassActivity.class);
                startActivity(i1);
                break;
            case R.id.menu_addcar_layout:
                Intent i2=new Intent(CarMainActivity.this,AddCarActivity.class);
                startActivity(i2);
                break;
        }
    }

    /**
     * 初始化皮肤数据
     */
    private void initSkinData()
    {
        data = new ArrayList<HashMap<String,Integer>>();
        HashMap<String, Integer> map1 = new HashMap<String, Integer>();
        map1.put("icon", R.drawable.pf);
        map1.put("picture", R.drawable.background);
        data.add(map1);

        HashMap<String, Integer> map2 = new HashMap<String, Integer>();
        map2.put("icon", R.drawable.pf_00);
        map2.put("picture", R.drawable.pic_bg_10);
        data.add(map2);

        HashMap<String, Integer> map3 = new HashMap<String, Integer>();
        map3.put("icon", R.drawable.pf_01);
        map3.put("picture", R.drawable.pic_bg_11);
        data.add(map3);

        HashMap<String, Integer> map4 = new HashMap<String, Integer>();
        map4.put("icon", R.drawable.pf_02);
        map4.put("picture", R.drawable.pic_bg_12);
        data.add(map4);

        HashMap<String, Integer> map5 = new HashMap<String, Integer>();
        map5.put("icon", R.drawable.pf_03);
        map5.put("picture", R.drawable.pic_bg_13);
        data.add(map5);

        HashMap<String, Integer> map6 = new HashMap<String, Integer>();
        map6.put("icon", R.drawable.pf_04);
        map6.put("picture", R.drawable.pic_bg_14);
        data.add(map6);

        HashMap<String, Integer> map7 = new HashMap<String, Integer>();
        map7.put("icon", R.drawable.pf_05);
        map7.put("picture", R.drawable.pic_bg_15);
        data.add(map7);
    }

    /**
     * 显示切换背景的dialog
     */
    public void showChangeSkinDialog() {

        final int defaultSkinRes = sp.getInt(ApplicationGlobal.SKIN_KEY, R.drawable.background);
        LayoutInflater li = LayoutInflater.from(this);
        final MyDialog addDialog = new MyDialog(this, R.style.dialog);
        View view = li.inflate(R.layout.dialog_changeskin,null);
        addDialog.setContentView(view);
        addDialog.setCancelable(true);
        Window window = addDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DeviceUtil.getWindowW(this);
        int height = DeviceUtil.getWindowH(this);
        width = width < height ? width : height;
        lp.width = width;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LinearLayout items = (LinearLayout)addDialog.findViewById(R.id.skin_items);
        skinsView = new ArrayList<ImageView>();
        for (int i = 0; i < data.size(); i++) {
            View v = li.inflate(R.layout.item_changeskin, null);
            WindowManager.LayoutParams llp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            //llp.setMargins(0, 0, CommonUtils.dip2px(this, 20), 0);
//            llp.setMargins(0, 0, CommonUtils.dip2px(CarMainActivity.this, 20), 0);
            v.setLayoutParams(llp);
            ImageView image = (ImageView)v.findViewById(R.id.skin_item_image);
            ImageView ok = (ImageView)v.findViewById(R.id.skin_item_ok);
            skinsView.add(ok);
            image.setBackgroundResource(data.get(i).get("icon"));
            if(defaultSkinRes == data.get(i).get("picture"))
            {
                ok.setVisibility(View.VISIBLE);
            }
            else
            {
                ok.setVisibility(View.GONE);
            }
            final int index = i;
            v.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View arg0) {
                    if(defaultSkinRes != data.get(index).get("picture"))
                    {
                        addDialog.dismiss();
                        //保存皮肤数据
                        sp.edit().putInt(ApplicationGlobal.SKIN_KEY, data.get(index).get("picture")).commit();
                        //发送换肤广播
                        sendBroadcast(new Intent(CHANGE_BG_BROADCAST));
                    }
                }
            });
            items.addView(v);
        }
        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0) {
                addDialog.dismiss();
            }
        });
        addDialog.show();
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            ProgressDialogUtil.showAlertDialogThree(CarMainActivity.this,
//                    "您确定退出系统?", new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View arg0) {
//                            sendBroadcast(new Intent(EXIT_BROADCAST));
//                        }
//                    }, new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View arg0) {
//                        }
//                    });
//            return false;
//        }
//        return super.dispatchKeyEvent(event);
//    }

}

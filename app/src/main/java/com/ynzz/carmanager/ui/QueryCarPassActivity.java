package com.ynzz.carmanager.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ynzz.carmanager.ui.bean.CarManage;
import com.ynzz.carmanager.ui.bean.CarManagerItem;
import com.ynzz.carmanager.ui.bean.StatusIinfo;
import com.ynzz.carmanager.ui.consts.MyApplication;
import com.ynzz.carmanager.ui.util.Tool;
import com.ynzz.lbstest.R;

import java.util.ArrayList;
import java.util.List;

public class QueryCarPassActivity extends SuperActivity implements View.OnClickListener {
    private EditText searchEt;
    private LinearLayout show_ll;
    private ImageView resultIv;
    private TextView resultTv;
    private TextView provinceTv;
    private Button searchBtn;

    private PopupWindow pw;

    List<CarManagerItem> datas = new ArrayList<CarManagerItem>();
    List<StatusIinfo> user_list = new ArrayList<StatusIinfo>();
    private EditCarActivity.UserAdapter user_adapter;
    private String []types={"京","沪","浙","苏","粤","鲁","晋","冀","豫","川","渝","辽","吉","黑","皖","鄂","湘","赣","闽","陕","甘","宁","蒙","津","贵","云","桂","琼","青","新","藏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_query_pass);
        findView();
    }
    public void findView(){
        searchEt= (EditText) findViewById(R.id.search_et);
        show_ll= (LinearLayout) findViewById(R.id.show_layout);
        resultIv= (ImageView) findViewById(R.id.ispass);
        resultTv= (TextView) findViewById(R.id.ispass_hint);
        provinceTv= (TextView) findViewById(R.id.province_tv);
        searchBtn= (Button) findViewById(R.id.search_btn);

        searchBtn.setOnClickListener(this);
        provinceTv.setOnClickListener(this);

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                searchEt.removeTextChangedListener(this);//解除文字改变事件
                searchEt.setText(s.toString().toUpperCase());//转换
                searchEt.setSelection(s.toString().length());//重新设置光标位置
                searchEt.addTextChangedListener(this);//重新绑
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        setActivityTitle("停车查询");
        showRightActionBarBtn(0, "车辆列表", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(QueryCarPassActivity.this,CarListActivity.class);
                startActivity(i2);
            }
        });
        for (int i = 0; i < types.length; i++) {
            StatusIinfo info=new StatusIinfo();
            info.setId(""+i+1);
            info.setName(types[i]);
            info.setIsChoose(0);
            user_list.add(info);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_btn:
                String inputNum=searchEt.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(inputNum)) {
                    Tool.initToast(QueryCarPassActivity.this, "请输入车牌号");
                    return;
                }
                if (!Tool.isCarNumber(inputNum)) {
                    Tool.initToast(QueryCarPassActivity.this, "车牌号格式不正确");
                    return;
                }
                if (inputNum.length()!=6) {
                    Tool.initToast(QueryCarPassActivity.this, "车牌号格式不正确");
                    return;
                }
                String name=provinceTv.getText().toString()+searchEt.getText().toString().trim();
                boolean f = CarManage.getManage(((MyApplication) MyApplication.getAppInstance()).getSQLHelper()).isInfoExists(name);
                if (f) {
                    show_ll.setVisibility(View.VISIBLE);
                    datas = CarManage.getManage(
                            ((MyApplication) MyApplication.getAppInstance())
                                    .getSQLHelper()).QueryCarByName(name);
                    if(datas!=null&&datas.size()>0){
                        String status=datas.get(0).getPass();
                        if(status.equals("0")){
                            resultIv.setImageResource(R.mipmap.icon_car_up);
                            resultTv.setText("车辆停放地面");
                        }else{
                            resultIv.setImageResource(R.mipmap.icon_car_down);
                            resultTv.setText("车辆停放地下");
                        }
                    }else{
                        resultIv.setImageResource(R.mipmap.icon_nocar);
                        resultTv.setText("查无此车辆");
                    }
                }else{
                    show_ll.setVisibility(View.VISIBLE);
                    resultIv.setImageResource(R.mipmap.icon_nocar);
                    resultTv.setText("查无此车辆");
                }
                break;
            case R.id.province_tv:
                showProvincePopup();
                break;
            case R.id.cancel_btn:
                pw.dismiss();
                break;
        }
    }

    public void showProvincePopup() {
        View contentView = LayoutInflater.from(QueryCarPassActivity.this).inflate(R.layout.cs_city_layout, null);
        pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        GridView gridView = (GridView) contentView.findViewById(R.id.pop_gridview);
        Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
        UserAdapter user_adapter=new UserAdapter(QueryCarPassActivity.this,user_list);
        gridView.setAdapter(user_adapter);

        cancelBtn.setOnClickListener(this);

        pw.setOutsideTouchable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        // 重写onKeyListener
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        if (pw.isShowing()) {
            return;
        }
        pw.showAtLocation(findViewById(R.id.bg_view), Gravity.BOTTOM, 20, 0);

    }

    public class UserAdapter extends BaseAdapter {
        private LayoutInflater inflater;//这个一定要懂它的用法及作用{
        List<StatusIinfo> list;

        public UserAdapter(Context context, List<StatusIinfo> items) {
            this.inflater = LayoutInflater.from(context);
            this.list = items;
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
            return list.size();
        }

        @Override
        public StatusIinfo getItem(int position) {
            if (list == null || list.size() <= position) {
                return null;
            }
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //思考这里为何要判断convertView是否为空  ？？
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_province_list, null);
                //通过上面layout得到的view来获取里面的具体控件
                holder.name = (TextView) convertView.findViewById(R.id.item_province_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final StatusIinfo bean=list.get(position);
            if(bean.getName()!=null&&!bean.getName().equals("")){
                holder.name.setText(bean.getName());
            }else{
                holder.name.setText("");
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name=bean.getName();
                    provinceTv.setText(name+"");
                    pw.dismiss();
                }
            });

            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }
}

package com.ynzz.carmanager.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ynzz.carmanager.ui.bean.CarManage;
import com.ynzz.carmanager.ui.bean.CarManagerItem;
import com.ynzz.carmanager.ui.bean.StatusIinfo;
import com.ynzz.carmanager.ui.consts.MyApplication;
import com.ynzz.carmanager.ui.util.CommonUtils;
import com.ynzz.carmanager.ui.util.DeviceUtil;
import com.ynzz.carmanager.ui.util.ProgressDialogUtil;
import com.ynzz.carmanager.ui.util.Tool;
import com.ynzz.carmanager.ui.view.MyDialog;
import com.ynzz.lbstest.R;

import java.util.ArrayList;
import java.util.List;

public class AddCarActivity extends SuperActivity implements View.OnClickListener {

    private EditText carEt;
    private TextView statusTv;
    private TextView provinceTv;
    private Button submitBtn;
    private String status="0";
    private static Dialog currentDialog;
    List<StatusIinfo> user_temp = new ArrayList<StatusIinfo>();
    private String []types={"地面","地下"};
    private ListAdapter adapter;

    private PopupWindow pw;
    List<StatusIinfo> user_list = new ArrayList<StatusIinfo>();
    private UserAdapter user_adapter;
    private String []types1={"京","沪","浙","苏","粤","鲁","晋","冀","豫","川","渝","辽","吉","黑","皖","鄂","湘","赣","闽","陕","甘","宁","蒙","津","贵","云","桂","琼","青","新","藏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_addcar);
        findView();
        initView();
    }
    public void findView(){
        carEt= (EditText) findViewById(R.id.car_et);
        statusTv= (TextView) findViewById(R.id.carpassType);
        submitBtn= (Button) findViewById(R.id.submit_btn);
        provinceTv= (TextView) findViewById(R.id.carNum1);

        statusTv.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        provinceTv.setOnClickListener(this);
    }

    public void initView(){
        setActivityTitle("添加车辆");
        carEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                carEt.removeTextChangedListener(this);//解除文字改变事件
                carEt.setText(s.toString().toUpperCase());//转换
                carEt.setSelection(s.toString().length());//重新设置光标位置
                carEt.addTextChangedListener(this);//重新绑
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        for (int i = 0; i < types.length; i++) {
            StatusIinfo info=new StatusIinfo();
            info.setId(""+i+1);
            info.setName(types[i]);
            if(i==0){
                info.setIsChoose(1);
            }else{
                info.setIsChoose(0);
            }
            user_temp.add(info);
        }
        for (int i = 0; i < types1.length; i++) {
            StatusIinfo info=new StatusIinfo();
            info.setId(""+i+1);
            info.setName(types1[i]);
            info.setIsChoose(0);
            user_list.add(info);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.carpassType:
                showStatusPopup(AddCarActivity.this);
                break;
            case R.id.carNum1:
                showProvincePopup();
                break;
            case R.id.cancel_btn:
                pw.dismiss();
                break;
            case R.id.submit_btn:
                String inputNum=carEt.getText().toString().trim().toUpperCase();
                if (TextUtils.isEmpty(inputNum)) {
                    Tool.initToast(AddCarActivity.this, "请输入车牌号");
                    return;
                }
                if (!Tool.isCarNumber(inputNum)) {
                    Tool.initToast(AddCarActivity.this, "车牌号格式不正确");
                    return;
                }
                if (inputNum.length()!=6) {
                    Tool.initToast(AddCarActivity.this, "车牌号格式不正确");
                    return;
                }
                String carNmae=provinceTv.getText().toString()+inputNum;
                boolean f = CarManage.getManage(((MyApplication) MyApplication.getAppInstance()).getSQLHelper()).isInfoExists(carNmae);
                if (!f) {
                    CarManagerItem info =new CarManagerItem();
                    String numid= CommonUtils.getRandomString(8);
                    info.setPkid(numid);
                    info.setName(carNmae);
                    info.setPass(status);
                    CarManage.getManage(((MyApplication) MyApplication.getAppInstance()).getSQLHelper()).saveUserChannel(info);
                    Toast.makeText(AddCarActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    carEt.setText("");
                }else{
                    Toast.makeText(AddCarActivity.this,"车辆已存在,请重新添加",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public Dialog showStatusPopup(Context context) {
        ProgressDialogUtil.dismiss();
        currentDialog = new MyDialog(context, R.style.update_dialog);
        currentDialog.setContentView(R.layout.cs_status_layout);
        Window window = currentDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DeviceUtil.getWindowW(context);
        int height = DeviceUtil.getWindowH(context);
        width = width < height ? width : height;
        lp.width = (int) (0.7 * width);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        currentDialog.setCancelable(true);

        final ListView listView = (ListView) currentDialog
                .findViewById(R.id.list);

        adapter = new ListAdapter(AddCarActivity.this,user_temp);
        listView.setAdapter(adapter);

        if (!this.isFinishing())
            currentDialog.show();
        return currentDialog;
    }

    /**
     * adapter
     */
    private class ListAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        private Context context;
        private List<StatusIinfo> list = new ArrayList<StatusIinfo>();

        public ListAdapter(Context context, List<StatusIinfo> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public StatusIinfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final StatusIinfo user = getItem(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.item_status, null);
                viewHolder.textView = (TextView) convertView
                        .findViewById(R.id.item_status_name);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            boolean selected = user.getIsChoose()==0? false : true;
            if(selected){
                viewHolder.textView.setTextColor(Color.parseColor("#1e82d2"));
            }else{
                viewHolder.textView.setTextColor(Color.parseColor("#4a4a4a"));
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO 自动生成的方法存根
                    boolean selected = user.getIsChoose()==0? false : true;
                    if(position==0){
                        status="0";
                        list.get(0).setIsChoose(1);
                        list.get(1).setIsChoose(0);
                    }else{
                        status="1";
                        list.get(1).setIsChoose(1);
                        list.get(0).setIsChoose(0);
                    }
                    notifyDataSetChanged();
                    String names=status.equals("0")?"地面":"地下";
                    statusTv.setText(""+names);
                    currentDialog.dismiss();
                }
            });
            // 加载昵称
            if (user.getName().equals("")) {
                viewHolder.textView.setText(user.getId());
            }else{
                viewHolder.textView.setText(user.getName());
            }
            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }
    }

    public void showProvincePopup() {
        View contentView = LayoutInflater.from(AddCarActivity.this).inflate(R.layout.cs_city_layout, null);
        pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        GridView gridView = (GridView) contentView.findViewById(R.id.pop_gridview);
        Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
        user_adapter=new UserAdapter(AddCarActivity.this,user_list);
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

        public UserAdapter(Context context,List<StatusIinfo> items) {
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

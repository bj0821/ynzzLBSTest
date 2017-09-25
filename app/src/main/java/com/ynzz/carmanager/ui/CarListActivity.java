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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ynzz.carmanager.ui.adapter.CarListAdapter;
import com.ynzz.carmanager.ui.bean.CarManage;
import com.ynzz.carmanager.ui.bean.CarManagerItem;
import com.ynzz.carmanager.ui.bean.StatusIinfo;
import com.ynzz.carmanager.ui.consts.MyApplication;
import com.ynzz.carmanager.ui.util.DeviceUtil;
import com.ynzz.carmanager.ui.util.ProgressDialogUtil;
import com.ynzz.carmanager.ui.view.ClearEditText;
import com.ynzz.carmanager.ui.view.MyDialog;
import com.ynzz.lbstest.R;

import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends SuperActivity implements View.OnClickListener {
    private ClearEditText searchEt;
    private ListView listview;
    private ImageView delBtn;
    private ImageView editBtn;
    private LinearLayout none_ll;
    private TextView provinceTv;
    //适配器
    private CarListAdapter adapter;
    List<CarManagerItem> datas = new ArrayList<CarManagerItem>();
    List<CarManagerItem> dataValues = new ArrayList<CarManagerItem>();
    private static Dialog currentDialog;
    private static Dialog currentDialog1;
    private static Dialog currentDialog2;
    private String status="0";
    List<StatusIinfo> user_temp = new ArrayList<StatusIinfo>();
    private String []types={"地面","地下"};
    private ListAdapter adapter1;
    private TextView textviewState;
    private int isEdit=0;//0未编辑1编辑
    private PopupWindow pw;
    List<StatusIinfo> user_list = new ArrayList<StatusIinfo>();
    private UserAdapter user_adapter;
    private String []types1={"京","沪","浙","苏","粤","鲁","晋","冀","豫","川","渝","辽","吉","黑","皖","鄂","湘","赣","闽","陕","甘","宁","蒙","津","贵","云","桂","琼","青","新","藏"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_carlist);
        findView();
        initView();
    }
    public void findView(){
        searchEt= (ClearEditText) findViewById(R.id.txtSearch);
        listview= (ListView) findViewById(R.id.car_list);
        delBtn= (ImageView) findViewById(R.id.delete_btn);
        editBtn= (ImageView) findViewById(R.id.edit_btn);
        none_ll= (LinearLayout) findViewById(R.id.nocontent_layout);
        delBtn.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }

    public void initView() {
        setActivityTitle("车辆列表");
        adapter=new CarListAdapter(CarListActivity.this);
        // 设置列表adapter
        listview.setAdapter(adapter);

        for (int i = 0; i < types.length; i++) {
            StatusIinfo info=new StatusIinfo();
            info.setId(""+i+1);
            info.setName(types[i]);
            info.setIsChoose(0);
            user_temp.add(info);
        }

        for (int i = 0; i < types1.length; i++) {
            StatusIinfo info=new StatusIinfo();
            info.setId(""+i+1);
            info.setName(types1[i]);
            info.setIsChoose(0);
            user_list.add(info);
        }

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
                String s=editable.toString().trim();
                if(TextUtils.isEmpty(s)){
                    adapter.setItems(datas,isEdit);
                }else{
                    BindData(s.toUpperCase());
                }
            }
        });

        showRightActionBarBtn(0, "全选", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((TextView)view).getText().equals("全选")){
                    for (int i = 0; i < dataValues.size(); i++) {
                        dataValues.get(i).setIsChoose(1);
                    }
                    rightActionBarBtn.setText("反选");
                    rightActionBarBtn2.setVisibility(View.VISIBLE);
                }else{//反选
                    for (int i = 0; i < dataValues.size(); i++) {
                        if (dataValues.get(i).getIsChoose()==1) {
                            dataValues.get(i).setIsChoose(0);
                        }else {
                            dataValues.get(i).setIsChoose(1);
                        }
                    }
                }
                isEdit=1;
                adapter.setItems(dataValues,isEdit);
            }
        });

        showRightActionBarBtn2(0, "清空", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i <dataValues.size() ; i++) {
                    dataValues.get(i).setIsChoose(0);
                }
                rightActionBarBtn2.setVisibility(View.GONE);
                rightActionBarBtn.setText("全选");
                isEdit=0;
                adapter.setItems(dataValues,isEdit);
            }
        });
        rightActionBarBtn2.setVisibility(View.GONE);

    }

    /**
     * 加载网络数据
     */
    private void getData() {
        datas = CarManage.getManage(
                ((MyApplication) MyApplication.getAppInstance())
                        .getSQLHelper()).getUserChannel();
        dataValues=datas;
        if (datas != null && datas.size() > 0) {
            none_ll.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            adapter.setItems(datas,isEdit);
        } else {
            none_ll.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }

    private void BindData(String where) {
        if(datas!=null&&datas.size()>0){
            List<CarManagerItem> list = new ArrayList<CarManagerItem>();
            for (int i = 0; i < datas.size(); i++) {
                CarManagerItem info=datas.get(i);
                if(info.getName().contains(where)){
                    list.add(info);
                }
            }
            none_ll.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            adapter.setItems(list,isEdit);
        }else{
            none_ll.setVisibility(View.VISIBLE);
            listview.setVisibility(View.GONE);
        }
    }

    public void saveValues(List<CarManagerItem> data) {
        dataValues = data;
    }

//    public Dialog showUpdatePopup(Context context, final String pkid, String number, String state) {
//        ProgressDialogUtil.dismiss();
//        status=state;
//        currentDialog = new MyDialog(context, R.style.update_dialog);
//        currentDialog.setContentView(R.layout.cs_update_layout);
//        Window window = currentDialog.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        int width = DeviceUtil.getWindowW(context);
//        int height = DeviceUtil.getWindowH(context);
//        width = width < height ? width : height;
//        lp.width = (int) (0.7 * width);
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        currentDialog.setCancelable(true);
//        currentDialog.setCanceledOnTouchOutside(false);
//
//        final EditText edittext = (EditText) currentDialog
//                .findViewById(R.id.car_et);
//        textviewState = (TextView) currentDialog
//                .findViewById(R.id.carpassType);
//        provinceTv = (TextView) currentDialog
//                .findViewById(R.id.carNum1);
//        final Button btn1 = (Button) currentDialog
//                .findViewById(R.id.dialog_enter);
//        final Button btn2 = (Button) currentDialog
//                .findViewById(R.id.dialog_cancel);
//
//        edittext.setText(number+"");
//        if(status.equals("0")){
//            textviewState.setText("地面");
//            user_temp.get(0).setIsChoose(1);
//        }else{
//            textviewState.setText("地下");
//            user_temp.get(1).setIsChoose(1);
//        }
//        textviewState.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showStatusPopup(CarListActivity.this);
//            }
//        });
//        provinceTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showProvincePopup();
//            }
//        });
//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String inputNum=edittext.getText().toString().trim().toUpperCase();
//                if (TextUtils.isEmpty(inputNum)) {
//                    Tool.initToast(CarListActivity.this, "请输入车牌号");
//                    return;
//                }
//                if (!Tool.isCarNumber(inputNum)) {
//                    Tool.initToast(CarListActivity.this, "车牌号格式不正确");
//                    return;
//                }
//                if (inputNum.length()!=6) {
//                    Tool.initToast(CarListActivity.this, "车牌号格式不正确");
//                    return;
//                }
//                String carNmae=provinceTv.getText().toString()+inputNum;
//                CarManage.getManage(((MyApplication) MyApplication.getAppInstance()).getSQLHelper()).updateChannel(pkid,carNmae,status);
//                Toast.makeText(CarListActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
//                datas = CarManage.getManage(
//                        ((MyApplication) MyApplication.getAppInstance())
//                                .getSQLHelper()).getUserChannel();
//                adapter.setItems(datas);
//                currentDialog.dismiss();
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currentDialog.dismiss();
//            }
//        });
//        if (!this.isFinishing())
//            currentDialog.show();
//        return currentDialog;
//    }

//    public Dialog showStatusPopup(Context context) {
//        ProgressDialogUtil.dismiss();
//        currentDialog1 = new MyDialog(context, R.style.update_dialog);
//        currentDialog1.setContentView(R.layout.cs_status_layout);
//        Window window = currentDialog1.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        int width = DeviceUtil.getWindowW(context);
//        int height = DeviceUtil.getWindowH(context);
//        width = width < height ? width : height;
//        lp.width = (int) (0.7 * width);
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        currentDialog1.setCancelable(true);
//
//        final ListView listView = (ListView) currentDialog1
//                .findViewById(R.id.list);
//
//        adapter1 = new ListAdapter(CarListActivity.this,user_temp);
//        listView.setAdapter(adapter1);
//
//        if (!this.isFinishing())
//            currentDialog1.show();
//        return currentDialog1;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public Dialog showDeletePopup(Context context) {
        ProgressDialogUtil.dismiss();
        currentDialog2 = new MyDialog(context, R.style.update_dialog);
        currentDialog2.setContentView(R.layout.my_dialog);
        Window window = currentDialog2.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DeviceUtil.getWindowW(context);
        int height = DeviceUtil.getWindowH(context);
        width = width < height ? width : height;
        lp.width = (int) (0.7 * width);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        currentDialog2.setCancelable(true);

        final TextView textView = (TextView) currentDialog2
                .findViewById(R.id.dialog_result0);
        final Button btn1 = (Button) currentDialog2
                .findViewById(R.id.dialog_enter);
        final Button btn2 = (Button) currentDialog2
                .findViewById(R.id.dialog_cancel);

        textView.setText("您确定删除数据?");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataValues != null) {
                    for (int i = 0; i < dataValues.size(); i++) {
                        String pkid=dataValues.get(i).getPkid();
                        //选中的栏目
                        if (dataValues.get(i).getIsChoose() != 0) {
                            CarManage.getManage(((MyApplication) MyApplication.getAppInstance()).getSQLHelper()).deleteChannelById(pkid);
                        }
                    }
                    datas = CarManage.getManage(
                            ((MyApplication) MyApplication.getAppInstance())
                                    .getSQLHelper()).getUserChannel();
                    adapter.setItems(datas,isEdit);
                }
                currentDialog2.dismiss();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDialog2.dismiss();
            }
        });

        if (!this.isFinishing())
            currentDialog2.show();
        return currentDialog2;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.delete_btn:
                if(isEdit==1){
                    int flag=0;
                    if (dataValues != null) {
                        for (int i = 0; i < dataValues.size(); i++) {
                            //选中的栏目
                            if (dataValues.get(i).getIsChoose() != 0) {
                                flag=1;
                            }
                        }
                    }
                    if(flag==0){
                        Toast.makeText(CarListActivity.this,"请至少选择一条进行操作",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showDeletePopup(CarListActivity.this);
                }
                break;
            case R.id.edit_btn:
                isEdit=1;
                adapter.setItems(dataValues,isEdit);
                rightActionBarBtn2.setVisibility(View.VISIBLE);
                break;
            case R.id.cancel_btn:
                pw.dismiss();
                break;
        }
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
                    textviewState.setText(""+names);
                    currentDialog1.dismiss();
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
        View contentView = LayoutInflater.from(CarListActivity.this).inflate(R.layout.cs_city_layout, null);
        pw = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        GridView gridView = (GridView) contentView.findViewById(R.id.pop_gridview);
        Button cancelBtn = (Button) contentView.findViewById(R.id.cancel_btn);
        user_adapter=new UserAdapter(CarListActivity.this,user_list);
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
        pw.showAtLocation(findViewById(R.id.update_layout), Gravity.BOTTOM, 20, 0);

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

package com.ynzz.carmanager.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ynzz.carmanager.ui.CarListActivity;
import com.ynzz.carmanager.ui.EditCarActivity;
import com.ynzz.carmanager.ui.bean.CarManagerItem;
import com.ynzz.carmanager.ui.util.CommonUtils;
import com.ynzz.lbstest.R;

import java.util.ArrayList;
import java.util.List;


public class CarListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<CarManagerItem> data = new ArrayList<CarManagerItem>();
    private CarListActivity context;
    private int m=2;
    private int types=0;

    public CarListAdapter(CarListActivity context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public List<CarManagerItem> getData() {
        return data;
    }

    public void setItems(List<CarManagerItem> items,int type) {
        this.data = items;
        this.types=type;
        notifyDataSetChanged();
    }

    public void addItems(List<CarManagerItem> items) {
        data = (List<CarManagerItem>) CommonUtils.addToArrayList2(items, data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (data == null || data.size() == 0) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        if (data == null || data.size() <= position) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView != null && convertView.getTag() != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_car_list, null);
            viewHolder.cb = (CheckBox) convertView.findViewById(R.id.checkbox);
            viewHolder.name = (TextView) convertView.findViewById(R.id.item_car_name);
            viewHolder.pass = (TextView) convertView.findViewById(R.id.item_car_pass);
            viewHolder.edit = (TextView) convertView.findViewById(R.id.item_car_edit);
            convertView.setTag(viewHolder);
        }
        // 得到当前要显示的类
        final CarManagerItem bean = data.get(position);
        viewHolder.name.setText(bean.getName() + "");
        if(bean.getPass().equals("0")){
            viewHolder.pass.setText("地面");
        }else{
            viewHolder.pass.setText("地下");
        }
        if(types==0){
            viewHolder.cb.setVisibility(View.GONE);
            viewHolder.edit.setVisibility(View.GONE);
        }else{
            viewHolder.cb.setVisibility(View.VISIBLE);
            viewHolder.edit.setVisibility(View.VISIBLE);
        }
        if(bean.getIsChoose()!=0){
            viewHolder.cb.setChecked(true);
        }else{
            viewHolder.cb.setChecked(false);
        }
        viewHolder.cb.setOnClickListener(new MyClick(position,viewHolder.cb,bean,m,data));
        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, EditCarActivity.class);
                i.putExtra("id",bean.getPkid());
                i.putExtra("name",bean.getName());
                i.putExtra("pass",bean.getPass());
                context.startActivity(i);
            }
        });
        return convertView;
    }

    class ViewHolder {
        CheckBox cb;
        TextView name;
        TextView pass;
        TextView edit;
    }

    class MyClick implements View.OnClickListener {
        private int pos;
        private int mNum;
        private CheckBox check;
        CarManagerItem bean;
        List<CarManagerItem> list;
        public MyClick(int position,CheckBox check,CarManagerItem bean,int mNum,List<CarManagerItem> list){
            this.pos=position;
            this.mNum=mNum;
            this.check=check;
            this.bean=bean;
            this.list=list;
        }

        @Override
        public void onClick(View view) {
            if(bean.getIsChoose()==0){
                if(mNum%2==0){
                    bean.setIsChoose(1);
                    check.setChecked(true);
                }else{
                    bean.setIsChoose(0);
                    check.setChecked(false);
                }
            }else{
                if(bean.getIsChoose()==1){
                    if(mNum%2==0){
                        bean.setIsChoose(0);
                        check.setChecked(false);
                    }else{
                        bean.setIsChoose(1);
                        check.setChecked(true);
                    }
                }
            }
            context.saveValues(data);
            notifyDataSetChanged();
        }
    }

}

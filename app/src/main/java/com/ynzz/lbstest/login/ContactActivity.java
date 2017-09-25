package com.ynzz.lbstest.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ynzz.lbstest.AboutActivity;
import com.ynzz.lbstest.R;

public class ContactActivity extends AppCompatActivity {
    private ListView mDataList;

    private String[] mData = new String[]{
            "公司名称：云南中智物联网科技有限公司",
            "公司网址：www.ynzziot.com",
            "联系电话：15559675778",
            "公司地址：云南省昆明市盘龙区七彩俊园4栋903"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        initToolbar();

        initList();
    }
    //initList
    private void initList() {

        mDataList = (ListView) findViewById(R.id.activity_about_list);

        mDataList.setAdapter(new ArrayAdapter<String>(ContactActivity.this, R.layout.simp_text, mData));
    }

    //initToolbar
    private void initToolbar() {

        View rootView = findViewById(R.id.activity_about_toolbar);

        if (rootView != null) {

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            toolbar.setTitle("联系我们");

            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContactActivity.this.finish();
                }
            });
        }
    }
}

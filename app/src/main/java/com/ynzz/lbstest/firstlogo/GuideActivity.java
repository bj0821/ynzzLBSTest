package com.ynzz.lbstest.firstlogo;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ynzz.lbstest.LoginActivity;
import com.ynzz.lbstest.R;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private View one;
    private View two;
    private View three;
    private ArrayList<View> lists;
    private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
    }
    /**
     * 初始化数据
     */
    private void initView() {
        //控件
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        //实现各个页面的布局
        LayoutInflater items = LayoutInflater.from(this);
        one = items.inflate(R.layout.one_guide, null);
        two = items.inflate(R.layout.two_guide, null);
        three = items.inflate(R.layout.three_guide, null);


        //实例化ArrayList对象
        lists = new ArrayList<View>();
        //将每个页面添加集合中展示
        lists.add(one);
        lists.add(two);
        lists.add(three);

        //给ViewPager做页面改变监听
        viewPager.setOnPageChangeListener(new OnViewPageChangeListener());

        //设置设配器
        viewPager.setAdapter(new ViewPagerAdapter(lists));


        //寻找第三页面的Button按钮
        btn = (Button) three.findViewById(R.id.button);
        //给按钮做监听
        btn.setOnClickListener(new OnBtnClickListener());

    }

    /**
     * 给按钮做的监听
     * @author Administrator
     */
    private final class OnBtnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            //存入数据并提交
//			WelcomeActivity.sp.edit().putInt("VERSION", WelcomeActivity.VERSION).commit();

            Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private final class OnViewPageChangeListener implements ViewPager.OnPageChangeListener {

        //界面改变的时候显示按钮(最主要的是当滑动到最后一个页面的时候让它显示出来)
        //最后一张图片ImageRes.size()-1
        @Override
        public void onPageSelected(int position) {

            if (position==lists.size()-1) {
                btn.setVisibility(View.VISIBLE);//最后一个显示按钮Button
            }else {
                btn.setVisibility(View.GONE);
            }

        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    }

}

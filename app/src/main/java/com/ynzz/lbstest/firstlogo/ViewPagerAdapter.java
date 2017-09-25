package com.ynzz.lbstest.firstlogo;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 用来绑定ViewPager的Adapter
 * @author Administrator
 */
public class ViewPagerAdapter extends PagerAdapter {
	
	private ArrayList<View> lists;

	public ViewPagerAdapter(ArrayList<View> lists) {
		
		this.lists = lists;
	}

	@Override
	public int getCount() {
		
		if (lists!=null) {
			
			return lists.size();
		}
		
		return 0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		
		return arg0 == arg1;
	}

	/**
	 * 销毁Position的位置
	 */


	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
//		super.destroyItem(container, position, object);
		container.removeView((View) object);
		
	}

	/**
	 * 初始化Position位置页面
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		container.addView(lists.get(position),0);
		
		return lists.get(position);
	}
}

package com.ynzz.carmanager.ui.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ynzz.carmanager.ui.consts.ApplicationGlobal;
import com.ynzz.carmanager.ui.consts.MyApplication;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CommonUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 计算ListView真实高度，使得ListView能在ScrollView中正常显示
     * <p>Description: The setListViewHeightBasedOnChildren</p>
     *
     * @param listView
     * @author: caie
     * @update: [updatedate] [changer][change description]
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 去小数点后两位并装换成String返回
     *
     * @param value
     * @return
     */
    public static String get2Double(double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    /**
     * 某个页面是否显示在当前
     * <p>Description: The isShowContextFront</p>
     *
     * @param activity
     * @return
     * @update: [updatedate] [changer][change description]
     */
    public static boolean isShowContextFront(Context context, Class activity) {
        if (activity == null) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(10);
        if (list.get(0).topActivity.getClassName() != null
                && activity.getName()
                .contains((list.get(0).topActivity.getClassName()))) {
            return true;
        }
        return false;
    }

    /**
     * app是否运行在手机前台
     *
     * @param activity
     * @return
     */
    public static boolean isTopApp(Context activity) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            //应用程序位于堆栈的顶层
            if (tasksInfo.get(0).topActivity.getPackageName().contains(ApplicationGlobal.APPPACKAGENAME)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 　　 * 判断当前界面是否是桌面
     *
     */
    public static boolean isHome(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<RunningTaskInfo> rti = am.getRunningTasks(1);
        return getHomes(context).contains(rti.get(0).topActivity.getPackageName());
    }

    /**
     * 获得属于桌面的应用的应用包名称
     * 　　 *
     * 　　 * @return 返回包含所有包名的字符串列表
     *
     */
    public static List<String> getHomes(Context context) {
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    /**
     * 把一个集合中得数据添加到另一个集合中
     *
     * @param data
     * @param dest
     * @return
     */
    public static ArrayList addToArrayList(ArrayList data, ArrayList dest) {
        if (dest == null) {
            dest = new ArrayList<Object>();
        }
        if (data != null && data.size() > 0) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                dest.add(data.get(i));
            }
        }
        return dest;
    }

    /**
     * 把一个集合中得数据添加到另一个集合中
     *
     * @param data
     * @param dest
     * @return
     */
    public static List addToArrayList2(List data, List dest) {
        if (dest == null) {
            dest = new ArrayList<Object>();
        }
        if (data != null && data.size() > 0) {
            int size = data.size();
            for (int i = 0; i < size; i++) {
                dest.add(data.get(i));
            }
        }
        return dest;
    }

    /**
     * 设置晃动动画
     */
    public static void setShakeAnimation(View view) {
        view.startAnimation(shakeAnimation(5));
    }

    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    /**
     * 获取资源颜色
     *
     * @param res
     * @return
     */
    public static int getResColor(int res) {
        return MyApplication.getAppInstance().getResources().getColor(res);
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

}
package com.ynzz.carmanager.ui.util;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.DatePicker;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * activity常用功能类
 *
 * @author zhukui
 */
public class CommonUtil {
    private Context context;
    private Calendar calendar;

    public CommonUtil(Context context) {
        calendar = Calendar.getInstance();
        this.context = context;
    }

    /**
     * 判断手机是否能联网
     *
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为空
     */
    public boolean stringNotEmpty(String string) {
        return (string != null && !"".equals(string.trim()) && !"null"
                .equals(string.trim()));
    }

    /**
     * 获取时间控件:yyyy-MM-dd
     *
     * @return
     */
    public void getTime(TextView textView) {
        final TextView tv = textView;
        String time = tv.getText().toString();
        if (time.equals("")) {
            new DatePickerDialog(this.context, new OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);
                    if ((monthOfYear + 1) < 10) {
                        month = "0" + month;
                    }
                    if (dayOfMonth < 10) {
                        day = "0" + day;
                    }
                    tv.setText(String.valueOf(year) + "-" + month + "-" + day);
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)).show();
        } else {
            int cyear = calendar.get(Calendar.YEAR);
            int cmoth = calendar.get(Calendar.MONTH);
            int cdate = calendar.get(Calendar.DATE);
            try {
                if (time.length() >= 10) {
                    cyear = Integer.parseInt(time.substring(0, 4));
                    cmoth = Integer.parseInt(time.substring(5, 7)) - 1;
                    cdate = Integer.parseInt(time.substring(8, 10));
                }
            } catch (Exception e) {
            }

            final int year = cyear;
            final int moth = cmoth;
            final int date = cdate;

            new DatePickerDialog(this.context, new OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    String month = String.valueOf(monthOfYear + 1);
                    String day = String.valueOf(dayOfMonth);
                    if ((monthOfYear + 1) < 10) {
                        month = "0" + month;
                    }
                    if (dayOfMonth < 10) {
                        day = "0" + day;
                    }
                    tv.setText(String.valueOf(year) + "-" + month + "-" + day);
                }
            }, year, moth, date).show();
        }

    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());
        return time;
    }

    /**
     * 判断存储卡是否存在
     */
    public boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断手机是否能联网
     *
     * @return
     */
    public boolean checkNetWork(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable() && info.isConnected()) {
            // do something
            // 能联网
            return true;
        } else {
            // do something
            // 不能联网
            return false;
        }

    }

    /**
     * 加载进度框
     *
     * @param title
     * @param msg
     * @return
     */
    public ProgressDialog showProg(String title, String msg) {
        ProgressDialog progressDialog = ProgressDialog.show(this.context,
                title, msg, true);
        progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
        progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
        return progressDialog;
    }

    /***
     * 系统自带加载进度框
     *
     * @param msg
     * @return
     */
    public ProgressDialog showProgressDialogNormal(String msg) {
        ProgressDialog progressDialog = ProgressDialog.show(this.context, null,
                msg, true);
        progressDialog.setIndeterminate(true);// 设置进度条是否为不明确
        progressDialog.setCancelable(true);// 设置进度条是否可以按退回键取消
        return progressDialog;
    }


    /**
     * 页面跳转
     */
    @SuppressWarnings("rawtypes")
    public void startMyActivity(Class myclass) {
        Intent intent = new Intent(context, myclass);
        context.startActivity(intent);
    }


    public String wrapHeader(String content) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        builder.append("<html><head>");
        builder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        builder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\"/>");
        builder.append("<title></title></head><body>");
        builder.append(content);
        builder.append("</body>");
        return builder.toString();
    }

    /**
     * 日期转换为星期
     *
     * @param day
     * @return
     * @throws ParseException
     */
    public String dayToWeek(String day) {
        String showDate = "";
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(day);
            Calendar cd = Calendar.getInstance();
            cd.setTime(date);
            int mydate = cd.get(Calendar.DAY_OF_WEEK); // 获取指定日期转换成星期几
            switch (mydate) {
                case 1:
                    showDate = "星期日";
                    break;
                case 2:
                    showDate = "星期一";
                    break;
                case 3:
                    showDate = "星期二";
                    break;
                case 4:
                    showDate = "星期三";
                    break;
                case 5:
                    showDate = "星期四";
                    break;
                case 6:
                    showDate = "星期五";
                    break;
                default:
                    showDate = "星期六";
                    break;
            }
        } catch (ParseException e) {
            showDate = "";
            e.printStackTrace();
        }
        return showDate;
    }

    /**
     * 取得jsonObject的值
     *
     * @param key :jsonObject的key
     * @throws JSONException
     */
    public String jsonObjectValue(String key, JSONObject jsonObject)
            throws JSONException {
        if (key == null)
            return "";
        return jsonObject.has(key) ? (jsonObject.getString(key).equals("null") ? ""
                : jsonObject.getString(key))
                : "";
    }

    /***
     * 判断一个apk是否在本机安装过了
     *
     * @param packageName
     * @return
     */
    public boolean isPkgInstalled(String packageName) {
        PackageManager pm = this.context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            return false;
        }
        return true;
    }

    /***
     * 安装确认包名的apk
     *
     */
    public void installApp(String package_name) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), package_name)),
                "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /***
     * 进入确认包名的apk
     *
     */
    public void enterApp(String package_name) {
        PackageManager pm = context.getPackageManager();

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(package_name);

        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String packageName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            ComponentName cn = new ComponentName(packageName, className);

            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

}

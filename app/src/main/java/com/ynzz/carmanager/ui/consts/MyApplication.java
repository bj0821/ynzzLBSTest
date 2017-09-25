package com.ynzz.carmanager.ui.consts;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.ynzz.carmanager.ui.db.SQLHelper;
import com.ynzz.carmanager.ui.util.DebugUtil;
import com.ynzz.carmanager.ui.util.FileUtils;

import java.io.File;
import java.util.List;


/**
 * Created by ZXL on 2016/12/1.
 */

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    /**
     * 应用上下文对象
     */
    private static Context context;

    private SQLHelper sqlHelper;

    /**
     * 获取应用app上下文对象
     *
     * @return
     */
    public static Context getAppInstance() {
        return context;
    }

    /**
     * 获取本地存储对象
     *
     * @return
     */
    public static SharedPreferences getSharePre() {
        return getAppInstance().getSharedPreferences(ApplicationGlobal.SPDATANAME, Context.MODE_PRIVATE);
//        return getAppInstance().getSharedPreferences(ApplicationGlobal.SPDATANAME, Context.MODE_PRIVATE);
    }

    public static String getImei(Context context) {
        String imei = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.e("getImei", e.getMessage());
        }
        return imei;
    }

    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> infos = activityManager.getRunningTasks(10);
        for (ActivityManager.RunningTaskInfo info : infos) {
            String name = info.topActivity.getClassName();
            if (activityClassName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void LogOut() {
        getSharePre().edit().putString(ApplicationGlobal.USERID, "").commit();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //初始化程序目录
        initFolderDir();

    }

    /**
     * 获取数据库Helper
     */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(context);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
        //整体摧毁的时候调用这个方法
    }

    /**
     * 创建目录
     */
    private void initFolderDir() {
        FileUtils fu = new FileUtils();
        fu.createSDDir("");
        //图片目录
        fu.createSDDir(File.separatorChar + ApplicationGlobal.imageBaseFoldName);
        //临时目录
        fu.createSDDir(File.separatorChar + ApplicationGlobal.tempBaseFoldName);
        fu.createSDDir(File.separatorChar + ApplicationGlobal.cacheBaseFoldName);
        DebugUtil.d(TAG, "程序目录构建完成");
    }

    /**
     * 创建存储缓存的文件夹路径
     *
     * @return
     */
    private File createSavePath() {
        String path;
        if (Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED).equals(Environment.MEDIA_MOUNTED)) {
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            path = Environment.getExternalStorageDirectory().getPath() + "/TestCash/";
            path = ApplicationGlobal.cachePath;
        } else {
            path = "/carCache/";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    //获取版本号
    public static String getVerName() {
        String verName = "";
        try {
            if(context != null){
                verName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0.0";
        }
        return verName;
    }
}

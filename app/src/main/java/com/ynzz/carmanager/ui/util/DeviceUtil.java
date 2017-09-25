package com.ynzz.carmanager.ui.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取设备相关信息的类
 *
 * @author jinzufan
 * @time 2013-08-20
 */
public class DeviceUtil {

    private static String ESN = "";
    private static String MODEL = "";
    private static String RELEASE = "";
    private static String VERSION_CODES = "";

    private static String IMSI = "";//460036941090298

    // 获取ESN
    public static String getESN(Context context) {
        if (ESN == null || ESN.equals("") || ESN.length() == 0) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                ESN = telephonyManager.getDeviceId();
            } catch (Exception ex) {

            }
        }
        return ESN;
    }

    // 获取IMSI
    public static String getIMSI(Context context) {
        if (IMSI == null || IMSI.length() == 0) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                IMSI = telephonyManager.getSubscriberId();
            } catch (Exception ex) {

            }
        }
        return IMSI;
    }

    // 获取MODEL
    public static String getMODEL(Context context) {
        if (MODEL == null || MODEL.equals("") || MODEL.length() == 0) {
            MODEL = Build.MODEL;
        }
        return MODEL;
    }

    // 获取RELEASE
    public static String getRELEASE(Context context) {
        if (RELEASE == null || RELEASE.equals("") || RELEASE.length() == 0) {
            RELEASE = Build.VERSION.RELEASE;
        }
        return RELEASE;
    }

    // 获取VERSION_CODES
    public static String getVERSIONCODES(Context context) {
        if (VERSION_CODES == null || VERSION_CODES.equals("") || VERSION_CODES.length() == 0) {
            VERSION_CODES = Build.BRAND;
//            VERSION_CODES = android.os.Build.VERSION_CODES;
        }
        return VERSION_CODES;
    }

    /**
     * 检查是否有网络
     *
     * @param context
     * @return
     */
    public static boolean checkNetWorkReady(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo mobileInfo = conMan.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE);
        State mobile = null;
        if (mobileInfo != null) {
            mobile = mobileInfo.getState();
        }
        if (mobile != null && mobile == State.CONNECTED) {
            return true;
        } else {
            mobileInfo = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobileInfo != null) {
                mobile = mobileInfo.getState();
            }
            if (mobile != null && mobile == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否有SDCard
     *
     * @return
     */
    public static boolean checkSDCardReady() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取SDCard的状态
     *
     * @return SDCard 可用的状态
     */
    public static boolean isSDCardUsable() {
        boolean SDCardMounted = false;
        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            SDCardMounted = true;
        }

        // 是否正在检测SD卡
        if (Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED).equals(Environment.MEDIA_CHECKING)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_NOFS)) {
            SDCardMounted = false;
        }

        // 检测是否插有SD卡
        if (Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED).equals(Environment.MEDIA_REMOVED)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            SDCardMounted = false;
        }

        // 检测SD卡是否连接电脑共享
        if (Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED).equals(Environment.MEDIA_SHARED)) {
            SDCardMounted = false;
        }

        return SDCardMounted;
    }

    /**
     * 获取SDCard剩下的大小
     *
     * @return SDCard剩下的大小
     * @since V1.0
     */
    public static long getSDCardRemainSize() {
        StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 检查GPS是否可见
     *
     * @return
     */
    public static boolean checkGPSEnable(Context context) {
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 获取设备屏幕宽度
     * <p>Description: The getWindowW</p>
     *
     * @param context
     * @return
     * @author: jinzufan
     * @update: [updatedate] [changer][change description]
     */
    public static int getWindowW(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getWidth();
        } else {
            return -1;
        }
    }

    /**
     * 获取设备屏幕高度
     * <p>Description: The getWindowH</p>
     *
     * @param context
     * @return
     * @author: jinzufan
     * @update: [updatedate] [changer][change description]
     */
    public static int getWindowH(Context context) {
        if (context instanceof Activity) {
            return ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getHeight();
        } else {
            return -1;
        }
    }

    /**
     * 获取跟卡目录
     *
     * @return
     */
    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName ：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        // 获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        // 用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        // 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}

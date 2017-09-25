package com.ynzz.carmanager.ui.consts;

import android.os.Environment;

import java.io.File;

/**
 * Created by ZXL on 2016/12/1.
 */

public class ApplicationGlobal {

    // 本地存储对象sharepreferance标识
    public static final String SPDATANAME = "userdata";

    public static final int NETWORKERROR = 3;
    //日志打印(发布时请改为false)
    public static boolean WRITELOG = false;
    public static boolean DEBUG = false;
    public static String OPENFILE = "openfile";
    public static String NETWORKNOTICE = "CheckNetWork";
    public static String CACHETIME = "cachetime";//缓存时间

    // 用户姓名
    public static String USERID = "userid";
    public static final String NICKNAME = "nikcname";
    public static final String LINKNAME = "linkname";

    public static String ADURL = "adurl";
    // 应用程序包名
    public static String APPPACKAGENAME = "com.ynzz.carmanager.ui";
    // 应用程序名称
    public static String applacation_name = "carmanager";

    // 皮肤标识
    public static final String SKIN_KEY = "skin";

    // sd卡路径
    public static String SdCardPath = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED)
            .getAbsolutePath() + File.separatorChar;
//    public static String SdCardPath = Environment.getExternalStorageDirectory()
//            .getAbsolutePath() + File.separatorChar;
    // 程序文件主目录
    public static String basePathFolder = SdCardPath + applacation_name;
    // 程序图片目录名称
    public static String imageBaseFoldName = "imgs";
    // 程序临时目录名称
    public static String tempBaseFoldName = "temp";
    public static String downloadBaseFoldName = "download";
    public static String cacheBaseFoldName = "cache";
    // 临时文件路径
    public static String tempPath = basePathFolder + File.separatorChar
            + tempBaseFoldName + File.separatorChar;

    // 下载文件路径
    public static String downloadPath = basePathFolder + File.separatorChar
            + downloadBaseFoldName + File.separatorChar;
    // 程序图片目录
    public static String imageBaseFold = basePathFolder + File.separatorChar
            + imageBaseFoldName + File.separatorChar;
    // 图片缓存路径
    public static String cachePath = basePathFolder + File.separatorChar
            + cacheBaseFoldName + File.separatorChar;
    // 程序日志目录名称
    public static String logBaseFoldName = "log";

    // 日志包
    public static String LogFlag = "userLog";

    // 程序日志
    public static String logBaseFold = basePathFolder + File.separatorChar
            + logBaseFoldName + File.separatorChar;
}

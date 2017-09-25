package com.ynzz.carmanager.ui.util;

import android.annotation.SuppressLint;
import android.util.Log;

import com.ynzz.carmanager.ui.consts.ApplicationGlobal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



@SuppressLint("SimpleDateFormat")
public class DebugUtil {

    private static final String TAG = "LogUtil";
    private static final String DEBUG_TAG = "debug";
    private static final String WARNING_TAG = "warning";
    private static final String ERROR_TAG = "error";
    private static final String VERBOSE_TAG = "verbose";
    private static final String INFO_TAG = "info";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//日志名称格式
    public static String logPath = getLogFileName();

    /**
     * 保存调试信息
     *
     * @param tag
     * @param info
     */
    public static void d(String tag, String info) {
        tag = tag == null ? "" : tag;
        info = info == null ? "" : info;
        String time = getCurrentTime();
        if (ApplicationGlobal.DEBUG) {
            Log.d(tag, info);
        }
        if (ApplicationGlobal.WRITELOG) {
            String log = String.format("%1$s  %2$s  %3$s %4$s\r\n", time, DEBUG_TAG, tag, info);
            writeLog(log);
        }
    }

    /**
     * 保存警告信息
     *
     * @param tag
     * @param info
     */
    public static void w(String tag, String info) {
        tag = tag == null ? "" : tag;
        info = info == null ? "" : info;
        String time = getCurrentTime();
        if (ApplicationGlobal.DEBUG) {
            Log.w(tag, info);
        }
        if (ApplicationGlobal.WRITELOG) {
            String log = String.format("%1$s  %2$s  %3$s %4$s\r\n", time, WARNING_TAG, tag, info);
            writeLog(log);
        }
    }

    /**
     * 保存错误信息
     *
     * @param tag
     * @param info
     */
    public static void e(String tag, String info) {
        tag = tag == null ? "" : tag;
        info = info == null ? "" : info;
        String time = getCurrentTime();
        if (ApplicationGlobal.DEBUG) {
            Log.e(tag, info);
        }
        if (ApplicationGlobal.WRITELOG) {
            String log = String.format("%1$s  %2$s  %3$s %4$s\r\n" + System.getProperty("line.separator"), time, ERROR_TAG, tag, info);
            writeLog(log);
        }
    }

    /**
     * 保存verbose信息
     *
     * @param tag
     * @param info
     */
    public static void v(String tag, String info) {
        tag = tag == null ? "" : tag;
        info = info == null ? "" : info;
        String time = getCurrentTime();
        if (ApplicationGlobal.DEBUG) {
            Log.v(tag, info);
        }
        if (ApplicationGlobal.WRITELOG) {
            String log = String.format("%1$s  %2$s  %3$s %4$s\r\n"
                    + System.getProperty("line.separator"), time, VERBOSE_TAG, tag, info);
            writeLog(log);
        }
    }

    /**
     * 保存一般信息
     *
     * @param tag
     * @param info
     */
    public static void i(String tag, String info) {
        tag = tag == null ? "" : tag;
        info = info == null ? "" : info;
        String time = getCurrentTime();
        if (ApplicationGlobal.DEBUG) {
            Log.i(tag, info);
        }
        if (ApplicationGlobal.WRITELOG) {
            String log = String.format("%1$s  %2$s  %3$s %4$s\r\n", time, INFO_TAG, tag, info);
            writeLog(log);
        }
    }

    /**
     * 获取日志文件全路径
     *
     * @return
     */
    public static String getLogFileName() {
        StringBuilder filePath = new StringBuilder();
        filePath.append(ApplicationGlobal.logBaseFold);
        filePath.append(ApplicationGlobal.LogFlag);
        filePath.append(File.separator);
        Date time = new Date();
        String date = sdf.format(time);
        // 文件名为 20080808.txt
        filePath.append(String.format("%1$s.txt", date));
        return filePath.toString();
    }

    /**
     * 获取当前时间 格式为2008-08-08 08:08:08
     *
     * @return
     */
    private static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        String time = String.format("%1$d-%2$d-%3$d %4$d:%5$d:%6$d", year, month + 1, day, hour, min, sec);
        return time;
    }

    /**
     * 获取输入文件Stream，如果文件不存在先创建
     *
     * @param path
     * @return
     */
    private static OutputStream getStream(String path) {
        OutputStream stream = null;
        try {
            File fLog = new File(path);
            if (!fLog.exists()) {
                fLog.getParentFile().mkdirs();
                fLog.createNewFile();
            }
            stream = new FileOutputStream(path, true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stream;
    }

    /**
     * 写日志
     *
     * @param log
     */
    private static void writeLog(String log) {
        OutputStream stream = getStream(logPath);
        if (stream == null) {
            Log.e(TAG, "write file log FAILED!(could not get stream)");
            return;
        }
        try {
            stream.write(log.getBytes());
            stream.flush();
        } catch (IOException e) {
            Log.e(TAG, "write file log FAILED!(write to stream exception)");
            e.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

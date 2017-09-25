package com.ynzz.carmanager.ui.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.ynzz.carmanager.ui.consts.ApplicationGlobal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;


/**
 * author:jinzufan create:2013-11-05 功能：文件操作类
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    // 建立一个MIME类型与文件后缀名的匹配表
    private static final String[][] MIME_MapTable = {
            // {后缀名， MIME类型}
            {".3gp", "video/3gpp"}, {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"}, {".avi", "video/x-msvideo"}, {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"}, {".c", "text/plain"}, {".class", "application/octet-stream"},
            {".conf", "text/plain"}, {".cpp", "text/plain"}, {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".exe", "application/octet-stream"}, {".gif", "image/gif"}, {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"}, {".h", "text/plain"}, {".htm", "text/html"}, {".html", "text/html"},
            {".jar", "application/java-archive"}, {".java", "text/plain"}, {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"}, {".js", "application/x-javascript"}, {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"}, {".m4a", "audio/mp4a-latm"}, {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"}, {".m4u", "video/vnd.mpegurl"}, {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"}, {".mp2", "audio/x-mpeg"}, {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"}, {".mpc", "application/vnd.mpohun.certificate"}, {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"}, {".mpg", "video/mpeg"}, {".mpg4", "video/mp4"}, {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"}, {".ogg", "audio/ogg"}, {".pdf", "application/pdf"},
            {".png", "image/png"}, {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rar", "application/x-rar-compressed"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"}, {".sh", "text/plain"},
            {".tar", "application/x-tar"}, {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"}, {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"}, {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".z", "application/x-compress"}, {".zip", "application/zip"}, {"", "*/*"}};
    private static String SDPATH;
    private int FILESIZE = 4 * 1024;

    public FileUtils() {
        // 得到当前外部存储设备的目录( /SDCARD )
        SDPATH = ApplicationGlobal.basePathFolder;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isImageFileExist(String fileName) {
        File file = new File(ApplicationGlobal.imageBaseFold + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的临时文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isTempFileExist(String fileName) {
        File file = new File(ApplicationGlobal.tempPath + fileName);
        return file.exists();
    }

    /**
     * 判断SD卡上的下载文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isDownloadFileExist(String fileName) {
        File file = new File(ApplicationGlobal.downloadPath + fileName);
        return file.exists();
    }

    /**
     * @param absolutePath
     * @return 创建时间：2011-5-16 方法描述：根据传入的绝对路径删除文件 （参数含义说明如下）
     */
    public static boolean deleteFileWithPath(String absolutePath) {
        File file = new File(absolutePath);
        return file.delete();
    }

    /*** 获取文件大小 ***/
    public static long getFileSizes(File f) throws Exception {
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return s;
    }

    /**
     * 获取文件大小
     *
     * @param
     * @return
     */
    public static long getFileSize(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        if (f.exists() && f.isDirectory()) {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 打开文件
     *
     * @param file
     */
    public static void openFile(Context context, File file) {
//		Intent intent = new Intent();
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		// 设置intent的Action属性
//		intent.setAction(Intent.ACTION_VIEW);
//		// 获取文件file的MIME类型
//		String type = getMIMEType(file);
//		// 设置intent的data和Type属性。
//		intent.setDataAndType(Uri.fromFile(file), type);
////		ResolveInfo ri = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
////		if (ri != null) {
////			context.startActivity(intent);
////		} else {
////			ToastUtil.showShortToast(context, "未安装打开此文件的应用,请安装后重试！");
////		}
//		//跳转
//		try {
//			context.startActivity(intent);     //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
//		} catch (Exception ex) {
//			Tool.initToast(context, "没有安装打开该类型文件的程序");
//		}
        if (file.exists() && file.isFile()) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(Uri.fromFile(file), type);
            //跳转
            try {
                context.startActivity(intent);     //这里最好try一下，有可能会报错。 //比如说你的MIME类型是打开邮箱，但是你手机里面没装邮箱客户端，就会报错。
            } catch (Exception ex) {
                Tool.initToast(context, "没有安装打开该类型文件的程序");
            }
        } else {
            Tool.initToast(context, "文件不存在或无法打开");
        }

    }

    /**
     * 清除缓存
     *
     * @param file
     */
    public static void clearCache(File file) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                if (child.isDirectory()) {
                    clearCache(child);
                } else {
                    child.delete();
                }
            }
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();

        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }

		/* 获取文件的后缀名 */
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "")
            return type;

        // 在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static byte[] File2Bytes(File file) {
        int byte_size = 1024;
        byte[] b = new byte[byte_size];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byte_size);
            for (int length; (length = fileInputStream.read(b)) != -1; ) {
                outputStream.write(b, 0, length);
            }
            fileInputStream.close();
            outputStream.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在SD卡上创建文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public File createSDFile(String fileName) {
        File file = new File(SDPATH + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            DebugUtil.d(TAG, "----->" + e.getMessage());
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     * @return
     */
    public File createSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.exists();
    }

    /**
     * 删除SD文件
     *
     * @param fileName
     * @return
     */
    public boolean deleteFile(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.delete();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param fileName
     * @param input
     * @return
     */
    public File write2SDFromInput(String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            int lastSeperator = fileName.lastIndexOf("/");
            if (lastSeperator != -1) {
                String path = fileName.substring(0, lastSeperator);
                createSDDir(path);
            }
            file = createSDFile(fileName);
            output = new FileOutputStream(file);
            byte[] buffer = new byte[FILESIZE];
            int len = 0;
            while ((len = input.read(buffer)) != -1) {
                output.write(buffer, 0, len);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param fileName
     * @param bytes
     * @return 创建时间：2011-5-16 方法描述：把byte数组写入到文件 位于SDCARD根目录下 （参数含义说明如下）
     * 文件名，字节数组(建议不能太大，容易内存溢出)
     */
    public String writeFile2SD(String fileName, byte[] bytes) {
        File file = null;
        OutputStream output = null;
        try {
            int lastSeperator = fileName.lastIndexOf("/");
            if (lastSeperator != -1) {
                String path = fileName.substring(0, lastSeperator);
                createSDDir(path);
            }
            file = createSDFile(fileName);
            output = new FileOutputStream(file);
            output.write(bytes);
            output.flush();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}

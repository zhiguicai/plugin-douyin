package com.hb.douyinplugin.utils;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;

/**
 * createTime: 2019/3/14.12:02
 * updateTime: 2019/3/14.12:02
 * author: singleMan.
 * desc: log打印类
 */

public class XLog {

    /**
     * 初始化配置打印工具类
     *
     * @param tag
     * @param logSavePath
     */
    public static void init(String tag, String logSavePath) {
        mLogSavePath = logSavePath;
        PREFIX += tag;
    }

    private static String mLogSavePath = "/mnt/sdcard/";

    private static String PREFIX = "xp_";//tag 的前缀  用来过滤log


    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static void v(String tag, String msg) {
        print(VERBOSE, tag, msg, null);
    }

    public static void v(String tag, String msg, Throwable tr) {
        print(VERBOSE, tag, msg, tr);
    }

    public static void d(String tag, String msg) {
        print(DEBUG, tag, msg, null);
    }

    public static void d(String tag, String msg, Throwable tr) {
        print(DEBUG, tag, msg, tr);
    }

    public static void i(String tag, String msg) {
        print(INFO, tag, msg, null);
    }

    public static void i(String tag, String msg, Throwable tr) {
        print(INFO, tag, msg, tr);
    }

    public static void w(String tag, String msg) {
        print(WARN, tag, msg, null);
    }

    public static void w(String tag, String msg, Throwable tr) {
        print(WARN, tag, msg, tr);
    }

    public static void w(String tag, Throwable tr) {
        print(WARN, tag, "", tr);
    }

    public static void e(String tag, String msg) {
        print(ERROR, tag, msg, null);
    }

    public static void exception(String tag, Exception e) {
        logException(tag, "", e);
    }

    public static void exception(String tag, String message, Exception e) {
        logException(tag, message, e);
    }

    public static void e(String tag, String msg, Throwable tr) {
        print(ERROR, tag, msg, tr);
    }


    /**
     * @param tag
     * @param ex
     */
    private static void logException(String tag, String message, Exception ex) {
        if (null == ex) {
            e("exception" + tag, "exception is null！");
            return;
        }
        StringBuffer sb = new StringBuffer();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        e("exception" + tag, message + "===>>" + sb.toString());
    }


    //规定每段显示的长度
    private static int LOG_MAXLENGTH = 2000;

    /**
     * 如果超过长度的话，另起一行来打印
     */
    private static void print(int ID, String TAG, String msg, Throwable e) {
        int strLength = msg.length();
        int start = 0;
        int end = LOG_MAXLENGTH;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                p(ID, TAG + i, msg.substring(start, end), null);
                start = end;
                end = end + LOG_MAXLENGTH;
            } else {
                p(ID, TAG + i, msg.substring(start, strLength), null);
                break;
            }
        }
    }

    private static void p(int ID, String tag, String msg, Throwable e) {
//        saveLogToSdcard(tag, msg);
        switch (ID) {
            case VERBOSE:
                Log.v(PREFIX + tag, msg, e);
                break;
            case DEBUG:
                Log.d(PREFIX + tag, msg, e);
                break;
            case INFO:
                Log.i(PREFIX + tag, msg, e);
                break;
            case WARN:
                Log.w(PREFIX + tag, msg, e);
                break;
            case ERROR:
                Log.e(PREFIX + tag, msg, e);
                break;

        }
    }

    /**
     * @param tag
     * @param content
     */
    private static void saveLogToSdcard(String tag, String content) {
        if (content == null) {
            content = "null";
        }
        SimpleDateFormat logFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String logContent = String.format("%s    %s :  %s", logFormat.format(System.currentTimeMillis()), tag, content);
        SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            String fileDir = mLogSavePath;
            File file = new File(fileDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String fileName = fileDir + fileNameFormat.format(System.currentTimeMillis()) + ".log";
            File logFile = new File(fileName);
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(fileName, true);//SD卡中的路径
            fw.flush();
            fw.write(logContent + "\n");
            fw.close();
        } catch (Exception e) {
        }
    }
}

package com.linlic.ccmtv.yx.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**获取系统或SDCARD剩余空间信息
 * Created by Administrator on 2019/6/19.
 */

public class SDCard {
    /*SDCARD剩余空间信息*/
    public static long readSDCard() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long blockSize = sf.getBlockSize();
            long blockCount = sf.getBlockCount();
            long availCount = sf.getAvailableBlocks();
//            Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//            Log.d("", "可用的block数目：:"+ availCount+",剩余空间:"+ availCount*blockSize/1024+"KB");
            return  availCount*blockSize/1024;
        }
        return 0;
    }

    /*系统 剩余空间信息*/
    public static  Long readSystem() {
        File root = Environment.getRootDirectory();
        StatFs sf = new StatFs(root.getPath());
        long blockSize = sf.getBlockSize();
        long blockCount = sf.getBlockCount();
        long availCount = sf.getAvailableBlocks();
//        Log.d("", "block大小:"+ blockSize+",block数目:"+ blockCount+",总大小:"+blockSize*blockCount/1024+"KB");
//        Log.d("", "可用的block数目：:"+ availCount+",可用大小:"+ availCount*blockSize/1024+"KB");
        return  availCount*blockSize/1024;
    }



    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getAvailableInternalMemorySize(Context context) {

        //获取内存可用剩余空间
        long romFreeSpace = Environment.getDataDirectory().getFreeSpace();
        //获取SD卡可用剩余空间
        long SDFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        LogUtil.e("我要看的空间","内存可用"+ FormetFileSize(romFreeSpace));
        //格式化大小
        LogUtil.e("我要看的空间","内存可用"+Formatter.formatFileSize(context, romFreeSpace));
        LogUtil.e("我要看的空间","sd卡可用" + Formatter.formatFileSize(context, SDFreeSpace));
            return Formatter.formatFileSize(context, romFreeSpace);

    }

    public  static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        LogUtil.e("剩余空间","KB:"+kb+"   MB:"+mb+"   GB:"+gb);
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }

    public static final String byteToString(long size) {

        long GB = 1024 * 1024 * 1024;//定义GB的计算常量
        long MB = 1024 * 1024;//定义MB的计算常量
        long KB = 1024;//定义KB的计算常量
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String resultSize = "";
        if (size / GB >= 1) {
            //如果当前Byte的值大于等于1GB
            resultSize = df.format(size / (float) GB) + " GB   ";
        } else if (size / MB >= 1) {
            //如果当前Byte的值大于等于1MB
            resultSize = df.format(size / (float) MB) + " MB   ";
        } else if (size / KB >= 1) {
            //如果当前Byte的值大于等于1KB
            resultSize = df.format(size / (float) KB) + " KB   ";
        } else {
            resultSize = size + " B   ";
        }
        return resultSize;
    }

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) +"B";//B

        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024)+"KB";//KB

        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576)+"MB";//MB

        } else {
            fileSizeString = df.format((double) fileS / 1073741824)+"GB";//GB

        }
        return fileSizeString;
    }


    public static void readSystem22(Context context) {
        //获取内存可用剩余空间
        long romFreeSpace = Environment.getDataDirectory().getFreeSpace();

        //获取SD卡可用剩余空间
        long SDFreeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        LogUtil.e("我要看的空间","内存可用"+ FormetFileSize(romFreeSpace));
        //格式化大小
        LogUtil.e("我要看的空间","内存可用"+Formatter.formatFileSize(context, romFreeSpace));
        LogUtil.e("我要看的空间","sd卡可用" + Formatter.formatFileSize(context, SDFreeSpace));

    }




}

package com.linlic.ccmtv.yx.utils;

import android.net.Uri;
import android.util.Log;

import com.linlic.ccmtv.yx.config.URLConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * name：
 * author：Larry
 * data：2016/8/17.
 */
public class DownLoadImageUtil {
    /*
   * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
   * 这里的path是图片的地址
   */
    public static Uri getImageURI(String font_name, File cache) throws Exception {
        String name = Base64utils.getBase64(font_name) + font_name.substring(font_name.lastIndexOf("."));
        File file = new File(cache, name);
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
//            Log.i("ffffff", file.toString() + "   本地缓存      " + name);
            return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            // 从网络上获取图片
//            Log.e("ffffff", "hhhhhhhhhhhhhhhhhhhhhhhhh" + name);

            URL url = new URL(font_name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return Uri.fromFile(file);
            }
        }
        return null;
    }

    public static Uri getImageURI(String font_name, File cache,  String name ) throws Exception {

        File file = new File(cache, name);
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
            Log.i("ffffff", file.toString() + "   本地缓存      " + name);
            file.delete();
        }
            // 从网络上获取图片
//            Log.e("ffffff", "hhhhhhhhhhhhhhhhhhhhhhhhh" + name);

            URL url = new URL(font_name);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return Uri.fromFile(file);
            }

        return null;
    }


 /*
   * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
   * 这里的downUrl是图片的地址,picName为图片的名字
   */

    public static Uri getImageURI_Name(String picName, String downUrl) throws Exception {
        File file = new File(URLConfig.ccmtvapp_basesdcardpath + "/arImage", picName);
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
            Log.i("ffffff", file.toString() + "   本地缓存      " + picName);
            return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            // 从网络上获取图片
            Log.i("ffffff", "hhhhhhhhhhhhhhhhhhhhhhhhh" + "服务器" + picName);
            Log.i("ffffff", "sssssssssss" + downUrl);

//            downUrl = HttpClientUtils.encodingtoStr(downUrl);
            URL url = new URL(FirstLetter.getSpells(downUrl));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.i("getResponseCode", conn.getResponseCode() + "");
           /* if (conn.getResponseCode() == 200) {*/

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return Uri.fromFile(file);
            }
       /* }
        return null;*/
    }
}

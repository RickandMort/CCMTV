package com.linlic.ccmtv.yx.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClientUtils {
    /**
     * 用来判别是否是网络异常的2个标识  不可与后台返回的错误码相同
     */
    public static final int NET_ERROT_CODE_SENDPOST = 1000;
    public static final int NET_ERROT_CODE_SENDPOSTTOGP = 700;
    /**
     * 未知错误  用来标识Json解析时的异常
     */
    public static final int UNKONW_EXCEPTION_CODE = -1;

    /**
     * name：系统提供 POST
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午5:21:20
     *
     * @return
     */
    public static String sendPost(final Context context, final String url, final String json) {
        //注释的加载框放到当前类最下边
        final StringBuilder strResult = new StringBuilder();
        strResult.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint2) + "\"}");
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            // 判断当前网络是否已经连接
            if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                System.out.println("=========手机有网络================");
                URL realurl = null;
                InputStream in = null;
                HttpURLConnection conn = null;
                try {
                    realurl = new URL(url);
                    conn = (HttpURLConnection) realurl.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10000);
                    PrintWriter pw = new PrintWriter(conn.getOutputStream());
                    //往接口传输的数据调用这个方法
                    submitDataInterface(context, pw, json);

                    if (conn.getResponseCode() == 200) {
//                        System.out.println("==========访问服务器成功=============");
                        in = conn.getInputStream();
                        byte[] data = read(in);
                        String base64 = new String(data);
                        LogUtil.e("解密前:", base64);
                        final String resultJSON = Base64utils.getFromBase64(Base64utils.getFromBase64(base64));
                        LogUtil.e("解密后:", resultJSON);
//                        System.out.println("message信息:" + encodingtoStr(new JSONObject(resultJSON).getString("errorMessage")));

                        //如果当前返回状态为2，直接跳转到首页（每个调用者都已经有提示Toast）
                        ifStateIsTwo(context, resultJSON);
                        strResult.setLength(0);
                        strResult.append(resultJSON);
                    } else {
                        LogUtil.e("网络数据", conn.getResponseMessage().toString());
                        LogUtil.e("网络数据code", conn.getResponseCode() + "+++++++++"+json);
                        strResult.setLength(0);
                        strResult.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint5) + "\"}");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    strResult.setLength(0);
                    strResult.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint6) + "\"}");
                }
            }
        } else {
            strResult.setLength(0);
            strResult.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint4) + "\"}");
            Log.e("PRETTY_LOGGER", "sendPost() returned: " + strResult.toString());
        }
        return strResult.toString();
    }

    /**
     * name：系统提供 POST
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午5:21:20
     *
     * @return
     */
    public static String sendPostToGP(final Context context, final String url, final String json) {
        //注释的加载框放到当前类最下边
        final StringBuilder strResult = new StringBuilder();
        strResult.append("{\"code\":203,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint2) + "\"}");
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            // 判断当前网络是否已经连接
            if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
//                System.out.println("=========手机有网络================");
                URL realurl = null;
                InputStream in = null;
                HttpURLConnection conn = null;
                try {
                    realurl = new URL(url);
                    conn = (HttpURLConnection) realurl.openConnection();
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setConnectTimeout(10000);
                    PrintWriter pw = new PrintWriter(conn.getOutputStream());
                    //往接口传输的数据调用这个方法
                    submitDataInterface(context, pw, json);

                    if (conn.getResponseCode() == 200) {
//                        System.out.println("==========访问服务器成功=============");
                        in = conn.getInputStream();
                        byte[] data = read(in);
                        String base64 = new String(data);
                        LogUtil.e("解密前:", base64);
                        final String resultJSON = Base64utils.getFromBase64(Base64utils.getFromBase64(base64));
                        LogUtil.e("解密后:", resultJSON);
//                        System.out.println("message信息:" + encodingtoStr(new JSONObject(resultJSON).getString("errorMessage")));

                        //如果当前返回状态为2，直接跳转到首页（每个调用者都已经有提示Toast）
                        ifStateIsTwoToGP(context, resultJSON);
                        strResult.setLength(0);
                        strResult.append(resultJSON);
                    } else {
                        LogUtil.e("网络数据", conn.getResponseMessage().toString());
                        LogUtil.e("网络数据code", conn.getResponseCode() + "+++++++++"+json);
                        strResult.setLength(0);
                        strResult.append("{\"code\":203,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint5) + "\"}");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    strResult.setLength(0);
                    strResult.append("{\"code\":203,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint6) + "\"}");
                }
            }
        } else {
            strResult.setLength(0);
            strResult.append("{\"code\":code,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint4) + "\"}");
        }
        return strResult.toString();
    }


    /**
     * name：如果当前返回状态为2，直接跳转到首页（每个调用者都已经有提示Toast）
     * author：MrSong
     * data：2016/4/21 21:37
     */
    private static void ifStateIsTwo(final Context context, String resultJSON) {
        try {
            if (new JSONObject(resultJSON).getString("status").equals("2")) {
                //跳转首页
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("type", "changepass");
                        ((Activity) context).startActivity(intent);
                        //清空数据
                        SharedPreferencesTools.saveUid(context, "");
                        SharedPreferencesTools.saveUserName(context, "");
                        SharedPreferencesTools.savePassword(context, "");
                    }
                });
            }
        } catch (Exception e) {
        }
    }

    /**
     * name：如果当前返回状态为2，直接跳转到首页（每个调用者都已经有提示Toast）
     * author：MrSong
     * data：2016/4/21 21:37
     */
    private static void ifStateIsTwoToGP(final Context context, String resultJSON) {
        try {
            if (new JSONObject(resultJSON).getString("code").equals("405") || new JSONObject(resultJSON).getString("code").equals("406")) {
                //跳转首页
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("type", "changepass");
                        ((Activity) context).startActivity(intent);
                        //清空数据
                        SharedPreferencesTools.saveUid(context, "");
                        SharedPreferencesTools.saveUserName(context, "");
                        SharedPreferencesTools.savePassword(context, "");
                        SharedPreferencesTools.saveVipFlag(context, 0);
                        SharedPreferencesTools.saveIntegral(context, "0");
                    }
                });
            }
        } catch (Exception e) {
        }
    }


    /**
     * name：往接口提交数据
     * author：MrSong
     * data：2016/4/21 18:01
     */
    private static void submitDataInterface(Context context, PrintWriter pw, String json) {
        //如果uid有就把账户密码传输给接口，没有就不传
        try {
            // 加密二次传输数据
            String strBase64 = Base64utils.getBase64(Base64utils.getBase64(json));
            Log.e("data", json.toString());
            JSONObject object = new JSONObject(json);
            if (object.has("uid")) {
                JSONObject obj = new JSONObject();
                obj.put("userAccount", SharedPreferencesTools.getUserName(context));
                obj.put("password", SharedPreferencesTools.getPassword(context));
                obj.put("sourceflag", SharedPreferencesTools.getWhether_the_quick_login(context));
                obj.put("mobphone", SharedPreferencesTools.getMobphone(context));
                Log.e("datacheck", obj.toString());
                String datacheck = Base64utils.getBase64(Base64utils.getBase64(obj.toString()));
                pw.print("data=" + strBase64 + "&datacheck=" + datacheck);
//                    System.out.println("传输json和验证：" + json + ",datacheck:" + obj.toString());
            } else {
                pw.print("data=" + strBase64);
//                  System.out.println("传输json：" + json);
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * name：xUtils POST
     * <p/>
     * author: Mr.song
     * 时间：2016-2-19 下午12:16:20
     *
     * @param context
     * @param url
     * @param json
     * @return
     */
    public static String xUtilsPost(final Context context, String url, String act, String json) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint2) + "\"}");
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();
        if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
            // 判断当前网络是否已经连接
            if (mNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                try {
//                    System.out.println("=========手机有网络================");
                    HttpUtils httpUtils = new HttpUtils();
                    RequestParams params = new RequestParams();
                    // 加密二次
                    String strBase64 = Base64utils.getBase64(Base64utils.getBase64(json));
                    params.addBodyParameter("data", strBase64);
                    params.addBodyParameter("act", act);
                    httpUtils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
//                            System.out.println("error:" + arg0 + "|" + arg1);
                            builder.setLength(0);
                            builder.append("{\"status\":0,\"errorMessage\":\"" + arg1 + "\"}");
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> info) {
                            try {
//								System.out.println("解密前:"+info.result);
                                String aa = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
//								System.out.println("解密后:"+aa);
                                builder.setLength(0);
                                builder.append(aa);
//								Toast.makeText(context, new JSONObject(strResult).getString("errorMessage"), 0).show();
//								Log.i("chuanci",encodingtoStr(new JSONObject(strResult).getString("errorMessage")));
                            } catch (Exception e) {
                                e.printStackTrace();
//                                System.out.println(R.string.post_hint1);
                                builder.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint6) + "\"}");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
//                    System.out.println(R.string.post_hint3);
                    builder.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint6) + "\"}");
                }
            }
        } else {
//            System.out.println(R.string.post_hint4);
            builder.append("{\"status\":0,\"errorMessage\":\"" + context.getResources().getString(R.string.post_hint4) + "\"}");
        }
        return builder.toString();
    }

    /**
     * Unicode字符解码
     * <p/>
     * author:宋双双
     * 时间：2015-1-22 下午3:51:35
     *
     * @param str
     * @return
     */
    public static String encodingtoStr(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

    /**
     * name：读取流中的数据
     * <p/>
     * author: Mr.song
     * 时间：2016-2-20 下午5:58:41
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] read(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }

    /**
     * name：获取文件内容（用户协议）
     * <p/>
     * author: Mr.song
     * 时间：2016-2-22 下午1:35:19
     *
     * @param inputStream
     * @return
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 判断是否是由于网络错误原因出现的请求失败，true：展示neterror页面
     * 规则可以自己定义
     *
     * @param context
     * @param code
     * @return
     */
    public static boolean isNetConnectError(Context context, int code) {
        if (context == null) return false;
        return (code == NET_ERROT_CODE_SENDPOST || code == NET_ERROT_CODE_SENDPOSTTOGP) ||
                !NetUtil.isNetworkConnected(context);
    }
}
package com.linlic.ccmtv.yx.activity.home.apricotcup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.activity.home.willowcup.Type;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * name：杏林杯--首页
 * author：Larry
 * data：2017/3/29.
 */
public class ApricotHomePageFragment extends BaseLazyFragment {
    private boolean isInit = false;
    private MyListView listview_home;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Context context;
    private String aid;
    private String dataurl, video_title, picurl;
    BaseListAdapter baseListAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    data.clear();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) {// 成功
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject customJson = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("num", customJson.getString("num"));
                                    map.put("fname", customJson.getString("fname"));
                                    map.put("docname", customJson.getString("docname"));
                                    map.put("hosname", customJson.getString("hosname"));
                                    map.put("xqurl", customJson.getString("xqurl"));             //病例详情的链接
                                    map.put("videourl", customJson.getString("videourl"));       //分享地址
                                    data.add(map);
                                }
                            } else {
                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 6001:
                    Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 7001:
                    Toast.makeText(getActivity(), "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apricot_home_page, container, false);
        context = getActivity();
        listview_home = (MyListView) view.findViewById(R.id.listview_home);
        setText();
        isInit = true;
        LazyLoad();    //让第二个页面加载数据
        return view;
    }


    @Override
    public void LazyLoad() {
        if (isInit && isVisible) {
            isInit = true;      //若为false数据仅加载一次
            setmsgdb();
        } else {
            setmsgdb();
        }
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.xlbIndex);
                    obj.put("type", Type.HOME.getIndex());
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }


    private void setText() {

        baseListAdapter = new BaseListAdapter(listview_home, data, R.layout.xlbhome_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(final ListHolder helper, final Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.tv_lybvideo_title, ((Map) item).get("title").toString());
                helper.setText(R.id.tv_docname, ((Map) item).get("docname").toString());
                helper.setText(R.id.tv_hosname, ((Map) item).get("hosname").toString());
                helper.setText(R.id.btn_zan, ((Map) item).get("num").toString() + "票 | 投票");
                helper.getView(R.id.btn_zan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                        if (uid == null || ("").equals(uid)) {
                            return;
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("uid", uid);
                                    object.put("aid", ((Map) item).get("aid").toString());
                                    object.put("type", "2");
                                    object.put("act", URLConfig.lybVote);
                                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                                    try {
                                        final JSONObject jsonObject = new JSONObject(result);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (jsonObject.getInt("status") == 1) {
                                                        Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                                        String str_piaoshu = helper.getStr(R.id.btn_zan);
                                                       // int a = Integer.parseInt(((Map) item).get("num").toString());
                                                        int a = Integer.parseInt(str_piaoshu .substring(0, str_piaoshu.indexOf("票")));
                                                        helper.setText(R.id.btn_zan, a + 1 + "票 | 投票");
                                                    } else {
                                                        Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(500);
                                }
                            }
                        }).start();
                    }
                });
                helper.getView(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        share(item);
                    }
                });
            }
        };
        listview_home.setAdapter(baseListAdapter);
        // listview点击事件
        listview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(context, ApricotDetailsActivity.class);
                intent.putExtra("aid", data.get(position).get("aid").toString());
                intent.putExtra("picurl", data.get(position).get("picurl").toString());
                intent.putExtra("title", data.get(position).get("title").toString());
                intent.putExtra("xqurl", data.get(position).get("xqurl").toString());
                intent.putExtra("videourl", data.get(position).get("videourl").toString());
                startActivityForResult(intent, 100);
            }
        });
    }


    //分享
    private void share(final Object item) {
        aid = ((Map) item).get("aid").toString();
        picurl = ((Map) item).get("picurl").toString();
        //   picurl = "http://www.ccmtv.cn/upload_files/video/20140508/孙东辉：手术切除早期直肠癌.jpg";
        video_title = ((Map) item).get("title").toString();
        dataurl = ((Map) item).get("videourl").toString();
        //分享操作
        if (!TextUtils.isEmpty(dataurl)) {
            //分享操作
            //ShareSDK.initSDK(getActivity());
            final ShareDialog shareDialog = new ShareDialog(getActivity());
            shareDialog.setCancelButtonOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                }
            });
            shareDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> item = (HashMap<String, Object>) arg0.getItemAtPosition(arg2);
                    if (item.get("ItemText").equals("微博")) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setText("医学视频:" + video_title + "~" + dataurl); //分享文本
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Toast.makeText(getActivity(), "微博分享成功", Toast.LENGTH_LONG).show();
                                CheckIntegral();
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Message msg = new Message();
                                msg.what = 7001;
                                msg.obj = throwable.getMessage();
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                handler.sendEmptyMessage(6001);
                            }
                        }); // 设置分享事件回调
                        sinaWeibo.share(sp);
                    } else if (item.get("ItemText").equals("微信好友")) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE);//非常重要：一定要设置分享属性
                        sp.setTitle(video_title);  //分享标题
                        sp.setImageUrl(picurl);//网络图片rul
                        sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                        wechat.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Toast.makeText(getActivity(), "微信好友分享成功", Toast.LENGTH_LONG).show();
                                CheckIntegral();
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Message msg = new Message();
                                msg.what = 7001;
                                msg.obj = throwable.getMessage();
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                handler.sendEmptyMessage(6001);
                            }
                        }); // 设置分享事件回调
                        wechat.share(sp);
                    } else if (item.get("ItemText").equals("朋友圈")) {
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setShareType(Platform.SHARE_WEBPAGE); //非常重要：一定要设置分享属性
                        sp.setTitle(video_title);  //分享标题
                        // sp.setText(video_title);   //分享文本
                        sp.setImageUrl(picurl);//网络图片rul
                        sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Toast.makeText(getActivity(), "朋友圈分享成功", Toast.LENGTH_LONG).show();
                                CheckIntegral();
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Message msg = new Message();
                                msg.what = 7001;
                                msg.obj = throwable.getMessage();
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                handler.sendEmptyMessage(6001);
                            }
                        }); // 设置分享事件回调
                        wechatMoments.share(sp);
                    } else if (item.get("ItemText").equals("QQ")) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle(video_title);
                        sp.setImageUrl(picurl);//网络图片rul
                        sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Toast.makeText(getActivity(), "QQ分享成功", Toast.LENGTH_LONG).show();
                                CheckIntegral();
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Message msg = new Message();
                                msg.what = 7001;
                                msg.obj = throwable.getMessage();
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                handler.sendEmptyMessage(6001);
                            }
                        }); // 设置分享事件回调
                        // 执行分享
                        qq.share(sp);
                    } else if (item.get("ItemText").equals("QQ空间")) {
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle(video_title);
                        sp.setTitleUrl(dataurl); // 标题的超链接
                        sp.setImageUrl(picurl);
                        sp.setSite("CCMTV临床医学频道");
                        sp.setSiteUrl(dataurl);
                        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                        qzone.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Toast.makeText(getActivity(), "QQ空间分享成功", Toast.LENGTH_LONG).show();
                                CheckIntegral();
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Message msg = new Message();
                                msg.what = 7001;
                                msg.obj = throwable.getMessage();
                                handler.sendMessage(msg);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                handler.sendEmptyMessage(6001);
                            }
                        }); // 设置分享事件回调
                        // 执行分享
                        qzone.share(sp);
                    } else {
                        ClipboardManager cmb = (ClipboardManager) context
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        cmb.setText(dataurl.trim());
                        Toast.makeText(getActivity(), "复制成功",
                                Toast.LENGTH_LONG).show();
                    }
                    shareDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
        }
    }


    //分享成功后，检查是否需要增加积分，一天十次机会，每次增加一个积分
    private void CheckIntegral() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoShareSuc);
                    obj.put("aid", aid);
                    obj.put("uid", SharedPreferencesTools.getUid(getActivity()));
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP, obj.toString());
                    JSONObject jsonresult = new JSONObject(result);
                    if (jsonresult.getInt("status") == 1) {// 成功
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent dataIntent) {
        if (resultCode == 17) {
            setmsgdb();
        }
    }


}

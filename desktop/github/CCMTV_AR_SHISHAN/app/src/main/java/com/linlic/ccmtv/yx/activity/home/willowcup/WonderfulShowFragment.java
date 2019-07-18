package com.linlic.ccmtv.yx.activity.home.willowcup;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.my.dialog.ShareDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
 * name：柳叶杯--精彩展播
 * author：Larry
 * data：2017/3/27.
 */
public class WonderfulShowFragment extends BaseLazyFragment {
    // TODO: Rename parameter arguments, choose names that match
    private TextView tv_totype;
    private TextView tv_type;
    private MyListView lv_wonderful_show;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    ArrayList<String> types = new ArrayList<>();
    private Context context;
    private String dataurl, video_title, picurl, aid;
    String str_piaoshu;             //票数
    int voteNum;
    int positions;
    HomePageAdapter adapter;
    private boolean isInit = false;
    private static long lastClickTime;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    types.clear();
                    data.clear();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (jsonObject.getInt("status") == 1) {// 成功
                            JSONArray dataArray = dataObject
                                    .getJSONArray("videolist");
                            JSONArray typeArray = dataObject
                                    .getJSONArray("navlist");
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject customJson = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("num", customJson.getString("num"));
                                    map.put("docname", customJson.getString("docname"));
                                    map.put("hosname", customJson.getString("hosname"));
                                    map.put("videourl", customJson.getString("videourl"));
                                    data.add(map);
                                }
                            } else {
                                Toast.makeText(context, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                            int i;
                            for (i = 0; i <= typeArray.length(); i++) {
                                if (i == typeArray.length()) {
                                    if (typeArray.length() > 1) {
                                        types.add(i, "全部类型");
                                    }
                                } else {
                                    String type = typeArray.get(i).toString();
                                    types.add(i, type);
                                }
                            }

                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
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
        View view = inflater.inflate(R.layout.fragment_wonderful_show, container, false);
        context = getActivity();
        tv_totype = (TextView) view.findViewById(R.id.tv_totype);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        lv_wonderful_show = (MyListView) view.findViewById(R.id.lv_wonderful_show);
        tv_totype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowTypeActivity.class);
                intent.putStringArrayListExtra("types", types);
                startActivityForResult(intent, 100);
            }
        });

        setTexts();
        isInit = true;
        LazyLoad();    //让第二个页面加载数据
        return view;
    }

    private void setTexts() {
        adapter = new HomePageAdapter(context, data);
        lv_wonderful_show.setAdapter(adapter);
        // listview点击事件
        lv_wonderful_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取视频信息
                try {
                    if (!isFastDoubleClick()) {
                        getVideoRulest(data.get(position).get("aid").toString());
                        positions = position;
                        voteNum = Integer.parseInt(data.get(position).get("num").toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "加载数据失败，请检查网络并刷新首页！", Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    e.printStackTrace();
                }
            }
        });
    }


    //视频分享成功后，检查是否需要增加积分，一天十次机会，每次增加一个积分
    private void CheckIntegral() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.videoShareSuc);
                    obj.put("aid", aid);
                    obj.put("uid", uid);
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


    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.lybIndex);
                    obj.put("type", Type.JCZHB.getIndex());
                    String videotype = tv_type.getText().toString();
                    if (videotype.equals("全部类型")) {
                        //  obj.put("videotype", Type.JCZHB.getIndex());
                    } else {
                        obj.put("videotype", videotype);
                    }

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

    //分享视频
    private void shareVideo(int position) {
        picurl = data.get(position).get("picurl").toString();
        video_title = data.get(position).get("title").toString();
        aid = data.get(position).get("aid").toString();
        dataurl = data.get(position).get("videourl").toString();
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
                        // sp.setText("输入手机号码，轻松注册领取积分，尽享海量医学视频！"); //分享文本
                        //sp.setImageUrl(picurl);//网络图片rul
                        //3、非常重要：获取平台对象
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        //sinaWeibo.removeAccount(true);
                        // ShareSDK.removeCookieOnAuthorize(true);
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
                        // sp.setText(video_title);   //分享文本
                        sp.setImageUrl(picurl);//网络图片rul
                        sp.setUrl(dataurl);   //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
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
                        //2、设置分享内容
                        Platform.ShareParams sp = new Platform.ShareParams();
                        sp.setTitle(video_title);
                        //  sp.setText(video_title);
                        sp.setImageUrl(picurl);//网络图片rul
                        sp.setTitleUrl(dataurl);  //网友点进链接后，可以看到分享的详情
                        //3、非常重要：获取平台对象
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
                        //  sp.setText(video_title);
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


    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(getActivity(), VideoFive.class);
        intent.putExtra("aid", aid);
        intent.putExtra("isVote", true);
        intent.putExtra("voteNum", voteNum);
        intent.putExtra("positions", positions);
        startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);
        if (requestCode == 100) {
            String type = dataIntent.getStringExtra("type");
            tv_type.setText(type);
            setmsgdb();
        }
        if (resultCode == 17) {
            int position1 = dataIntent.getIntExtra("positions", 0);
            int votenum = dataIntent.getIntExtra("votenum", 0);
            Map<String, Object> map = data.get(position1);
            map.put("num", votenum);
            data.set(position1, map);
            adapter.notifyDataSetChanged();
        }
    }

    //是否是多次点击
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    class HomePageAdapter extends BaseAdapter {
        List<Map<String, Object>> data;
        Context context;

        public HomePageAdapter(Context context, List<Map<String, Object>> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.lybhome_list, null);
                viewHolder.tv_lybvideo_title = (TextView) convertView.findViewById(R.id.tv_lybvideo_title);
                viewHolder.tv_docname = (TextView) convertView.findViewById(R.id.tv_docname);
                viewHolder.tv_hosname = (TextView) convertView.findViewById(R.id.tv_hosname);
                viewHolder.btn_zan = (Button) convertView.findViewById(R.id.btn_zan);
                viewHolder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
                viewHolder.iv_lybvideo = (ImageView) convertView.findViewById(R.id.iv_lybvideo);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_lybvideo_title.setText(data.get(position).get("title").toString());
            viewHolder.tv_docname.setText(data.get(position).get("docname").toString());
            viewHolder.tv_hosname.setText(data.get(position).get("hosname").toString());
            Glide.with(context).load(data.get(position).get("picurl").toString()).into(viewHolder.iv_lybvideo);
            viewHolder.btn_zan.setText(data.get(position).get("num").toString() + "票 | 投票");
            viewHolder.btn_zan.setOnClickListener(new View.OnClickListener() {
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
                                object.put("aid", data.get(position).get("aid").toString());
                                object.put("type", "1");
                                object.put("act", URLConfig.lybVote);
                                String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());
                                try {
                                    final JSONObject jsonObject = new JSONObject(result + "");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (jsonObject.getInt("status") == 1) {
                                                    Toast.makeText(getActivity(), jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                                    try {
                                                        str_piaoshu = viewHolder.btn_zan.getText().toString();
                                                        // int a = Integer.parseInt(((Map) item).get("num").toString());\
                                                        int a = Integer.parseInt(str_piaoshu.substring(0, str_piaoshu.indexOf("票")));
                                                        //  viewHolder.btn_zan.setText( a + 1 + "票 | 投票");
                                                        Map<String, Object> map = data.get(position);
                                                        map.put("num", a + 1);
                                                        data.set(position, map);
                                                        adapter.notifyDataSetChanged();
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    }

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
                    }).start();                                                                                          //投票
                }

            });
            viewHolder.btn_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                                                   //分享
                    shareVideo(position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView tv_lybvideo_title;
            TextView tv_docname;
            TextView tv_hosname;
            ImageView iv_lybvideo;
            Button btn_zan;
            Button btn_share;
        }
    }

}

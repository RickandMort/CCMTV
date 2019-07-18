package com.linlic.ccmtv.yx.activity.home.apricotcup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseLazyFragment;
import com.linlic.ccmtv.yx.activity.home.willowcup.ShowTypeActivity;
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

/**
 * name：杏林杯---病例展播
 * author：Larry
 * data：2017/3/29.
 */
public class ApricotCaseShowFragment extends BaseLazyFragment {
    private TextView tv_totype;
    private TextView tv_type;
    private MyListView lv_wonderful_show;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    ArrayList<String> types = new ArrayList<>();
    private Context context;
    private String dataurl, video_title, picurl, aid;
    private boolean isInit = false;
    BaseListAdapter baseListAdapter;

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
                            for (int i = 0; i <= typeArray.length(); i++) {
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
                        baseListAdapter.notifyDataSetChanged();
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
        // Inflate the layout for this fragment
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
        setText();
        isInit = true;
        LazyLoad();                                                                                 //让第二个页面加载数据
        return view;
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


    private void setText() {

        baseListAdapter = new BaseListAdapter(lv_wonderful_show, data, R.layout.xlbhome_list) {

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
                                                        int a = Integer.parseInt(str_piaoshu.substring(0, str_piaoshu.indexOf("票")));
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
                                                                          shareCase(item);
                                                                      }
                                                                  }

                );
            }
        }

        ;
        lv_wonderful_show.setAdapter(baseListAdapter);
        // listview点击事件
        lv_wonderful_show.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                 {
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
                                                         startActivity(intent);
                                                     }
                                                 }

        );
    }


    //分享病例
    private void shareCase(final Object item) {
        picurl = ((Map) item).get("picurl").toString();
        video_title = ((Map) item).get("title").toString();
        aid = ((Map) item).get("aid").toString();
        dataurl = ((Map) item).get("videourl").toString();
        //分享操作
        if (!TextUtils.isEmpty(dataurl)) {
        } else {
            Toast.makeText(context, "获取分享链接失败！", Toast.LENGTH_SHORT).show();
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
//                    System.out.println("加载数据出错了！");
                }
            }
        };
        new Thread(runnable).start();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            String type = data.getStringExtra("type");
            tv_type.setText(type);
            setmsgdb();
        }
    }
}

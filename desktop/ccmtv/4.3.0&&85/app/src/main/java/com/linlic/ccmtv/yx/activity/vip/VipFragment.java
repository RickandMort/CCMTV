package com.linlic.ccmtv.yx.activity.vip;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.util.RecyclerItemClickListener;
import com.linlic.ccmtv.yx.activity.my.MyOpenMenberActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.VipRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.util.ImageLoader;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * name：Vip专区
 * author：Larry
 * data：2016/4/12 14:42
 */
public class VipFragment extends BaseFragment {
    private RecyclerView vip_item_horv;
    private ImageView vip_topimg, iv_vip_toserch;
    private TextView iv_openvip, tv_tip;
    private CircleImageView iv_vipheader;
    private VipRecyclerViewAdapter adapter;
    private int pagePingTai = 1, pageHot = 1, pageVip = 1;
    private String keyId = "0";
    //平台推荐
    private List<ImageView> pingTaiImg = new ArrayList<ImageView>();
    private List<ImageView> pingTaiImgMark = new ArrayList<ImageView>();
    private List<TextView> pingTaiTitle = new ArrayList<TextView>();
    //热门推荐
    private List<ImageView> hotImg = new ArrayList<ImageView>();
    private List<ImageView> hotImgMark = new ArrayList<ImageView>();
    private List<TextView> hotText = new ArrayList<TextView>();
    //Vip都在看
    private List<ImageView> vipImg = new ArrayList<ImageView>();
    private List<ImageView> vipImgMark = new ArrayList<ImageView>();
    private List<TextView> vipText = new ArrayList<TextView>();
    private RelativeLayout rl_pingtai_lot, rl_hot_lot, rl_vip_lot;
    String imgurl,userimg;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONObject data = result.getJSONObject("data");
                            JSONObject bannerArr = data.getJSONObject("bannerArr");
                            imgurl = bannerArr.getString("imgurl");
                            userimg = bannerArr.getString("icon");
                            ImageLoader.load(getActivity(), imgurl, vip_topimg);
                            JSONArray array = data.getJSONArray("keyArr");
                            List<String> sss = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {
                                sss.add(array.get(i).toString());
                            }

                            new Carousel_figure(getActivity()).loadImageNoCache(userimg, iv_vipheader);  //无缓存

                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            vip_item_horv.setLayoutManager(linearLayoutManager);
//                            adapter = new VipRecyclerViewAdapter(getActivity(), sss);
                            adapter.setSelectIndex(0);
                            vip_item_horv.setAdapter(adapter);
//                            Log.i("data1", "获取图片地址" + data);
                        } else {
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONArray array = result.getJSONArray("data");
//                            Log.i("data1", "获取平台推荐" + array.length());
                            if (array.length() < 4) {
                                pagePingTai = 1;
                            }
                            setImgandText(array, pingTaiImg,pingTaiImgMark, pingTaiTitle);
                        } else {
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONArray array = result.getJSONArray("data");
//                            Log.i("data1", "获取热门推荐" + array.toString() + "长度：" + array.length());
                            //热门推荐
                            if (array.length() < 4) {
                                pageHot = 1;
                            }
                            setImgandText(array, hotImg,hotImgMark, hotText);
                        } else {
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONArray array = result.getJSONArray("data");
//                            Log.i("data1", "获取Vip都在看" + array.length());
                            if (array.length() < 4) {
                                pageVip = 1;
                            }
                            setImgandText(array, vipImg,vipImgMark, vipText);
                        } else {
                            Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_vip, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  vip_recyclerview_horizontal = (RecyclerView) getActivity().findViewById(R.id.vip_recyclerview_horizontal);
        findViewId();
        initView();
        initTitle();
        //获取图片url和顶部列表
        initDataImgAndTop();
        //平台推荐
        initDataPingTai();
        //热门视频
        initDataHot();
        //Vip都在看
        initDataVip();
    }

    public void initTitle() {
//        ImageLoader.load(getActivity(), SharedPreferencesTools.getStricon(getActivity()), iv_vipheader);

        if (SharedPreferencesTools.getVipFlag(getActivity()) == 1) {
            tv_tip.setText("尊敬的VIP用户" + SharedPreferencesTools.getUserName(getActivity()) + ",您好");
            iv_openvip.setVisibility(View.INVISIBLE);
            tv_tip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Str_Uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                    if (!TextUtils.isEmpty(Str_Uid)) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        mainActivity.ToFragment(4);
                        mainActivity.refresh();
                    }
                }
            });
        } else {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.img_default2);
            iv_vipheader.setImageDrawable(drawable);
            tv_tip.setText("开通会员VIP海量视频免费看");
            iv_openvip.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        vip_item_horv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        adapter.setSelectIndex(position);
                        adapter.notifyDataSetChanged();
                        keyId = position + "";
                        initDataPingTai();
                    }

                    @Override
                    public void onLongClick(View view, int posotion) {
//                        Log.d("123", "onLongClick position : " + posotion);
                    }
                }));
        iv_vip_toserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), CustomActivity.class);
                intent.putExtra("type","home");
                startActivity(intent);*/

                Intent intent = new Intent(getActivity(), VipSpecialAreaActivity.class);
                //intent.putExtra("type","home");
                startActivity(intent);
            }
        });
        iv_openvip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesTools.getUids(getActivity()) == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), MyOpenMenberActivity.class);
                    startActivity(intent);
                }

            }
        });

    }

    private void findViewId() {
        vip_topimg = (ImageView) getActivity().findViewById(R.id.vip_topimg);
        iv_openvip = (TextView) getActivity().findViewById(R.id.iv_openvip);
        iv_vip_toserch = (ImageView) getActivity().findViewById(R.id.iv_vip_toserch);
        iv_vipheader = (CircleImageView) getActivity().findViewById(R.id.iv_vipheader);
        vip_item_horv = (RecyclerView) getActivity().findViewById(R.id.vip_item_horv);
        tv_tip = (TextView) getActivity().findViewById(R.id.tv_tip);

        ImageView pingtai_top_img1 = (ImageView) getActivity().findViewById(R.id.pingtai_top_img1);
        ImageView pingtai_top_img2 = (ImageView) getActivity().findViewById(R.id.pingtai_top_img2);
        ImageView pingtai_top_img3 = (ImageView) getActivity().findViewById(R.id.pingtai_top_img3);
        ImageView pingtai_top_img4 = (ImageView) getActivity().findViewById(R.id.pingtai_top_img4);
        pingTaiImgMark.add(pingtai_top_img1);
        pingTaiImgMark.add(pingtai_top_img2);
        pingTaiImgMark.add(pingtai_top_img3);
        pingTaiImgMark.add(pingtai_top_img4);

        ImageView pingtai_img1 = (ImageView) getActivity().findViewById(R.id.pingtai_img1);
        ImageView pingtai_img2 = (ImageView) getActivity().findViewById(R.id.pingtai_img2);
        ImageView pingtai_img3 = (ImageView) getActivity().findViewById(R.id.pingtai_img3);
        ImageView pingtai_img4 = (ImageView) getActivity().findViewById(R.id.pingtai_img4);
        pingTaiImg.add(pingtai_img1);
        pingTaiImg.add(pingtai_img2);
        pingTaiImg.add(pingtai_img3);
        pingTaiImg.add(pingtai_img4);

        TextView pingtai_text1 = (TextView) getActivity().findViewById(R.id.pingtai_text1);
        TextView pingtai_text2 = (TextView) getActivity().findViewById(R.id.pingtai_text2);
        TextView pingtai_text3 = (TextView) getActivity().findViewById(R.id.pingtai_text3);
        TextView pingtai_text4 = (TextView) getActivity().findViewById(R.id.pingtai_text4);
        pingTaiTitle.add(pingtai_text1);
        pingTaiTitle.add(pingtai_text2);
        pingTaiTitle.add(pingtai_text3);
        pingTaiTitle.add(pingtai_text4);

        ImageView hot_top_img1 = (ImageView) getActivity().findViewById(R.id.hot_top_img1);
        ImageView hot_top_img2 = (ImageView) getActivity().findViewById(R.id.hot_top_img2);
        ImageView hot_top_img3 = (ImageView) getActivity().findViewById(R.id.hot_top_img3);
        ImageView hot_top_img4 = (ImageView) getActivity().findViewById(R.id.hot_top_img4);
        hotImgMark.add(hot_top_img1);
        hotImgMark.add(hot_top_img2);
        hotImgMark.add(hot_top_img3);
        hotImgMark.add(hot_top_img4);

        ImageView hot_img1 = (ImageView) getActivity().findViewById(R.id.hot_img1);
        ImageView hot_img2 = (ImageView) getActivity().findViewById(R.id.hot_img2);
        ImageView hot_img3 = (ImageView) getActivity().findViewById(R.id.hot_img3);
        ImageView hot_img4 = (ImageView) getActivity().findViewById(R.id.hot_img4);
        hotImg.add(hot_img1);
        hotImg.add(hot_img2);
        hotImg.add(hot_img3);
        hotImg.add(hot_img4);

        TextView hot_text1 = (TextView) getActivity().findViewById(R.id.hot_text1);
        TextView hot_text2 = (TextView) getActivity().findViewById(R.id.hot_text2);
        TextView hot_text3 = (TextView) getActivity().findViewById(R.id.hot_text3);
        TextView hot_text4 = (TextView) getActivity().findViewById(R.id.hot_text4);
        hotText.add(hot_text1);
        hotText.add(hot_text2);
        hotText.add(hot_text3);
        hotText.add(hot_text4);

        ImageView vip_top_img1 = (ImageView) getActivity().findViewById(R.id.vip_top_img1);
        ImageView vip_top_img2 = (ImageView) getActivity().findViewById(R.id.vip_top_img2);
        ImageView vip_top_img3 = (ImageView) getActivity().findViewById(R.id.vip_top_img3);
        ImageView vip_top_img4 = (ImageView) getActivity().findViewById(R.id.vip_top_img4);
        vipImgMark.add(vip_top_img1);
        vipImgMark.add(vip_top_img2);
        vipImgMark.add(vip_top_img3);
        vipImgMark.add(vip_top_img4);

        ImageView vip_img1 = (ImageView) getActivity().findViewById(R.id.vip_img1);
        ImageView vip_img2 = (ImageView) getActivity().findViewById(R.id.vip_img2);
        ImageView vip_img3 = (ImageView) getActivity().findViewById(R.id.vip_img3);
        ImageView vip_img4 = (ImageView) getActivity().findViewById(R.id.vip_img4);
        vipImg.add(vip_img1);
        vipImg.add(vip_img2);
        vipImg.add(vip_img3);
        vipImg.add(vip_img4);

        TextView vip_text1 = (TextView) getActivity().findViewById(R.id.vip_text1);
        TextView vip_text2 = (TextView) getActivity().findViewById(R.id.vip_text2);
        TextView vip_text3 = (TextView) getActivity().findViewById(R.id.vip_text3);
        TextView vip_text4 = (TextView) getActivity().findViewById(R.id.vip_text4);
        vipText.add(vip_text1);
        vipText.add(vip_text2);
        vipText.add(vip_text3);
        vipText.add(vip_text4);

        rl_pingtai_lot = (RelativeLayout) getActivity().findViewById(R.id.rl_pingtai_lot);
        rl_hot_lot = (RelativeLayout) getActivity().findViewById(R.id.rl_hot_lot);
        rl_vip_lot = (RelativeLayout) getActivity().findViewById(R.id.rl_vip_lot);
        rl_pingtai_lot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagePingTai++;
                initDataPingTai();
            }
        });
        rl_hot_lot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageHot++;
                initDataHot();
            }
        });
        rl_vip_lot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageVip++;
                initDataVip();
            }
        });
        vip_topimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOpenMenberActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        initDataImgAndTop();
        super.onResume();
    }

    public void initDataImgAndTop() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipArea);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
//                    Log.e("vipresult",result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }


    private void initDataPingTai() {
        Runnable runnable0 = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipChangeVideo);
                    obj.put("keyid", keyId);  //    "经典手术",       //0     "超级访问",      //1    "名家视角",      //2    "走进科室",    //3      "百家讲坛",    //4   "多学科会诊"   //5
                    obj.put("page", pagePingTai);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable0).start();
    }


    private void initDataHot() {
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipChangeVideo);
                    obj.put("keyid", "hit");
                    obj.put("page", pageHot);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable1).start();
    }

    private void initDataVip() {
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.vipChangeVideo);
                    obj.put("keyid", "vip");
                    obj.put("page", pageVip);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(),
                            URLConfig.CCMTVAPP1, obj.toString());
                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable2).start();
    }

    public void setImgandText(final JSONArray array, List<ImageView> imageViews,List<ImageView> topimageViews, List<TextView> textViews) {

        try {
            for (int i = 0; i < 4; i++) {
                if (i < array.length()) {
                    imageViews.get(i).setVisibility(View.VISIBLE);
                    textViews.get(i).setVisibility(View.VISIBLE);
                    final JSONObject object = array.getJSONObject(i);
                    if(array.getJSONObject(i).getInt("videopaymoney")>0){
                        topimageViews.get(i).setVisibility(View.VISIBLE);
                        topimageViews.get(i).setImageResource(R.mipmap.charge);
                    }else{
                        topimageViews.get(i).setVisibility(View.GONE);
                         if (array.getJSONObject(i).getInt("flag")==3){
                            topimageViews.get(i) .setImageResource(R.mipmap.vip_img);
                            topimageViews.get(i).setVisibility(View.VISIBLE);
                        }
                    }
                    ImageLoader.load(getActivity(), array.getJSONObject(i).getString("picurl"), imageViews.get(i));
                    textViews.get(i).setText(array.getJSONObject(i).getString("title"));
                    imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                            if (uid == null || ("").equals(uid)) {
                                return;
                            }
                            Intent intent = new Intent(v.getContext(), VideoFive.class);
                            try {
                                intent.putExtra("aid", object.getString("aid"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            v.getContext().startActivity(intent);
                        }
                    });
                    textViews.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                            if (uid == null || ("").equals(uid)) {
                                return;
                            }
                            Intent intent = new Intent(v.getContext(), VideoFive.class);
                            try {
                                intent.putExtra("aid", object.getString("aid"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            v.getContext().startActivity(intent);
                        }
                    });
                } else {
                    imageViews.get(i).setVisibility(View.GONE);
                    topimageViews.get(i).setVisibility(View.GONE);
                    textViews.get(i).setVisibility(View.GONE);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
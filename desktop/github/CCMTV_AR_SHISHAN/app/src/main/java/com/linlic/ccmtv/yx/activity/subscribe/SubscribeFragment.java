package com.linlic.ccmtv.yx.activity.subscribe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceDepartmentBean;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.MaxDepartmentAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.MinDepartmentAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.TitleAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.SubKeshiData;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.linlic.ccmtv.yx.utils.permission.SpacesItemDecoration;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.igexin.sdk.GTServiceManager.context;

/**
 * name：订阅
 * author：Larry
 * data：2017/6/15.
 */
public class SubscribeFragment extends BaseFragment {
    private NodataEmptyLayout subscribe_list_nodata;
    private ImageView iv_addsub;
    private LinearLayout layout_subnologin, id_recyclerview_conference_department_layout;
    private LinearLayout layout_nodata1;
    private RecyclerView recyclerViewDepartment;
    private RecyclerView recyclerViewYear;
    private NestedScrollView my_scroll_view;
    private MyListView list_sub;
    private TitleAdapter adapter;
    private List<SubKeshiData> keshiDatas = new ArrayList<>();
    private MaxDepartmentAdapter maxDepartmentAdapter;
    private MinDepartmentAdapter minDepartmentAdapter;
    private List<ConferenceDepartmentBean> maxdepartmentDatas = new ArrayList<>();
    private List<ConferenceDepartmentBean> mindepartmentDatas = new ArrayList<>();
    private List<List<ConferenceDepartmentBean>> departmentDatas = new ArrayList<>();
    private List<String> titles = new ArrayList();
    public static List<JSONArray> titlesIllness;
    private LinearLayout layout_keshitype;
    private Button btn_tosub;
    public static List<Followks> followkses;
    private List<ConferenceBean> conferenceBannerList = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    List<Followks> nfollowkses;
    private ArrayList<JSONObject> fk;
    private JSONObject jo;
    private Banner banner;
    private ArrayList jb;
    private LinkedHashMap km;
    private ArrayList dt;
//    private SmartRefreshLayout refreshLayout;
    private int page=1;
    private int index=0;
    private String id;
    private boolean isNoMore = false;
    private boolean isload = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        titles.clear();
                        JSONObject result = new JSONObject(msg.obj.toString());
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject data = result.getJSONObject("data");
                            followkses = new Gson().fromJson(data.getJSONArray("followks").toString(), new TypeToken<List<Followks>>() {
                            }.getType());
//                            Log.e("followkses", followkses.toString());


                            JSONArray dataConferenceBanner = data.getJSONArray("meeting");
                            if (dataConferenceBanner != null && dataConferenceBanner.length() > 0) {
                                images.clear();
                                conferenceBannerList.clear();
                                for (int i = 0; i < dataConferenceBanner.length(); i++) {
                                    JSONObject conferenceBannerObject = dataConferenceBanner.getJSONObject(i);
                                    ConferenceBean conferenceBean = new ConferenceBean();
                                    conferenceBean.setId(conferenceBannerObject.getString("id"));
                                    conferenceBean.setTitle(conferenceBannerObject.getString("title"));
                                    conferenceBean.setIconUrl(conferenceBannerObject.getString("picurl"));
                                    conferenceBannerList.add(conferenceBean);
                                    images.add(conferenceBean.getIconUrl());
                                }
                                initBanner();
                            }

                            JSONArray jsonArray = data.getJSONArray("followks");
                            titlesIllness = new ArrayList<>();
                            //回传已订阅数据
                            maxdepartmentDatas.clear();
                            departmentDatas.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(j);
                                ConferenceDepartmentBean conferenceDepartmentBean = new ConferenceDepartmentBean();
                                conferenceDepartmentBean.setId(jsonObject.getString("id"));
                                conferenceDepartmentBean.setKid("");
                                conferenceDepartmentBean.setKname(jsonObject.getString("name"));
                                maxdepartmentDatas.add(conferenceDepartmentBean);
                                //获取二级科室
                                JSONArray array = jsonObject.has("alljb") ? jsonObject.getJSONArray("alljb") : null;
                                //判断是否有子集
                                if (array != null) {
                                    List<ConferenceDepartmentBean> mindepartment = new ArrayList<>();
                                    for (int k = 0; k < array.length(); k++) {
                                        //设置
                                        JSONObject jsonObject1 = (JSONObject) array.get(k);
                                        ConferenceDepartmentBean conferenceDepartmentBean2 = new ConferenceDepartmentBean();
                                        conferenceDepartmentBean2.setId(jsonObject1.getString("id"));
                                        conferenceDepartmentBean2.setKname(jsonObject1.getString("name"));
                                        mindepartment.add(conferenceDepartmentBean2);
                                    }
                                    maxdepartmentDatas.get(j).setKid(departmentDatas.size() + "");
                                    departmentDatas.add(mindepartment);
                                }

                                titlesIllness.add(j, array);
                            }
                            if (maxdepartmentDatas.size() > 0) {

                                maxDepartmentAdapter.setSelected(0);

                                if (maxdepartmentDatas.get(0).getKid().trim().length() < 1) {
                                    mindepartmentDatas = new ArrayList<ConferenceDepartmentBean>();
                                    id_recyclerview_conference_department_layout.setVisibility(View.GONE);
                                    initData2(0);
                                } else {
                                    id_recyclerview_conference_department_layout.setVisibility(View.VISIBLE);
                                    mindepartmentDatas = departmentDatas.get(Integer.parseInt(maxdepartmentDatas.get(0).getKid()));
                                    minDepartmentAdapter.setmList(mindepartmentDatas);
                                    minDepartmentAdapter.setSelected(0);
                                    id=mindepartmentDatas.get(0).getId();
                                    initData1(id);
                                }
                            }

                            if (followkses.size() == 0) {
                                layout_nodata1.setVisibility(View.VISIBLE);
                                layout_keshitype.setVisibility(View.GONE);
                            } else {
                                layout_nodata1.setVisibility(View.GONE);
                                layout_keshitype.setVisibility(View.VISIBLE);
                            }


                            LogUtil.e("nfollowks", data.getJSONArray("nfollowks").toString());
                            nfollowkses = new Gson().fromJson(data.getJSONArray("nfollowks").toString()
                                    , new TypeToken<List<Followks>>() {
                                    }.getType());

                            for (int i = 0; i < followkses.size(); i++) {
                                titles.add(i, followkses.get(i).getName());
                            }

                           /* setExploreShow(getActivity().getSupportFragmentManager());*/
//                            Log.i("Titles", titles.toString());
//                            Log.i("Titles", followkses.toString());
//                            Log.i("Titles", "刷新title");
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(),
                                    result.getString("errorMessage"),
                                    Toast.LENGTH_SHORT).show();
                        }
//                        setResultStatus(followkses.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
//                        setResultStatus(followkses.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    }
                    break;
                case 2:
                    JSONObject result = null;
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONArray dataArray = result.getJSONArray("data");
                            List<SubKeshiData> datas = JsonUtils.fromJsonArray(dataArray.toString(),SubKeshiData.class);
                            if (page==1){
                                keshiDatas.clear();
                            }
                            keshiDatas.addAll(datas);
                            if(datas.size()==0&&page!=1){
                                //MyProgressBarDialogTools.hide();
                                isNoMore = true;
                                ToastUtils.makeText(getActivity(),"暂无更多数据");
                                return;
                            }
                            if (mindepartmentDatas.size() != 0) { //选择的科室有疾病
                                if (adapter==null){
                                    adapter = new TitleAdapter(getActivity(), keshiDatas);
                                    list_sub.setAdapter(adapter);
                                    list_sub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            isload=true;
                                            String aid=keshiDatas.get(i).getAid();
                                            Intent intent = new Intent(getActivity(), VideoFive.class);
                                            intent.putExtra("aid",aid);
                                            startActivity(intent);
                                        }
                                    });

                                    my_scroll_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                        @Override
                                        public void onScrollChange(NestedScrollView v, int x, int y, int oldScrollX, int oldScrollY) {
                                            int scrollY = v.getScrollY();//顶端以及滑出去的距离
                                            int height = v.getHeight();//界面的高度
                                            int scrollViewMeasuredHeight = v.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                                            if (scrollY == 0) {//在顶端的时候
                                            } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
                                                if (!isNoMore) {
                                                    page++;
                                                    initData1(id);
                                                }
                                            } else {//当在中间的时候

                                            }
                                        }
                                    });
                                }
                            }
                            adapter.notifyDataSetChanged();
                            MyProgressBarDialogTools.hide();
                        } else {
                            MyProgressBarDialogTools.hide();
                            isNoMore = true;
                            // 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(keshiDatas.size() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        setResultStatus(keshiDatas.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(keshiDatas.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            list_sub.setVisibility(View.VISIBLE);
            subscribe_list_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                subscribe_list_nodata.setNetErrorIcon();
            } else {
                subscribe_list_nodata.setLastEmptyIcon();
            }
            list_sub.setVisibility(View.GONE);
            subscribe_list_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_subscribe, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findId();
        setClick();
        initRecyclerView();
        initData();
        //refresh();
    }

    private void initRecyclerView() {
        //创建LinearLayoutManager
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        //设置
        recyclerViewDepartment.setLayoutManager(manager);
        recyclerViewYear.setLayoutManager(manager2);
        recyclerViewYear.addItemDecoration(new SpacesItemDecoration(10));
        //设置为横向滑动
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        //实例化适配器
        maxDepartmentAdapter = new MaxDepartmentAdapter(getContext(), maxdepartmentDatas);
        minDepartmentAdapter = new MinDepartmentAdapter(getContext(), mindepartmentDatas);
        //设置适配器
        recyclerViewDepartment.setAdapter(maxDepartmentAdapter);
        recyclerViewYear.setAdapter(minDepartmentAdapter);
        maxDepartmentAdapter.setOnItemClickListener(new MaxDepartmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                maxDepartmentAdapter.setSelected(position);
                MyProgressBarDialogTools.show(getActivity());
                if (maxdepartmentDatas.get(position).getKid().trim().length() < 1) {
                    mindepartmentDatas = new ArrayList<ConferenceDepartmentBean>();
                    id_recyclerview_conference_department_layout.setVisibility(View.GONE);
                    initData2(position);
                } else {
                    id_recyclerview_conference_department_layout.setVisibility(View.VISIBLE);
                    mindepartmentDatas = departmentDatas.get(Integer.parseInt(maxdepartmentDatas.get(position).getKid()));
                    minDepartmentAdapter.setmList(mindepartmentDatas);
                    minDepartmentAdapter.setSelected(0);
                    id=mindepartmentDatas.get(0).getId();
                    page=1;
                    initData1(id);
                }
            }
        });

        minDepartmentAdapter.setOnItemClickListener(new MinDepartmentAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                page=1;
                id=mindepartmentDatas.get(position).getId();
                initData1(id);
            }
        });

    }

    private void initBanner() {
        //设置banner样式
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Log.e("会议专题banner位置", position + "  =================");
                String bannerId = conferenceBannerList.get(position).getId();
                Intent intent = new Intent(getContext(), ConferenceDetailActivity.class);
                intent.putExtra("conferenceId", bannerId);
                startActivity(intent);

            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }


    public void findId() {
        subscribe_list_nodata = getActivity().findViewById(R.id.subscribe_list_nodata);
//        refreshLayout=getActivity().findViewById(refreshLayout);
        banner = (Banner) getActivity().findViewById(R.id.id_conference_banner);
        iv_addsub = (ImageView) getActivity().findViewById(R.id.iv_addsub);
        layout_subnologin = (LinearLayout) getActivity().findViewById(R.id.layout_subnologin);
        layout_nodata1 = (LinearLayout) getActivity().findViewById(R.id.layout_nodata1);
        my_scroll_view =(NestedScrollView) getActivity().findViewById(R.id.my_scroll_view);
        id_recyclerview_conference_department_layout = (LinearLayout) getActivity().findViewById(R.id.id_recyclerview_conference_department_layout);
        btn_tosub = (Button) getActivity().findViewById(R.id.btn_tosub);
        ImageView img_show_no = getActivity().findViewById(R.id.img_show_no);
        img_show_no.setImageResource(R.mipmap.nodata_follow);

        layout_keshitype = (LinearLayout) getActivity().findViewById(R.id.layout_keshitype);
        list_sub = (MyListView) getActivity().findViewById(R.id.list_sub);
        recyclerViewDepartment = (RecyclerView) getActivity().findViewById(R.id.id_recyclerview_conference_department);
        recyclerViewYear = (RecyclerView) getActivity().findViewById(R.id.id_recyclerview_conference_time);



    }

//    private void refresh() {
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                page=1;
//                initData1(id);
//                refreshlayout.finishRefresh();
//            }
//        });
//        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                page=page+1;
//                LogUtil.e("分页",String.valueOf(page));
//                initData1(id);
//                refreshlayout.finishLoadmore();
//            }
//        });
//    }

    private void setClick() {
        iv_addsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isload = false;
                Intent intent = new Intent(getActivity(), SubscribeManagerActivity.class);
                List<Followks> item_followks = new ArrayList<Followks>();
                if (followkses != null) {
                    item_followks.addAll(followkses);
                }
                if (nfollowkses != null) {
                    item_followks.addAll(nfollowkses);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable("followkses", (Serializable) followkses);
                bundle.putSerializable("nfollowkses", (Serializable) item_followks);
                bundle.putSerializable("dt", dt);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
        btn_tosub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isload = false;
                Intent intent = new Intent(getActivity(), SubscribeManagerActivity.class);
                Bundle bundle = new Bundle();
                List<Followks> item_followks = new ArrayList<Followks>();
                if (followkses != null) {
                    item_followks.addAll(followkses);
                }
                if (nfollowkses != null) {
                    item_followks.addAll(nfollowkses);
                }
                bundle.putSerializable("followkses", (Serializable) followkses);
                bundle.putSerializable("nfollowkses", (Serializable) item_followks);
                intent.putExtras(bundle);
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isload==false){
            initData();
        }
    }

    public void initData() {
        String uid = SharedPreferencesTools.getUids(getActivity());
        if (uid == null || ("").equals(uid)) {
            layout_subnologin.setVisibility(View.VISIBLE);
            layout_keshitype.setVisibility(View.GONE);
        } else {
            layout_subnologin.setVisibility(View.GONE);
            layout_keshitype.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
//                        obj.put("act", URLConfig.subscribeKs);
                        obj.put("act", URLConfig.subscribeKsnew);
                        obj.put("uid", SharedPreferencesTools.getUidToLoginClose(getActivity()));
                        String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, obj.toString());
//                        Log.e("subresult", result.toString());
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }

    }


 /*   */

    /**
     * FragmentLayout界面赋值
     */
   /* private void setExploreShow(FragmentManager mFragmentManager) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ArrayList<Class<?>> clazzList = new ArrayList<Class<?>>();
        for (int i = 0; i < titles.size(); i++) {
            clazzList.add(PublicFragment.class);
        }
        ft.replace(
                R.id.main_frame,
                ExploreViewPagerFragment
                        .newInstance()
                        .setFragmentObjList(getActivity(), titles, clazzList)
                        .setViewpagerIndxe(0)
                        .setViewpagerCacheLimit(0)
                        .setTextSizeTag(true)
//                        .setSlidingTabStripImage(R.drawable.store_title_image_mr, R.drawable.store_title_image_xz)
                        .setSlidingTabStripImage(R.drawable.store_title_image_mr, R.drawable.conference_condition_selected)
                        .setTextColorSelect(R.color.store_selector_slide_title))
                .commit();
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
        if (requestCode == 0 && resultCode == 4) {
            followkses = (List<Followks>) data.getSerializableExtra("followkses");
            nfollowkses = (List<Followks>) data.getSerializableExtra("nfollowkses");
//            followksesNew = (ArrayList<JSONObject>) data.getSerializableExtra("followksesNew");
//            Log.e("followksesNew0",followksesNew.toString());
        }
    }


    public void initData1(final String id) {
       // MyProgressBarDialogTools.show(getActivity());
        isNoMore = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("act", URLConfig.getSubscribeData);
                    jsonObject.put("id",id);
                    jsonObject.put("page",page);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, jsonObject.toString());

                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void initData2(final int index) {
       // MyProgressBarDialogTools.show(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("act", URLConfig.getSubscribeData);
                    jsonObject.put("id", maxdepartmentDatas.get(index).getId());
                    jsonObject.put("page",page);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP1, jsonObject.toString());

                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}

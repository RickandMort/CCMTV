
package com.linlic.ccmtv.yx.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.home.PlayVideo_expert;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.medical_database.Article_details;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.CategoryView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.TextHighLight;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name:搜索
 * author:Tom
 * 2016-3-2下午7:07:12
 */
public class CustomActivity2 extends BaseActivity implements OnClickListener {
    static CustomActivity2 isFilsh;
    private ListView department_list;// 数据加载
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    BaseListAdapter baseListAdapter;
    private Context context = null;
    //用户统计
    private String  type;
    private String custom_title;
    private String video_class = "";
    private String disease_class = "";
    private String keywords = "";
    private String keyword = "";
    private String posttime = "";
    private LinearLayout custom_result;
    private String departmentsSelect = "";
    private int page = 1;
    private boolean isNoMore = false;
    private RelativeLayout hearderViewLayout;
    private CategoryView categoryView;
    private CategoryView categoryView2;
    private static long lastClickTime;
    /*====================筛选条件====================*/
    /*private LinearLayout llVideoList;
    private LinearLayout llConferenceList;
    private LinearLayout llExpertList;
    private ScrollView svThreeTypeList;*/
    private NodataEmptyLayout lt_nodata1;
    /*private ListView lvTypeVideo;
    private ListView lvTypeConference;
    private ListView lvTypeExpert;*/
    private ListView lvAll;
    /*private GridView lvTypeVideo;
    private GridView lvTypeConference;
    private GridView lvTypeExpert;
    private Button btnTypeVideoMore;
    private Button btnTypeConferenceMore;
    private Button btnTypeExpertMore;*/
    private BaseListAdapter baseListAdapterVideo;
    private BaseListAdapter baseListAdapterConference;
    private BaseListAdapter baseListAdapterExpert;
    private List<Map<String, Object>> dataVideo = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> dataConference = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> dataExpert = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> dataAritual = new ArrayList<Map<String, Object>>();
    private int dataType = 1;
    private int pageVideo = 0;//第一次点击查看还是查看第一页数据
    private int pageConference = 0;
    private int pageExpert = 0;
    private int pageArtial = 0;

    //private SearchVideoListAdapter baseListAdapterVideo;
    /*====================筛选条件====================*/
    private String newestKey = "热度";
    private List<String> newestList = null;//最新
    private String typesKey = "类型";
    private List<String> typesList = null;//类型
    private String yearsKey = "年份";
    private List<String> yearsList = null;//年份
    private String departmentsKey = "科室";
    private List<String> departmentsList = null;//科室
    private String memberKey = "会员";
    private List<String> memberList = null;//是否是会员
    private TextView newest_result;
    private TextView types_result;
    private TextView years_result;
    private TextView departments_result;
    private String departments_name = "";
    private EditText editText1;
    private TextView member_result;
    private LinearLayout categoryLayout;
    private ImageView categoryImg;
    private Map<String, Object> keshiMap = new HashMap<>();
    private List<Map<String, Object>> dataSearchAll = new ArrayList<>();
    private int globalPosition = 0;
    private String videoCount,specCount,zjsCount,articleCount;
    /*====================筛选条件====================*/

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        dataSearchAll.clear();
                        dataVideo.clear();
                        dataConference.clear();
                        dataExpert.clear();
                        dataAritual.clear();
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            JSONArray dataArrayVideo = jsonObjectData.getJSONArray("video");
                            JSONArray dataArrayConference = jsonObjectData.getJSONArray("spec");
                            JSONArray dataArrayExpert = jsonObjectData.getJSONArray("zjs");
                            JSONArray dataArrayarticle = jsonObjectData.has("article")?jsonObjectData.getJSONArray("article"):new JSONArray();
                            videoCount=jsonObjectData.getString("videoCount");
                            specCount=jsonObjectData.getString("specCount");
                            zjsCount=jsonObjectData.getString("zjsCount");
                            articleCount=jsonObjectData.getString("articleCount");
                            if (dataArrayVideo.length() > 0) {
                                Map<String, Object> mapVideoTitle = new HashMap<String, Object>();
                                mapVideoTitle.put("layoutType", "1");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                mapVideoTitle.put("title", "视频");
                                dataSearchAll.add(mapVideoTitle);

                                for (int i = 0; i < dataArrayVideo.length(); i++) {
                                    JSONObject customJson = dataArrayVideo.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "3");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("posttime", customJson.getString("posttime"));
                                    map.put("hits", customJson.getString("hits"));
                                    map.put("flag", customJson.getString("flag"));
                                    map.put("money", customJson.getString("money"));
                                    map.put("videopaymoney", customJson.getString("videopaymoney"));
                                    dataSearchAll.add(map);
                                }

                                Map<String, Object> mapVideoMore = new HashMap<String, Object>();
                                mapVideoMore.put("layoutType", "2");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                mapVideoMore.put("title", "视频");
                                mapVideoMore.put("text", "查看更多");
                                dataSearchAll.add(mapVideoMore);
                            }


                            if (dataArrayConference.length() > 0) {
                                Map<String, Object> mapConferenceTitle = new HashMap<String, Object>();
                                mapConferenceTitle.put("layoutType", "1");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                mapConferenceTitle.put("title", "会议");
                                dataSearchAll.add(mapConferenceTitle);

                                for (int i = 0; i < dataArrayConference.length(); i++) {
                                    JSONObject customJson = dataArrayConference.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "4");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                    map.put("id", customJson.getString("id"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("time", customJson.getString("time"));
                                    dataSearchAll.add(map);
                                }

                                Map<String, Object> mapConferenceMore = new HashMap<String, Object>();
                                mapConferenceMore.put("layoutType", "2");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                mapConferenceMore.put("title", "会议");
                                mapConferenceMore.put("text", "查看更多");
                                dataSearchAll.add(mapConferenceMore);
                            }

                            if (dataArrayExpert.length() > 0) {
                                Map<String, Object> mapExpertTitle = new HashMap<String, Object>();
                                mapExpertTitle.put("layoutType", "1");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家   6：文章
                                mapExpertTitle.put("title", "专家");
                                dataSearchAll.add(mapExpertTitle);

                                for (int i = 0; i < dataArrayExpert.length(); i++) {
                                    JSONObject customJson = dataArrayExpert.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "5");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家  6：文章
                                    map.put("mid", customJson.getString("mid"));
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("smalltitle", customJson.getString("smalltitle"));
                                    map.put("keywords", customJson.getString("keywords"));
                                    map.put("flag", customJson.getString("flag"));
                                    map.put("content", customJson.getString("content"));
                                    map.put("description", customJson.getString("description"));
                                    dataSearchAll.add(map);
                                }

                                Map<String, Object> mapExpertMore = new HashMap<String, Object>();
                                mapExpertMore.put("layoutType", "2");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家
                                mapExpertMore.put("title", "专家");
                                mapExpertMore.put("text", "查看更多");
                                dataSearchAll.add(mapExpertMore);
                            }
                            // 新增 文章板块
                            if (dataArrayarticle.length() > 0) {
                                Map<String, Object> mapArticleTitle = new HashMap<String, Object>();
                                mapArticleTitle.put("layoutType", "1");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家  6：文章
                                mapArticleTitle.put("title", "文章");
                                dataSearchAll.add(mapArticleTitle);

                                for (int i = 0; i < dataArrayarticle.length(); i++) {
                                    JSONObject customJson = dataArrayarticle.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "6");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家 6：文章
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("posttime", customJson.getString("posttime"));
                                    map.put("hits", customJson.getString("hits"));
                                    dataSearchAll.add(map);
                                }

                                Map<String, Object> mapExpertMore2 = new HashMap<String, Object>();
                                mapExpertMore2.put("layoutType", "2");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家  6：文章
                                mapExpertMore2.put("title", "文章");
                                mapExpertMore2.put("text", "查看更多");
                                dataSearchAll.add(mapExpertMore2);
                                LogUtil.e("CustomActivity2", "dataArrayarticle Length:" + dataSearchAll.size());
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                        /*baseListAdapterVideo.notifyDataSetChanged();
                        baseListAdapterConference.notifyDataSetChanged();
                        baseListAdapterExpert.notifyDataSetChanged();*/

                        /*if (dataVideo.size() <= 0) {
                            llVideoList.setVisibility(View.GONE);
                        } else {
                            llVideoList.setVisibility(View.VISIBLE);
                        }
                        if (dataConference.size() <= 0) {
                            llConferenceList.setVisibility(View.GONE);
                        } else {
                            llConferenceList.setVisibility(View.VISIBLE);
                        }
                        if (dataExpert.size() <= 0) {
                            llExpertList.setVisibility(View.GONE);
                        } else {
                            llExpertList.setVisibility(View.VISIBLE);
                        }*/

//                        if (dataSearchAll.size() <= 0) {
//                            lvAll.setVisibility(View.GONE);
//                            lt_nodata1.setVisibility(View.VISIBLE);
//                        } else {
//                            lvAll.setVisibility(View.VISIBLE);
//                            lt_nodata1.setVisibility(View.GONE);
//                        }
                        setResultStatus(dataSearchAll.size() > 0, jsonObject.getInt("status"));
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataArray = jsonObject.getJSONObject("data");
                            newestList = new ArrayList<String>();//最新
                            typesList = new ArrayList<String>();//类型
                            yearsList = new ArrayList<String>();//年份
                            departmentsList = new ArrayList<String>();//科室
                            memberList = new ArrayList<String>();//是否是会员
                            newestList.clear();
                            typesList.clear();
                            yearsList.clear();
                            departmentsList.clear();
                            memberList.clear();
                            newestList.clear();
                            categoryView.removeAllViews();
                            categoryView2.removeAllViews();
                            try {
                                if (getIntent().getExtras().getString("mode").equals("1")) {
                                    editText1.setText(custom_title);
                                }
                                if (getIntent().getExtras().getString("mode").equals("2")) {
                                    editText1.setText(custom_title);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            JSONArray newHotArr = dataArray.getJSONArray("newHotArr");
                            for (int i = 0; i < newHotArr.length(); i++) {
                                newestList.add(newHotArr.getString(i));
                            }
                            categoryView.add(newestList, newestKey, video_class);

                            for (String str : newestList) {
                                if (str.equals(video_class)) {
                                    newest_result.setText(video_class);
                                }
                            }

                            JSONArray videoClassArr = dataArray.getJSONArray("videoClassArr");
                            for (int i = 0; i < videoClassArr.length(); i++) {
                                typesList.add(videoClassArr.getString(i));
                            }

                            categoryView.add(typesList, typesKey, video_class);
                            switch (video_class) {
                                case "为我推荐":
                                    newest_result.setText("最新");
                                    break;
                                case "最新视频":
                                    newest_result.setText("最新");
                                    break;
                                case "手术演示":
                                    types_result.setText("·手术");
                                    video_class = "手术";
                                    break;
                                case "病例讨论":
                                    types_result.setText("·病例");
                                    video_class = "病例";
                                    break;
                                case "超级访问":
                                    types_result.setText("·座谈");
                                    video_class = "座谈";
                                    break;
                                case "百家讲坛":
                                    types_result.setText("·讲座");
                                    video_class = "讲座";
                                    break;
                                case "名家视角":
                                    types_result.setText("·采访");
                                    video_class = "采访";
                                    break;
                            }

                            JSONArray timeArr = dataArray.getJSONArray("timeArr");
                            for (int i = 0; i < timeArr.length(); i++) {
                                yearsList.add(timeArr.getString(i));
                            }

                            categoryView.add(yearsList, yearsKey, "");

                            JSONArray keshiArr = dataArray.getJSONArray("keshiArr");
                            for (int i = 0; i < keshiArr.length(); i++) {
                                JSONObject keshi = keshiArr.getJSONObject(i);
                                keshiMap.put(keshi.getString("name"), keshi.getString("id"));
                                departmentsList.add(keshi.getString("name"));
                            }

                            categoryView2.add(departmentsList, departmentsKey, disease_class);
                            if (keshiMap.containsKey(disease_class)) {
                                departments_result.setText(keshiMap.get(disease_class).toString());
                                departments_name = disease_class;
                            }

                            JSONArray hyArr = dataArray.getJSONArray("hyArr");
                            for (int i = 0; i < hyArr.length(); i++) {
                                memberList.add(hyArr.getString(i));
                            }

                            categoryView.add(memberList, memberKey, "");
                            if (video_class.equals("为我推荐")) {
                                video_class = "";
                            }
                            if (video_class.equals("最新视频")) {
                                video_class = "";
                            }
                        } else {
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        baseListAdapter.notifyDataSetChanged();
                        wDelayed();
                        setmsgdb();
                        //setTexts();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("更多视频数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            JSONArray dataArrayVideo = jsonObjectData.getJSONArray("video");
                            if (dataArrayVideo.length() > 0) {
                                /*List<Map<String, Object>> leftList = new ArrayList<>() ;
                                for (Map<String, Object> map:dataSearchAll.subList(0, globalPosition)){
                                    leftList.add(map);
                                }*/
                                List<Map<String, Object>> newList = new ArrayList<>();
                                newList.addAll(dataSearchAll);
                                List<Map<String, Object>> leftList = newList.subList(0, globalPosition);
                                List<Map<String, Object>> rightList = newList.subList(globalPosition, dataSearchAll.size());

                                dataVideo.clear();
                                for (int i = 0; i < dataArrayVideo.length(); i++) {
                                    JSONObject customJson = dataArrayVideo.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "3");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("posttime", customJson.getString("posttime"));
                                    map.put("hits", customJson.getString("hits"));
                                    map.put("flag", customJson.getString("flag"));
                                    map.put("money", customJson.getString("money"));
                                    map.put("videopaymoney", customJson.getString("videopaymoney"));
                                    dataVideo.add(map);
                                }
                                if (dataVideo.size()<50){
                                    rightList.get(rightList.size()-1).put("text","暂无更多数据");
                                }
                                dataSearchAll.clear();
                                for (Map<String, Object> map : leftList) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : dataVideo) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : rightList) {
                                    dataSearchAll.add(map);
                                }

                                if (dataSearchAll.size() > 0) {
                                    baseListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        //baseListAdapterVideo.notifyDataSetChanged();
                        MyProgressBarDialogTools.hide();

                        /*if (dataVideo.size() <= 0) {
                            llVideoList.setVisibility(View.GONE);
                        } else {
                            llVideoList.setVisibility(View.VISIBLE);
                        }*/
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            JSONArray dataArrayConference = jsonObjectData.getJSONArray("spec");
                            if (dataArrayConference.length() > 0) {
                                List<Map<String, Object>> newList = new ArrayList<>();
                                newList.addAll(dataSearchAll);
                                List<Map<String, Object>> leftList = newList.subList(0, globalPosition);
                                List<Map<String, Object>> rightList = newList.subList(globalPosition, dataSearchAll.size());
                                dataConference.clear();
                                for (int i = 0; i < dataArrayConference.length(); i++) {
                                    JSONObject customJson = dataArrayConference.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "4");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家
                                    map.put("id", customJson.getString("id"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("time", customJson.getString("time"));
                                    dataConference.add(map);
                                }
                                if (dataConference.size()<50){
                                    rightList.get(rightList.size()-1).put("text","暂无更多数据");
                                }
                                dataSearchAll.clear();
                                for (Map<String, Object> map : leftList) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : dataConference) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : rightList) {
                                    dataSearchAll.add(map);
                                }

                                if (dataSearchAll.size() > 0) {
                                    baseListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        baseListAdapterConference.notifyDataSetChanged();
                        MyProgressBarDialogTools.hide();

                        /*if (dataConference.size() <= 0) {
                            llConferenceList.setVisibility(View.GONE);
                        } else {
                            llConferenceList.setVisibility(View.VISIBLE);
                        }*/
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            JSONArray dataArrayExpert = jsonObjectData.getJSONArray("zjs");
                            if (dataArrayExpert.length() > 0) {
                                List<Map<String, Object>> newList = new ArrayList<>();
                                newList.addAll(dataSearchAll);
                                List<Map<String, Object>> leftList = newList.subList(0, globalPosition);
                                List<Map<String, Object>> rightList = newList.subList(globalPosition, dataSearchAll.size());
                                dataExpert.clear();
                                for (int i = 0; i < dataArrayExpert.length(); i++) {
                                    JSONObject customJson = dataArrayExpert.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "5");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家
                                    map.put("mid", customJson.getString("mid"));
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("smalltitle", customJson.getString("smalltitle"));
                                    map.put("keywords", customJson.getString("keywords"));
                                    map.put("flag", customJson.getString("flag"));
                                    map.put("content", customJson.getString("content"));
                                    map.put("description", customJson.getString("description"));
                                    dataExpert.add(map);
                                }
                                if (dataExpert.size()<50){
                                    rightList.get(rightList.size()-1).put("text","暂无更多数据");
                                }
                                dataSearchAll.clear();
                                for (Map<String, Object> map : leftList) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : dataExpert) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : rightList) {
                                    dataSearchAll.add(map);
                                }

                                if (dataSearchAll.size() > 0) {
                                    baseListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        baseListAdapterExpert.notifyDataSetChanged();
                        MyProgressBarDialogTools.hide();
                        /*if (dataExpert.size() <= 0) {
                            llExpertList.setVisibility(View.GONE);
                        } else {
                            llExpertList.setVisibility(View.VISIBLE);
                        }*/
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 15:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        LogUtil.e("搜索数据", msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                            JSONArray dataArrayArticle = jsonObjectData.getJSONArray("article");
                            if (dataArrayArticle.length() > 0) {
                                List<Map<String, Object>> newList = new ArrayList<>();
                                newList.addAll(dataSearchAll);
                                List<Map<String, Object>> leftList = newList.subList(0, globalPosition);
                                List<Map<String, Object>> rightList = newList.subList(globalPosition, dataSearchAll.size());
                                dataAritual.clear();
                                for (int i = 0; i < dataArrayArticle.length(); i++) {
                                    JSONObject customJson = dataArrayArticle.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("layoutType", "6");//layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家 6：文章
                                    map.put("aid", customJson.getString("aid"));
                                    map.put("title", customJson.getString("title"));
                                    map.put("picurl", customJson.getString("picurl"));
                                    map.put("posttime", customJson.getString("posttime"));
                                    map.put("hits", customJson.getString("hits"));
                                    dataAritual.add(map);
                                }
                                if (dataAritual.size()<50){
                                    rightList.get(rightList.size()-1).put("text","暂无更多数据");
                                }
                                dataSearchAll.clear();
                                for (Map<String, Object> map : leftList) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : dataAritual) {
                                    dataSearchAll.add(map);
                                }
                                for (Map<String, Object> map : rightList) {
                                    dataSearchAll.add(map);
                                }

                                if (dataSearchAll.size() > 0) {
                                    baseListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            isNoMore = true;
                            Toast.makeText(CustomActivity2.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        baseListAdapterExpert.notifyDataSetChanged();
                        MyProgressBarDialogTools.hide();
                        /*if (dataExpert.size() <= 0) {
                            llExpertList.setVisibility(View.GONE);
                        } else {
                            llExpertList.setVisibility(View.VISIBLE);
                        }*/
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                    }
                    break;
                case 201:
                    LinearLayout rrr = (LinearLayout) categoryView.getChildAt(3);
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) rrr.getChildAt(0);
                    RadioGroup groupr = (RadioGroup) horizontalScrollView.getChildAt(0);
                    RadioButton radioButton = (RadioButton) findViewById(groupr.getCheckedRadioButtonId());
                    horizontalScrollView.fullScroll(ScrollView.FOCUS_RIGHT);
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(dataSearchAll.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOST);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvAll.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvAll.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_activity2_main);
        context = this;
        isFilsh = this;
        type = getIntent().getStringExtra("type");
        try {
            if (getIntent().getExtras().getString("mode").equals("1")) {
                custom_title = getIntent().getExtras().getString("custom_title");
                keyword = getIntent().getExtras().getString("custom_title");
            }
            if (getIntent().getExtras().getString("mode").equals("2")) {
                video_class = getIntent().getExtras().getString("video_class");
                custom_title = getIntent().getExtras().getString("video_class");
            }
            if (getIntent().getExtras().getString("mode").equals("3")) {
                disease_class = getIntent().getExtras().getString("disease_class");
                custom_title = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("4")) {
                disease_class = getIntent().getExtras().getString("disease_class");
                custom_title = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().getString("mode").equals("5")) {
                video_class = getIntent().getExtras().getString("video_class");
            }
            if (getIntent().getExtras().getString("mode").equals("6")) {
                video_class = getIntent().getExtras().getString("video_class");
                disease_class = getIntent().getExtras().getString("disease_class");
            }
            if (getIntent().getExtras().containsKey(departmentsSelect)) {
                departmentsSelect = getIntent().getExtras().getString("getIntent().getExtras().");
            }
            switch (video_class) {
                case "手术演示":
                    video_class = "手术";
                    break;
                case "病例讨论":
                    video_class = "病例";
                    break;
                case "超级访问":
                    video_class = "座谈";
                    break;
                case "百家讲坛":
                    video_class = "讲座";
                    break;
                case "名家视角":
                    video_class = "采访";
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        findId();
        newvideoSearch();

//        setmsgdb();
        onClick();

        initListView();
        initData();
    }

    private void initData() {
        setmsgdb();
        /*for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("departemnt_item_title", "标题" + i);
            map.put("department_id", i);
            map.put("department_on_demand", "播放数：" + i);
            map.put("department_times", "2018-05-24");
            map.put("departemnt_item_img", "http://img1.imgtn.bdimg.com/it/u=3014892180,449401099&fm=27&gp=0.jpg");
            map.put("money", "0");
            map.put("flag", "0");
            map.put("videopaymoney", "0");
            dataVideo.add(map);
        }
        baseListAdapterVideo.notifyDataSetChanged();*/
    }

    private void initListView() {
        baseListAdapter = new BaseListAdapter(lvAll, dataSearchAll, R.layout.custom_search_item) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                super.convert(helper, item, isScrolling, position);
                //layoutType 1:标题    2：查看更多按钮    3：视频    4：会议    5：专家
                String layoutType = dataSearchAll.get(position).get("layoutType").toString();
                switch (layoutType) {
                    case "1":
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.VISIBLE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.GONE);
                        //helper.setText(R.id.id_tv_custom_search_item_title, ((Map) item).get("title").toString());
                        helper.setVisibility(R.id.search_item_article, View.GONE);
                        String my_title=((Map) item).get("title").toString();
                        TextView textView=helper.getView(R.id.id_tv_custom_search_item_title);
                        switch (my_title){
                            case "视频":
                                textView.setText(my_title+"（"+videoCount+")");
                                break;
                            case "会议":
                                textView.setText(my_title+"（"+specCount+")");
                                break;
                            case "文章":
                                textView.setText(my_title+"（"+zjsCount+")");
                                break;
                            case "专家":
                                textView.setText(my_title+"（"+articleCount+")");
                                break;
                        }
                        break;
                    case "2":
                        helper.setText(R.id.id_btn_search_more,((Map) item).get("text").toString());
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.VISIBLE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.GONE);
                        helper.setVisibility(R.id.search_item_article, View.GONE);

                        break;
                    case "3":
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.VISIBLE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.GONE);
                        helper.setVisibility(R.id.search_item_article, View.GONE);
                        SpannableStringBuilder spannableStringBuilderVideo = TextHighLight.matcherSearchContent(((Map) item).get("title") + "", new String[]{editText1.getText().toString()});
                        helper.setText(R.id.search_video_title, spannableStringBuilderVideo);
                        helper.setText(R.id.search_video_id, ((Map) item).get("aid") + "");
                        helper.setText(R.id.search_video_play_times, intChange2Str(Integer.valueOf(((Map) item).get("hits").toString())));
                        helper.setText(R.id.search_video_times, "发布时间："+((Map) item).get("posttime") + "");
                        // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                        helper.setImageBitmapGlide(context, R.id.search_video_img, ((Map) item).get("picurl") + "", 100, 80);
                        //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                        if (!((Map) item).get("videopaymoney").equals("0")) {
                            //收费
                            helper.setImage(R.id.search_video_top_img, R.mipmap.charge);
                            helper.setVisibility(R.id.search_video_top_img, View.VISIBLE);
                        } else {
                            helper.setVisibility(R.id.search_video_top_img, View.GONE);
                            if (((Map) item).get("flag").toString().equals("3")) {
                                //VIP
                                helper.setImage(R.id.search_video_top_img, R.mipmap.vip_img);
                                helper.setVisibility(R.id.search_video_top_img, View.VISIBLE);
                            }
                        }
                        break;
                    case "4":
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.VISIBLE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.GONE);
                        helper.setVisibility(R.id.search_item_article, View.GONE);
                        SpannableStringBuilder spannableStringBuilderConference = TextHighLight.matcherSearchContent(((Map) item).get("title") + "", new String[]{editText1.getText().toString()});
                        helper.setText(R.id.search_conference_title, spannableStringBuilderConference);
                        helper.setText(R.id.search_conference_id, ((Map) item).get("id") + "");
                        helper.setText(R.id.search_conference_time, "发布时间" + ((Map) item).get("time") + "");
                        // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                        helper.setImageBitmapGlide(context, R.id.search_conference_img, ((Map) item).get("picurl") + "", 100, 80);
                        break;
                    case "5":
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.VISIBLE);
                        helper.setVisibility(R.id.search_item_article, View.GONE);
                        helper.setText(R.id.search_expert_name, ((Map) item).get("title") + "");
                        helper.setText(R.id.search_expert_id, ((Map) item).get("mid") + "");
                        helper.setText(R.id.search_expert_hospital, ((Map) item).get("description") + "");
                        helper.setText(R.id.search_expert_job, ((Map) item).get("keywords") + "" + ((Map) item).get("smalltitle") + "");
                        //helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("picurl") + "");
                        helper.setCircleImageViewBitmapGlide(context, R.id.search_expert_img, ((Map) item).get("picurl") + "");
                        //helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("picurl") + "", 100, 80);
                        break;

                    case "6":
                        helper.setVisibility(R.id.id_tv_custom_search_item_title, View.GONE);
                        helper.setVisibility(R.id.id_ll_search_more, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_conference, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_video, View.GONE);
                        helper.setVisibility(R.id.id_ll_custom_search_item_expert, View.GONE);
                        helper.setVisibility(R.id.search_item_article, View.VISIBLE);
                        helper.setText(R.id.search_art_id, ((Map) item).get("aid") + "");
                        helper.setText(R.id.search_art_title, ((Map) item).get("title") + "");
                        helper.setText(R.id.search_art_time, ((Map) item).get("posttime") + "");
                        String picurl = (String) ((Map) item).get("picurl");
                        if (!TextUtils.isEmpty(picurl))
                            helper.setImageBitmapGlide(context, R.id.search_art_img, ((Map) item).get("picurl") + "", 100, 60);
                        else
                            helper.setVisibility(R.id.search_art_img, View.GONE);
                        break;
                }
            }
        };

        lvAll.setAdapter(baseListAdapter);

        lvAll.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String layoutType = dataSearchAll.get(position).get("layoutType").toString();
                switch (layoutType) {
                    case "1":
                        break;
                    case "2":
                        if(dataSearchAll.get(position).get("text").toString().equals("查看更多")){
                            globalPosition = position;
                            String title = dataSearchAll.get(position).get("title").toString();
                            switch (title) {
                                case "视频":
                                    if (pageVideo <= 0) {
                                        if (dataSearchAll.get(position - 1).get("layoutType").equals("3")) {
                                            dataSearchAll.remove(position - 1);
                                            globalPosition--;
                                        }
                                        if (dataSearchAll.get(position - 2).get("layoutType").equals("3")) {
                                            dataSearchAll.remove(position - 2);
                                            globalPosition--;
                                        }
                                    }
                                    pageVideo += 1;
                                    dataType = 2;
                                    setmsgdb();
                                    break;
                                case "会议":
                                    if (pageConference <= 0) {
                                        if (dataSearchAll.get(position - 1).get("layoutType").equals("4")) {
                                            dataSearchAll.remove(position - 1);
                                            globalPosition--;
                                        }
                                        if (dataSearchAll.get(position - 2).get("layoutType").equals("4")) {
                                            dataSearchAll.remove(position - 2);
                                            globalPosition--;
                                        }
                                    }
                                    pageConference += 1;
                                    dataType = 3;
                                    setmsgdb();
                                    break;
                                case "专家":
                                    if (pageExpert <= 0) {
                                        if (dataSearchAll.get(position - 1).get("layoutType").equals("5")) {
                                            dataSearchAll.remove(position - 1);
                                            globalPosition--;
                                        }
                                        if (dataSearchAll.get(position - 2).get("layoutType").equals("5")) {
                                            dataSearchAll.remove(position - 2);
                                            globalPosition--;
                                        }
                                    }
                                    pageExpert += 1;
                                    dataType = 4;
                                    setmsgdb();
                                    break;
                                case "文章":
                                    if (pageArtial <= 0) {
                                        if (dataSearchAll.get(position - 1).get("layoutType").equals("6")) {
                                            dataSearchAll.remove(position - 1);
                                            globalPosition--;
                                        }
                                        if (dataSearchAll.get(position - 2).get("layoutType").equals("6")) {
                                            dataSearchAll.remove(position - 2);
                                            globalPosition--;
                                        }
                                    }
                                    pageArtial += 1;
                                    dataType = 5;
                                    setmsgdb();
                                    break;
                            }
                        }
                        break;
                    case "3":
                        Intent intentVideo = new Intent(context, VideoFive.class);
                        intentVideo.putExtra("aid", dataSearchAll.get(position).get("aid").toString());
                        if(SharedPreferencesTools.getUidONnull(context).equals("")){
                            startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                            LocalApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            startActivity(intentVideo);

                        }

                        break;
                    case "4":
                        Intent intentConference = new Intent(CustomActivity2.this, ConferenceDetailActivity.class);
                        intentConference.putExtra("conferenceId", dataSearchAll.get(position).get("id").toString());
                        startActivity(intentConference);
                        break;
                    case "5":
                        Intent intentExpert = new Intent(CustomActivity2.this, PlayVideo_expert.class);
                        intentExpert.putExtra("fid", dataSearchAll.get(position).get("mid").toString());
                        intentExpert.putExtra("aid", dataSearchAll.get(position).get("aid").toString());
                        startActivity(intentExpert);
                        break;
                    case "6":
                        //跳转文章详情
                        Intent intentArticle = new Intent(CustomActivity2.this, Article_details.class);
                        intentArticle.putExtra("aid", dataSearchAll.get(position).get("aid").toString());
                        if(SharedPreferencesTools.getUidONnull(context).equals("")){
                            startActivity(new Intent(context, LoginActivity.class).putExtra("source", ""));
                            LocalApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            startActivity(intentArticle);
                        }
                        break;
                }
            }
        });

        //baseListAdapterVideo=new SearchVideoListAdapter(context,dataVideo);
        /*baseListAdapterVideo = new BaseListAdapter(lvTypeVideo, dataVideo, R.layout.custom_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                SpannableStringBuilder spannableStringBuilder = TextHighLight.matcherSearchContent(((Map) item).get("title") + "", new String[]{editText1.getText().toString()});
                helper.setText(R.id.departemnt_item_title, spannableStringBuilder);
                helper.setText(R.id.department_id, ((Map) item).get("aid") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("hits") + "");
                helper.setText(R.id.department_times, ((Map) item).get("posttime") + "");
                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("picurl") + "", 100, 80);
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if (((Map) item).get("flag").toString().equals("3")) {
                        //VIP
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }
            }
        };

        baseListAdapterConference = new BaseListAdapter(lvTypeVideo, dataConference, R.layout.custom_item_conference) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                SpannableStringBuilder spannableStringBuilder = TextHighLight.matcherSearchContent(((Map) item).get("title") + "", new String[]{editText1.getText().toString()});
                helper.setText(R.id.departemnt_item_title, spannableStringBuilder);
                helper.setText(R.id.department_id, ((Map) item).get("id") + "");
                helper.setText(R.id.department_times, "发布时间" + ((Map) item).get("time") + "");
                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("picurl") + "", 100, 80);
            }
        };

        baseListAdapterExpert = new BaseListAdapter(lvTypeVideo, dataExpert, R.layout.custom_item_expert) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.departemnt_item_title, ((Map) item).get("title") + "");
                helper.setText(R.id.department_id, ((Map) item).get("mid") + "");
                helper.setText(R.id.department_times, ((Map) item).get("description") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("keywords") + "" + ((Map) item).get("smalltitle") + "");
                //helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("picurl") + "");
                helper.setCircleImageViewBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("picurl") + "");
                //helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("picurl") + "", 100, 80);
            }
        };

        lvTypeVideo.setAdapter(baseListAdapterVideo);
        lvTypeConference.setAdapter(baseListAdapterConference);
        lvTypeExpert.setAdapter(baseListAdapterExpert);

        lvTypeVideo.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                MyProgressBarDialogTools.hide();
            }
        });
        lvTypeConference.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                MyProgressBarDialogTools.hide();
            }
        });
        lvTypeExpert.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                MyProgressBarDialogTools.hide();
            }
        });*/

    }

    public void wDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    // 水平直接滚动800px，如果想效果更平滑可以使用smoothScrollTo(int x, int y)
                    LinearLayout linearLayout = (LinearLayout) categoryView2.getChildAt(0);
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) linearLayout.getChildAt(0);
                    RadioGroup groupr = (RadioGroup) horizontalScrollView.getChildAt(0);
                    RadioButton radioButton = (RadioButton) findViewById(groupr.getCheckedRadioButtonId());
                    horizontalScrollView.requestChildFocus(groupr, radioButton);
                    LinearLayout linearLayout1 = (LinearLayout) categoryView.getChildAt(1);
                    HorizontalScrollView horizontalScrollView1 = (HorizontalScrollView) linearLayout1.getChildAt(0);
                    RadioGroup groupr1 = (RadioGroup) horizontalScrollView1.getChildAt(0);
                    RadioButton radioButton1 = (RadioButton) findViewById(groupr1.getCheckedRadioButtonId());
                    horizontalScrollView1.requestChildFocus(groupr1, radioButton1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    public void findId() {
        super.findId();
        department_list = (ListView) findViewById(R.id.department_list);
        hearderViewLayout = (RelativeLayout) View.inflate(this, R.layout.custom_text, null);
        department_list.addHeaderView(hearderViewLayout);
        categoryView = (CategoryView) findViewById(R.id.category);
        custom_result = (LinearLayout) findViewById(R.id.custom_result);
        newest_result = (TextView) findViewById(R.id.newest_result);
        types_result = (TextView) findViewById(R.id.types_result);
        categoryView2 = (CategoryView) findViewById(R.id.category2);
        years_result = (TextView) findViewById(R.id.years_result);
        departments_result = (TextView) findViewById(R.id.departments_result);
        member_result = (TextView) findViewById(R.id.member_result);
        categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);
        categoryImg = (ImageView) findViewById(R.id.categoryImg);
        editText1 = (EditText) findViewById(R.id.editText1);

        lt_nodata1 = (NodataEmptyLayout) findViewById(R.id.lt_nodata1);
        lvAll = (ListView) findViewById(R.id.lv_search_all);
        /*svThreeTypeList = (ScrollView) findViewById(R.id.id_sv_three_type_list);
        llVideoList = (LinearLayout) findViewById(R.id.id_ll_search_video_list);
        llConferenceList = (LinearLayout) findViewById(R.id.id_ll_search_conference_list);
        llExpertList = (LinearLayout) findViewById(R.id.id_ll_search_exper_list);
        lvTypeVideo = (GridView) findViewById(R.id.id_lv_search_video_list);
        lvTypeConference = (GridView) findViewById(R.id.id_lv_search_conference_list);
        lvTypeExpert = (GridView) findViewById(R.id.id_lv_search_expert_list);
        btnTypeVideoMore = (Button) findViewById(R.id.id_btn_search_more_video);
        btnTypeConferenceMore = (Button) findViewById(R.id.id_btn_search_more_conference);
        btnTypeExpertMore = (Button) findViewById(R.id.id_btn_search_more_expert);*/

        /*lvTypeVideo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "点击视频"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, VideoFive.class);
                intent.putExtra("aid", dataVideo.get(position).get("aid").toString());
                startActivity(intent);
            }
        });
        lvTypeConference.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "点击会议"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomActivity2.this, ConferenceDetailActivity.class);
                intent.putExtra("conferenceId", dataConference.get(position).get("id").toString());
                startActivity(intent);
            }
        });
        lvTypeExpert.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "点击专家"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomActivity2.this, PlayVideo_expert.class);
                intent.putExtra("fid", dataExpert.get(position).get("mid").toString());
                intent.putExtra("aid", dataExpert.get(position).get("aid").toString());
                startActivity(intent);
            }
        });

        btnTypeVideoMore.setOnClickListener(this);
        btnTypeConferenceMore.setOnClickListener(this);
        btnTypeExpertMore.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.id_btn_search_more_video:
                if (pageVideo == 0) {
                    //第一次加载更多清除最初的两条数据
                    dataVideo.clear();
                }
                pageVideo += 1;
                dataType = 2;
                setmsgdb();
                break;
            case R.id.id_btn_search_more_conference:
                if (pageConference == 0) {
                    //第一次加载更多清除最初的两条数据
                    dataConference.clear();
                }
                pageConference += 1;
                dataType = 3;
                setmsgdb();
                break;
            case R.id.id_btn_search_more_expert:
                if (pageExpert == 0) {
                    //第一次加载更多清除最初的两条数据
                    dataExpert.clear();
                }
                pageExpert += 1;
                dataType = 4;
                setmsgdb();
                break;*/
        }
    }

    private void setAnotherListClose(int type) {
        if (type == 0) {
            for (int i = 2; i < dataConference.size(); i++) {
                dataConference.remove(i);
            }
            for (int i = 2; i < dataExpert.size(); i++) {
                dataExpert.remove(i);
            }
            baseListAdapterConference.notifyDataSetChanged();
            baseListAdapterExpert.notifyDataSetChanged();
        } else if (type == 1) {
            for (int i = 2; i < dataVideo.size(); i++) {
                dataVideo.remove(i);
            }
            for (int i = 2; i < dataExpert.size(); i++) {
                dataExpert.remove(i);
            }
            baseListAdapterVideo.notifyDataSetChanged();
            baseListAdapterExpert.notifyDataSetChanged();
        } else if (type == 2) {
            for (int i = 2; i < dataConference.size(); i++) {
                dataConference.remove(i);
            }
            for (int i = 2; i < dataVideo.size(); i++) {
                dataVideo.remove(i);
            }
            baseListAdapterConference.notifyDataSetChanged();
            baseListAdapterVideo.notifyDataSetChanged();
        }
    }


    public void onClick() {
        editText1.setOnKeyListener(new View.OnKeyListener() {

            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //以下方法防止两次发送请求
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CustomActivity2.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    //存储数据
                    saveHot_search_grid();
                    dataType = 1;
                    setmsgdb();
                }
                return false;
            }
        });

        categoryView2.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("热度")) {
                    newest_result.setText(button.getText());
                } else if (button.getTag().equals("类型")) {
                    types_result.setText("·" + button.getText());
                } else if (button.getTag().equals("年份")) {
                    years_result.setText("·" + button.getText());
                } else if (button.getTag().equals("科室")) {
                    departments_result.setText(keshiMap.get(button.getText()).toString());
                    departments_name = button.getText().toString();
                } else if (button.getTag().equals("会员")) {
                    member_result.setText("·" + button.getText());
                }
                video_class = types_result.getText().toString();
                disease_class = departments_result.getText().toString();
                keywords = "";
                posttime = years_result.getText().toString();
                setEmptyString();
                page = 1;
                pageVideo = 0;
                pageConference = 0;
                pageExpert = 0;
                dataType = 1;
                setmsgdb();
            }
        });

        //设置自定义监听器
        categoryView.setOnClickCategoryListener(new CategoryView.OnClickCategoryListener() {
            //逻辑回掉
            @Override
            public void click(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                if (button.getTag().equals("热度")) {
                    newest_result.setText(button.getText());
                } else if (button.getTag().equals("类型")) {
                    types_result.setText("·" + button.getText());
                } else if (button.getTag().equals("年份")) {
                    years_result.setText("·" + button.getText());
                } else if (button.getTag().equals("科室")) {
                    departments_result.setText(keshiMap.get(button.getText()).toString());
                    departments_name = button.getText().toString();
                } else if (button.getTag().equals("会员")) {
                    member_result.setText("·" + button.getText());
                }
                video_class = types_result.getText().toString();
                disease_class = departments_result.getText().toString();
                keywords = "";
                posttime = years_result.getText().toString();
                setEmptyString();
                page = 1;
                pageVideo = 0;
                pageConference = 0;
                pageExpert = 0;
                pageArtial = 0;
                dataType = 1;
                setmsgdb();
            }
        });

        categoryLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("展开")) {
                    categoryImg.setImageResource(R.mipmap.categoryimg1);
                    categoryView.setVisibility(View.GONE);
                    v.setTag("收缩");
                } else {
                    categoryImg.setImageResource(R.mipmap.categoryimg2);
                    categoryView.setVisibility(View.VISIBLE);
                    v.setTag("展开");
                }
            }
        });
    }

    /**
     * 为获取新增加的专家和会议数据，点击不限时使上传参数为空字符串，否则服务器无法正常返回数据
     */
    private void setEmptyString() {
        if (newest_result.getText().toString().equals("·不限") || newest_result.getText().toString().equals("不限")) {
            newest_result.setText("");
        }
        if (types_result.getText().toString().equals("·不限") || video_class.equals("·不限") || types_result.getText().toString().equals("不限") || video_class.equals("不限")) {
            types_result.setText("");
            video_class = "";
        }
        if (years_result.getText().toString().equals("·不限") || posttime.equals("·不限") || years_result.getText().toString().equals("不限") || posttime.equals("不限")) {
            years_result.setText("");
            posttime = "";
        }
        if (departments_result.getText().toString().equals("·不限") || departments_name.equals("·不限") || departments_result.getText().toString().equals("不限") || departments_name.equals("不限")) {
            departments_result.setText("");
            departments_name = "";
        }
        if (member_result.getText().toString().equals("·不限") || member_result.getText().toString().equals("不限")) {
            member_result.setText("");
        }
    }

    public void saveHot_search_grid() {
        if (editText1.getText().toString().trim().length() > 0) {
            MyDbUtils.saveHot_search_grid(context, editText1.getText().toString().trim());
        }
        keyword = editText1.getText().toString().trim();
    }

    /*public void setTexts() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.xinvideoSearch);
                    obj.put("video_class", video_class.replaceAll("·", ""));
                    obj.put("keshiid", departments_result.getText().toString());
                    obj.put("keshiname", departments_name);
                    obj.put("keywords", keywords.replaceAll("·", ""));
                    obj.put("keyword", keyword.replaceAll("·", ""));
                    obj.put("posttime", posttime.replaceAll("·", ""));
                    obj.put("newest_result", newest_result.getText().toString().replaceAll("·", ""));
                    obj.put("member_result", member_result.getText().toString().replaceAll("·", ""));
                    obj.put("page", page);
                    obj.put("type", dataType);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_SEARCH, obj.toString());
                    MyProgressBarDialogTools.hide();
                    Log.e("搜索数据-下行", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();

        baseListAdapter = new BaseListAdapter(department_list, data, R.layout.custom_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.departemnt_item_title, ((Map) item).get("departemnt_item_title") + "");
                helper.setText(R.id.department_id, ((Map) item).get("department_id") + "");
                helper.setText(R.id.department_on_demand, ((Map) item).get("department_on_demand") + "");
                helper.setText(R.id.department_times, ((Map) item).get("department_times") + "");
                // helper.setImageBitmap(R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "", ((Map) item).get("department_id") + "");
                helper.setImageBitmapGlide(context, R.id.departemnt_item_img, ((Map) item).get("departemnt_item_img") + "");
                //videopaymoney  不为0则为收费 为0 测试代表着是非收费视频
                if (!((Map) item).get("videopaymoney").equals("0")) {
                    //收费
                    helper.setImage(R.id.departemnt_item_top_img, R.mipmap.charge);
                    helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.departemnt_item_top_img, View.GONE);
                    if (((Map) item).get("flag").toString().equals("3")) {
                        //VIP
                        helper.setImage(R.id.departemnt_item_top_img, R.mipmap.vip_img);
                        helper.setVisibility(R.id.departemnt_item_top_img, View.VISIBLE);
                    }
                }

            }
        };
        department_list.setAdapter(baseListAdapter);
        // listview点击事件
        department_list.setOnItemClickListener(new casesharing_listListener());
        baseListAdapter.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = department_list.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == 0) {
                        isNoMore = false;
                    }
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = department_list.getChildAt(department_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == department_list.getHeight()) {
                        if (!isNoMore) {
                            page += 1;
                            setmsgdb();
                        }
                    }
                }
            }
        });
    }*/

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        if (page == 1) {
            data.removeAll(data);
            //   baseListAdapter.notifyDataSetChanged();

        }
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.secVideoSearch);
                    obj.put("video_class", video_class.replaceAll("·", ""));
                    obj.put("keshiid", departments_result.getText().toString());
                    obj.put("keshiname", departments_name);
                    //obj.put("keywords", keywords.replaceAll("·", ""));
                    obj.put("keyword", keyword.replaceAll("·", ""));
                    obj.put("posttime", posttime.replaceAll("·", ""));
//                    obj.put("newest_result", newest_result.getText().toString().replaceAll("·", ""));
                    obj.put("newest_result", newest_result.getText().toString());
                    obj.put("member_result", member_result.getText().toString().replaceAll("·", ""));
                    //obj.put("page", page + "");
                    obj.put("type", dataType + "");

                    if (dataType == 2) {
                        obj.put("page", pageVideo);
                    } else if (dataType == 3) {
                        obj.put("page", pageConference);
                    } else if (dataType == 4) {
                        obj.put("page", pageExpert);
                    } else if (dataType == 5) {
                        obj.put("page", pageArtial);
                    } else {
                        obj.put("page", page);
                    }

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_SEARCH, obj.toString());
//                    LogUtil.e("搜索页返回数据数据：", result);


                    if (dataType == 1) {
                        //所有数据
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    }

                    if (dataType == 2) {
                        //视频更多数据
                        Message message = new Message();
                        message.what = 12;
                        message.obj = result;
                        handler.sendMessage(message);
                    }

                    if (dataType == 3) {
                        //会议更多数据
                        Message message = new Message();
                        message.what = 13;
                        message.obj = result;
                        handler.sendMessage(message);
                    }

                    if (dataType == 4) {
                        //专家更多数据
                        Message message = new Message();
                        message.what = 14;
                        message.obj = result;
                        handler.sendMessage(message);
                    }

                    if (dataType == 5) {
                        //专家更多数据
                        Message message = new Message();
                        message.what = 15;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void newvideoSearch() {
        if (page == 1) {
            data.removeAll(data);
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.appNewSearch);
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP_SEARCH, obj.toString());
//                    LogUtil.e("搜索页条件", result);
                    Message message = new Message();
                    message.what = 2;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        new Thread(runnable).start();
    }


    public void getVideoRulest(final String aid) {
        final String uid = SharedPreferencesTools.getUidToLoginClose(context);
        if (uid == null || ("").equals(uid)) {
            return;
        }
        Intent intent = new Intent(context, VideoFive.class);
        intent.putExtra("aid", aid);
        startActivity(intent);

    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.department_id);
            String id = textView.getText().toString();
            // MyProgressBarDialogTools.show(context);
            getVideoRulest(id);

        }

    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {

        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(CustomActivity2.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }

    @Override
    public void onResume() {
        MyProgressBarDialogTools.hide();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (type.equals("home")) {
            enterUrl = "http://www.ccmtv.cn";
        } else {
            enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        }
        super.onPause();
    }

    private String intChange2Str(int number) {
        String str = "";
        if (number <= 0) {
            str = "";
        } else if (number < 1000) {
            str = number + "千次播放";
        } else {
            double d = (double) number;
            double num = d / 1000;//1.将数字转换成以万为单位的数字

            BigDecimal b = new BigDecimal(num);
            double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
            str = f1 + "千次播放";
        }
        return str;
    }


}
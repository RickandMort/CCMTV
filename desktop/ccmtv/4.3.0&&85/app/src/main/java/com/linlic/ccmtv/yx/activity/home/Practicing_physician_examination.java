package com.linlic.ccmtv.yx.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.conference.ConferenceMainActivity;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.entity.Practicing_itme;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 执业医师考试
 * Created by Administrator on 2016/12/6.
 */
public class Practicing_physician_examination extends BaseActivity {
    Context context;
    JSONObject result;
    JSONArray dataArray;

    @Bind(R.id.id_lv_conference)
    MyGridView id_lv_conference;//
    @Bind(R.id.top_img)
    ImageView top_img;//
    @Bind(R.id.text_type1)
    TextView text_type1;//
    @Bind(R.id.text_type2)
    TextView text_type2;//
    @Bind(R.id.text_type3)
    TextView text_type3;//
    @Bind(R.id.text_type4)
    TextView text_type4;//
    @Bind(R.id.text_type_1)
    View text_type_1;//
    @Bind(R.id.text_type_2)
    View text_type_2;//
    @Bind(R.id.text_type_3)
    View text_type_3;//
    @Bind(R.id.text_type_4)
    View text_type_4;//
    @Bind(R.id.rl_practicing_nodata1)
    NodataEmptyLayout rl_nodata;//
    private BaseListAdapter baseListAdapter;
    private List<String> title_list = new ArrayList<>();
    private Map<String,List<Practicing_itme>> practicing_map = new HashMap<>();
    String curr_title = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        result = new JSONObject(msg.obj + "");
                        boolean status = result.getInt("status") == 1;
                        if (status) {// 成功
                            dataArray = result.getJSONArray("data");
//                            loadImg(practicing_top_img, result.getString("topicon"));
                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject json = dataArray.getJSONObject(i);
                                title_list.add(json.getString("title"));

                                JSONArray items = json.getJSONArray("listdata");
                                List<Practicing_itme> practicing_itmes = new ArrayList<>();
                                for (int j = 0; j < items.length();j++ ) {
                                    JSONObject item = items.getJSONObject(j);
                                    Practicing_itme map = new Practicing_itme();
                                    map.setImg( item.getString("logo"));
                                    map.setFid( item.getString("fid"));
                                    map.setTitle( item.getString("name"));
                                    practicing_itmes.add(map);
                                }
                                practicing_map.put(json.getString("title"),practicing_itmes);
                            }
                            curr_title = title_list.get(0);
                            text_type1.setText(title_list.get(0));
                            text_type2.setText(title_list.get(1));
                            text_type3.setText(title_list.get(2));
                            text_type4.setText(title_list.get(3));
                            text_type1.setTextColor(Color.parseColor("#3897F9"));
                            text_type2.setTextColor(Color.parseColor("#666666"));
                            text_type3.setTextColor(Color.parseColor("#666666"));
                            text_type4.setTextColor(Color.parseColor("#666666"));
                            text_type_1.setVisibility(View.VISIBLE);
                            text_type_2.setVisibility(View.INVISIBLE);
                            text_type_3.setVisibility(View.INVISIBLE);
                            text_type_4.setVisibility(View.INVISIBLE);
                            // 将布局加入到当前布局中
                            baseListAdapter.refresh(practicing_map.get(curr_title));
                        } else {                                                                        // 失败
                            Toast.makeText(Practicing_physician_examination.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        setResultStatus(dataArray != null && dataArray.length() > 0, result.getInt("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };



    private void setResultStatus(boolean status, int code) {
        if (status) {
            id_lv_conference.setVisibility(View.VISIBLE);
            rl_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                rl_nodata.setNetErrorIcon();
            } else {
                rl_nodata.setLastEmptyIcon();
            }
            id_lv_conference.setVisibility(View.GONE);
            rl_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practicing_physician_examination);
        context = this;
//        mConxt = this;
        ButterKnife.bind(this);
        findId();
        setText();
        getListView();
    }



    public void setText() {
        text_type1.getPaint().setFakeBoldText(true);
        text_type2.getPaint().setFakeBoldText(true);
        text_type3.getPaint().setFakeBoldText(true);
        text_type4.getPaint().setFakeBoldText(true);
        text_type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!curr_title.equals(title_list.get(0))){
                    curr_title = title_list.get(0);
                    text_type1.setTextColor(Color.parseColor("#3897F9"));
                    text_type2.setTextColor(Color.parseColor("#666666"));
                    text_type3.setTextColor(Color.parseColor("#666666"));
                    text_type4.setTextColor(Color.parseColor("#666666"));
                    baseListAdapter.refresh(practicing_map.get(curr_title));
                    text_type_1.setVisibility(View.VISIBLE);
                    text_type_2.setVisibility(View.INVISIBLE);
                    text_type_3.setVisibility(View.INVISIBLE);
                    text_type_4.setVisibility(View.INVISIBLE);
                }
            }
        });
        text_type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!curr_title.equals(title_list.get(1))){
                    curr_title = title_list.get(1);
                    text_type1.setTextColor(Color.parseColor("#666666"));
                    text_type2.setTextColor(Color.parseColor("#3897F9"));
                    text_type3.setTextColor(Color.parseColor("#666666"));
                    text_type4.setTextColor(Color.parseColor("#666666"));
                    baseListAdapter.refresh(practicing_map.get(curr_title));
                    text_type_1.setVisibility(View.INVISIBLE);
                    text_type_2.setVisibility(View.VISIBLE);
                    text_type_3.setVisibility(View.INVISIBLE);
                    text_type_4.setVisibility(View.INVISIBLE);
                }
            }
        });
        text_type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!curr_title.equals(title_list.get(2))){
                    curr_title = title_list.get(2);
                    text_type1.setTextColor(Color.parseColor("#666666"));
                    text_type2.setTextColor(Color.parseColor("#666666"));
                    text_type3.setTextColor(Color.parseColor("#3897F9"));
                    text_type4.setTextColor(Color.parseColor("#666666"));
                    baseListAdapter.refresh(practicing_map.get(curr_title));
                    text_type_1.setVisibility(View.INVISIBLE);
                    text_type_2.setVisibility(View.INVISIBLE);
                    text_type_3.setVisibility(View.VISIBLE);
                    text_type_4.setVisibility(View.INVISIBLE);
                }
            }
        });
        text_type4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!curr_title.equals(title_list.get(3))){
                    curr_title = title_list.get(3);
                    text_type1.setTextColor(Color.parseColor("#666666"));
                    text_type2.setTextColor(Color.parseColor("#666666"));
                    text_type3.setTextColor(Color.parseColor("#666666"));
                    text_type4.setTextColor(Color.parseColor("#3897F9"));
                    baseListAdapter.refresh(practicing_map.get(curr_title));
                    text_type_1.setVisibility(View.INVISIBLE);
                    text_type_2.setVisibility(View.INVISIBLE);
                    text_type_3.setVisibility(View.INVISIBLE);
                    text_type_4.setVisibility(View.VISIBLE);
                }
            }
        });
        setActivity_title_name("执业考试");
        baseListAdapter = new BaseListAdapter(id_lv_conference, practicing_map.get(curr_title), R.layout.medical_congress_item3) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Practicing_itme conferenceBean = (Practicing_itme) item;
                helper.setText(R.id.practicing_itme_tilte, conferenceBean .getTitle());

            }
        };

        id_lv_conference.setAdapter(baseListAdapter);
        id_lv_conference.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(fastClick()){
                    String conferenceId =  practicing_map.get(curr_title).get(position).getFid();
                    Intent intent = new Intent(context, Practicing_physician_examination_list.class);
                    intent.putExtra("practicing_itme_tilte", practicing_map.get(curr_title).get(position).getTitle());
                    intent.putExtra("practicing_itme__img", practicing_map.get(curr_title).get(position).getImg());
                    intent.putExtra("practicing_itme__fuid", practicing_map.get(curr_title).get(position).getFid());
                    startActivity(intent);
                }


            }
        });
    }

    public void loadImg(ImageView img, String path) {
        ImageLoader.getInstance().displayImage(FirstLetter.getSpells(path), img);
    }

    public void getListView() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.examDocQualification);

                    String result = HttpClientUtils.sendPost(Practicing_physician_examination.this,
                            URLConfig.CCMTVAPP, obj.toString());
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}

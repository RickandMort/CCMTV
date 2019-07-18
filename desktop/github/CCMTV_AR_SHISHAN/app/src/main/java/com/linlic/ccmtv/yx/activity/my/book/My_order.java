package com.linlic.ccmtv.yx.activity.my.book;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Tutor_students_bean;
import com.linlic.ccmtv.yx.activity.entity.Video_book_Entity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_chapter;
import com.linlic.ccmtv.yx.activity.my.OpenMenberActivity2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity4;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.adapter.Video_book_Adapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.AutoTextView;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2019/7/4.
 */

public class My_order extends BaseActivity  {
    Context context;
    @Bind(R.id.activity_title_name)
    TextView activity_title_name;//
    @Bind(R.id.text1)
    TextView text1;//
    @Bind(R.id.text2)
    TextView text2;//
    @Bind(R.id.lt_book_nodata)
    NodataEmptyLayout book_nodata;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;//

    private int type = 2;
    BaseRecyclerViewAdapter baseRecyclerViewAdapter ;
    private List<Map<String,String>> video_book_entities = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                                JSONArray data = jsonObjects.getJSONArray("data");
                            video_book_entities.clear();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject dataObject = data.getJSONObject(i);
                                Map<String,String> map = new HashMap<>();
                                map.put("id",dataObject.getString("id"));
                                map.put("uid",dataObject.getString("uid"));
                                map.put("bid",dataObject.getString("bid"));
                                map.put("oid",dataObject.getString("oid"));
                                map.put("title",dataObject.getString("title"));
                                map.put("money",dataObject.getString("money"));
                                map.put("num",dataObject.getString("num"));
                                map.put("yz",dataObject.getString("yz"));
                                map.put("logistics",dataObject.getString("logistics"));
                                map.put("freight",dataObject.getString("freight"));
                                map.put("banner",dataObject.getString("banner"));
                                map.put("payment",dataObject.getString("payment"));
                                video_book_entities.add(map);
                            }
                        } else {
                            Toast.makeText(context , jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                        baseRecyclerViewAdapter.notifyDataSetChanged();
                        setResultStatus(video_book_entities.size() > 0, jsonObjects.getInt("status"));
                    } catch (Exception e) {
                        setResultStatus(video_book_entities.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                        e.printStackTrace();
                    }

                    break;

                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            recycler_view.setVisibility(View.VISIBLE);
            book_nodata.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                book_nodata.setNetErrorIcon();
            } else {
                book_nodata.setLastEmptyIcon();
            }
            recycler_view.setVisibility(View.GONE);
            book_nodata.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        context = this;
        ButterKnife.bind(this);
        SharedPreferencesTools.getUid(context);
        findId();
        initView();
        setmsgdb();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    public void initView() {

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setTextColor(Color.parseColor("#3997F9"));
                text2.setTextColor(Color.parseColor("#333333"));
                type = 2;
                setmsgdb();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setTextColor(Color.parseColor("#3997F9"));
                text1.setTextColor(Color.parseColor("#333333"));
                type = 1;
                setmsgdb();
            }
        });
        baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(R.layout.item_book_order_details,video_book_entities){
            @Override
            public void convert(BaseViewHolder helper, Object item) {
                Map<String,String> bean = (Map<String,String>) item;

                if(type == 2){
                    helper.setGone(R.id._item_7,false );
                }else{
                    helper.setText(R.id._item_7,"快递单号："+bean.get("logistics") );
                    helper.setGone(R.id._item_7,true );
                }

                helper.setText(R.id._item_1, "订单号："+ bean.get("oid").toString());
                helper.setText(R.id._item_2, bean.get("title") );
                helper.setText(R.id._item_3,"￥"+bean.get("money") );
                helper.setText(R.id._item_4,"X"+bean.get("num") );
                helper.setText(R.id._item_5,"运费 ￥"+bean.get("freight") );
                helper.setText(R.id._item_6,Html.fromHtml( "实付款：<font color='red'>￥"+bean.get("payment")+"</font>") );

                ImageView view = helper.getView(R.id._item_img);

                RequestOptions options = new RequestOptions();
                RequestOptions error = options.placeholder(R.mipmap.img_default)
                        .error(R.mipmap.img_default);
                Glide.with(context)
                        .load(FirstLetter.getSpells(bean.get("banner").toString()))
                        .apply(options)
                        .into(view);


            }
        };
        baseRecyclerViewAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recycler_view.setAdapter(baseRecyclerViewAdapter);

        baseRecyclerViewAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(context,Order_details.class);
                intent.putExtra("id",  video_book_entities.get(position).get("id"));
                startActivity(intent);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this );
//设置布局管理器
        recycler_view.setLayoutManager(layoutManager);
//设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.myBooksOrder);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("type", type);

                    String result = HttpClientUtils.sendPost(context, URLConfig.book_url, obj.toString());
                    MyProgressBarDialogTools.hide();

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
    }

}

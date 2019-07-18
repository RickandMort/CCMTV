package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Evaluation_in_detail_bean;
import com.linlic.ccmtv.yx.activity.entity.Evaluation_of_item;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/20.
 */

public class Training_Evaluation_in_detail extends BaseActivity   {
    private Context context;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.score)
    TextView score;//总得分
    @Bind(R.id.time_text)
    TextView time_text;//提交时间
    @Bind(R.id.lecturer_text)
    TextView lecturer_text;//主讲人

    @Bind(R.id.button_layout)
    LinearLayout button_layout;//

    private String  detailid;
    Evaluation_in_detail_bean evaluation_in_detail_bean = new Evaluation_in_detail_bean();
    private LinearLayout item_evaluation_department_list_top,item_the_evaluation_of_teaching_list_buttom;
    JSONObject result, data;
    private String fid;
    private String is_teaching = "";
    private String s_uid = "";

    private BaseListAdapter baseListAdapterVideo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray contentJson = dataJson.getJSONObject("dataList").getJSONArray("temp");
                                //分数
                                evaluation_in_detail_bean.setScore(dataJson.getJSONObject("dataList").getString("score").length()>0?dataJson.getJSONObject("dataList").getInt("score"):0);
                                //编辑查看状态 0 不可修改 1可修改
                                evaluation_in_detail_bean.setStatus(dataJson.getJSONObject("dataList").has("is_show")?0:1);

                                evaluation_in_detail_bean.setId(detailid );
                                time_text.setText(dataJson.getJSONObject("dataList").getString("eva_time").substring(0,10));
                                lecturer_text.setText(dataJson.getJSONObject("dataList").getString("realname") );

                                initDatas();


                                List<Evaluation_of_item> list = new ArrayList<>();
                                for (int i = 0; i<contentJson.length();i++){
                                    JSONObject dataJson1 = contentJson.getJSONObject(i);
                                    Evaluation_of_item evaluation_of_item = new Evaluation_of_item();
                                    evaluation_of_item.setId(i+"");
                                    evaluation_of_item.setGrade(dataJson1.getInt("user_score"));
                                    evaluation_of_item.setContent(dataJson1.getString("title"));
                                    evaluation_of_item.setThe_weight(dataJson1.getInt("weight"));
                                    evaluation_of_item.setMaxgrade(dataJson1.getInt("max_score"));
                                    List<String> grades =new ArrayList<String>();
                                    for(int k = 1; k <=dataJson1.getInt("max_score");k++ ){
                                        grades.add(""+k);
                                    }
                                    evaluation_of_item.setGrades(grades);
                                    list.add(evaluation_of_item);
                                }
                                evaluation_in_detail_bean.setEvaluation_of_items(list);
                                initViews();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    //设置分数
                    if(evaluation_in_detail_bean.getScore()<1){
                        score.setText("");
                    }else{
                        score.setText(evaluation_in_detail_bean.getScore()+"");
                    }

                    //根据状态 设置是否可以编辑
                    switch (evaluation_in_detail_bean.getStatus()){
                        case 0:

                            button_layout.setVisibility(View.GONE);
                            break;
                        case 1:

                            button_layout.setVisibility(View.VISIBLE);
                            break;
                        default:

                            button_layout.setVisibility(View.GONE);
                            break;
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                evaluation_in_detail_bean.setStatus(0);
                                baseListAdapterVideo.notifyDataSetChanged();

                                button_layout.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.training_evaluation_in_detail);
        context = this;
        ButterKnife.bind(this);
        detailid = getIntent().getStringExtra("detailid");
        fid = getIntent().getStringExtra("fid");
        is_teaching = getIntent().getStringExtra("is_teaching");
        s_uid = getIntent().getStringExtra("s_uid");

        findId();
        this.setActivity_title_name("课后评价");
        initDatas();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/activities/index.html";
        super.onPause();
    }

    private void initDatas() {
        Message message = new Message();
        message.what = 2;
        handler.sendMessage(message);
    }

    private void initViews() {

        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(listView, evaluation_in_detail_bean.getEvaluation_of_items(), R.layout.item_evaluation_in_detail) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Evaluation_of_item map = (Evaluation_of_item) item;
                helper.setText(R.id._item_id,map.getId());
                helper.setText(R.id._item_content,map.getContent());
                helper.setText(R.id._item_the_weight,"权重："+map.getThe_weight());
                helper.setText(R.id.tv_value, map.getGrade()+"");

                helper.setSpinner(R.id._item_grade,score,evaluation_in_detail_bean,map,R.id.tv_value,evaluation_in_detail_bean.getStatus());

            }
        };
        listView.setAdapter(baseListAdapterVideo);

    }

    public void setCancel(View view){
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("你确定要取消吗？")
                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
    public void setSubmit(View view){


            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.activitiesAddTemp);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id",evaluation_in_detail_bean.getId());
                        obj.put("fid",fid);
                        JSONObject data = new JSONObject();
                        data.put("score",evaluation_in_detail_bean.getScore() );

                        JSONArray content = new JSONArray();
                        for (int i = 0; i<evaluation_in_detail_bean.getEvaluation_of_items().size();i++){
                            Evaluation_of_item evaluation_of_item = evaluation_in_detail_bean.getEvaluation_of_items().get(i);
                            JSONObject item_json = new JSONObject();
                            item_json.put("title",evaluation_of_item.getContent());
                            item_json.put("weight",evaluation_of_item.getThe_weight());
                            item_json.put("user_score",evaluation_of_item.getGrade());
                            item_json.put("max_score",evaluation_of_item.getMaxgrade());
                            content.put(item_json);
                        }
                        data.put("temp",content);
                        obj.put("data",data);
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("课后评价提交", result);
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
            new Thread(runnable).start();

    }



    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesTempInfo);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid",getIntent().getStringExtra("fid"));
                    obj.put("id",detailid);
                    if(is_teaching.equals("1")){
                        obj.put("user_id",s_uid);
                    }
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("课后评价总接口", result);
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


}

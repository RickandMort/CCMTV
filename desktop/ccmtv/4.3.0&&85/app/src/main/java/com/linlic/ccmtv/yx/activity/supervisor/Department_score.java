package com.linlic.ccmtv.yx.activity.supervisor;

import android.content.Context;
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

import static com.linlic.ccmtv.yx.activity.supervisor.Department_evaluation.is_new;
import static com.linlic.ccmtv.yx.activity.supervisor.Department_evaluation.listData;

/**
 * Created by Administrator on 2018/6/20.
 */

public class Department_score extends BaseActivity   {
    private Context context;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.score)
    TextView score;//总得分
    @Bind(R.id.time_text)
    TextView time_text;//提交时间
    @Bind(R.id.lecturer_text)
    TextView lecturer_text;//主讲人
    @Bind(R.id.leave_submit_layout)
    LinearLayout leave_submit_layout;//
    @Bind(R.id.leave_close_text)
    TextView leave_close_text;//
    @Bind(R.id.leave_submit_text)
    TextView leave_submit_text;//

    @Bind(R.id.button_layout)
    LinearLayout button_layout;//

    Evaluation_in_detail_bean evaluation_in_detail_bean = new Evaluation_in_detail_bean();
    private LinearLayout item_evaluation_department_list_top,item_the_evaluation_of_teaching_list_buttom;
    JSONObject result, data;

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
                                JSONArray contentJson = dataJson.getJSONObject("data").getJSONArray("content");
                                //分数
                                evaluation_in_detail_bean.setScore(dataJson.getJSONObject("data").getString("scores").length()>0?dataJson.getJSONObject("data").getInt("scores"):0);
                                //编辑查看状态 0 不可修改 1可修改
                                evaluation_in_detail_bean.setStatus(dataJson.getJSONObject("data").has("is_edit")?dataJson.getJSONObject("data").getInt("is_edit"):0);

//                                evaluation_in_detail_bean.setId(dataJson.getJSONObject("data").getString("s_template_id") );
                                time_text.setText(dataJson.getJSONObject("data").getString("addtime"));
                                lecturer_text.setText(dataJson.getJSONObject("data").getString("ksname") );

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
                                initDatas();
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
                                listData.get(Integer.parseInt(getIntent().getStringExtra("curr_pos"))).setStatus("1");
                                listData.get(Integer.parseInt(getIntent().getStringExtra("curr_pos"))).setScore(evaluation_in_detail_bean.getScore()+"分");
                                is_new = true;
                               finish();
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
        setContentView(R.layout.training_evaluation_in_detail2);
        context = this;
        ButterKnife.bind(this);

        findId();
        this.setActivity_title_name("科室评估");

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
        leave_close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
            }
        });
        leave_submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leave_submit_layout.setVisibility(View.GONE);
                finish();
            }
        });
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
        leave_submit_layout.setVisibility(View.VISIBLE);

    }
    public void setSubmit(View view){


            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.addSupFormData);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("apply_id",getIntent().getStringExtra("apply_id"));
                        obj.put("hospital_kid",getIntent().getStringExtra("hospital_kid"));
                        obj.put("scores",evaluation_in_detail_bean.getScore() );

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
                        obj.put("content",content);
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

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
                    obj.put("act", URLConfig.getSupKsForm);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("apply_id",getIntent().getStringExtra("apply_id"));
                    obj.put("hospital_kid",getIntent().getStringExtra("hospital_kid"));

                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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

package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

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
 * Created by Administrator on 2018/6/20.
 * 学生评价带教打分页面
 */

public class Evaluation_in_detail extends BaseActivity   {
    private Context context;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.score)
    TextView score;//总得分
    @Bind(R.id.gridview)
    MyGridView gridview;//左边介绍内容
    @Bind(R.id.prompt)
    TextView prompt;//提示内容
    @Bind(R.id.comments)
    EditText comments;//评语
    @Bind(R.id.button_layout)
    LinearLayout button_layout;//评语
    @Bind(R.id.tv_createtime)
    TextView tv_createtime;//评语
    private String manage_id,status,dj_name,
            appraise_teacher_id,fid,detailid,pg_uid;
    Evaluation_in_detail_bean evaluation_in_detail_bean = new Evaluation_in_detail_bean();
    private LinearLayout item_evaluation_department_list_top,item_the_evaluation_of_teaching_list_buttom;
    JSONObject result, data;
    private String is_comment = "";
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo,baseListAdapterGridview;
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
                                JSONArray djinfoJson = dataJson.getJSONObject("data").getJSONArray("djinfo");
                                //带教/科室ID
                                manage_id = dataJson.getJSONObject("data").getString("manage_id");
                                //状态
                                status = dataJson.getJSONObject("data").getString("status");
                                //带教姓名
                                dj_name = dataJson.getJSONObject("data").getString("tname");
                                //带教老师评论id
                                appraise_teacher_id = dataJson.getJSONObject("data").getString("appraise_teacher_id");
                                //分数
                                evaluation_in_detail_bean.setScore(dataJson.getJSONObject("data").getString("score").length()>0?dataJson.getJSONObject("data").getInt("score"):0);
                                //评论
                                evaluation_in_detail_bean.setComments(dataJson.getJSONObject("data").getString("comment"));
                                //头部提示语言
                                evaluation_in_detail_bean.setPrompt(dataJson.getJSONObject("data").getString("alert_msg"));
                                //编辑查看状态 0 不可修改 1可修改
                                evaluation_in_detail_bean.setStatus(dataJson.getJSONObject("data").getInt("is_allow_edit"));
                                //是否需要输入评语 1 需要输入 1可不输入
                                is_comment =  dataJson.getJSONObject("data").getString("is_comment");
                                //tv_createtime  打分时间savetime
                                if(dataJson.getJSONObject("data").has("savetime")){
                                    tv_createtime.setText("打分时间："+dataJson.getJSONObject("data").getString("savetime"));
                                }else {
                                    tv_createtime.setText("");
                                }
                        /*        if(dataJson.getJSONObject("data").has("savetime")){

                                }else{

                                }*/
                                evaluation_in_detail_bean.setId(dataJson.getJSONObject("data").getString("detail_id") );
                                pg_uid = dataJson.getJSONObject("data").getString("uid") ;
                                initDatas();

                                for (int i = 0; i<djinfoJson.length();i++){
                                    JSONObject dataJson1 = djinfoJson.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("name",dataJson1.getString("name"));
                                    map.put("value",dataJson1.getString("value"));
                                    listData.add(map);
                                }
                                List<Evaluation_of_item> list = new ArrayList<>();
                                for (int i = 0; i<contentJson.length();i++){
                                    JSONObject dataJson1 = contentJson.getJSONObject(i);
                                    Evaluation_of_item evaluation_of_item = new Evaluation_of_item();
                                    evaluation_of_item.setId(i+"");
                                    evaluation_of_item.setGrade(dataJson1.getInt("score"));
                                    evaluation_of_item.setContent(dataJson1.getString("name"));
                                    evaluation_of_item.setThe_weight(dataJson1.getInt("weight"));
                                    evaluation_of_item.setMaxgrade(dataJson1.getInt("grade"));
                                    List<String> grades =new ArrayList<String>();
                                    for(int k = 1; k <=dataJson1.getInt("grade");k++ ){
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

                    //设置左边内容
                    //设置提示内容
                    //prompt.setText(evaluation_in_detail_bean.getPrompt());
                    //设置评语
                    comments.setText(evaluation_in_detail_bean.getComments());
                    //根据状态 设置是否可以编辑
                    switch (evaluation_in_detail_bean.getStatus()){
                        case 0:
                            comments.setEnabled(false);
                            button_layout.setVisibility(View.GONE);
                            break;
                        case 1:
                            comments.setEnabled(true);
                            button_layout.setVisibility(View.VISIBLE);
                            break;
                        default:
                            comments.setEnabled(false);
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
                                comments.setEnabled(false);
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
        setContentView(R.layout.evaluation_in_detail);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        detailid = getIntent().getStringExtra("detailid");

        findId();
        this.setActivity_title_name("带教评价");
        initDatas();
        getUrlRulest();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
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
        baseListAdapterGridview = new BaseListAdapter(gridview,listData, R.layout.evaluation_in_detail_gridview_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map<String, Object>) item;
                helper.setText(R.id._item_name,map.get("name").toString());
                helper.setText(R.id._item_value,map.get("value").toString());

            }
        };
        gridview.setAdapter(baseListAdapterGridview);
        //取消GridView中Item选中时默认的背景色
        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
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

        if(is_comment.equals("1") && comments.getText().length()<15){
            Toast.makeText(context, "评语不能少于15字！", Toast.LENGTH_SHORT).show();
        }else{
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.addDjscore);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("fid", fid);
                        JSONObject data = new JSONObject();
                        data.put("score",evaluation_in_detail_bean.getScore() );
                        data.put("manage_id",manage_id);
                        data.put("status",status);
                        data.put("uid",pg_uid);
                        data.put("tname",dj_name);
                        data.put("appraise_teacher_id",appraise_teacher_id);
                        data.put("detail_id",evaluation_in_detail_bean.getId());
                        data.put("comment",comments.getText().toString());
                        JSONArray content = new JSONArray();
                        for (int i = 0; i<evaluation_in_detail_bean.getEvaluation_of_items().size();i++){
                            Evaluation_of_item evaluation_of_item = evaluation_in_detail_bean.getEvaluation_of_items().get(i);
                            JSONObject item_json = new JSONObject();
                            item_json.put("name",evaluation_of_item.getContent());
                            item_json.put("weight",evaluation_of_item.getThe_weight());
                            item_json.put("score",evaluation_of_item.getGrade());
                            item_json.put("grade",evaluation_of_item.getMaxgrade());
                            content.put(item_json);
                        }
                        data.put("content",content);
                        obj.put("data",data);
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                        LogUtil.e("月度评价总接口", result);
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

    }



    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.userAppraiseDetail);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid",getIntent().getStringExtra("fid"));
                    obj.put("detailid",detailid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("月度评价总接口", result);
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

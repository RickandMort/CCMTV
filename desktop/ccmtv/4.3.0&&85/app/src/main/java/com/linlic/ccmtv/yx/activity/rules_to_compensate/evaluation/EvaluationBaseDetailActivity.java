package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.KsScoreInfo;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.BaseDetailEntity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
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
import butterknife.OnClick;

import static com.linlic.ccmtv.yx.R.id.score;

/**
 * Created by bentley on 2019/1/18.
 * 评价基地，科室打分页面
 */

public class EvaluationBaseDetailActivity extends BaseActivity {
    @Bind(R.id.tv_submit)
    TextView tvSubmit;
    @Bind(R.id.tv_createtime)
    TextView tvCreatetime;
    private Context context;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(score)
    TextView tvScore;//总得分
    @Bind(R.id.gridview)
    MyGridView gridview;//左边介绍内容
    @Bind(R.id.prompt)
    TextView prompt;//提示内容
    @Bind(R.id.comments)
    EditText comments;//评语
    @Bind(R.id.button_layout)
    LinearLayout button_layout;//评语
    @Bind(R.id.activity_title_name)
    TextView activity_title_name;
    private String fid;
    private List<BaseDetailEntity.ConfigBean> listData = new ArrayList<>();
    private List<KsScoreInfo> ks_listData = new ArrayList<>();
    private BaseListAdapter baseListAdapterVideo;
    private BaseDetailEntity mBaseDetailEntity;
    private String base_id;
    private String detail_id;
    private String master_id;
    private TextView tv_submit;
    private String flag;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.evaluation_in_detail);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        context = this;
        ButterKnife.bind(this);
        flag = getIntent().getStringExtra("flag");
        if (flag.equals("Base")) {
            activity_title_name.setText("基地评价");
            fid = getIntent().getStringExtra("fid");
            master_id = getIntent().getStringExtra("master_id");
            base_id = getIntent().getStringExtra("base_id");
            detail_id = getIntent().getStringExtra("detail_id");
            getUrlRulest();
        } else if (flag.equals("ks")) {
            activity_title_name.setText("评价科室");
            id = getIntent().getStringExtra("id");
            getKsScoreList();
        }
        initViews();

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


    private void initViews() {
        tvCreatetime.setVisibility(View.GONE);
        gridview.setVisibility(View.GONE);
        prompt.setVisibility(View.GONE);
        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_evaluation_in_detail) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                BaseDetailEntity.ConfigBean map = (BaseDetailEntity.ConfigBean) item;
                helper.setText(R.id._item_content, map.getName());
                helper.setText(R.id._item_the_weight, "权重：" + map.getWeight());
                helper.setText(R.id.tv_value, map.getGrade() + "");

                helper.setBaseSpinner(R.id._item_grade, tvScore, listData, map, R.id.tv_value, R.id.id_iv_spinner_arrow);

            }
        };
        listView.setAdapter(baseListAdapterVideo);
    }
    private String is_comment = "";
    private void showDetail(BaseDetailEntity baseDetailEntity) {
        if (!TextUtils.isEmpty(baseDetailEntity.getScore())) {
            tvScore.setText(baseDetailEntity.getScore());
        } else {
            tvScore.setText("100");
        }
        is_comment = baseDetailEntity.getIs_comment();
        String comment = baseDetailEntity.getComment();
        if (comment.equals("null")) {
            comments.setText("");
        } else {
            comments.setText(comment);
        }
        baseListAdapterVideo.notifyDataSetChanged();
    }

    public void setCancel(View view) {
//        new AlertDialog.Builder(this)
//                .setTitle("提示")
//                .setMessage("你确定要取消吗？")
//                .setPositiveButton("是的", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("等一下", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
        finish();

    }

    public void setSubmit() {
        if (!is_comment.equals("")) {
            if(is_comment.equals("1")&&comments.getText().toString().trim().length()<15){
                Toast.makeText(context, "评语不能少于15个字~", Toast.LENGTH_SHORT).show();
                return;
            }else if(is_comment.equals("2")){
            }
          }
            tv_submit.setClickable(false);
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.getUserBaseUpDetail);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("fid", fid);
                        JSONObject data = new JSONObject();
                        JSONArray content = new JSONArray();
                        for (int i = 0; i < listData.size(); i++) {
                            BaseDetailEntity.ConfigBean configBean = listData.get(i);
                            JSONObject item_json = new JSONObject();
                            item_json.put("name", configBean.getName());
                            item_json.put("weight", configBean.getWeight());
                            item_json.put("score", configBean.getScore());
                            item_json.put("grade", configBean.getGrade());
                            content.put(item_json);
                        }

                        data.put("score", tvScore.getText());
                        data.put("base_id", mBaseDetailEntity.getBase_id());
                        data.put("master_id", mBaseDetailEntity.getMaster_id());
                        data.put("createtime", mBaseDetailEntity.getCreatetime());
                        data.put("detail_id", mBaseDetailEntity.getDetail_id());
                        data.put("comment", comments.getText());
                        data.put("config", content);
                        obj.put("data", data.toString());
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

    public void setSubmit_KS() {
        if (!is_comment.equals("")) {
            if(is_comment.equals("1")&&comments.getText().toString().trim().length()<15){
                Toast.makeText(context, "评语不能少于15个字~", Toast.LENGTH_SHORT).show();
                return;
            }else if(is_comment.equals("2")){
            }
        }
            tv_submit.setClickable(false);
            MyProgressBarDialogTools.show(context);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.getksScoreFormPost);
                        obj.put("uid", SharedPreferencesTools.getUid(context));
                        obj.put("id", id);
                        //JSONObject data = new JSONObject();
                        JSONArray content = new JSONArray();
                        for (int i = 0; i < ks_listData.size(); i++) {
                            KsScoreInfo configBean = ks_listData.get(i);
                            JSONObject item_json = new JSONObject();
                            item_json.put("name", configBean.getName());
                            item_json.put("weight", configBean.getWeight());
                            item_json.put("score", configBean.getScore());
                            item_json.put("grade", configBean.getGrade());
                            content.put(item_json);
                        }
                        obj.put("comment", comments.getText().toString());
                        obj.put("score", tvScore.getText().toString());
                        obj.put("content", content.toString());
                        String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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
            new Thread(runnable).start();

    }


    /***
     *
     *基地评价，详情，二：getUserBaseDetail
     */
    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUserBaseDetail);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("fid", fid);
                    obj.put("master_id", master_id);
                    obj.put("base_id", base_id);
                    obj.put("detail_id", detail_id);
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

    /***
     *
     *  评价科室打分
     */
    public void getKsScoreList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getUserScoreForm);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
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
        new Thread(runnable).start();
    }

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
                                JSONObject data = dataJson.getJSONObject("data");
                                BaseDetailEntity baseDetailEntity = new BaseDetailEntity();
                                baseDetailEntity.setIs_comment(data.getString("is_comment"));
                                baseDetailEntity.setCreatetime(data.getString("createtime"));
                                baseDetailEntity.setComment(data.getString("comment"));
                                baseDetailEntity.setScore(data.getString("score"));
                                baseDetailEntity.setBase_id(data.getString("base_id"));
                                baseDetailEntity.setMaster_id(data.getString("master_id"));
                                baseDetailEntity.setDetail_id(data.getString("detail_id"));
                                List<BaseDetailEntity.ConfigBean> configs = baseDetailEntity.getConfig();
                                if (!data.isNull("config")) {
                                    JSONArray configArray = data.getJSONArray("config");
                                    for (int i = 0; i < configArray.length(); i++) {
                                        JSONObject listObject = configArray.getJSONObject(i);
                                        BaseDetailEntity.ConfigBean configBean = new BaseDetailEntity.ConfigBean();
                                        configBean.setName(listObject.getString("name"));
                                        configBean.setWeight(listObject.getString("weight"));
                                        configBean.setGrade(listObject.getString("grade"));
                                        if (!listObject.isNull("score")) {
                                            configBean.setScore(listObject.getString("score"));
                                        }
                                        configs.add(configBean);
                                        listData.add(configBean);
                                    }
                                }
                                mBaseDetailEntity = baseDetailEntity;
                                showDetail(baseDetailEntity);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONObject data = dataJson.getJSONObject("data");
                                BaseDetailEntity baseDetailEntity = new BaseDetailEntity();
                                baseDetailEntity.setIs_comment(data.getString("is_comment"));
                                baseDetailEntity.setComment(data.getString("comment"));
                                baseDetailEntity.setScore(data.getString("score"));
                                if (!data.isNull("content")) {
                                    JSONArray configArray = data.getJSONArray("content");
                                    for (int i = 0; i < configArray.length(); i++) {
                                        JSONObject listObject = configArray.getJSONObject(i);
                                        KsScoreInfo configBean = new KsScoreInfo();
                                        configBean.setName(listObject.getString("name"));
                                        configBean.setWeight(listObject.getString("weight"));
                                        configBean.setGrade(listObject.getString("grade"));
                                        if (!listObject.isNull("score")) {
                                            configBean.setScore(listObject.getString("score"));
                                        }
                                        ks_listData.add(configBean);
                                    }
                                    baseListAdapterVideo = new BaseListAdapter(listView, ks_listData, R.layout.item_evaluation_in_detail) {

                                        @Override
                                        public void refresh(Collection datas) {
                                            super.refresh(datas);
                                        }

                                        @Override
                                        public void convert(ListHolder helper, Object item, boolean isScrolling) {
                                            super.convert(helper, item, isScrolling);
                                            KsScoreInfo map = (KsScoreInfo) item;
                                            helper.setText(R.id._item_content, map.getName());
                                            helper.setText(R.id._item_the_weight, "权重：" + map.getWeight());
                                            helper.setText(R.id.tv_value, map.getGrade() + "");

                                            helper.setKsSpinner(R.id._item_grade, tvScore, ks_listData, map, R.id.tv_value, R.id.id_iv_spinner_arrow);

                                        }
                                    };
                                    listView.setAdapter(baseListAdapterVideo);
                                }
//                            mBaseDetailEntity = baseDetailEntity;
                                showDetail(baseDetailEntity);
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 3:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                tv_submit.setClickable(true);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
//                                baseListAdapterVideo.notifyDataSetChanged();
//                                comments.setEnabled(false);
//                                button_layout.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                tv_submit.setClickable(true);
                            }
//
                        } else {
                            toastShort(jsonObject.getString("errorMessage"));
                            tv_submit.setClickable(true);
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 4:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                tv_submit.setClickable(true);
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                finish();
//                                baseListAdapterVideo.notifyDataSetChanged();
//                                comments.setEnabled(false);
//                                button_layout.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                tv_submit.setClickable(true);
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            tv_submit.setClickable(true);
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

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        if (flag.equals("Base")) {
            if (checkDoubleClick() == false) {
                setSubmit();
            }
        } else if (flag.equals("ks")) {
            if (checkDoubleClick() == false) {
                setSubmit_KS();
            }
        }
    }

    /**
     * 判断是否是快速点击
     */
    private static long lastClickTime;

    public static boolean checkDoubleClick() {
        //点击时间
        long clickTime = SystemClock.uptimeMillis();
        //如果当前点击间隔小于500毫秒
        if (lastClickTime >= clickTime - 500) {
            return true;
        }
        //记录上次点击时间
        lastClickTime = clickTime;
        return false;
    }
}

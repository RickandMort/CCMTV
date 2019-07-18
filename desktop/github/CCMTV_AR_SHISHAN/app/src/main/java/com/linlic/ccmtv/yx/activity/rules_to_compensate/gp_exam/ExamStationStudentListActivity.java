package com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.PeriodicalExamActivity;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExamStationStudentListActivity extends BaseActivity {
    @Bind(R.id.layout_nodata)
    NodataEmptyLayout layoutNodata;
    private Context context;
    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;
    @Bind(R.id.student_recyclerview)
    RecyclerView studentRecyclerview;
    private BaseRecyclerViewAdapter adapter;
    private List<ListInfo> list_data = new ArrayList<>();
    private String fid = "";
    private String id = "";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) { // 成功
                            JSONObject data = result.getJSONObject("data");
                            if (data.getString("status").equals("1")) {
                                JSONArray jsonArray = data.getJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    layoutNodata.setVisibility(View.VISIBLE);
                                    studentRecyclerview.setVisibility(View.GONE);
                                }
                                if (jsonArray.length() != 0) {
                                    layoutNodata.setVisibility(View.GONE);
                                    studentRecyclerview.setVisibility(View.VISIBLE);
                                    list_data = JsonUtils.fromJsonArray(jsonArray.toString(), ListInfo.class);
                                    if (list_data.size() != 0) {
                                        adapter = new BaseRecyclerViewAdapter(R.layout.adapter_exam_station_student_list, list_data) {
                                            @Override
                                            public void convert(BaseViewHolder helper, Object item) {
                                                super.convert(helper, item);
                                                ListInfo data = (ListInfo) item;
                                                helper.setText(R.id.tv_realname, data.getRealname());
                                                helper.setText(R.id.tv_lun_keshi,"所属基地："+data.getBasename());
                                                CircleImageView imageView=helper.getView(R.id.iv_img);
                                                ImageView iv_paint=helper.getView(R.id.iv_paint);
                                                LinearLayout ll_score=helper.getView(R.id.ll_score);
                                                TextView tv_score=helper.getView(R.id.tv_score);
                                                RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.img_default2);
                                                Glide.with(context)
                                                        .load(data.getIDphoto())
                                                        .transition(DrawableTransitionOptions.withCrossFade())
                                                        .apply(options)
                                                        .into(imageView);
                                                String score=data.getHave_score();
                                                if(score.equals("0")){
                                                    iv_paint.setVisibility(View.VISIBLE);
                                                    tv_score.setTextColor(context.getResources().getColor(R.color.white));
                                                    tv_score.setText("打分");
                                                    ll_score.setBackground(context.getResources().getDrawable(R.mipmap.station_nofen));
                                                }else if(score.equals("1")){
                                                    iv_paint.setVisibility(View.GONE);
                                                    tv_score.setTextColor(context.getResources().getColor(R.color.color3897f9));
                                                    tv_score.setText(data.getScore()+"分");
                                                    ll_score.setBackground(context.getResources().getDrawable(R.mipmap.station_fen));
                                                }
                                            }
                                        };
                                        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
                                        adapter.isFirstOnly(false);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                        studentRecyclerview.setLayoutManager(layoutManager);
                                        studentRecyclerview.setAdapter(adapter);
                                        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                                String error_msg=list_data.get(position).getErrormsg();
                                                if(error_msg.equals("")){//error_msg为空
                                                    Intent intent = new Intent(context, PeriodicalExamActivity.class);
                                                    intent.putExtra("fid",fid);
                                                    intent.putExtra("site_detail_id",list_data.get(position).getSite_detail_id());
                                                    intent.putExtra("user_id", list_data.get(position).getUser_id());
                                                    intent.putExtra("score_id",list_data.get(position).getScore_id());
                                                    intent.putExtra("disabled",list_data.get(position).getHave_score());
                                                    startActivity(intent);
                                                }else {
                                                    ToastUtils.makeText(context,error_msg);
                                                }

                                            }
                                        });
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_station_student_list);
        ButterKnife.bind(this);
        context = ExamStationStudentListActivity.this;
        getIntentData();
    }

    private void getIntentData() {
        fid = getIntent().getStringExtra("fid");
        id = getIntent().getStringExtra("id");
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
    }

    private void initdata() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getStageExaminerUserList);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("id", id);
                    obj.put("new", "1");
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("单多站考核学生列表数据：", result);

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


    class ListInfo implements Serializable {

        private String user_id;
        private String realname;
        private String IDphoto;
        private String score_id;
        private String detail_id;
        private String score;
        private String site_detail_id;
        private String errormsg;
        private String have_score;
        private String basename;

        public String getBasename() {
            return basename;
        }

        public void setBasename(String basename) {
            this.basename = basename;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getIDphoto() {
            return IDphoto;
        }

        public void setIDphoto(String IDphoto) {
            this.IDphoto = IDphoto;
        }

        public String getScore_id() {
            return score_id;
        }

        public void setScore_id(String score_id) {
            this.score_id = score_id;
        }

        public String getDetail_id() {
            return detail_id;
        }

        public void setDetail_id(String detail_id) {
            this.detail_id = detail_id;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getSite_detail_id() {
            return site_detail_id;
        }

        public void setSite_detail_id(String site_detail_id) {
            this.site_detail_id = site_detail_id;
        }

        public String getErrormsg() {
            return errormsg;
        }

        public void setErrormsg(String errormsg) {
            this.errormsg = errormsg;
        }

        public String getHave_score() {
            return have_score;
        }

        public void setHave_score(String have_score) {
            this.have_score = have_score;
        }
    }
}

package com.linlic.ccmtv.yx.activity.AppointmentCourse;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.YKInfoEntity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.File_down;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.R.id.courseware;

/**
 * 约课详情
 * Created by bentley on 2019/1/10.
 */

public class YKDetailActivity extends BaseActivity {
    @Bind(R.id.tv_right)
    TextView mTvRight;
    @Bind(R.id.entry_month_nodata)
    NodataEmptyLayout emptyView;
    @Bind(R.id.view_yk_info)
    View mViewYkInfo;
    @Bind(R.id.tv_tittle)
    TextView mTvTittle;
    @Bind(R.id.tv_username)
    TextView mTvUsername;
    @Bind(R.id.tv_createtime)
    TextView mTvCreatetime;
    @Bind(R.id.tv_num)
    TextView mTvNum;
    @Bind(R.id.tv_status)
    TextView mTvStatus;
    @Bind(R.id.view_course)
    View mViewCourse;
    @Bind(R.id.tv_course_info)
    TextView mTvCourseInfo;
    @Bind(R.id.view_course_file)
    View mViewCourseFile;
    @Bind(courseware)
    MyGridView mCourseware;
    @Bind(R.id.tv_course_time)
    TextView mTvCourseTime;
    @Bind(R.id.tv_course_address)
    TextView mTvCourseAddress;
    @Bind(R.id.view_teacher)
    View mViewTeacher;
    @Bind(R.id.tv_course_teacher)
    TextView mTvCourseTeacher;
    @Bind(R.id.close_text)
    TextView close_text;
    @Bind(R.id.submit_text)
    TextView submit_text;
    @Bind(R.id.view_line)
    View view_line;
    @Bind(R.id.submit_layout)
    LinearLayout submit_layout;

    private Context context;
    private List<YKInfoEntity.FileBean> coursewares = new ArrayList<>();//课件
    private BaseListAdapter baseListAdapterCoursewares;
    private String http = "";
    private String id;
    private int sign_up = 1;//1已报名 0未报名 2调研
    private String fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yk_detail_activity);
        context = this;
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        fid = getIntent().getStringExtra("fid");
        initViews();
        getYkInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Yueke.html";
        super.onPause();
    }

    private void initViews(){

        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_layout.setVisibility(View.GONE);
            }
        });
        submit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mTvRight.getText())) {
                    signUp(mTvRight.getTag().toString());
                }
            }
        });
        baseListAdapterCoursewares = new BaseListAdapter(mCourseware, coursewares, R.layout.item_coursewares2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                YKInfoEntity.FileBean map = (YKInfoEntity.FileBean) item;
                helper.setText(R.id._item_text, map.getFile_name());
            }
        };
        mCourseware.setAdapter(baseListAdapterCoursewares);
        mCourseware.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mCourseware.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YKInfoEntity.FileBean courseware_bean = coursewares.get(position);
                ArrayList urls = null;
                Intent intent = null;
                switch (courseware_bean.getFile_name().substring(courseware_bean.getFile_name().lastIndexOf(".") + 1, courseware_bean.getFile_name().length())) {
                    case "png":
                        urls = new ArrayList();
                        urls.add(http + "" + courseware_bean.getUrl());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpg":
                        urls = new ArrayList();
                        urls.add(http + "" + courseware_bean.getUrl());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "jpeg":
                        urls = new ArrayList();
                        urls.add(http + "" + courseware_bean.getUrl());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    case "bmp":
                        urls = new ArrayList();
                        urls.add(http + "" + courseware_bean.getUrl());
                        intent = new Intent(context, PicViewerActivity.class);
                        intent.putExtra("type", "my_case");
                        intent.putExtra("urls_case", urls);
                        intent.putExtra("current_index", 0);
                        startActivity(intent);
                        break;
                    default:
                        Intent intent1 = new Intent(context, File_down.class);
                        intent1.putExtra("file_path", http + "" + courseware_bean.getUrl());
                        intent1.putExtra("file_name", courseware_bean.getUrl().substring(courseware_bean.getUrl().lastIndexOf("/") + 1, courseware_bean.getUrl().length()));
                        startActivity(intent1);
                        break;
                }
            }
        });
    }

    /**
     * 设置空界面
     * @param code
     */
    private void setResultStatus(int code) {
        if (HttpClientUtils.isNetConnectError(context, code)) {
            emptyView.setNetErrorIcon();
        } else {
            emptyView.setLastEmptyIcon();
        }
        view_line.setVisibility(View.GONE);
        mViewYkInfo.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }
    public static float sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }
    /***
     * 展示约课详情信息
     * @param ykInfoEntity
     */
    private void showYKInfo(YKInfoEntity ykInfoEntity){

        String endText = "  "+ykInfoEntity.getStatus_name()+"  ";

        SpannableString textSpanned1 = new SpannableString(ykInfoEntity.getTitle() +endText);
        //为了显示效果在每个标签文字前加两个空格,后面加三个空格(前两个和后两个填充背景,最后一个作标签分割)
//        textSpanned1
//        textSpanned1.insert(0, "  " + goodsTags.get(i).getTags_name() + "   ");
        int start = ykInfoEntity.getTitle().length();
        int end = ykInfoEntity.getTitle().length()+endText.length();
        //稍微设置标签文字小一点
        textSpanned1.setSpan(new RelativeSizeSpan(0.7f), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        textSpanned1.setSpan(new AbsoluteSizeSpan(14), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textSpanned1.setSpan(new StyleSpan(Typeface.BOLD), 0, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置圆角背景
        int fontSizePx1 = (int)sp2px(context, 12);
        Drawable d ;
        ImageSpan span;
        switch (ykInfoEntity.getStatus_name()){
            case "未开始":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#ef8e10"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = context.getResources().getDrawable(R.mipmap.spanned01);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "进行中":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#eeeeee"),Color.parseColor("#ef8e10"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = context.getResources().getDrawable(R.mipmap.spanned02);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "已结束":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#eeeeee"),Color.parseColor("#666666"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = context.getResources().getDrawable(R.mipmap.spanned03);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "开始报名":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#3897f9"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = context.getResources().getDrawable(R.mipmap.spanned04);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case "停止报名":
//                textSpanned1.setSpan(new RoundBackgroundColorSpan(holder.mTittle.getContext(), Color.parseColor("#ef8e10"),Color.parseColor("#ffffff"),fontSizePx1,position), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                d = context.getResources().getDrawable(R.mipmap.spanned05);
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
                textSpanned1.setSpan(span,start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
        }

        mTvTittle.setText(textSpanned1);
        mTvUsername.setText(ykInfoEntity.getUsername()+"发布于");
        mTvCreatetime.setText(ykInfoEntity.getCreatetime());
        mTvNum.setText(Html.fromHtml("报名人数："+"<font color=#FCA962>" + ykInfoEntity.getEnroll_num()  + "</font><font color=#666666>/"+ ykInfoEntity.getMax_num()  + "</font>"));
        mTvStatus.setText("报名范围："+ykInfoEntity.getNotice_name());
        //无课程介绍则不展示
        if (!TextUtils.isEmpty(ykInfoEntity.getContent_desc())) {
            mViewCourse.setVisibility(View.VISIBLE);
            mTvCourseInfo.setText(ykInfoEntity.getContent_desc());

        } else {
            mViewCourse.setVisibility(View.GONE);
        }
        mTvCourseTime.setText(ykInfoEntity.getStart_time() + "~" + ykInfoEntity.getEnd_time());
        mTvCourseAddress.setText(ykInfoEntity.getAddress());
        //无主讲人介绍则不展示
        if (!TextUtils.isEmpty(ykInfoEntity.getTeacher_desc())) {
            mViewTeacher.setVisibility(View.VISIBLE);
            mTvCourseTeacher.setText(ykInfoEntity.getTeacher_desc());

        } else {
            mViewTeacher.setVisibility(View.GONE);
        }
        sign_up = ykInfoEntity.getSign_up();//1已报名 0未报名 2调研
        mTvRight.setTag(ykInfoEntity.getId());
        mTvRight.setText(ykInfoEntity.getSign_up_name());
        if(ykInfoEntity.getSign_up_name().equals("已报名")){
            mTvRight.setTextColor(Color.parseColor("#999999"));
        }else{
            mTvRight.setTextColor(Color.parseColor("#4492da"));
        }
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (sign_up) {
                    case 0:
                        //报名
                      /*  if (!TextUtils.isEmpty(mTvRight.getText())) {
                            signUp(v.getTag().toString());
                        }*/
                      submit_layout.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        //调研
                        Intent intent = new Intent(context, YKSurveyActivity.class);
                        intent.putExtra("id", v.getTag().toString());
                        intent.putExtra("fid", fid);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /***
     * 约课报名
     */
    private void signUp(final String mId){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykSignUp);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", mId);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("约课报名", result);

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

    /***
     * 获取约课详情
     */
    private void getYkInfo(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.ykInfo);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("id", id);
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("约课info", result);

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://刷新
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONObject dateJson = dataJson.getJSONObject("data");
                                YKInfoEntity ykInfoEntity = new YKInfoEntity();
                                ykInfoEntity.setId(dateJson.getString("id"));
                                ykInfoEntity.setTitle(dateJson.getString("title")+"  ");
                                ykInfoEntity.setManage_id(dateJson.getString("manage_id"));
                                ykInfoEntity.setStart_time(dateJson.getString("start_time"));
                                ykInfoEntity.setEnd_time(dateJson.getString("end_time"));
                                ykInfoEntity.setMax_num(dateJson.getString("max_num"));
                                ykInfoEntity.setTeacher_desc(dateJson.getString("teacher_desc"));
                                ykInfoEntity.setContent_desc(dateJson.getString("content_desc"));
                                ykInfoEntity.setAddress(dateJson.getString("address"));
                                ykInfoEntity.setStatus(dateJson.getString("status"));
                                ykInfoEntity.setCreatetime(dateJson.getString("createtime"));
                                ykInfoEntity.setUsername(dateJson.getString("username"));
                                ykInfoEntity.setEnroll_num(dateJson.getString("enroll_num"));
                                ykInfoEntity.setNotice_name(dateJson.getString("notice_name"));
                                ykInfoEntity.setStatus_name(dateJson.getString("status_name"));
                                if (!dateJson.isNull("sign_up")) {
                                    ykInfoEntity.setSign_up(dateJson.getInt("sign_up"));
                                }
                                if (!dateJson.isNull("sign_up_name")) {
                                    ykInfoEntity.setSign_up_name(dateJson.getString("sign_up_name"));
                                }
                                ykInfoEntity.setNum(dateJson.getString("num"));
                                List<YKInfoEntity.FileBean> fileBeanList = ykInfoEntity.getFile();
                                coursewares.clear();
                                if (!dateJson.isNull("file")){
                                    JSONArray file = dateJson.getJSONArray("file");
                                    for (int i = 0; i < file.length(); i++) {
                                        JSONObject dataJson1 = file.getJSONObject(i);
                                        YKInfoEntity.FileBean fileBean = new YKInfoEntity.FileBean();
                                        fileBean.setFile_name(dataJson1.getString("file_name"));
                                        fileBean.setUrl(dataJson1.getString("url"));
                                        fileBeanList.add(fileBean);
                                        coursewares.add(fileBean);
                                    }
                                    if (file.length() > 0) {
                                        mViewCourseFile.setVisibility(View.VISIBLE);
                                        baseListAdapterCoursewares.notifyDataSetChanged();
                                    } else {
                                        mViewCourseFile.setVisibility(View.GONE);
                                    }
                                }
                                showYKInfo(ykInfoEntity);

                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 2://报名
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        submit_layout.setVisibility(View.GONE);
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                Toast.makeText(context, "报名成功", Toast.LENGTH_SHORT).show();
                                MyProgressBarDialogTools.hide();
                                //报名成功，刷新详情
                                getYkInfo();
                            } else {
                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                                MyProgressBarDialogTools.hide();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };
}

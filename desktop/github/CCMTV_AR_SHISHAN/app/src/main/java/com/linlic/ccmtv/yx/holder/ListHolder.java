package com.linlic.ccmtv.yx.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.FromEntity;
import com.linlic.ccmtv.yx.activity.comment360.entity.AppraiseFromEntity;
import com.linlic.ccmtv.yx.activity.conference.databean.ConferenceBean;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.Commodity;
import com.linlic.ccmtv.yx.activity.entity.Condition;
import com.linlic.ccmtv.yx.activity.entity.Direct_broadcast;
import com.linlic.ccmtv.yx.activity.entity.Evaluation_in_detail_bean;
import com.linlic.ccmtv.yx.activity.entity.Evaluation_of_item;
import com.linlic.ccmtv.yx.activity.entity.Examination_paper;
import com.linlic.ccmtv.yx.activity.entity.KsScoreInfo;
import com.linlic.ccmtv.yx.activity.entity.Live_broadcast;
import com.linlic.ccmtv.yx.activity.entity.Option;
import com.linlic.ccmtv.yx.activity.entity.Problem;
import com.linlic.ccmtv.yx.activity.home.Practicing_physician_examination_list;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.special_more;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMallClassification;
import com.linlic.ccmtv.yx.activity.medical_database.Article_details;
import com.linlic.ccmtv.yx.activity.my.MyFollowDetails;
import com.linlic.ccmtv.yx.activity.my.book.Video_book_Five;
import com.linlic.ccmtv.yx.activity.my.learning_task.VideoSignActivity;
import com.linlic.ccmtv.yx.activity.my.medical_examination.ClassOfTextWatcher;
import com.linlic.ccmtv.yx.activity.my.medical_examination.ClassOfTextWatcher2;
import com.linlic.ccmtv.yx.activity.my.medical_examination.ClassOfTextWatcher5;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerAdapter;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.SpinerPopWindow;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.BaseDetailEntity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.Daily_exam_of_item;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.javabean.GraduateExamQuestion;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamDetailActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam.StuPeriodicalExamScoreActivity;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.adapter.CommodityGridAdapter;
import com.linlic.ccmtv.yx.adapter.Common_answer_questionGridAdapter;
import com.linlic.ccmtv.yx.adapter.Common_answer_questionGridAdapter2;
import com.linlic.ccmtv.yx.adapter.Common_answer_questionGridAdapter3;
import com.linlic.ccmtv.yx.adapter.Direct_broadcast_list_itemAdapter;
import com.linlic.ccmtv.yx.adapter.Formal_examinationMyGridAdapter;
import com.linlic.ccmtv.yx.adapter.OptionAdapter;
import com.linlic.ccmtv.yx.adapter.OptionAdapter2;
import com.linlic.ccmtv.yx.adapter.Problem_GridAdapter;
import com.linlic.ccmtv.yx.adapter.Problem_GridAdapter2;
import com.linlic.ccmtv.yx.adapter.Public_case_analysis_problem;
import com.linlic.ccmtv.yx.adapter.Public_case_analysis_problem2;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.CalendarReminderUtils;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.CustomizedProgressBar;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.MyRecyclerView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.linlic.ccmtv.yx.utils.fill_in_the_blanks.FillBlankView;
import com.linlic.ccmtv.yx.utils.permission.PermissionCheckLinstenterImpl;
import com.linlic.ccmtv.yx.utils.permission.Permissionutils;
import com.linlic.ccmtv.yx.widget.AutoLinefeedLayout;
import com.linlic.ccmtv.yx.widget.CircleImageViewRGB565;
import com.linlic.ccmtv.yx.widget.LeanTextView;
import com.linlic.ccmtv.yx.widget.RoundProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.wolfspider.autowraplinelayout.AutoWrapLineLayout;

/**
 * Created by Administrator on 2016/4/22.
 */
public class ListHolder {
    private final SparseArray<View> mViews;
    private final View mConvertView;
    RecyclerView.LayoutManager mLayoutManager;

    private ListHolder(ViewGroup parent, int layoutId) {
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(
                layoutId, parent, false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到全部View
     *
     * @return
     */
    public SparseArray<View> getAllView() {
        return mViews;
    }

    /**
     * 拿到一个ViewHolder对象
     */
    public static ListHolder get(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new ListHolder(parent, layoutId);
        } else {
            return (ListHolder) convertView.getTag();
        }
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ListHolder setText(int viewId, CharSequence text) {
        TextView view = getView(viewId);

        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串 字体颜色
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setTextColor(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(view.getResources().getColor(color));
        return this;
    }

    /**
     * 为TextView设置字符串 字体颜色
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setTextColor2(int viewId, int color) {
        TextView view = getView(viewId);
        view.setTextColor(color);
        return this;
    }

    /**
     * 为TextView设置字符串 字体颜色
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setTextselect(int viewId, boolean bool) {
        TextView view = getView(viewId);
        view.setSelected(bool);
        return this;
    }

    /**
     * 为ProgressBar设置进度
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setProgress(int viewId, int progress) {
        ProgressBar pb = getView(viewId);
        pb.setProgress(progress);
        return this;
    }

    /**
     * 为RoundProgressBar设置进度
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setRoundProgress(int viewId, int progress, String text) {
        RoundProgressBar pb = getView(viewId);
        pb.setMax(100);
        pb.setValue(progress);
        pb.setmText(text);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ListHolder setText(int viewId, CharSequence text, String type) {
        if (type.equals("html")) {
            TextView view = getView(viewId);
            view.setText(Html.fromHtml(text.toString()));
        } else {
            TextView view = getView(viewId);
            view.setText(text);
        }

        return this;
    }

    /**
     * 删除view
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder deleteview(int viewId, int delId1, int delId2) {
        LinearLayout view = getView(viewId);
        view.removeView(getView(delId1));
        view.removeView(getView(delId2));
        return this;
    }

    /**
     * 删除view
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder deleteview(int viewId, int delId1) {
        AutoLinefeedLayout view = getView(viewId);
        view.removeView(getView(delId1));
        return this;
    }

    /**
     * 改变监听
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFocus(int viewId, Examination_paper examination_paper, int layout) {
        EditText view = getView(viewId);
        LinearLayout linearLayout = getView(layout);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        ClassOfTextWatcher classofTextWatcher = new ClassOfTextWatcher(view, examination_paper, linearLayout);
        view.addTextChangedListener(classofTextWatcher);
        view.setTag(classofTextWatcher);
        return this;
    }

    /**
     * 监听失去光标
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setOnFocusChangeListener(int viewId, final Examination_paper examination_paper, final String qid) {
        final EditText view = getView(viewId);
        view.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    submit_server(qid, view.getText().toString().trim(), examination_paper, view.getContext());
                }
            }
        });
        return this;
    }

    public void submit_server(final String qid, final String answer, final Examination_paper examination_paper, final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.disasterSave);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("qid", qid);
                    obj.put("redis_key", examination_paper.getRedis_key());
                    obj.put("answer", answer);
                    obj.put("remain_time", examination_paper.getSs() * 60 + examination_paper.getMinss());

                    LogUtil.e("用户作答-提交服务器上行", obj.toString());
                    //测试暂时封掉
                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
                    LogUtil.e("用户作答-提交服务器下行", result);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 改变监听
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFocus5(int viewId, Examination_paper examination_paper, int layout) {
        EditText view = getView(viewId);
        LinearLayout linearLayout = getView(layout);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        ClassOfTextWatcher5 classofTextWatcher = new ClassOfTextWatcher5(view, examination_paper, linearLayout);
        view.addTextChangedListener(classofTextWatcher);
        view.setTag(classofTextWatcher);
        return this;
    }

    /**
     * 清除监听
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFocus3(int viewId) {
        EditText view = getView(viewId);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }

        return this;
    }

    /**
     * 改变监听
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFocus2(int viewId, Examination_paper examination_paper, int layout) {
        EditText view = getView(viewId);
        AutoLinefeedLayout linearLayout = getView(layout);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        ClassOfTextWatcher2 classofTextWatcher = new ClassOfTextWatcher2(view, examination_paper, linearLayout);
        view.addTextChangedListener(classofTextWatcher);
        view.setTag(classofTextWatcher);
        return this;
    }


    /**
     * 为 View设置Tag
     *
     * @param viewId
     * @param text
     * @return
     */
    public ListHolder setTag(int viewId, CharSequence text) {
        View view = getView(viewId);
        view.setTag(text);
        return this;
    }

    /**
     * 为 View设置Tag
     *
     * @param viewId
     * @return
     */
    public ListHolder setClickable(int viewId, boolean bool) {
        View view = getView(viewId);
        view.setClickable(bool);
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @param text
     * @return
     */
    public ListHolder setSpecialOnClick(int viewId, final CharSequence text) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), special_more.class);
                intent.putExtra("fid", v.getTag().toString());
                intent.putExtra("special_more", text);
                view.getContext().startActivity(intent);
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入执考内页
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setPracticingOnClick(int viewId, final String practicing_itme_tilte, final String practicing_itme__img, final String practicing_itme__fuid) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), Practicing_physician_examination_list.class);
                intent.putExtra("practicing_itme_tilte", practicing_itme_tilte);
                intent.putExtra("practicing_itme__img", practicing_itme__img);
                intent.putExtra("practicing_itme__fuid", practicing_itme__fuid);
                view.getContext().startActivity(intent);
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setCommentAdapterDelOnClick(int viewId, final VideoFive video_menu_comment, String aid) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                // TODO Auto-generated method stub

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delNewVc);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", VideoFive.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            System.out.println("视频评论删除:" + obj + "|" + data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功

                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment2();

                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setCommentAdapterDelOnClick2(int viewId, final VideoFive video_menu_comment) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                // TODO Auto-generated method stub

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delNewVc);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", VideoFive.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            System.out.println("视频评论删除:" + obj + "|" + data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功

                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment2();

                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }



    /**
     * 为 View设置添加点击事件 删除评论
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setCommentAdapterDelOnClick3(int viewId, final VideoFive video_menu_comment) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delFatherAndChilds);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", VideoFive.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            LogUtil.e("mason","---------删除评论"+ data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功
                                            //删除后刷新评论列表
                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment();
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 文章详情删除评论
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setArticleCommentAdapterDelOnClick(int viewId, final Article_details video_menu_comment) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delFatherAndChilds);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", Article_details.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            LogUtil.e("mason","---------删除评论"+ data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功
                                            //删除后刷新评论列表
                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment();
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setCommentAdapterDelOnClick3(int viewId, final Video_book_Five video_menu_comment) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delFatherAndChilds);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", Video_book_Five.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            LogUtil.e("mason","---------删除评论"+ data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功
                                            //删除后刷新评论列表
                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment();
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }

    public ListHolder setCommentAdapterDelOnClick3(int viewId, final VideoSignActivity video_menu_comment) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.delFatherAndChilds);
                            obj.put("uid", SharedPreferencesTools.getUid(view.getContext()));
                            obj.put("id", view.getTag());
                            obj.put("aid", VideoSignActivity.aid);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, obj.toString());
                            LogUtil.e("mason","---------删除评论"+ data);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject result = new JSONObject(data);
                                        if (result.getInt("status") == 1) {// 成功
                                            //删除后刷新评论列表
                                            video_menu_comment.currPage = 1;
                                            video_menu_comment.setcomment();
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(view.getContext(),
                                                "网络不稳定，请稍后再试！",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setVideoOnClick(int viewId) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = SharedPreferencesTools.getUidToLoginClose(view.getContext());
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(view.getContext(), VideoFive.class);
                intent.putExtra("aid", view.getTag().toString());
                view.getContext().startActivity(intent);
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder findCalendarReminder(int viewId, String time, String title, String aid, int viewId2, int viewId3) {
        TextView view = getView(viewId);
        ImageView view2 = getView(viewId2);
        LinearLayout view3 = getView(viewId3);
//        Log.e("调用日程", "新增日程");
        String[] times = time.split("至");
        Date date = DateUtil.strToDate(times[0], "yyyy-MM-dd hh:mm");
        long startMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date);
//        Log.e("调用日程", startTime.get(Calendar.YEAR) + " " + startTime.get(Calendar.MONTH) + " " + startTime.get(Calendar.DAY_OF_MONTH) + " " + startTime.get(Calendar.HOUR_OF_DAY) + " " + startTime.get(Calendar.MINUTE));
        beginTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), 0);  //注意，月份的下标是从0开始的
        startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
        //判断是否已经新增到本地日程
        if (MyDbUtils.findCalendarReminder(view.getContext(), aid) == null && 1 == 1) {
            //没有该日程
            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu7));
            view.setText("  加入日程");
            view2.setVisibility(View.VISIBLE);
        } else {
            //有该日程
            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu27));
            view.setText(" 已加入提醒 ");
            view2.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder findCalendarReminder2(int viewId, String time, String title, String aid, int viewId2, int viewId3) {
        TextView view = getView(viewId);
        ImageView view2 = getView(viewId2);
        LinearLayout view3 = getView(viewId3);
//        Log.e("调用日程", "新增日程");
        String[] times = time.split("至");
        Date date = DateUtil.strToDate(times[0], "yyyy-MM-dd hh:mm");
        long startMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(date);
//        Log.e("调用日程", startTime.get(Calendar.YEAR) + " " + startTime.get(Calendar.MONTH) + " " + startTime.get(Calendar.DAY_OF_MONTH) + " " + startTime.get(Calendar.HOUR_OF_DAY) + " " + startTime.get(Calendar.MINUTE));
        beginTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), 0);  //注意，月份的下标是从0开始的
        startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
        //判断是否已经新增到本地日程
        if (MyDbUtils.findCalendarReminder(view.getContext(), aid) == null && 1 == 1) {
            //没有该日程
            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu36));
            view.setText("  加入日程");
            view2.setVisibility(View.VISIBLE);
        } else {
            //有该日程
            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu37));
            view.setText(" 已加入提醒 ");
            view.setTextColor(Color.parseColor("#ffffff"));
            view2.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @param onPermissionCheckListener
     * @return
     */
    public ListHolder setAdd_scheduleOnClick(int viewId, final String time, final String title, final String aid, final String content, int viewId2, int viewId3, final Permissionutils.OnPermissionCheckListener onPermissionCheckListener) {
        final TextView view = getView(viewId);
        final View view2 = getView(viewId2);
        final View view3 = getView(viewId3);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"2019-02-09 16:14至2021-02-09 16:14"
//                Log.e("调用日程", "新增日程");
                String[] times = time.split("至");
                Date date = DateUtil.strToDate(times[0], "yyyy-MM-dd hh:mm");
                long startMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                Calendar startTime = Calendar.getInstance();
                startTime.setTime(date);
//                Log.e("调用日程", startTime.get(Calendar.YEAR) + " " + startTime.get(Calendar.MONTH) + " " + startTime.get(Calendar.DAY_OF_MONTH) + " " + startTime.get(Calendar.HOUR_OF_DAY) + " " + startTime.get(Calendar.MINUTE));
                beginTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), 0);  //注意，月份的下标是从0开始的
                startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
                //判断是否已经新增到本地日程
                if (MyDbUtils.findCalendarReminder(v.getContext(), aid) == null && 1 == 1) {
                    //新增本地日程
                    Uri uri = CalendarReminderUtils.addCalendarEvent(v.getContext(), title, content, startMillis, 1);
                    if (uri == null) {
                        if (onPermissionCheckListener != null) {
                            onPermissionCheckListener.onCheckFail();
                        }
//                        Permissionutils.showDialogPrompt(v.getContext(), "日历加载错误，请确认在权限中设置了日历权限!");
                    } else {
                        Cursor eventCursor = v.getContext().getContentResolver().query(uri, null, null, null, null);
                        eventCursor.moveToFirst();
                        if (eventCursor != null && 1 == 1) {
                            //新增成功 记录到数据库中
                            eventCursor.moveToFirst();
                   /*     CalendarReminder calendarReminder = new CalendarReminder();
                        calendarReminder.setAid(aid);
                        calendarReminder.setUri(uri.toString());
                        calendarReminder.setCalendar_id(eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID))+"");*/
                            MyDbUtils.saveCalendarReminder(v.getContext(), aid, eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) + "", uri.toString());
//                        Log.e("新增日程后查询", "  eventTitle:" + eventCursor.getString(eventCursor.getColumnIndex("title")) + " id:" + eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)));
                            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu27));
                            view.setText(" 已加入日程 ");
                            view2.setVisibility(View.GONE);
                        }
                    }
                }
           /*     CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的考试》即将开始");
                CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的随机考试》即将开始");
                CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的乱序固定考试》即将开始");*/
                CalendarReminderUtils.findCalendarEventAll(v.getContext(), title);
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setAdd_scheduleOnClick2(int viewId, final String time, final String title, final String aid, final String content, int viewId2, int viewId3, final PermissionCheckLinstenterImpl onPermissionCheckListener) {
        final TextView view = getView(viewId);
        final View view2 = getView(viewId2);
        final View view3 = getView(viewId3);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"2019-02-09 16:14至2021-02-09 16:14"
//                Log.e("调用日程", "新增日程");
                String[] times = time.split("至");
                Date date = DateUtil.strToDate(times[0], "yyyy-MM-dd hh:mm");
                long startMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                Calendar startTime = Calendar.getInstance();
                startTime.setTime(date);
//                Log.e("调用日程", startTime.get(Calendar.YEAR) + " " + startTime.get(Calendar.MONTH) + " " + startTime.get(Calendar.DAY_OF_MONTH) + " " + startTime.get(Calendar.HOUR_OF_DAY) + " " + startTime.get(Calendar.MINUTE));
                beginTime.set(startTime.get(Calendar.YEAR), startTime.get(Calendar.MONTH), startTime.get(Calendar.DAY_OF_MONTH), startTime.get(Calendar.HOUR_OF_DAY), startTime.get(Calendar.MINUTE), 0);  //注意，月份的下标是从0开始的
                startMillis = beginTime.getTimeInMillis();  //插入日历时要取毫秒计时
                //判断是否已经新增到本地日程
                if (MyDbUtils.findCalendarReminder(v.getContext(), aid) == null && 1 == 1) {
                    //新增本地日程
                    Uri uri = CalendarReminderUtils.addCalendarEvent(v.getContext(), title, content, startMillis, 1);
                    if (uri == null) {
                        if (onPermissionCheckListener != null) {
                            onPermissionCheckListener.onCheckFail();
                        }
                    } else {
                        Cursor eventCursor = v.getContext().getContentResolver().query(uri, null, null, null, null);
                        eventCursor.moveToFirst();
                        if (eventCursor != null && 1 == 1) {
                            //新增成功 记录到数据库中
                            eventCursor.moveToFirst();
                   /*     CalendarReminder calendarReminder = new CalendarReminder();
                        calendarReminder.setAid(aid);
                        calendarReminder.setUri(uri.toString());
                        calendarReminder.setCalendar_id(eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID))+"");*/
                            MyDbUtils.saveCalendarReminder(v.getContext(), aid, eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)) + "", uri.toString());
//                        Log.e("新增日程后查询", "  eventTitle:" + eventCursor.getString(eventCursor.getColumnIndex("title")) + " id:" + eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID)));
                            view3.setBackground(view.getContext().getResources().getDrawable(R.drawable.anniu37));
                            view.setText(" 已加入提醒 ");
                            view.setTextColor(Color.parseColor("#ffffff"));
                            view2.setVisibility(View.GONE);
                        }
                    }
                }
           /*     CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的考试》即将开始");
                CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的随机考试》即将开始");
                CalendarReminderUtils.deleteCalendarEvent(v.getContext(),"您有一门《未开始的乱序固定考试》即将开始");*/
                CalendarReminderUtils.findCalendarEventAll(v.getContext(), title);
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setDirect_broadcastOnClick(int viewId, int list, final BaseListAdapter baseListAdapter, final Direct_broadcast direct_broadcast) {
        final ImageView view = getView(viewId);
        final MyRecyclerView view2 = getView(list);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().toString().equals("1")) {
//                    view.setImageResource( R.mipmap.direct_broadcast_icon06);
                    v.setBackground(view.getResources().getDrawable(R.mipmap.direct_broadcast_icon06));
                    v.setTag("2");
                    direct_broadcast.setDirect_broadcast_item_icon("2");
//                    view2.setVisibility(View.GONE);
                    baseListAdapter.notifyDataSetChanged();
                } else {
//                    view.setImageResource( R.mipmap.direct_broadcast_icon07);
                    v.setBackground(view.getResources().getDrawable(R.mipmap.direct_broadcast_icon07));
                    v.setTag("1");
                    direct_broadcast.setDirect_broadcast_item_icon("1");
//                    view2.setVisibility(View.VISIBLE);
                    baseListAdapter.notifyDataSetChanged();
                }
            }
        });
        return this;
    }

    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setVideoOnClick2(final int viewId, final String aid, final Context context) {
        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(context, VideoFive.class);
                intent.putExtra("aid", aid);
                context.startActivity(intent);
            }
        });
        return this;
    }


    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setVideoOnClick4(int viewId, final String tag, final Context context) {

        final View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                Intent intent = new Intent(context, VideoFive.class);
                intent.putExtra("aid", tag);
                context.startActivity(intent);
            }
        });
        return this;
    }


    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setVideoCommImgOnClick(int viewId, int view_username) {
        View view = getView(viewId);
        final TextView Str_username = getView(view_username);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MyFollowDetails.class);
                intent.putExtra("Hisuid", v.getTag().toString());
                intent.putExtra("Str_username", Str_username.getText());
                v.getContext().startActivity(intent);

            }
        });
        return this;
    }

    /**
     * 为 View设置 隐藏
     *
     * @param viewId
     * @param visivility
     * @return
     */
    public ListHolder setVisibility(int viewId, int visivility) {
        View view = getView(viewId);
        view.setVisibility(visivility);
        return this;
    }

    /**
     * 为 View设置 隐藏
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setprogressbar(int viewId, int curr) {
        CustomizedProgressBar view = getView(viewId);
        view.setCurrentCount(curr);
        return this;
    }

    /**
     * 为 View设置 隐藏
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setcommreply(int viewId, final String video_menu_comment_item_name, final String cid) {
        View view = getView(viewId);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {
                // TODO Auto-generated method stub

                EditText editText2 = (EditText) ((Activity) view.getContext()).findViewById(R.id.video_menu_comment_edittext_f);
                LinearLayout video_comment_dilong = (LinearLayout) ((Activity) view.getContext()).findViewById(R.id.video_comment_list);


                editText2.setHint("回复 " + video_menu_comment_item_name + " :");
                editText2.setTag(cid);


                Log.i("jiaodian", "jiaodian1");
                editText2.setFocusable(true);
                editText2.requestFocus();
                //打开软键盘
                InputMethodManager imm = (InputMethodManager) editText2.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);


            }
        });

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public ListHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ListHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /**
     * 为ImageView设置背景图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ListHolder setBackground_Image(int viewId, int bm) {
        View view = getView(viewId);
        view.setBackground(view.getResources().getDrawable(bm));
        return this;
    }

    /**
     * 为ImageView设置背景图片
     *
     * @param viewId
     * @param bm
     * @return
     */
    public ListHolder setBackground_color(int viewId, int bm) {
        View view = getView(viewId);
        view.setBackgroundColor(bm);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImageBitmap(int viewId, String url) {
        ImageView view = getView(viewId);
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(view.getContext());
        xUtilsImageLoader.display(view, FirstLetter.getSpells(url));
        return this;
    }

    public ListHolder setImageBitmap2(int viewId, String url) {
        ImageView view = getView(viewId);
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(view.getContext());
        xUtilsImageLoader.display(view, url);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImageBitmapGlide(Context context, int viewId, String url) {
        ImageView view = getView(viewId);

        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Glide.with(context)
                .load(FirstLetter.getSpells(url))
                .apply(options)
                .into(view);

        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImageBitmapGlide(Context context, int viewId, String url, int width, int height) {
        ImageView view = getView(viewId);
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default)
                .override(width, height);
        Glide.with(context)
                .load(FirstLetter.getSpells(url))
                .apply(options)
                .into(view);

        return this;
    }

    /**
     * 为CircleImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setCircleImageViewBitmapGlide2(Context context, int viewId, String url) {
        CircleImageView view = getView(viewId);
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.img_default);
        Glide.with(context)
                .load(FirstLetter.getSpells(url))
//                .placeholder(R.mipmap.img_default)
                //.transform(new GlideCircleTransformScale(context))
                //.error(R.mipmap.icon_error)
                .apply(options)
                .into(view);
        return this;
    }

    /**
     * 为CircleImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setCircleImageViewBitmapGlide(Context context, int viewId, String url) {
        CircleImageViewRGB565 view = getView(viewId);
        RequestOptions options = new RequestOptions();
        options.error(R.mipmap.img_default).override(80, 80);
        Glide.with(context)
                .load(FirstLetter.getSpells(url))
                .apply(options)
                .into(view);
        return this;
    }

    public ListHolder setImageBitmaps(int viewId, String url) {
        ImageView view = getView(viewId);
        // ImageLoader.getInstance().loadImageSync(url),view);
        ImageLoader.getInstance().displayImage(FirstLetter.getSpells(url), view);
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImageBitmap(int viewId, String url, String aid) {
        ImageView view = getView(viewId);
        if (!aid.equals(view.getTag())) {
            XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(view.getContext());
            xUtilsImageLoader.display(view, FirstLetter.getSpells(url));
        }
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImage(int viewId, int url) {
        ImageView view = getView(viewId);
        view.setImageResource(url);
        return this;
    }
    /**
     * 为  GridView 设置 setAdapter
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setAdapter(int viewId, final List<Condition> item_conditions  ) {
        GridView view = getView(viewId);
        final BaseListAdapter  baseListAdapter_item = new BaseListAdapter(view, item_conditions, R.layout.item_our_resources2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;
                helper.setText(R.id._item_all_election, map.getTitle());
                if(map.is_select()){
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#3897F9"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu18);
                }else{
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#333333"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu33);
                }
            }
        };
        view.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        view.setAdapter(baseListAdapter_item);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(item_conditions.get(i).is_select()){
                    item_conditions.get(i).setIs_select(false);
                }else{
                    item_conditions.get(i).setIs_select(true);
                }
                baseListAdapter_item.notifyDataSetChanged();
            }
        });
        return this;
    }
    /**
     * 教学活动 主讲人 选择条件
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setAdapter2(int viewId, final Condition item_conditions,  final BaseListAdapter adapter   ) {
        GridView view = getView(viewId);
        final BaseListAdapter  baseListAdapter_item = new BaseListAdapter(view, item_conditions.getChilds(), R.layout.item_our_resources2) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Condition map = (Condition) item;
                helper.setText(R.id._item_all_election, map.getTitle());
                if(map.is_select()){
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#3897F9"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu18);
                }else{
                    helper.setTextColor2(R.id._item_all_election,Color.parseColor("#333333"));
                    helper.setBackground_Image(R.id._item_all_election,R.drawable.anniu33);
                }
            }
        };
        view.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        view.setAdapter(baseListAdapter_item);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (Condition condition:  item_conditions.getChilds()) {
                    condition.setIs_select(false);
                }
                item_conditions.setSelect_name(item_conditions.getChilds().get(i).getTitle());
                item_conditions.getChilds().get(i).setIs_select(true);
                baseListAdapter_item.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });
        return this;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param url
     * @return
     */
    public ListHolder setImageURL(int viewId, String url) {
        ImageView view = getView(viewId);
        //设置用户头像
        new Carousel_figure(view.getContext()).loadImageNoCache(url, view);  //无缓存
        return this;
    }

    /**
     * name：他的关注---给加关注---取消关注增加点击事件
     * author：Larry
     * data：2016/7/11 13:52
     */
    public ListHolder setFollow(final String hisuid, int id) {
        final TextView view = getView(id);
        final String uid = SharedPreferencesTools.getUid(view.getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", uid);
                            object.put("auid", hisuid);
                            object.put("act", URLConfig.attention);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, object.toString());
                            final JSONObject result = new JSONObject(data);
                            System.out.println("resultresult" + result);
                            if (result.getInt("status") == 1) {// 成功

                                ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if ("1".equals(result.getString("data"))) {
                                                view.setText("已关注");
                                            } else {
                                                view.setText("+关注");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            } else {// 失败
                                Toast.makeText(view.getContext(),
                                        result.getString("errorMessage"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });


        return this;
    }

    /**
     * 取TextView 值
     *
     * @param viewId
     * @return
     */
    public String getStr(int viewId) {
        TextView view = getView(viewId);

        return view.getText().toString() + "";
    }

    /**
     * 将控制设置显示 隐藏 占位隐藏
     *
     * @param viewId
     * @return
     */
    public void setViewVisibility(int viewId, int visibility) {
        getView(viewId).setVisibility(visibility);
    }

    /**
     * 为 视频评论加载全部的点击事件
     *
     * @param
     * @return
     */
    public ListHolder addview(TextView textView, int list) {
        LinearLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.addView(textView);

        return this;
    }

    /**
     * 为 视频评论加载全部的点击事件
     *
     * @param
     * @return
     */
    public ListHolder addview3(TextView textView, int list) {
        AutoWrapLineLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.addView(textView);

        return this;
    }

    /**
     * 为 视频评论加载全部的点击事件
     *
     * @param
     * @return
     */
    public ListHolder removeViews(int list) {
        AutoWrapLineLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.removeAllViews();

        return this;
    }
    /**
     * 为 视频评论加载全部的点击事件
     *
     * @param
     * @return
     */
    public ListHolder removeViews2(int list) {
        LinearLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.removeAllViews();

        return this;
    }

    /**
     * 动态增加view
     *
     * @param
     * @return
     */
    public ListHolder addview2(int list, View view) {
        LinearLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.addView(view);

        return this;
    }

    /**
     * 设置数据库不可点击
     *
     * @param
     * @return
     */
    public ListHolder setEnabled(int list, boolean bool) {
        View view = getView(list);
        view.setFocusableInTouchMode(bool);
        view.setFocusable(bool);
        view.requestFocus();

        return this;
    }

    /**
     * 动态增加view
     *
     * @param
     * @return
     */
    public ListHolder addview3(int list, String[] datas) {
        LinearLayout linearLayout;
        linearLayout = getView(list);


        //        每一行的布局，初始化第一行布局
        LinearLayout rowLL = new LinearLayout(linearLayout.getContext());
        LinearLayout.LayoutParams rowLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        float rowMargin = dipToPx(10, linearLayout);
        rowLP.setMargins(0, (int) rowMargin, 0, 0);
        rowLL.setLayoutParams(rowLP);
        boolean isNewLayout = false;
        float maxWidth = getScreenWidth(linearLayout) - dipToPx(30, linearLayout);
//        剩下的宽度
        float elseWidth = maxWidth;
        LinearLayout.LayoutParams textViewLP =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLP.setMargins((int) dipToPx(8, linearLayout), 0, 0, 0);
        for (int i = 0; i < datas.length; i++) {
//            若当前为新起的一行，先添加旧的那行
//            然后重新创建布局对象，设置参数，将isNewLayout判断重置为false
            if (isNewLayout) {
                linearLayout.addView(rowLL);
                rowLL = new LinearLayout(linearLayout.getContext());
                rowLL.setLayoutParams(rowLP);
                isNewLayout = false;
            }
            final TextView textView = new TextView(linearLayout.getContext());
            textView.setId(i);
//            计算是否需要换行
            final RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp1);
            textView.setText(datas[i]);
            textView.measure(0, 0);
//            若是一整行都放不下这个文本框，添加旧的那行，新起一行添加这个文本框
            if (maxWidth < textView.getMeasuredWidth()) {
                linearLayout.addView(rowLL);
                rowLL = new LinearLayout(linearLayout.getContext());
                rowLL.setLayoutParams(rowLP);
                rowLL.addView(textView);
                isNewLayout = true;
                continue;
            }
//            若是剩下的宽度小于文本框的宽度（放不下了）
//            添加旧的那行，新起一行，但是i要-1，因为当前的文本框还未添加
            if (elseWidth < textView.getMeasuredWidth()) {
                isNewLayout = true;
                i--;
//                重置剩余宽度
                elseWidth = maxWidth;
                continue;
            } else {
//                剩余宽度减去文本框的宽度+间隔=新的剩余宽度
                elseWidth -= textView.getMeasuredWidth() + dipToPx(8, linearLayout);
                if (rowLL.getChildCount() == 0) {
                    rowLL.addView(textView);
                } else {
                    textView.setLayoutParams(textViewLP);
                    rowLL.addView(textView);
                }
            }
        }
//        添加最后一行，但要防止重复添加
        linearLayout.removeView(rowLL);
        linearLayout.addView(rowLL);

        return this;
    }

    //    dp转px
    private float dipToPx(int dipValue, View view) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue,
                view.getContext().getResources().getDisplayMetrics());
    }

    //  获得评论宽度
    private float getScreenWidth(View view) {
        return view.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 删除容器内所有的子view
     *
     * @param
     * @return
     */
    public ListHolder delViews(int list) {
        LinearLayout linearLayout;
        linearLayout = getView(list);
        linearLayout.removeAllViews();

        return this;
    }

    /**
     * 获得容器内所有的子view数
     *
     * @param
     * @return
     */
    public int getViewsCount(int list) {
        LinearLayout linearLayout;
        linearLayout = getView(list);
        return linearLayout.getChildCount();
    }

    /**
     * 获得容器内所有的子view
     *
     * @param
     * @return
     */
    public View getView(int list, int i) {
        LinearLayout linearLayout;
        linearLayout = getView(list);

        return linearLayout.getChildAt(i);
    }

    /**
     * 为View设置背景图片
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setViewBG(int viewId, Drawable id) {
        View view = getView(viewId);
        view.setBackground(id);
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setMyGridView(int viewId, final List<String> thumbnail, List<String> imgs) {
        MyGridView view = getView(viewId);
        Formal_examinationMyGridAdapter myGridAdapter = new Formal_examinationMyGridAdapter(view.getContext(), thumbnail, imgs);
        view.setAdapter(myGridAdapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                ImageView textView = (ImageView) view.findViewById(R.id.tv_item);
                Intent intent = new Intent(view.getContext(), PicViewerActivity.class);
                ArrayList urls_teshujiancha = new ArrayList();
                urls_teshujiancha.add(textView.getTag().toString());
                intent.putExtra("urls_case", urls_teshujiancha);
                intent.putExtra("type", "my_case");
                intent.putExtra("current_index", 0);
                view.getContext().startActivity(intent);

            }
        });
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setProblemAdapter(int viewId, List<Problem> problems, int position) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        Problem_GridAdapter myGridAdapter = new Problem_GridAdapter(view.getContext(), problems, position);
        view.setAdapter(myGridAdapter);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setDirect_broadcastAdapter(int viewId, List<Live_broadcast> live_broadcasts) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        Direct_broadcast_list_itemAdapter myGridAdapter = new Direct_broadcast_list_itemAdapter(view.getContext(), live_broadcasts);
        view.setAdapter(myGridAdapter);
        view.setClickable(false);
        view.setPressed(false);
//        view.setEnables(false);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setProblemAdapter2(int viewId, List<Problem> problems, int position) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        Problem_GridAdapter2 myGridAdapter = new Problem_GridAdapter2(view.getContext(), problems, position);
        view.setAdapter(myGridAdapter);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setPublic_case_analysis_problem(int viewId, List<Problem> problems, int position, String eid) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        Public_case_analysis_problem myGridAdapter = new Public_case_analysis_problem(view.getContext(), problems, position, eid);
        view.setAdapter(myGridAdapter);

        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setPublic_case_analysis_problem2(int viewId, List<Problem> problems, int position, String eid) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        Public_case_analysis_problem2 myGridAdapter = new Public_case_analysis_problem2(view.getContext(), problems, position, eid);
        view.setAdapter(myGridAdapter);

        return this;
    }


    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setoptionProblemAdapter(int viewId, List<Option> options) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        OptionAdapter myGridAdapter = new OptionAdapter(view.getContext(), options);
        view.setAdapter(myGridAdapter);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setoptionProblemAdapter2(int viewId, List<Option> options) {
        MyRecyclerView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        // 设置布局管理器
        view.setLayoutManager(mLayoutManager);
        OptionAdapter2 myGridAdapter = new OptionAdapter2(view.getContext(), options);
        view.setAdapter(myGridAdapter);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }


    /**
     * 为MyGridView设置适配器
     *正式考试使用
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setoptionProblemAdapter(int viewId, Problem problem, String eid) {

        MyGridView view = getView(viewId);
        Common_answer_questionGridAdapter myGridAdapter = new Common_answer_questionGridAdapter(view.getContext(), problem, eid);
        view.setAdapter(myGridAdapter);

        return this;
    }
    /**
     * 为MyGridView设置适配器
     * 模拟考试使用
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setoptionProblemAdapter3(int viewId, Problem problem, String eid) {

        MyGridView view = getView(viewId);
        Common_answer_questionGridAdapter3 myGridAdapter = new Common_answer_questionGridAdapter3(view.getContext(), problem, eid);
        view.setAdapter(myGridAdapter);

        return this;
    }

    /**
     * 为MyGridView设置适配器
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setoptionProblemAdapter2(int viewId, Problem problems, String eid) {
        MyGridView view = getView(viewId);
        mLayoutManager = new LinearLayoutManager(view.getContext()
                , LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        view.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        Common_answer_questionGridAdapter2 myGridAdapter = new Common_answer_questionGridAdapter2(view.getContext(), problems, 0, eid);

        view.setAdapter(myGridAdapter);
//        LvHeightUtil.setListViewHeightBasedOnChildren(view);
        return this;
    }


    /**
     * 设置填空题
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFillInTheBlanks(int viewId, Problem problem, BaseListAdapter baseListAdapter) {
        FillBlankView view = getView(viewId);
        view.setProblem(problem);
        view.setBaseListAdapter(baseListAdapter);
        view.setData();
        return this;
    }

    /**
     * 为设置积分商城的商品
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setCommodityGridView(int viewId, List<Commodity> commodities) {
        MyGridView view = getView(viewId);
        CommodityGridAdapter myGridAdapter = new CommodityGridAdapter(view.getContext(), commodities);
        view.setAdapter(myGridAdapter);
        return this;
    }


    /**
     * 为 View设置添加点击事件 进入专题更多
     *
     * @param viewId
     * @return
     */
    public ListHolder setIntegral_mallOnClick(int viewId, String str) {
        View view = getView(viewId);
        switch (str) {
            case "纪念品":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), IntegralMallClassification.class);
                        intent.putExtra("position", 1);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case "电子产品":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), IntegralMallClassification.class);
                        intent.putExtra("position", 2);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            case "医学资料":
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), IntegralMallClassification.class);
                        intent.putExtra("position", 3);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
            default:
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), IntegralMallClassification.class);
                        intent.putExtra("position", 0);
                        v.getContext().startActivity(intent);
                    }
                });
                break;
        }

        return this;
    }

    /**
     * 为 View设置添加点击事件 点击关注
     *
     * @param viewId
     * @param item
     * @return
     */
    public ListHolder setUserSearchFocusOnClick(final String hisuid, int viewId, final Object item) {
        final TextView view = getView(viewId);
        final String uid = SharedPreferencesTools.getUid(view.getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProgressBarDialogTools.show(v.getContext());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", uid);
                            object.put("auid", hisuid);
                            object.put("act", URLConfig.attention);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, object.toString());
                            final JSONObject result = new JSONObject(data);
                            System.out.println("resultresult" + result);
                            if (result.getInt("status") == 1) {// 成功

                                ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if ("1".equals(result.getString("data"))) {
                                                view.setText("已关注");
                                                view.setBackgroundResource(R.mipmap.focus_bg_yet);
                                                view.setTextColor(Color.parseColor("#000000"));
                                            } else {
                                                view.setText("关注");
                                                view.setBackgroundResource(R.drawable.anniu17);
                                                view.setTextColor(Color.parseColor("#FFFFFF"));
                                            }
                                            ((Map) item).put("attentionflg", result.getString("data"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } finally {
                                            MyProgressBarDialogTools.hide();
                                        }
                                    }
                                });

                            } else {// 失败
                                Toast.makeText(view.getContext(),
                                        result.getString("errorMessage"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return this;
    }


    /**
     * 为 View设置添加点击事件 点击关注
     *
     * @param viewId
     * @return
     */
    public ListHolder setSpinner(int viewId, final TextView viewScore, final Evaluation_in_detail_bean bean, final Evaluation_of_item item, int select_id, final int status) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (status == 1) {

                    SpinerPopWindow mSpinerPopWindow;
                    SpinerAdapter mAdapter;
                    mAdapter = new SpinerAdapter(spinner.getContext(), item.getGrades());
                    mAdapter.refreshData(item.getGrades(), 0);
                    //初始化PopWindow
                    mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                    mSpinerPopWindow.setAdatper(mAdapter);
                    //设置mSpinerPopWindow显示的宽度
                    mSpinerPopWindow.setWidth(spinner.getWidth());
                    //设置显示的位置在哪个控件的下方
                    mSpinerPopWindow.showAsDropDown(spinner);
                    mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                        @Override
                        public void onItemClick(int pos) {
                            //每次更换 分数   先复制给当前item bean
                            item.setGrade(Integer.parseInt(item.getGrades().get(pos)));
                            select_item.setText(item.getGrades().get(pos));
                            //然后循环 计算分数
                            int count = 0;
                            for (Evaluation_of_item evaluation_of_item : bean.getEvaluation_of_items()) {
                                count += evaluation_of_item.getGrade() * evaluation_of_item.getThe_weight();
                            }
                            //赋值给当前评价 类
                            bean.setScore(count);
                            //更新
                            ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewScore.setText(bean.getScore() + "");
                                }
                            });
                        }
                    });

                }

            }
        });


        return this;
    }


    /**
     * 为 View设置添加点击事件 点击关注
     *
     * @param viewId
     * @return
     */
    public ListHolder setSpinnerDailyExam2(int viewId, final TextView viewScore, final List<Daily_exam_of_item> bean, final Daily_exam_of_item item, boolean canEditable, int select_id, int spinner_arrow_id) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final ImageView spinnerArrow = getView(spinner_arrow_id);


        if (canEditable) {
            spinner.setClickable(true);
            spinner.setEnabled(true);
            spinnerArrow.setVisibility(View.VISIBLE);
        } else {
            List<Integer> stringList = new ArrayList<>();
            stringList.add(item.getGrade());
            select_item.setText(item.getGrade() + "");
            spinner.setClickable(false);
            spinner.setEnabled(false);
            spinnerArrow.setVisibility(View.GONE);
        }
        for (int i = 0; i < item.getGrades().size(); i++) {
            if (item.getGrade() == Integer.parseInt(item.getGrades().get(i))) {
                select_item.setText(item.getGrades().get(i));
            }
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(spinner.getContext(), item.getGrades());
                mAdapter.refreshData(item.getGrades(), 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(spinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(spinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        item.setGrade(Integer.parseInt(item.getGrades().get(pos)));
                        select_item.setText(item.getGrades().get(pos));
                        //然后循环 计算分数
                        int count = 0;
                        for (Daily_exam_of_item daily_exam_of_item : bean) {
                            count += daily_exam_of_item.getGrade() * daily_exam_of_item.getThe_weight();
                        }
                        //更新
                        final int finalCount = count;
                        ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewScore.setText(finalCount + "");

                            }
                        });
                    }
                });

            }
        });
        return this;
    }

    public ListHolder setSpinnerYKExam(int viewId, final TextView viewScore, final List<FromEntity.Config> bean,
                                       final FromEntity.Config item, boolean canEditable, int select_id,
                                       int llDeductionReason_id, int etDeductionReason_id,
                                       final boolean isAddScore, boolean needReson) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final LinearLayout llDeductionReason = getView(llDeductionReason_id);
        final EditText etDeductionReason = getView(etDeductionReason_id);

        if (item.getDataType().equals("tittle")) {
            return this;
        }

        final int grade = Integer.parseInt(item.getGrade());
        final List<String> grades = new ArrayList<>();
        grades.clear();
        for (int i = grade; i >= 0; i--) {
            grades.add(i + "");
        }

        for (FromEntity.Config config : bean) {
            if (TextUtils.isEmpty(config.getScore())) {
                config.setScore("0");
            }
        }

        if (canEditable) {       //是否可以编辑  1、不可编辑   0、可以编辑
            spinner.setClickable(true);
            spinner.setEnabled(true);
            etDeductionReason.setEnabled(true);
            etDeductionReason.setClickable(true);
        } else {
            spinner.setClickable(false);
            spinner.setEnabled(false);
            etDeductionReason.setEnabled(false);
            etDeductionReason.setClickable(false);
        }

        if (canEditable) {
            if (TextUtils.isEmpty(item.getScore()) || "null".equals(item.getScore())) {
                select_item.setText(isAddScore ? item.getGrade() : "0");
            } else {
                select_item.setText(item.getScore());
            }

            if (needReson) {
                if (!isAddScore) {
                    if (TextUtils.isEmpty(item.getScore()) || "null".equals(item.getScore()) ||"0".equals(item.getScore())) {
                        llDeductionReason.setVisibility(View.GONE);
                        item.setEditShow(false);
                    } else {
                        llDeductionReason.setVisibility(View.VISIBLE);
                        item.setEditShow(true);
                    }
                } else {
                    //得分不需扣分理由
                    llDeductionReason.setVisibility(View.GONE);
                    item.setEditShow(false);
                }
            } else {
                llDeductionReason.setVisibility(View.GONE);
                item.setEditShow(false);
            }

            if (item.getDeduct_marks_account() != null) {
                etDeductionReason.setText(item.getDeduct_marks_account());
            }

            final List<String> finalSpinnerList = grades;
            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpinerPopWindow mSpinerPopWindow;
                    SpinerAdapter mAdapter;
                    mAdapter = new SpinerAdapter(spinner.getContext(), finalSpinnerList);
                    mAdapter.refreshData(finalSpinnerList, 0);
                    //初始化PopWindow
                    mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                    mSpinerPopWindow.setAdatper(mAdapter);
                    //设置mSpinerPopWindow显示的宽度
                    mSpinerPopWindow.setWidth(spinner.getWidth());
                    //设置显示的位置在哪个控件的下方
                    mSpinerPopWindow.showAsDropDown(spinner);
                    mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                        @Override
                        public void onItemClick(int pos) {
                            //每次更换 分数   先赋值给当前item bean
                            item.setScore(finalSpinnerList.get(pos));
                            select_item.setText(finalSpinnerList.get(pos));
                            //然后循环 计算分数
                            //deduct_marks扣分 score得分
                            if (!isAddScore) {
                                if (Integer.parseInt(item.getScore()) > 0) {
                                    llDeductionReason.setVisibility(View.VISIBLE);
                                    item.setEditShow(true);
                                } else {
                                    llDeductionReason.setVisibility(View.GONE);
                                    item.setEditShow(false);
                                }

                            } else {
                                llDeductionReason.setVisibility(View.GONE);
                                item.setEditShow(false);
                            }
                            int count = 0;
                            for (FromEntity.Config config : bean) {
                                if (config.getDataType().equals("content")) {
//                                        totalScore += Integer.parseInt(graduateExamQuestion.getScore());
//                                        if (graduateExamQuestion.getDeductionScore() != null) {
//                                            count += Integer.parseInt(graduateExamQuestion.getDeductionScore());
//                                        }
                                    if (!TextUtils.isEmpty(config.getScore())) {
                                        count += Integer.parseInt(config.getScore());
                                    } else {
                                        count += 0;
                                    }

                                }
                            }
                            //更新
                            final int finalCount;
                            if (isAddScore){
                                finalCount = count;
                            } else {
                                finalCount = 100 - count;
                            }
                            ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewScore.setText(finalCount + "");
                                }
                            });
                        }
                    });

                }
            });
        } else {
            select_item.setText(item.getScore());
            if (!isAddScore) {
                if (TextUtils.isEmpty(item.getScore()) || "null".equals(item.getScore()) ||"0".equals(item.getScore())) {
                    llDeductionReason.setVisibility(View.GONE);
                } else {
                    llDeductionReason.setVisibility(View.VISIBLE);
                    etDeductionReason.setText(item.getDeduct_marks_account());
                }
            } else {
                llDeductionReason.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public ListHolder setSpinnerAppraise(int viewId, final TextView viewScore, final List<AppraiseFromEntity.ContentBean> bean,
                                         final AppraiseFromEntity.ContentBean item, int select_id, int spinner_arrow_id) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final ImageView spinnerArrow = getView(spinner_arrow_id);

        if (!TextUtils.isEmpty(item.getScore())) {
            select_item.setText(item.getScore());
        } else {
            select_item.setText(item.getGrade());
        }

        spinner.setClickable(true);
        spinner.setEnabled(true);
        spinnerArrow.setVisibility(View.VISIBLE);

        final int grade = Integer.parseInt(item.getGrade());
        final List<String> grades = new ArrayList<>();
        grades.clear();
        for (int i = grade; i >= 0; i--) {
            grades.add(i + "");
        }

//        for (int i = 0; i < item.getGrades().size(); i++) {
//            if (item.getGrade() == Integer.parseInt(item.getGrades().get(i))) {
//                select_item.setText(item.getGrades().get(i));
//            }
//        }
        for (AppraiseFromEntity.ContentBean config : bean) {
            if (TextUtils.isEmpty(config.getScore())) {
                config.setScore("0");
            }
        }


        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(spinner.getContext(), grades);
                mAdapter.refreshData(grades, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(spinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(spinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        item.setScore(grades.get(pos));
                        select_item.setText(grades.get(pos));
                        //然后循环 计算分数
                        int count = 0;
                        for (AppraiseFromEntity.ContentBean conentBean : bean) {
                            count += Integer.parseInt(conentBean.getScore()) * Integer.parseInt(conentBean.getWeight());
                        }
                        //更新
                        final int finalCount = count;
                        ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewScore.setText(finalCount + "");

                            }
                        });
                    }
                });

            }
        });
        return this;
    }

    public ListHolder setBaseSpinner(int viewId, final TextView viewScore, final List<BaseDetailEntity.ConfigBean> bean,
                                         final BaseDetailEntity.ConfigBean item, int select_id, int spinner_arrow_id) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final ImageView spinnerArrow = getView(spinner_arrow_id);

        if (!TextUtils.isEmpty(item.getScore())) {
            select_item.setText(item.getScore());
        } else {
            select_item.setText(item.getGrade());
        }

        spinner.setClickable(true);
        spinner.setEnabled(true);
        spinnerArrow.setVisibility(View.VISIBLE);

        final int grade = Integer.parseInt(item.getGrade());
        final List<String> grades = new ArrayList<>();
        grades.clear();
        for (int i = grade; i >= 0; i--) {
            grades.add(i + "");
        }

//        for (int i = 0; i < item.getGrades().size(); i++) {
//            if (item.getGrade() == Integer.parseInt(item.getGrades().get(i))) {
//                select_item.setText(item.getGrades().get(i));
//            }
//        }

        for (BaseDetailEntity.ConfigBean config : bean) {
            if (TextUtils.isEmpty(config.getScore())) {
                config.setScore("0");
            }
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(spinner.getContext(), grades);
                mAdapter.refreshData(grades, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(spinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(spinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        item.setScore(grades.get(pos));
                        select_item.setText(grades.get(pos));
                        //然后循环 计算分数
                        int count = 0;
                        for (BaseDetailEntity.ConfigBean conentBean : bean) {
                            if (!TextUtils.isEmpty(conentBean.getScore())) {
                                count += Integer.parseInt(conentBean.getScore()) * Integer.parseInt(conentBean.getWeight());
                            } else {
                                count += Integer.parseInt(conentBean.getGrade()) * Integer.parseInt(conentBean.getWeight());
                            }
                        }
                        //更新
                        final int finalCount = count;
                        ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewScore.setText(finalCount + "");

                            }
                        });
                    }
                });

            }
        });
        return this;
    }
    /*
      设置评价科室打分
     */
    public ListHolder setKsSpinner(int viewId, final TextView viewScore, final List<KsScoreInfo> bean,
                                     final KsScoreInfo item, int select_id, int spinner_arrow_id) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final ImageView spinnerArrow = getView(spinner_arrow_id);

        if (!TextUtils.isEmpty(item.getScore())) {
            select_item.setText(item.getScore());
        } else {
            select_item.setText(item.getGrade());
        }

        spinner.setClickable(true);
        spinner.setEnabled(true);
        spinnerArrow.setVisibility(View.VISIBLE);

        final int grade = Integer.parseInt(item.getGrade());
        final List<String> grades = new ArrayList<>();
        grades.clear();
        for (int i = grade; i >= 0; i--) {
            grades.add(i + "");
        }

//        for (int i = 0; i < item.getGrades().size(); i++) {
//            if (item.getGrade() == Integer.parseInt(item.getGrades().get(i))) {
//                select_item.setText(item.getGrades().get(i));
//            }
//        }

        for (KsScoreInfo config : bean) {
            if (TextUtils.isEmpty(config.getScore())) {
                config.setScore("0");
            }
        }

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpinerPopWindow mSpinerPopWindow;
                SpinerAdapter mAdapter;
                mAdapter = new SpinerAdapter(spinner.getContext(), grades);
                mAdapter.refreshData(grades, 0);
                //初始化PopWindow
                mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                mSpinerPopWindow.setAdatper(mAdapter);
                //设置mSpinerPopWindow显示的宽度
                mSpinerPopWindow.setWidth(spinner.getWidth());
                //设置显示的位置在哪个控件的下方
                mSpinerPopWindow.showAsDropDown(spinner);
                mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                    @Override
                    public void onItemClick(int pos) {
                        //每次更换 分数   先复制给当前item bean
                        item.setScore(grades.get(pos));
                        select_item.setText(grades.get(pos));
                        //然后循环 计算分数
                        int count = 0;
                        for (KsScoreInfo conentBean : bean) {
                            if (!TextUtils.isEmpty(conentBean.getScore())) {
                                count += Integer.parseInt(conentBean.getScore()) * Integer.parseInt(conentBean.getWeight());
                            } else {
                                count += Integer.parseInt(conentBean.getGrade()) * Integer.parseInt(conentBean.getWeight());
                            }
                        }
                        //更新
                        final int finalCount = count;
                        ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewScore.setText(finalCount + "");

                            }
                        });
                    }
                });

            }
        });
        return this;
    }


    /**
     * 为 出科考试View设置添加点击事件 点击关注
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setSpinnerGraduateExam(int viewId, final EditText viewScore, final List<GraduateExamQuestion> bean, final GraduateExamQuestion item, boolean canEditable, int select_id, int llDeductionReason_id, int etDeductionReason_id) {
        final RelativeLayout spinner = getView(viewId);
        final TextView select_item = getView(select_id);
        final LinearLayout llDeductionReason = getView(llDeductionReason_id);
        final EditText etDeductionReason = getView(etDeductionReason_id);

        List<String> spinnerList = new ArrayList<>();
        final String marking = item.getMarking();

        if (item.getDataType().equals("title")) {
            return this;
        }
        if (item.getDisabled().equals("0")) {       //是否可以编辑  1、不可编辑   0、可以编辑
            spinner.setClickable(true);
            spinner.setEnabled(true);
            etDeductionReason.setEnabled(true);
            etDeductionReason.setClickable(true);
        } else {
            spinner.setClickable(false);
            spinner.setEnabled(false);
            etDeductionReason.setEnabled(false);
            etDeductionReason.setClickable(false);
        }

        if (!item.getStandardScore().isEmpty()) {
            if (item.getDeductionScore() != null && !item.getDeductionScore().isEmpty()) {
                select_item.setText(item.getDeductionScore());
            }

            if (Integer.parseInt(item.getDeductionScore()) > 0) {
                llDeductionReason.setVisibility(View.VISIBLE);
            } else {
                llDeductionReason.setVisibility(View.GONE);
            }

            if (item.getDeductionReason() != null) {
                etDeductionReason.setText(item.getDeductionReason());
            }

            if (marking.equals("deduct_marks")) {
                if (item.getStandardScore() != null) {
                    for (int i = 0; i <= Integer.parseInt(item.getStandardScore()); i++) {
                        spinnerList.add(i + "");
                    }
                }
            } else {
                if (item.getStandardScore() != null) {
                    for (int i = Integer.parseInt(item.getStandardScore()); i >= 0; i--) {
                        spinnerList.add(i + "");
                    }
                }
                llDeductionReason.setVisibility(View.GONE);
            }
            final List<String> finalSpinnerList = spinnerList;
            spinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SpinerPopWindow mSpinerPopWindow;
                    SpinerAdapter mAdapter;
                    mAdapter = new SpinerAdapter(spinner.getContext(), finalSpinnerList);
                    mAdapter.refreshData(finalSpinnerList, 0);
                    //初始化PopWindow
                    mSpinerPopWindow = new SpinerPopWindow(spinner.getContext());
                    mSpinerPopWindow.setAdatper(mAdapter);
                    //设置mSpinerPopWindow显示的宽度
                    mSpinerPopWindow.setWidth(spinner.getWidth());
                    //设置显示的位置在哪个控件的下方
                    mSpinerPopWindow.showAsDropDown(spinner);
                    mSpinerPopWindow.setItemListener(new SpinerAdapter.IOnItemSelectListener() {
                        @Override
                        public void onItemClick(int pos) {
                            //每次更换 分数   先赋值给当前item bean
                            item.setDeductionScore(finalSpinnerList.get(pos));
                            select_item.setText(finalSpinnerList.get(pos));
                            //然后循环 计算分数
                            //deduct_marks扣分 score得分
                            if (marking.equals("deduct_marks")) {
                                if (Integer.parseInt(item.getDeductionScore()) > 0) {
                                    llDeductionReason.setVisibility(View.VISIBLE);
                                } else {
                                    llDeductionReason.setVisibility(View.GONE);
                                }
                                int count = 0;
                                int totalScore = 0;
                                for (GraduateExamQuestion graduateExamQuestion : bean) {
                                    if (graduateExamQuestion.getDataType().equals("content") && !graduateExamQuestion.getStandardScore().isEmpty()) {
                                        totalScore += Integer.parseInt(graduateExamQuestion.getStandardScore());
                                        if (graduateExamQuestion.getDeductionScore() != null) {
                                            count += Integer.parseInt(graduateExamQuestion.getDeductionScore());
                                        }
                                    }
                                }
                                //更新
                                final int finalCount = totalScore - count;
                                ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewScore.setText(finalCount + "");

                                    }
                                });
                            } else {
                                llDeductionReason.setVisibility(View.GONE);
                                int count = 0;
                                for (GraduateExamQuestion graduateExamQuestion : bean) {
                                    if (graduateExamQuestion.getDeductionScore() != null && !graduateExamQuestion.getDeductionScore().isEmpty()) {
                                        count += Integer.parseInt(graduateExamQuestion.getDeductionScore());
                                    }
                                }
                                //更新
                                final int finalCount = count;
                                ((Activity) viewScore.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewScore.setText(finalCount + "");

                                    }
                                });
                            }
                        }
                    });

                }
            });
        }
        return this;
    }

    /**
     * 改变监听
     *
     * @param viewId
     * @param
     * @return
     */
    public ListHolder setFocusGraduateExamEdittext(int viewId, final GraduateExamQuestion graduateExamQuestion, final List<GraduateExamQuestion> listQuestion, int layout_id) {
        final EditText view = getView(viewId);
        final LinearLayout fLayout = getView(layout_id);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (view.getText().toString().trim().length() > 0) {
                    listQuestion.get(Integer.parseInt(((LinearLayout) view.getParent()).getTag().toString())).setDeductionReason(editable.toString().trim());
                }
            }
        };
        view.addTextChangedListener(textWatcher);
        view.setTag(textWatcher);
        return this;
    }
    /*

     */
    public ListHolder setFocusGraduateExamEdittext1(int viewId, final GraduateExamQuestion graduateExamQuestion, final List<GraduateExamQuestion> listQuestion, int layout_id) {
        final EditText view = getView(viewId);
        final LinearLayout fLayout = getView(layout_id);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (view.getText().toString().trim().length() > 0) {
                    listQuestion.get(Integer.parseInt(((LinearLayout) view.getParent()).getTag().toString())).setDeductionReason(editable.toString().trim());
                }else {
                    listQuestion.get(Integer.parseInt(((LinearLayout) view.getParent()).getTag().toString())).setDeductionReason(editable.toString().trim());
                }

            }
        };
        view.addTextChangedListener(textWatcher);
        view.setTag(textWatcher);
        return this;
    }

    public ListHolder setFocusSurveyEdittext(int viewId, final List<FromEntity.Config> congig) {
        final EditText view = getView(viewId);
        //清除焦点
        view.clearFocus();
        //先清除之前的文本改变监听
        if (view.getTag() instanceof TextWatcher) {
            view.removeTextChangedListener((TextWatcher) (view.getTag()));
        }
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (view.getText().toString().trim().length() > 0) {
                    congig.get(Integer.parseInt(((LinearLayout) view.getParent()).getTag().toString())).setEditTxt(editable+"");
                }

            }
        };
        view.addTextChangedListener(textWatcher);
        view.setTag(textWatcher);
        return this;
    }


    /**
     * 设置图片
     *
     * @param viewId
     * @param
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ListHolder setDrawable(int viewId, int png_id, String type) {
        TextView view = getView(viewId);
        switch (type) {
            case "left":
                view.setCompoundDrawablesWithIntrinsicBounds(view.getContext().getDrawable(png_id), null, null, null);
                break;
            case "top":
                view.setCompoundDrawablesWithIntrinsicBounds(null, view.getContext().getDrawable(png_id), null, null);
                break;
            case "right":
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, view.getContext().getDrawable(png_id), null);
                break;
            case "bottom":
                view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, view.getContext().getDrawable(png_id));
                break;

        }

        return this;
    }

    public ListHolder setConferenceCollectOnClick(int id_iv_item_conference_main_list_collect, final ConferenceBean item) {
        final ImageView view = getView(id_iv_item_conference_main_list_collect);
        final String uid = SharedPreferencesTools.getUid(view.getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", uid);
                            object.put("aid", item.getId());
                            if ("1".equals(item.getCollectStatus())) {  //是否收藏  0为未收藏     1为已收藏
                                object.put("flg", "cancelConference");
                            } else {
                                object.put("flg", "conference");
                            }
                            object.put("act", URLConfig.doVideo);
                            final String data = HttpClientUtils.sendPost(view.getContext(),
                                    URLConfig.CCMTVAPP, object.toString());
                            final JSONObject result = new JSONObject(data);
                            System.out.println("resultresult" + result);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (result.getInt("status") == 1) {// 成功
//                                            Toast.makeText(view.getContext(),
//                                                    result.getString("errorMessage"),
//                                                    Toast.LENGTH_SHORT).show();
                                            if ("1".equals(item.getCollectStatus())) {
                                                item.setCollectStatus("0");
                                                Toast.makeText(view.getContext(),
                                                        "取消收藏",
                                                        Toast.LENGTH_SHORT).show();
                                                view.setImageResource(R.mipmap.ic_conference_uncollect);
                                            } else {
                                                item.setCollectStatus("1");
                                                Toast.makeText(view.getContext(),
                                                        "收藏成功",
                                                        Toast.LENGTH_SHORT).show();
                                                view.setImageResource(R.mipmap.ic_conference_collect);
                                            }
                                        } else {// 失败
                                            Toast.makeText(view.getContext(),
                                                    result.getString("errorMessage"),
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return this;
    }

    public ListHolder setDetailOnClick(int view_id, final Map<String, String> map) {
        final View view = getView(view_id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), StuPeriodicalExamDetailActivity.class);
                intent.putExtra("fid", v.getTag().toString());
                intent.putExtra("exam_id", map.get("exam_id"));
                view.getContext().startActivity(intent);
            }
        });
        return this;
    }

    public ListHolder setCheckScoreClick(int view_id, final String fid, final Map<String, String> map) {
        final View view = getView(view_id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), StuPeriodicalExamScoreActivity.class);
                intent.putExtra("fid", fid);
                intent.putExtra("exam_id", map.get("exam_id"));
                view.getContext().startActivity(intent);
            }
        });
        return this;
    }

    public ListHolder setStuCheckScoreClick(final Context context, int view_id, final String fid, final Map<String, String> map) {
        final View view = getView(view_id);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.stageUserExaminerList);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("fid", fid);
                            obj.put("score_id", map.get("score_id"));
                            String data = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                            final JSONObject result = new JSONObject(data);
                            System.out.println("resultresult" + result);
                            ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (result.getString("code").equals("200")) {
                                            JSONObject data = result.getJSONObject("data");
                                            if (data.getInt("status") == 1) {
                                                JSONObject infoData = data.getJSONObject("info");
                                                Intent intent = new Intent(context, StuPeriodicalExamActivity.class);
                                                intent.putExtra("fid", fid);
                                                intent.putExtra("score_id", map.get("score_id"));
                                                context.startActivity(intent);
                                            } else {
                                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return this;
    }

    public ListHolder setDegrees(int view_id, int i) {
        LeanTextView view = getView(view_id);
        view.setDegrees(i);
        return this;
    }
}

package com.linlic.ccmtv.yx.activity.my;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.YKListActivity;
import com.linlic.ccmtv.yx.activity.assginks.AssginKsIndexAcivity;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.bigcase.BigCaseActivity;
import com.linlic.ccmtv.yx.activity.cashier.CashierActivity2;
import com.linlic.ccmtv.yx.activity.check_work_attendance.Check_work_attendance;
import com.linlic.ccmtv.yx.activity.comment360.AppraiseIndexActivity;
import com.linlic.ccmtv.yx.activity.entrance_edu.EntranceEduUserActivity;
import com.linlic.ccmtv.yx.activity.home.ActivityWebActivity;
import com.linlic.ccmtv.yx.activity.hospital_training.Practice_Main;
import com.linlic.ccmtv.yx.activity.integral_mall.IntegralMall;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.medal.MedalListActivity;
import com.linlic.ccmtv.yx.activity.message.Message_management;
import com.linlic.ccmtv.yx.activity.my.book.Video_book_Main;
import com.linlic.ccmtv.yx.activity.my.feedback.Feedback_Main;
import com.linlic.ccmtv.yx.activity.my.learning_task.LearningTaskActivity;
import com.linlic.ccmtv.yx.activity.my.medical_examination.My_exams_LearningTaskActivity;
import com.linlic.ccmtv.yx.activity.my.newDownload.DownloadFinishActivity;
import com.linlic.ccmtv.yx.activity.my.our_video.My_Our_Resources_Activity;
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.audit.Audit_list;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.college_level_activities.College_level_activities;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation.Evaluation2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.ApplyForDelayActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.ExaminationManageActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_exam.SingleStationExamActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity5;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.rotation.Rotation;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.students.Students_month;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.the_division_management.Entry_month_selection;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.the_teachers_management.The_teachers_management;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management2;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Training_management3;
import com.linlic.ccmtv.yx.activity.step.Clinical_exercise;
import com.linlic.ccmtv.yx.activity.supervisor.Oversight_Task_List;
import com.linlic.ccmtv.yx.activity.tutor.Tutor_students;
import com.linlic.ccmtv.yx.activity.user_statistics.StudyRecordListActivity;
import com.linlic.ccmtv.yx.activity.user_statistics.UserStatistics2Activity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.gensee.GenseeMainActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.BadgeUtil;
import com.linlic.ccmtv.yx.utils.CircleImageView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFragment2 extends BaseFragment implements View.OnClickListener {
    private LinearLayout my_mydata;//我的资料
    private CircleImageView my_myhead;//头像
    private LinearLayout layout_follow, layout_workmate, layout_yjfk,
            layout_openmenber, layout_mycollection, layout_phonerj,
            layout_invitation_friend, layout_Recent_Browse, clinical_exercise,
            layout_my_Integral, layout_my_changepass, integralMall, integralMall2, layout_wdxz, layout_cashier, reading_e_books;
    Map<String, Object> map;
    private TextView big_keshi, small_keshi, tv_hosname, my_name, my_open_vip;
    private int Str_IsYZ, Str_vipflg, Str_idcard_yz, Str_current_day_mony_flg;
    Button btn_jifen;
    String Str_icon, Str_username, Str_idcard_yz_reason, Str_idcard_imgurl, Str_cityid,
            Str_hyleibie, Str_cityname, Str_phonenum, Str_bigkeshi, Str_personalmoney, Str_smallkeshi,
            Str_zhicheng, Str_truename, Str_sexName, Str_address, Str_idcard, Str_sex, Str_danwei, Str_countmoney, Str_countpersonalmoney;
    String Bg_icon;
    ImageView layout_yszzrz, iv_isvip, layout_setting;
    private TextView iv_red_dians;
    private FrameLayout layout_message;
    private LinearLayout layout_haslogin;
    private LinearLayout layout_nologin;
    private LinearLayout iv_top;
    boolean ishas = false, isfirst = true;  //检查权限接口是否正确（数据库查询出错）
    String integration, integration_front;
    TextView myfragment_sign_in;
    private LinearLayout my_examination;
    private LinearLayout user_statistics, identity_layout;
    private View my_examination_view;
    private MyGridView cloud_housekeeper;
    private Dialog dialog;
    private View viewLayout;
    private ListView role_list;
    BaseListAdapter baseListAdapter;
    BaseListAdapter baseListAdapter2;
    private TextView identity, to_switch_identities;
    private List<Map<String, Object>> data = new ArrayList<>();
    private List<Map<String, Object>> data2 = new ArrayList<>();
    private int select_position = -1;
    private String fid;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            SharedPreferencesTools.savePerfectInformation(getContext(), msg.obj + "");
                            JSONObject object = result.getJSONObject("data");
                            Str_hyleibie = object.getString("hyleibie");
                            Str_bigkeshi = object.getString("keshilb");         //科室
                            Str_smallkeshi = object.getString("keshi");
                            Str_zhicheng = object.getString("my_694");          //职称
                            Str_truename = object.getString("truename");        //正式姓名
                            Str_sexName = object.getString("sexName");          //性别
                            Str_address = object.getString("address");          //联系地址
                            Str_idcard = object.getString("idcard");
                            Str_sex = object.getString("sex");                  //性别代号
                            Str_phonenum = object.getString("mobphone");        //手机号码
                            Str_username = object.getString("username");
                            Str_personalmoney = object.getString("personalmoney");//收银台
                            Str_vipflg = object.getInt("vipflg");             //是否为vip 标识 1代表是vip 0不是
                            Str_idcard_yz = object.getInt("idcard_yz");
                            Str_idcard_yz_reason = object.getString("idcard_yz_reason");
                            Str_idcard_imgurl = object.getString("idcard_imgurl");
                            Str_current_day_mony_flg = object.getInt("currentDayMonyFlg");
                            Str_countmoney = object.getString("countmoney");
                            integration = object.getString("integration");
                            Str_countpersonalmoney = object.getString("countpersonalmoney");
                            String cur_rank_name = object.getString("cur_rank_name");
                            String cur_grade = object.getString("cur_grade");
                            String cur_hatpic = object.getString("cur_hatpic");
                            my_name.setText(Str_username);
//                            big_keshi.setText(Str_bigkeshi+"\t\t"+Str_smallkeshi);
//                            small_keshi.setText(Str_smallkeshi);
                            if (Str_vipflg == 1) {
                                iv_isvip.setVisibility(View.VISIBLE);
//                                my_open_vip.setText("续费会员");
                                my_open_vip.setText("续费");
                            } else {
                                iv_isvip.setVisibility(View.INVISIBLE);
//                                my_open_vip.setText("开通VIP");
                                my_open_vip.setText("开通");
                            }
                          /*  if ((object.getString("ifnew").toString()).equals("1")) {
                                iv_red_dians.setVisibility(View.VISIBLE);
                            } else {
                                iv_red_dians.setVisibility(View.INVISIBLE);
                            }*/
                            SharedPreferencesTools.saveVipFlag(getActivity(), Str_vipflg);
                            Str_IsYZ = object.getInt("mob_yz");
                            Str_icon = FirstLetter.getSpells(object.getString("icon"));
                            SharedPreferencesTools.saveVipEndTime(getActivity(), object.getString("vip_end_time"));
                            SharedPreferencesTools.saveVipFlag(getActivity(), Str_vipflg);
                            SharedPreferencesTools.saveIntegral(getActivity(), String.valueOf(object.getInt("money")));
                            SharedPreferencesTools.saveStricon(getActivity(), Str_icon);
                            SharedPreferencesTools.savePersonalmoney(getActivity(), Str_personalmoney);
                            SharedPreferencesTools.savecurrentDayMonyFlg(getActivity(), Str_current_day_mony_flg + "");
                            SharedPreferencesTools.saveStrKeShi(getActivity(), Str_bigkeshi, Str_smallkeshi);

                            //显示头像
                            if (!TextUtils.isEmpty(Str_icon)) {
                                Picasso.with(getActivity()).load(Str_icon).error(R.mipmap.img_default)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)//跳过内存缓存
                                        .networkPolicy(NetworkPolicy.NO_CACHE)//跳过磁盘缓存
                                        .placeholder(my_myhead.getDrawable()).into(my_myhead);
                            }
                            JSONObject obj = object.getJSONObject("hosName");
                            tv_hosname.setText(obj.getString("name") + "\t\t" + Str_bigkeshi + "\t\t" + Str_smallkeshi);

                            Str_cityname = obj.getString("name");                //医院
                            Str_cityid = obj.getString("id");                   //医院代号
                            SharedPreferencesTools.saveStrHos(getActivity(), obj.getString("name"));
                            Str_danwei = object.getString("danwei");

                            if (Str_idcard_yz == 1) {
                                layout_yszzrz.setImageResource(R.mipmap.ic_my_verify);
                                layout_yszzrz.setVisibility(View.VISIBLE);
                            } else {
                                layout_yszzrz.setImageResource(R.mipmap.ic_my_verify);
                                layout_yszzrz.setVisibility(View.INVISIBLE);
                            }

                            //判断用户是否已签到
                            if (Str_current_day_mony_flg == 1) {//已签到
                                myfragment_sign_in.setBackground(getActivity().getResources().getDrawable(R.drawable.sign_yes));
                                myfragment_sign_in.setTextColor(getActivity().getResources().getColor(R.color.white));
                                myfragment_sign_in.setText("已领" + integration + "积分");
                                myfragment_sign_in.setClickable(false);
                            } else {//未签到
                                myfragment_sign_in.setBackground(getActivity().getResources().getDrawable(R.drawable.sign_no));
                                myfragment_sign_in.setTextColor(getActivity().getResources().getColor(R.color.sign_no));
                                myfragment_sign_in.setText("领积分");
                                myfragment_sign_in.setClickable(true);
                            }
                        } else {//失败
                            my_name.setText(SharedPreferencesTools.getUserName(getActivity()));
//                            big_keshi.setText(SharedPreferencesTools.getStrBigKeShi(getActivity())+"\t\t"+SharedPreferencesTools.getStrSmallKeShi(getActivity()));
//                            small_keshi.setText(SharedPreferencesTools.getStrSmallKeShi(getActivity()));
                            if (SharedPreferencesTools.getIsdocexam(getActivity()) && !SharedPreferencesTools.getUserIdentity(getActivity()).isEmpty()) {
                                tv_hosname.setText(SharedPreferencesTools.getStrhos(getActivity()) + "\t\t" + SharedPreferencesTools.getUserIdentity(getActivity()));
                            } else {
                                tv_hosname.setText(SharedPreferencesTools.getStrhos(getActivity()) + "\t\t" + SharedPreferencesTools.getStrBigKeShi(getActivity()) + "\t\t" + SharedPreferencesTools.getStrSmallKeShi(getActivity()));
                            }
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:                                                             //获取权限
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            ishas = true;
                            String integral_state = result.getString("integral_state");
                            integration_front = result.getString("money");
                            if (Integer.parseInt(integral_state) == 0) {                            //不可领取（已领过）

                            } else {
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject();
                                            obj.put("uid", SharedPreferencesTools.getUid(getActivity()));
                                            obj.put("act", URLConfig.getIntegration);
                                            String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP, obj.toString());

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
                        } else {//失败
                            ishas = false;
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            myfragment_sign_in.setBackground(getActivity().getResources().getDrawable(R.drawable.sign_yes));
                            myfragment_sign_in.setTextColor(getActivity().getResources().getColor(R.color.white));
                            myfragment_sign_in.setText("已领" + result.getString("integration") + "积分");
                            SharedPreferencesTools.savecurrentDayMonyFlg(getActivity(), "1");
                            myfragment_sign_in.setClickable(false);
                        } else {//失败
                            myfragment_sign_in.setBackground(getActivity().getResources().getDrawable(R.drawable.sign_no));
                            myfragment_sign_in.setTextColor(getActivity().getResources().getColor(R.color.sign_no));
                            myfragment_sign_in.setText("领积分");
                            SharedPreferencesTools.savecurrentDayMonyFlg(getActivity(), "0");
                            myfragment_sign_in.setClickable(true);
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            Intent intent = new Intent(getActivity(), My_exams_LearningTaskActivity.class);
                            startActivity(intent);
                        } else {//失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("code") == 200) {
                            JSONObject dataJson = result.getJSONObject("data");
                            String identity = dataJson.getString("identity");
                            String realname = dataJson.getString("realname");
                            SharedPreferencesTools.saveUserIdentity(getActivity(), identity);
                            SharedPreferencesTools.saveUserTrueName(getActivity(), realname);
                            if (dataJson.getInt("status") == 1) { //成功
                                data.clear();
                                JSONArray dataArray = dataJson.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataJSONObject = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", dataJSONObject.getString("id"));
                                    map.put("name", dataJSONObject.getString("name"));
                                    map.put("is_show", dataJSONObject.getString("is_show_new"));
                                    map.put("icon", dataJSONObject.getString("icon"));
                                    map.put("url", dataJSONObject.getString("url"));
                                    fid = dataJSONObject.getString("id");
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                                if (data.size() < 1) {
                                    my_examination.setVisibility(View.GONE);
                                    my_examination_view.setVisibility(View.VISIBLE);
                                } else {
                                    my_examination.setVisibility(View.VISIBLE);
                                    my_examination_view.setVisibility(View.GONE);
                                }
                                MyProgressBarDialogTools.hide();
                            } else {
                                my_examination.setVisibility(View.GONE);
                                my_examination_view.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        final JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) { //成功
                            JSONObject jsonObject = result.getJSONObject("data");
                            Bg_icon = jsonObject.getString("headimg");
                            //设置背景
                            new DownloadImageTask().execute(Bg_icon);
                        } else {//失败
//                            Toast.makeText(getApplicationContext(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("code") == 200) {
                            JSONObject dataJson = result.getJSONObject("data");
                            String identity = dataJson.getString("identity");
                            SharedPreferencesTools.saveUserIdentity(getActivity(), identity);
                            if (dataJson.getInt("status") == 1) { //成功
                                data.clear();
                                JSONArray dataArray = dataJson.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataJSONObject = dataArray.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("id", dataJSONObject.getString("id"));
                                    map.put("name", dataJSONObject.getString("name"));
                                    map.put("is_show", dataJSONObject.getString("is_show_new"));
                                    map.put("icon", dataJSONObject.getString("icon"));
                                    map.put("url", dataJSONObject.getString("url"));
                                    fid = dataJSONObject.getString("id");
                                    data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

                                if (data.size() < 1) {
                                    my_examination.setVisibility(View.GONE);
                                    my_examination_view.setVisibility(View.VISIBLE);
                                } else {
                                    my_examination.setVisibility(View.VISIBLE);
                                    my_examination_view.setVisibility(View.GONE);
                                }
                                MyProgressBarDialogTools.hide();
                            } else {
                                MyProgressBarDialogTools.hide();
                                my_examination.setVisibility(View.GONE);
                                my_examination_view.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 8:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                if (dataJson.getJSONObject("data").getString("count").equals("0")) {
                                    iv_red_dians.setVisibility(View.GONE);
                                    BadgeUtil.removeBadgeCount(getActivity());
                                } else {
                                    iv_red_dians.setVisibility(View.VISIBLE);
                                    int count = Integer.parseInt(dataJson.getJSONObject("data").getString("count").trim());
                                    BadgeUtil.applyBadgeCount(getActivity(), count);
                                    if (Integer.parseInt(dataJson.getJSONObject("data").getString("count").trim()) > 99) {
                                        iv_red_dians.setText("99+");
                                    } else {
                                        iv_red_dians.setText(dataJson.getJSONObject("data").getString("count").trim());
                                    }

                                }

                            } else {
                                Toast.makeText(getActivity(), dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    private int medalTag = 0;////1为勋章模块开启   0为关
    private View myfragment_layout_medal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViewById();
        setAllonClick();
        init();
    }

    public void init() {
        // Bundle bundle =this.getArguments();//得到从Activity传来的数据
//        medalTag = 0;
//        if (bundle != null) {
//           medalTag = bundle.getInt("1");//改成固定值一直显示勋章
//
//        }
//        if (medalTag != 1) {
//            myfragment_layout_medal.setVisibility(View.INVISIBLE);
//        } else {
//            myfragment_layout_medal.setVisibility(View.VISIBLE);
//        }
        if (SharedPreferencesTools.getIsdocexam(getContext())) {
            my_examination.setVisibility(View.VISIBLE);
            my_examination_view.setVisibility(View.GONE);

            try {
                //获取身份列表
//            to_switch_identities
                JSONArray roleListJson = new JSONArray(SharedPreferencesTools.getRoleList(getContext()));
                if (roleListJson.length() > 1) {
                    to_switch_identities.setVisibility(View.VISIBLE);
                } else {
                    to_switch_identities.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            my_examination.setVisibility(View.GONE);
            my_examination_view.setVisibility(View.VISIBLE);
        }

        baseListAdapter = new BaseListAdapter(cloud_housekeeper, data, R.layout.item_cloud_housekeeper) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, Object> map = (Map<String, Object>) item;

                helper.setText(R.id._item_id, map.get("id").toString());
                helper.setText(R.id._item_text, map.get("name").toString());
                if (map.get("is_show").toString().equals("1")) {
                    helper.setViewVisibility(R.id._item_id_dot2, View.VISIBLE);
                } else {
                    helper.setViewVisibility(R.id._item_id_dot2, View.GONE);
                }
                switch (map.get("icon").toString()) {
                    case "task":
                        helper.setImage(R.id._item_id_img, R.mipmap.activity_my_23);
                        break;
                    case "exam":
                        helper.setImage(R.id._item_id_img, R.mipmap.activity_my_20);
                        break;
                    case "notice":
                        helper.setImage(R.id._item_id_img, R.mipmap.activity_my_21);
                        break;
                    case "resource":
                        helper.setImage(R.id._item_id_img, R.mipmap.activity_my_22);
                        break;
                    case "test":
                        helper.setImage(R.id._item_id_img, R.mipmap.practice_main_icon07);
                        break;
                    case "plan":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon9);
                        break;
                    case "appraise":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon3);
                        break;
                    case "rk":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon2);
                        break;
                    case "examine":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon1);
                        break;
                   /* case "消息":
                        helper.setImage(R.id._item_id_img,R.mipmap.rules_to_compensate_icon4);
                        break;*/
                    case "reflect":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon5);
                        break;
                    case "faculty"://师资管理
                        helper.setImage(R.id._item_id_img, R.mipmap.faculty);
                        break;
                    case "book":
                        helper.setImage(R.id._item_id_img, R.mipmap.round_robin_handbook);
                        break;
                    case "sign":
                        helper.setImage(R.id._item_id_img, R.mipmap.check_work_attendance_icon);
                        break;
                    case "activities":
                        helper.setImage(R.id._item_id_img, R.mipmap.gp_active_icon);
                        break;
                    case "student":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon10);
                        break;
                    case "verify":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_lcsh);
                        break;
                    case "stageExam":
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_kg);
                        break;
                    case "big_case":
                        //大病历
                        helper.setImage(R.id._item_id_img, R.mipmap.big_case);
                        break;
                    case "rkedu":
                        //入科教育
                        helper.setImage(R.id._item_id_img, R.mipmap.ruke);
                        break;
                    case "activity":
                        //院级活动
                        helper.setImage(R.id._item_id_img, R.mipmap.activity);
                        break;
                    case "yueke":
                        //约课(pic)
                        helper.setImage(R.id._item_id_img, R.mipmap.yueke);
                        break;
                    case "rkks":
                        //入科分配科室
                        helper.setImage(R.id._item_id_img, R.mipmap.rkks);
                        break;
                    case "appraiseT":
                        //评价住院医师
                        helper.setImage(R.id._item_id_img, R.mipmap.appraise);
                        break;
                    case "live":
                        //直播
                        helper.setImage(R.id._item_id_img, R.mipmap.gensee_icon03);
                        break;
                    case "tealist":
                        //全院教学活动列表
                        helper.setImage(R.id._item_id_img, R.mipmap.tealist_icon01);
                        break;
                    case "my_activities":
                        //我的培训
                        helper.setImage(R.id._item_id_img, R.mipmap.rules_to_compensate_icon8);
                        break;
                    case "osce":
                        helper.setImage(R.id._item_id_img, R.mipmap.osce_icon);
                        break;
                    case "supervisor":
                        //督导
                        helper.setImage(R.id._item_id_img, R.mipmap.supervisor);
                        break;
                    case "tutor":
                        //导师
                        helper.setImage(R.id._item_id_img, R.mipmap.tutor);
                        break;
                    case "complete":
                        //结业申请
                        helper.setImage(R.id._item_id_img, R.mipmap.ic_completion_application);
                        break;

                    case "postpone":
                        helper.setImage(R.id._item_id_img,R.mipmap.delay_icon);
                        break;
                }


            }
        };

        cloud_housekeeper.setAdapter(baseListAdapter);
        cloud_housekeeper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isfast = checkDoubleClick();
                Map<String, Object> map = data.get(position);
                Intent intent = null;
                //判断是否登录
                if (SharedPreferencesTools.getUids(getActivity()) == null) {
                    intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra("source", "my");
                    startActivity(intent);
                    return;
                }
                switch (data.get(position).get("icon").toString()) {
                    case "task":
                        intent = new Intent(getActivity(), LearningTaskActivity.class);
                        break;
                    case "supervisor":
                        intent = new Intent(getActivity(), Oversight_Task_List.class);
                        intent.putExtra("title", map.get("name").toString());
                        break;
                    case "exam":
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("cuid", SharedPreferencesTools.getUids(getActivity()));
                                    object.put("act", URLConfig.checkHosUser);
                                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.Medical_examination, object.toString());
                                    Message message = new Message();
                                    message.what = 4;
                                    message.obj = result;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(500);
                                }
                            }
                        }).start();
                        break;
                    case "notice":
                        intent = new Intent(getActivity(), Message_management.class);
                        break;
                    case "tutor":
                        intent = new Intent(getActivity(), Tutor_students.class);
                        intent.putExtra("title", map.get("name").toString());
                        break;
                    case "resource":
                        intent = new Intent(getActivity(), My_Our_Resources_Activity.class);
                        break;
                    case "test":
                        intent = new Intent(getActivity(), Practice_Main.class);
                        break;
                    case "plan"://轮转计划
                        intent = new Intent(getActivity(), Rotation.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("title", map.get("name").toString());
                        break;
                    case "appraise":
                        switch (SharedPreferencesTools.getGp_type(getContext())) {
                            case "3":
                                break;
                            case "1":
                                intent = new Intent(getActivity(), Evaluation.class);
                                intent.putExtra("fid", map.get("id").toString());
                                intent.putExtra("name", map.get("name").toString());
                                break;
                            case "2":
                                intent = new Intent(getActivity(), Evaluation2.class);
                                intent.putExtra("fid", map.get("id").toString());
                                intent.putExtra("name", map.get("name").toString());
                                break;
                        }

                        break;
                    case "rk"://入科管理
                        intent = new Intent(getActivity(), Entry_month_selection.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "examine":
                        intent = new Intent(getActivity(), ExaminationManageActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "reflect":
                        if (SharedPreferencesTools.getUid(getContext()).length() > 0) {
                            intent = new Intent(getActivity(), Feedback_Main.class);
                        }
                        break;
                    case "faculty"://师资管理
                        intent = new Intent(getActivity(), The_teachers_management.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "activities"://教学活动
                        intent = new Intent(getActivity(), Training_management.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "tealist":
                        intent = new Intent(getActivity(), Training_management3.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "my_activities":
                        intent = new Intent(getActivity(), Training_management2.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "student":
                        intent = new Intent(getActivity(), Students_month.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "verify":
                        intent = new Intent(getActivity(), Audit_list.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "stageExam":
//                        intent = new Intent(getActivity(), PeriodicalExamListActivity.class);
//                        intent.putExtra("fid", map.get("id").toString());
                        intent = new Intent(getActivity(), SingleStationExamActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("icon", "stageExam");
                        break;
                    case "sign"://考勤签到
                        intent = new Intent(getActivity(), Check_work_attendance.class);
                        intent.putExtra("fid", map.get("id").toString());
                        //intent = new Intent(getActivity(), LearningPackageActivity.class);
//                        intent.putExtra("fid", map.get("id").toString());
                        //intent = new Intent(getActivity(), YKListActivity.class);
                        break;
                    case "activity"://院级活动
                        intent = new Intent(getActivity(), College_level_activities.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "big_case"://大病历
                        intent = new Intent(getActivity(), BigCaseActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "rkedu"://入科教育
                        intent = new Intent(getActivity(), EntranceEduUserActivity.class);
                        break;
                    case "yueke"://约课
                        intent = new Intent(getActivity(), YKListActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "rkks":
                        //入科分配科室
                        intent = new Intent(getActivity(), AssginKsIndexAcivity.class);
                        intent.putExtra("name", map.get("name").toString());
                        break;
                    case "appraiseT":
                        //评价住院医师
                        intent = new Intent(getActivity(), AppraiseIndexActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "live":
                        //直播
                        intent = new Intent(getActivity(), GenseeMainActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        break;
                    case "osce":
                        intent = new Intent(getActivity(), SingleStationExamActivity.class);
                        intent.putExtra("fid", map.get("id").toString());
                        intent.putExtra("icon", "osce");
                        break;
                    case "complete":
                        Intent intent2 = new Intent(getContext(), GpPersonalInformationActivity5.class);
                        intent2.putExtra("uid",SharedPreferencesTools.getUidONnull(getContext()));
                        intent2.putExtra("name",Str_truename);
                        startActivity(intent2);
                        break;
                    case "postpone":
                        intent = new Intent(getActivity(), ApplyForDelayActivity.class);
                        intent.putExtra("fid",map.get("id").toString());
                        break;
                }

                //先判断url是否为空，不为空则跳转轮转手册网页
                if (map.get("url").toString().length() > 0 && !map.get("url").toString().endsWith("gphome/gp_user/rotaryCareer")) {//轮转手册
//                    intent = new Intent(getActivity(), GpWebViewActivity.class);
//                    intent.putExtra("url", map.get("url").toString());
//                    intent.putExtra("name", map.get("name").toString());
                    intent = new Intent(getActivity(), ActivityWebActivity.class);
                    intent.putExtra("title", map.get("name").toString());
                    intent.putExtra("aid", map.get("url").toString());
                }
                if (intent != null && isfast == false) {
                    startActivity(intent);
                }

            }
        });
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


    public void chcheckIntegration() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("uid", SharedPreferencesTools.getUid(getActivity()));
                    obj.put("act", URLConfig.getIntegration);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP, obj.toString());

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

    /**
     * 获取功能模块儿icon
     */
    public void checkRedDot() {
        if (SharedPreferencesTools.getIsdocexam(getContext())) {
            try {
                final JSONObject roleJson = new JSONObject(SharedPreferencesTools.getRole(getContext()));
                if (!roleJson.getString("id").equals("0")) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("act", URLConfig.gpRoleList);
                                obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                                obj.put("gp_type", SharedPreferencesTools.getGp_type(getContext()));

                                obj.put("gp_rid", roleJson.getString("id"));
                                String result = HttpClientUtils.sendPostToGP(getActivity(), URLConfig.CCMTVAPP_GpApi, obj.toString());
                                LogUtil.e("功能点", result);
                                Message message = new Message();
                                message.what = 5;
                                message.obj = result;
                                handler.sendMessage(message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(500);
                            }

                        }
                    };
                    new Thread(runnable).start();
                } else {
                    my_examination.setVisibility(View.GONE);
                    my_examination_view.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否显示红点
     */
    public void checkRedDot2() {
        if (SharedPreferencesTools.getIsdocexam(getContext())) {

            MyProgressBarDialogTools.show(getContext());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.gpRoleList);
                        obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                        obj.put("gp_type", SharedPreferencesTools.getGp_type(getContext()));
                        JSONObject roleJson = new JSONObject(SharedPreferencesTools.getRole(getContext()));
                        obj.put("gp_rid", roleJson.getString("id"));
                        String result = HttpClientUtils.sendPostToGP(getActivity(), URLConfig.CCMTVAPP_GpApi, obj.toString());
//                        LogUtil.e("功能点",result);
                        Message message = new Message();
                        message.what = 7;
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

    private void findViewById() {
        my_examination_view = getActivity().findViewById(R.id.my_examination_view);
        my_mydata = (LinearLayout) getActivity().findViewById(R.id.my_mydata);
        my_examination = (LinearLayout) getActivity().findViewById(R.id.my_examination);
        layout_yjfk = (LinearLayout) getActivity().findViewById(R.id.layout_yjfk);
        my_myhead = (CircleImageView) getActivity().findViewById(R.id.my_myhead);
        layout_message = (FrameLayout) getActivity().findViewById(R.id.layout_message);
        layout_follow = (LinearLayout) getActivity().findViewById(R.id.layout_follow);
        layout_cashier = (LinearLayout) getActivity().findViewById(R.id.layout_cashier);
        layout_workmate = (LinearLayout) getActivity().findViewById(R.id.layout_workmate);
        layout_openmenber = (LinearLayout) getActivity().findViewById(R.id.layout_openmenber);
        clinical_exercise = (LinearLayout) getActivity().findViewById(R.id.clinical_exercise);
        layout_mycollection = (LinearLayout) getActivity().findViewById(R.id.layout_mycollection);
        layout_invitation_friend = (LinearLayout) getActivity().findViewById(R.id.invitation_friend);
        reading_e_books = (LinearLayout) getActivity().findViewById(R.id.reading_e_books);
        layout_yszzrz = (ImageView) getActivity().findViewById(R.id.layout_yszzrz);
        layout_Recent_Browse = (LinearLayout) getActivity().findViewById(R.id.layout_Recent_Browse);
        my_open_vip = (TextView) getActivity().findViewById(R.id.my_open_vip);
        my_name = (TextView) getActivity().findViewById(R.id.my_name);
//        big_keshi = (TextView) getActivity().findViewById(R.id.big_keshi);
//        small_keshi = (TextView) getActivity().findViewById(R.id.small_keshi);
        tv_hosname = (TextView) getActivity().findViewById(R.id.tv_his_hosp);
        btn_jifen = (Button) getActivity().findViewById(R.id.btn_jifen);
        iv_isvip = (ImageView) getActivity().findViewById(R.id.iv_isvip);
        iv_red_dians = (TextView) getActivity().findViewById(R.id.iv_red_dians);
        layout_setting = (ImageView) getActivity().findViewById(R.id.layout_setting);
        layout_wdxz = (LinearLayout) getActivity().findViewById(R.id.layout_wdxz);
        layout_haslogin = (LinearLayout) getActivity().findViewById(R.id.layout_haslogin);
        layout_nologin = (LinearLayout) getActivity().findViewById(R.id.layout_nologin);
        iv_top = (LinearLayout) getActivity().findViewById(R.id.iv_top);
        myfragment_sign_in = (TextView) getActivity().findViewById(R.id.myfragment_sign_in);


        integralMall = (LinearLayout) getActivity().findViewById(R.id.integralMall);
        integralMall2 = (LinearLayout) getActivity().findViewById(R.id.integralMall2);
        cloud_housekeeper = (MyGridView) getActivity().findViewById(R.id.cloud_housekeeper);
        user_statistics = (LinearLayout) getActivity().findViewById(R.id.user_statistics);
        identity_layout = (LinearLayout) getActivity().findViewById(R.id.identity_layout);
        identity = (TextView) getActivity().findViewById(R.id.identity);
        to_switch_identities = (TextView) getActivity().findViewById(R.id.to_switch_identities);
        myfragment_layout_medal = getActivity().findViewById(R.id.myfragment_layout_medal);
        /*user_statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UserStatistics2Activity.class));
            }
        });*/
    }

    private void setAllonClick() {
        layout_haslogin.setOnClickListener(this);
        layout_yjfk.setOnClickListener(this);
        my_myhead.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        layout_follow.setOnClickListener(this);
        layout_cashier.setOnClickListener(this);
        layout_workmate.setOnClickListener(this);
        layout_openmenber.setOnClickListener(this);
        layout_mycollection.setOnClickListener(this);
        layout_invitation_friend.setOnClickListener(this);
        layout_yszzrz.setOnClickListener(this);
        layout_Recent_Browse.setOnClickListener(this);
        layout_setting.setOnClickListener(this);
        layout_wdxz.setOnClickListener(this);
        layout_nologin.setOnClickListener(this);
        myfragment_sign_in.setOnClickListener(this);
        integralMall.setOnClickListener(this);
        integralMall2.setOnClickListener(this);
        clinical_exercise.setOnClickListener(this);
        reading_e_books.setOnClickListener(this);
        user_statistics.setOnClickListener(this);
        to_switch_identities.setOnClickListener(this);
        getActivity().findViewById(R.id.myfragment_layout_medal).setOnClickListener(this);
    }

    //获取用户信息
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initdata() {
        final String uid = SharedPreferencesTools.getUids(getActivity());
        getLoginImgUrl();

        if (uid == null || ("").equals(uid)) {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiscCache();
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.img_default);
            my_myhead.setImageDrawable(drawable);
            myfragment_sign_in.setBackground(getActivity().getResources().getDrawable(R.drawable.sign_no));
            ;//未登录下显示未签到状态
            myfragment_sign_in.setTextColor(getActivity().getResources().getColor(R.color.sign_no));
            myfragment_sign_in.setText("领积分");
            myfragment_sign_in.setClickable(true);
            layout_nologin.setVisibility(View.VISIBLE);
            layout_haslogin.setVisibility(View.GONE);
            BadgeUtil.removeBadgeCount(getActivity());
//            //未登录状态隐藏成长值信息
            return;
        } else {
            allNoReadCount();
            layout_nologin.setVisibility(View.GONE);
            layout_haslogin.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("uid", uid);
                        object.put("act", URLConfig.getUserInfo);
                        String result = HttpClientUtils.sendPost(getActivity(), URLConfig.CCMTVAPP, object.toString());
                        Message message = new Message();
                        message.what = 1;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();

            checkRedDot();
        }
    }

    @Override
    public void onResume() {
        try {
            init();
            initdata();

            if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                if (SharedPreferencesTools.getIsdocexam(getContext())) {
                    //检查用户身份列表是否大于一个
                    JSONArray jsonArray = new JSONArray(SharedPreferencesTools.getRoleList(getContext()));
                    if (jsonArray.length() > 0) {
                        //第一步查看用户是否选中身份
                        if (SharedPreferencesTools.getRole(getContext()).length() > 0) {
                            //第二步检查该选中身份 是否在身份列表中
                            JSONObject roleJson = new JSONObject(SharedPreferencesTools.getRole(getContext()));
                            int role_i = 0;
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (roleJson.getString("id").equals(jsonObject.getString("id"))) {
                                    role_i++;
                                }
                            }

                            if (role_i < 1) {
                                //该身份在身份列表中没有重新给用户赋值 一个新的身份
                                SharedPreferencesTools.saveRole(getContext(), jsonArray.getJSONObject(0).toString());
                            }
                        } else {
                            //位选中身份 已经登录 默认给用户选中第一个身份
                            SharedPreferencesTools.saveRole(getContext(), jsonArray.getJSONObject(0).toString());
                        }
                        JSONObject roleJson_select = new JSONObject(SharedPreferencesTools.getRole(getContext()));
                        identity.setText(roleJson_select.getString("name"));
                        if (SharedPreferencesTools.getGp_type(getContext()).equals("3")) {
                            identity_layout.setVisibility(View.GONE);
                        } else {
                            identity_layout.setVisibility(View.VISIBLE);
                        }
                        checkRedDot();
                    } else {
                        SharedPreferencesTools.saveUid(getContext(), "");
                        SharedPreferencesTools.saveUserName(getContext(), "");
                        SharedPreferencesTools.savePassword(getContext(), "");
                        SharedPreferencesTools.saveVipFlag(getContext(), 0);
                        SharedPreferencesTools.saveIntegral(getContext(), "0");
                        Toast.makeText(getContext(), "身份获取失败，请重新登录！", Toast.LENGTH_SHORT).show();
                        SharedPreferencesTools.getUid(getContext());
                    }
                } else {
                    identity_layout.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        //判断是否登录
        if (SharedPreferencesTools.getUids(getActivity()) == null) {
            intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("source", "my");
            startActivity(intent);
            return;
        }
        switch (view.getId()) {
            case R.id.layout_haslogin://我的资料
                //判断是否登录


                //首先判断是否是医考用户，如果是再判断身份（规培生或员工），如果不是，则跳转原来的我的资料
                if (SharedPreferencesTools.getIsdocexam(getActivity())) {
                    //用户身份类型  3医考  1规培生 2医院正式员工
//                    Log.e("MyFragment2", "onClick: 用户身份类型："+SharedPreferencesTools.getGp_type(getActivity()));
                    if (SharedPreferencesTools.getGp_type(getActivity()).equals("1") || SharedPreferencesTools.getGp_type(getActivity()).equals("2")) {
                        intent = new Intent(getActivity(), GpPersonalInformationActivity.class);
                    } else {
                        intent = new Intent(getActivity(), MyProfile.class);
                        intent.putExtra("source", "my");
                        intent.putExtra("Str_hyleibie", Str_hyleibie);
                        intent.putExtra("Str_cityname", Str_cityname);
                        intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                        intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                        intent.putExtra("Str_zhicheng", Str_zhicheng);
                        intent.putExtra("Str_truename", Str_truename);
                        intent.putExtra("Str_sexName", Str_sexName);
                        intent.putExtra("Str_sex", Str_sex);
                        intent.putExtra("Str_cityid", Str_cityid);
                        intent.putExtra("Str_address", Str_address);
                        intent.putExtra("Str_idcard", Str_idcard);
                        intent.putExtra("Str_danwei", Str_danwei);
                    }
                } else {
                    intent = new Intent(getActivity(), MyProfile.class);
                    intent.putExtra("source", "my");
                    intent.putExtra("Str_hyleibie", Str_hyleibie);
                    intent.putExtra("Str_cityname", Str_cityname);
                    intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                    intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                    intent.putExtra("Str_zhicheng", Str_zhicheng);
                    intent.putExtra("Str_truename", Str_truename);
                    intent.putExtra("Str_sexName", Str_sexName);
                    intent.putExtra("Str_sex", Str_sex);
                    intent.putExtra("Str_cityid", Str_cityid);
                    intent.putExtra("Str_address", Str_address);
                    intent.putExtra("Str_idcard", Str_idcard);
                    intent.putExtra("Str_danwei", Str_danwei);
                }
                break;
            case R.id.my_myhead://头像
//                intent = new Intent(getActivity(), GpPersonalInformationActivity.class);
                if (Str_icon == null) {
                    Toast.makeText(getActivity(), R.string.post_hint3, Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(getActivity(), HeadLargeImgActivity.class);
                    intent.putExtra("images", Str_icon);
                }
                break;
            case R.id.layout_message://消息
//                intent = new Intent(getActivity(), MyMessageActivity.class);
//                intent.putExtra("Str_username", Str_username);  //传过去作为发件人用户名
                intent = new Intent(getActivity(), Message_management.class);
                break;
            case R.id.myfragment_sign_in:
                chcheckIntegration();
                break;
            case R.id.layout_follow:
                intent = new Intent(getActivity(), MyFollowActivity.class);
                intent.putExtra("Str_username", Str_username);  //关注
                break;
            case R.id.layout_cashier:
                intent = new Intent(getActivity(), CashierActivity2.class);
                intent.putExtra("Str_personalmoney", Str_personalmoney);
                intent.putExtra("Str_username", Str_username);  //钱包
                intent.putExtra("Str_countmoney", Str_countmoney);
                intent.putExtra("Str_countpersonalmoney", Str_countpersonalmoney);
                break;
            case R.id.layout_workmate://我的同事
                intent = new Intent(getActivity(), MyWorkmateActivity.class);
                intent.putExtra("Str_username", Str_username);  //传过去作为发件人用户名
                break;
            case R.id.layout_openmenber:
                intent = new Intent(getActivity(), MyOpenMenberActivity.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                break;
            case R.id.layout_mycollection:                       // 我的收藏
//                intent = new Intent(getActivity(), MyCollectionActivity.class);
                intent = new Intent(getActivity(), MyCollectionActivity2.class);
                break;
            case R.id.invitation_friend:                        //积分任务
                intent = new Intent(getActivity(), MyIntegralTaskActivity.class);
                intent.putExtra("Str_hyleibie", Str_hyleibie);
                intent.putExtra("Str_cityname", Str_cityname);
                intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                intent.putExtra("Str_zhicheng", Str_zhicheng);
                intent.putExtra("Str_truename", Str_truename);
                intent.putExtra("Str_sexName", Str_sexName);
                intent.putExtra("Str_sex", Str_sex);
                intent.putExtra("Str_cityid", Str_cityid);
                intent.putExtra("Str_address", Str_address);
                intent.putExtra("Str_idcard", Str_idcard);
                intent.putExtra("Str_danwei", Str_danwei);
                break;
            case R.id.layout_yszzrz:                                             // 医生执照认证
                if (Str_idcard_yz == 1) {
                    intent = new Intent(getActivity(), HasZzrzActivity.class);   //审核通过
                    intent.putExtra("Str_idcard_imgurl", Str_idcard_imgurl);
                } else if (Str_idcard_yz == -1) {
                    intent = new Intent(getActivity(), HasZzrzActivity.class);   //等待审核
                    intent.putExtra("Str_idcard_imgurl", Str_idcard_imgurl);
                } else if (Str_idcard_yz == 0) {
                    if (Str_idcard_yz_reason == null || "".equals(Str_idcard_yz_reason)) {
                        intent = new Intent(getActivity(), MyYszzrzActivity.class);     //未提交或者审核失败
                    } else {
                        intent = new Intent(getActivity(), HasZzrzActivity.class);
                        intent.putExtra("Str_idcard_imgurl", Str_idcard_imgurl);
                        intent.putExtra("Str_idcard_yz_reason", Str_idcard_yz_reason);
                    }
                }
                intent.putExtra("Str_idcard_yz", Str_idcard_yz);
                break;
            case R.id.layout_Recent_Browse:                                              //最近浏览（历史）
                intent = new Intent(getActivity(), StudyRecordListActivity.class);
                intent.putExtra("type", "video");
                break;
            case R.id.layout_wdxz:                                                      //我的下载
                intent = new Intent(getActivity(), DownloadFinishActivity.class);
                intent.putExtra("type", "home");
                break;
            case R.id.layout_setting:                                                   //设置
                intent = new Intent(getActivity(), MySettingActivity.class);
                intent.putExtra("Str_IsYZ", Str_IsYZ + "");
                intent.putExtra("Str_phonenum", Str_phonenum);
                intent.putExtra("source", "my");
                intent.putExtra("Str_hyleibie", Str_hyleibie);
                intent.putExtra("Str_cityname", Str_cityname);
                intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                intent.putExtra("Str_zhicheng", Str_zhicheng);
                intent.putExtra("Str_truename", Str_truename);
                intent.putExtra("Str_sexName", Str_sexName);
                intent.putExtra("Str_sex", Str_sex);
                intent.putExtra("Str_cityid", Str_cityid);
                intent.putExtra("Str_address", Str_address);
                intent.putExtra("Str_idcard", Str_idcard);
                intent.putExtra("Str_danwei", Str_danwei);
                break;
            case R.id.layout_yjfk: // 意见反馈
                if (SharedPreferencesTools.getUid(getContext()).length() > 0) {
                    intent = new Intent(getActivity(), Feedback_Main.class);
                }
                break;
            case R.id.integralMall:                                                      //积分商城
                intent = new Intent(getActivity(), IntegralMall.class);
                break;
            case R.id.integralMall2:                                                      //积分攻略
                //intent = new Intent(getActivity(), IntegralMall.class);
                intent = new Intent(getActivity(), MyIntegralTaskActivity.class);
                intent.putExtra("Str_hyleibie", Str_hyleibie);
                intent.putExtra("Str_cityname", Str_cityname);
                intent.putExtra("Str_bigkeshi", Str_bigkeshi);
                intent.putExtra("Str_smallkeshi", Str_smallkeshi);
                intent.putExtra("Str_zhicheng", Str_zhicheng);
                intent.putExtra("Str_truename", Str_truename);
                intent.putExtra("Str_sexName", Str_sexName);
                intent.putExtra("Str_sex", Str_sex);
                intent.putExtra("Str_cityid", Str_cityid);
                intent.putExtra("Str_address", Str_address);
                intent.putExtra("Str_idcard", Str_idcard);
                intent.putExtra("Str_danwei", Str_danwei);
                break;
            case R.id.clinical_exercise:                                                      //运动
                intent = new Intent(getActivity(), Clinical_exercise.class);
                break;
            case R.id.reading_e_books:                                                      //阅读
                intent = new Intent(getActivity(), Video_book_Main.class);
                break;
            case R.id.user_statistics:                                                      //记录
                intent = new Intent(getActivity(), UserStatistics2Activity.class);
                break;
            case R.id.myfragment_layout_medal:                                                      //勋章
                intent = new Intent(getActivity(), MedalListActivity.class);
                break;
          /*  case R.id.my_fragment_official_name:                                                      //成长
                intent = new Intent(getActivity(), GrowthValueActivity.class);
                break;*/
            case R.id.to_switch_identities: //身份切换
                // 弹出自定义dialog
                LayoutInflater inflater = LayoutInflater.from(getContext());
                viewLayout = inflater.inflate(R.layout.dialog_item13, null);

                // 对话框
                dialog = new Dialog(getContext());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
                // 设置宽度为屏幕的宽度
//                WindowManager windowManager = getContext().getWindowManager();
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() - 100); // 设置宽度
                dialog.getWindow().setAttributes(lp);
                dialog.getWindow().setContentView(viewLayout);
                dialog.setCancelable(true);
                final ListView role_list = (ListView) viewLayout.findViewById(R.id.role_list);//
                final TextView i_understand = (TextView) viewLayout.findViewById(R.id.i_understand);//
                final TextView _item_content = (TextView) viewLayout.findViewById(R.id._item_content);//
                _item_content.setText("选择身份");
                data2.clear();
                select_position = -1;
                try {
                    JSONArray jsonArray = new JSONArray(SharedPreferencesTools.getRoleList(getContext()));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", jsonObject.getString("id"));
                        map.put("name", jsonObject.getString("name"));
                        data2.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //设置TabLayout点击事件
                baseListAdapter2 = new BaseListAdapter(role_list, data2, R.layout.item_role_list) {

                    @Override
                    public void refresh(Collection datas) {
                        super.refresh(datas);
                    }

                    @Override
                    public void convert(ListHolder helper, Object item, boolean isScrolling) {
                        super.convert(helper, item, isScrolling);
                        Map<String, Object> map = (Map) item;
                        helper.setText(R.id._item_text, map.get("name").toString());

                        if (select_position > -1 && data2.get(select_position).get("id").toString().equals(map.get("id").toString())) {
                            helper.setViewVisibility(R.id._item_check_img, View.VISIBLE);
                            helper.setTextColor(R.id._item_text, R.color.login_button);
                        } else {
                            helper.setViewVisibility(R.id._item_check_img, View.GONE);
                            helper.setTextColor2(R.id._item_text, R.color.videotree_text_bg);

                        }

                    }
                };
                role_list.setAdapter(baseListAdapter2);

                role_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        select_position = position;
                        baseListAdapter2.notifyDataSetChanged();
                    }
                });


                i_understand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //判断是否选中身份
                            if (select_position > -1) {
                                JSONArray jsonArray = new JSONArray(SharedPreferencesTools.getRoleList(getContext()));
                                //设置选中的身份
                                SharedPreferencesTools.saveRole(getContext(), jsonArray.getJSONObject(select_position).toString());
                                identity.setText(jsonArray.getJSONObject(select_position).getString("name"));
                                dialog.dismiss();
                                dialog = null;
                                viewLayout = null;
                                checkRedDot2();

                            } else {
                                Toast.makeText(getActivity(), "请选择身份！", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                break;
            default:
                break;
        }
        if (intent != null && checkDoubleClick() == false) {
            startActivity(intent);
        }
    }

    //获取节日图片
    public void getLoginImgUrl() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activityimg);
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.CCMTVAPP, obj.toString());
//                    Log.e("headresult",result);
                    Message message = new Message();
                    message.what = 6;
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

    //消息数
    public void allNoReadCount() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allNoReadCount);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                    String result = HttpClientUtils.sendPostToGP(getContext(), URLConfig.CCMTVAPP_GpApi, obj.toString());
//                    Log.e("消息数", result);
                    Message message = new Message();
                    message.what = 8;
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

    private class DownloadImageTask extends AsyncTask<String, Void, Drawable> {

        protected Drawable doInBackground(String... urls) {
            return loadImageFromNetwork(urls[0]);
        }

        protected void onPostExecute(Drawable result) {
            iv_top.setBackgroundDrawable(result);
        }
    }

    private Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过第二个参数(文件名)来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), null);
        } catch (IOException e) {
//            Log.d("skythinking", e.getMessage());
        }
        if (drawable == null) {
//            Log.d("skythinking", "null drawable");
        } else {
//            Log.d("skythinking", "not null drawable");
        }

        return drawable;
    }

}

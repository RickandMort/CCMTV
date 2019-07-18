package com.linlic.ccmtv.yx.activity.my;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.cashier.CashierActivity;
import com.linlic.ccmtv.yx.activity.home.Recent_Browse_File;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.my.download.Download;
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.CircleImageView;

import org.json.JSONObject;

import java.util.Map;

public class MyFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout my_mydata;//我的资料
    private CircleImageView my_myhead;//头像
    private LinearLayout layout_message, layout_follow, layout_workmate,
            layout_openmenber, layout_mycollection, layout_phonerj,
            layout_yszzrz, layout_invitation_friend, layout_Recent_Browse,
            layout_my_Integral, layout_my_changepass, layout_setting, layout_wdxz, layout_cashier;
    Map<String, Object> map;
    private TextView big_keshi, small_keshi, tv_hosname, my_name;
    private int Str_IsYZ, Str_vipflg, Str_idcard_yz;
    Button btn_jifen;
    String Str_icon, Str_username, Str_idcard_yz_reason, Str_idcard_imgurl, Str_cityid, Str_hyleibie, Str_cityname, Str_phonenum, Str_bigkeshi, Str_personalmoney, Str_smallkeshi, Str_zhicheng, Str_truename, Str_sexName, Str_address, Str_idcard, Str_sex, Str_danwei;
    ImageView iv_isvip, iv_red_dians, iv_top;
    private LinearLayout layout_haslogin, layout_nologin;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
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
                            my_name.setText(Str_username);
                            btn_jifen.setText("积分:" + String.valueOf(object.getInt("money")));
                            big_keshi.setText(Str_bigkeshi);
                            small_keshi.setText(Str_smallkeshi);
                            if (Str_vipflg == 1) {
                                iv_isvip.setVisibility(View.VISIBLE);
                            } else {
                                iv_isvip.setVisibility(View.INVISIBLE);
                            }
                            if ((object.getString("ifnew").toString()).equals("1")) {
                                iv_red_dians.setVisibility(View.VISIBLE);
                            } else {
                                iv_red_dians.setVisibility(View.INVISIBLE);
                            }
                            SharedPreferencesTools.saveVipFlag(getActivity(), Str_vipflg);
                            Str_IsYZ = object.getInt("mob_yz");
                            Str_icon = FirstLetter.getSpells(object.getString("icon"));
                            SharedPreferencesTools.saveVipEndTime(getActivity(), object.getString("vip_end_time"));
                            SharedPreferencesTools.saveVipFlag(getActivity(), Str_vipflg);
                            SharedPreferencesTools.saveStricon(getActivity(), Str_icon);
                            SharedPreferencesTools.savePersonalmoney(getActivity(), Str_personalmoney);
                            SharedPreferencesTools.saveStrKeShi(getActivity(), Str_bigkeshi, Str_smallkeshi);

                            //显示头像
                            new Carousel_figure(getActivity()).loadImageNoCache(Str_icon, my_myhead);  //无缓存
                            //new Carousel_figure(getActivity()).loadImageNoCache(SharedPreferencesTools.getmemberUserDataPath(getActivity()), iv_top);  //无缓存
                            RequestOptions options = new RequestOptions()
                                    .placeholder(R.mipmap.img_default)
                                    .error(R.mipmap.img_default);
                            Glide.with(getActivity())
                                    .load(FirstLetter.getSpells(SharedPreferencesTools.getmemberUserDataPath(getActivity())))
                                    .apply(options)
                                    .into(iv_top);
                            JSONObject obj = object.getJSONObject("hosName");
                            tv_hosname.setText(obj.getString("name"));
                            Str_cityname = obj.getString("name");                //医院
                            Str_cityid = obj.getString("id");                   //医院代号
                            SharedPreferencesTools.saveStrHos(getActivity(), obj.getString("name"));
                            Str_danwei = object.getString("danwei");
                        } else {//失败
                            my_name.setText(SharedPreferencesTools.getUserName(getActivity()));
                            big_keshi.setText(SharedPreferencesTools.getStrBigKeShi(getActivity()));
                            small_keshi.setText(SharedPreferencesTools.getStrSmallKeShi(getActivity()));
                            tv_hosname.setText(SharedPreferencesTools.getStrhos(getActivity()));
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViewById();
        setAllonClick();
        //initdata();
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Glide.with(getActivity())
                .load(FirstLetter.getSpells(SharedPreferencesTools.getmemberUserDataPath(getActivity())))
                .apply(options)
                .into(iv_top);
    }


    private void findViewById() {
        my_mydata = (LinearLayout) getActivity().findViewById(R.id.my_mydata);
        my_myhead = (CircleImageView) getActivity().findViewById(R.id.my_myhead);
        layout_message = (LinearLayout) getActivity().findViewById(R.id.layout_message);
        layout_follow = (LinearLayout) getActivity().findViewById(R.id.layout_follow);
        layout_cashier = (LinearLayout) getActivity().findViewById(R.id.layout_cashier);
        layout_workmate = (LinearLayout) getActivity().findViewById(R.id.layout_workmate);
        layout_openmenber = (LinearLayout) getActivity().findViewById(R.id.layout_openmenber);
        layout_mycollection = (LinearLayout) getActivity().findViewById(
                R.id.layout_mycollection);
        layout_phonerj = (LinearLayout) getActivity().findViewById(
                R.id.layout_phonerj);
        layout_invitation_friend = (LinearLayout) getActivity().findViewById(R.id.invitation_friend);
        layout_yszzrz = (LinearLayout) getActivity().findViewById(
                R.id.layout_yszzrz);
        layout_Recent_Browse = (LinearLayout) getActivity().findViewById(R.id.layout_Recent_Browse);
        layout_my_Integral = (LinearLayout) getActivity().findViewById(R.id.my_Integral);
        my_name = (TextView) getActivity().findViewById(R.id.my_name);
        big_keshi = (TextView) getActivity().findViewById(R.id.big_keshi);
        small_keshi = (TextView) getActivity().findViewById(R.id.small_keshi);
        tv_hosname = (TextView) getActivity().findViewById(R.id.tv_his_hosp);
        btn_jifen = (Button) getActivity().findViewById(R.id.btn_jifen);
        iv_isvip = (ImageView) getActivity().findViewById(R.id.iv_isvip);
        iv_red_dians = (ImageView) getActivity().findViewById(R.id.iv_red_dians);
        layout_my_changepass = (LinearLayout) getActivity().findViewById(R.id.layout_my_changepass);
        layout_setting = (LinearLayout) getActivity().findViewById(R.id.layout_setting);
        layout_wdxz = (LinearLayout) getActivity().findViewById(R.id.layout_wdxz);
        layout_haslogin = (LinearLayout) getActivity().findViewById(R.id.layout_haslogin);
        layout_nologin = (LinearLayout) getActivity().findViewById(R.id.layout_nologin);
        iv_top = (ImageView) getActivity().findViewById(R.id.iv_top);
    }

    private void setAllonClick() {
        my_mydata.setOnClickListener(this);
        my_myhead.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        layout_follow.setOnClickListener(this);
        layout_cashier.setOnClickListener(this);
        layout_workmate.setOnClickListener(this);
        layout_openmenber.setOnClickListener(this);
        layout_mycollection.setOnClickListener(this);
        layout_phonerj.setOnClickListener(this);
        layout_invitation_friend.setOnClickListener(this);
        layout_yszzrz.setOnClickListener(this);
        layout_Recent_Browse.setOnClickListener(this);
        layout_my_Integral.setOnClickListener(this);
        layout_my_changepass.setOnClickListener(this);
        layout_setting.setOnClickListener(this);
        layout_wdxz.setOnClickListener(this);
        layout_nologin.setOnClickListener(this);
    }

    //获取用户信息
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initdata() {
        final String uid = SharedPreferencesTools.getUids(getActivity());
        if (uid == null || ("").equals(uid)) {
            btn_jifen.setText("积分:" + 0);
            //Drawable drawable = ContextCompat.getDrawable(getActivity(),R.mipmap.app_icon);
            Drawable drawable = ContextCompat.getDrawable(getActivity(), R.mipmap.app_icon);
            my_myhead.setImageDrawable(drawable);
            // my_myhead.setImageDrawable(getResources().getDrawable(R.mipmap.menu_icon2)) ;
            // my_myhead.setImageDrawable(getActivity().getDrawable(R.mipmap.app_icon));
            layout_nologin.setVisibility(View.VISIBLE);
            layout_haslogin.setVisibility(View.GONE);
            return;
        } else {
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
        }
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
            case R.id.my_mydata://我的资料
                //判断是否登录
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
                break;
            case R.id.my_myhead://头像
                if (Str_icon == null) {
                    Toast.makeText(getActivity(), R.string.post_hint3, Toast.LENGTH_LONG).show();
                } else {
                    //intent = new Intent(getActivity(), ShowBigImageActivity.class);
                    intent = new Intent(getActivity(), HeadLargeImgActivity.class);
                    intent.putExtra("images", Str_icon);
                }
                break;
            case R.id.layout_message:
                intent = new Intent(getActivity(), MyMessageActivity.class);
                intent.putExtra("Str_username", Str_username);  //传过去作为发件人用户名
                break;
            case R.id.layout_follow:
                intent = new Intent(getActivity(), MyFollowActivity.class);
                intent.putExtra("Str_username", Str_username);  //传过去作为发件人用户名
                break;
            case R.id.layout_cashier:
                intent = new Intent(getActivity(), CashierActivity.class);
                intent.putExtra("Str_personalmoney", Str_personalmoney);
                break;
            case R.id.layout_workmate:
                intent = new Intent(getActivity(), MyWorkmateActivity.class);
                intent.putExtra("Str_username", Str_username);  //传过去作为发件人用户名
                break;
            case R.id.layout_openmenber:
                intent = new Intent(getActivity(), MyOpenMenberActivity.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                break;
            case R.id.layout_mycollection:                       // 我的收藏
                intent = new Intent(getActivity(), MyCollectionActivity.class);
                break;
            case R.id.invitation_friend:                        //积分任务
                //  intent = new Intent(getActivity(), MyInvitationFriend.class);
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
            case R.id.layout_phonerj:                           // 手机认证
                if (Str_IsYZ == 1) {
                    intent = new Intent(getActivity(), HasPhoneNumActivity.class);   //已认证
                    intent.putExtra("Str_phonenum", Str_phonenum);
                } else {
                    intent = new Intent(getActivity(), MyPhonerzActivity.class);      //未认证
                    intent.putExtra("Str_phonenum", Str_phonenum);
                }
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
            case R.id.layout_my_changepass:                                              // 修改密码
                intent = new Intent(getActivity(), MyChangePassActivity.class);
                break;
            case R.id.layout_Recent_Browse:                                              //最近浏览
                intent = new Intent(getActivity(), Recent_Browse_File.class);
                break;
            case R.id.layout_wdxz:                                                      //我的下载
                intent = new Intent(getActivity(), Download.class);
                break;
            case R.id.layout_setting:                                                   //设置
                intent = new Intent(getActivity(), MySettingActivity.class);
                break;
            case R.id.my_Integral:  //我的积分
                intent = new Intent(getActivity(), Integral.class);
                intent.putExtra("Str_vipflg", Str_vipflg);
                intent.putExtra("Str_icon", Str_icon);
                intent.putExtra("Str_username", Str_username);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

}

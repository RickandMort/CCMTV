package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * name：积分规则
 * author：Larry
 * data：2016/5/27.
 */
public class Integral_RuleActivity extends BaseActivity {
    Context context;
    TextView tv_regist_inte, tv_comp_info, tv_qiandao, tv_upvideo_inva, tv_upcase_inva, tv_upwenxian_inva, tv_upzixun_inva, tv_comment_inva;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {//成功
                            hideNoData();
                            JSONObject data = result.getJSONObject("data");
                            tv_regist_inte.setText("    • 新用户注册:注册新用户可获得" + data.get("j_register").toString() + "积分");
                            tv_comp_info.setText("    • 完善个人资料:用户完善个人资料可获得" + data.get("j_upload").toString() + "积分");
                            tv_qiandao.setText("    • 用户每日登录CCMTV-临床频道网站/app签到即可获得" + data.get("j_login") + "积分");
                            tv_comment_inva.setText("    • 每天参与视频评论，每条评论可获得" + data.get("j_comment") + "积分，每天最高" + data.get("j_comment_max").toString() + "分");
                            tv_upvideo_inva.setText("    • 上传视频，审核通过后，可获得" + data.get("j_uploadVideo") + "积分");
                            tv_upcase_inva.setText("    • 上传医学病例，审核通过后，可获得" + data.get("j_uploadCase") + "积分");
                            tv_upwenxian_inva.setText("    • 上传文献、PPT、审核通过后，可获得" + data.get("j_uploadPpt") + "积分");
                            tv_upzixun_inva.setText("    • 上传资讯，审核通过后，可获得" + data.get("j_uploadInformation") + "积分");
                        } else {//失败
                            showNoData();
                        }
                    } catch (Exception e) {
                        showNoData();
                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.integral_rule);
        context = this;
        findId();
        initData();
        setText();
    }

    public void findId() {
        super.findId();
        tv_regist_inte = (TextView) findViewById(R.id.tv_regist_inte);
        tv_comp_info = (TextView) findViewById(R.id.tv_comp_info);
        tv_upvideo_inva = (TextView) findViewById(R.id.tv_upvideo_inva);
        tv_upcase_inva = (TextView) findViewById(R.id.tv_upcase_inva);
        tv_upwenxian_inva = (TextView) findViewById(R.id.tv_upwenxian_inva);
        tv_upzixun_inva = (TextView) findViewById(R.id.tv_upzixun_inva);
        tv_comment_inva = (TextView) findViewById(R.id.tv_comment_inva);
        tv_qiandao = (TextView) findViewById(R.id.tv_qiandao);
    }

    private void setText() {
        super.setActivity_title_name(getResources().getString(R.string.interule));
        // TextView tv_regist_inte, tv_comp_info,tv_upvideo_inva,tv_upcase_inva,tv_upwenxian_inva,tv_upzixun_inva;
    }

    private void initData() {
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject object = new JSONObject();
                try {
                    object.put("act", URLConfig.integralRule);
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, object.toString());

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    public void tofriend(View v) {
        Intent intent = new Intent(Integral_RuleActivity.this, MyInvitationFriend.class);
        intent.putExtra("type", "home");
        startActivity(intent);
    }

    public void tobuyinte(View v) {
        startActivity(new Intent(Integral_RuleActivity.this, Get_Free_Integral.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}

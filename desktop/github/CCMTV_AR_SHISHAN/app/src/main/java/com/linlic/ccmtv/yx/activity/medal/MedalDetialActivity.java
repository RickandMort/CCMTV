package com.linlic.ccmtv.yx.activity.medal;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.home.util.Utils;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalDetialBean;
import com.linlic.ccmtv.yx.activity.medal.bean.MedalShareBean;
import com.linlic.ccmtv.yx.activity.medal.utils.MyHandler;
import com.linlic.ccmtv.yx.activity.medal.utils.Rotatable;
import com.linlic.ccmtv.yx.activity.medal.view.MedalDetialTitleImageView;
import com.linlic.ccmtv.yx.activity.medal.view.MedalDetialView;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONException;
import org.json.JSONObject;

public class MedalDetialActivity extends BaseActivity implements View.OnClickListener {
    public static final String DETIAL_KEY = "medalDetialActivity_detial_key";

    private MedalDetialView medal_detial_view;
    private View parent;
    private JSONObject object;
    private FrameLayout medal_detial_content;
    private MedalDetialBean detialBean;
    private MedalDetialTitleImageView icon_upper, icon_lower;

    private MyHandler handler = new MyHandler(this){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    //勋章分享链接
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if ("1".equals(jsonObject.getString("status"))) {
                            Gson gson = new Gson();
                            JSONObject data = jsonObject.getJSONObject("data");
                            MedalShareBean medalShareBean = gson.fromJson(data.toString(), MedalShareBean.class);
                            medal_detial_view.setShareBean(medalShareBean);
                        } else {
                            Utils.showToast(MedalDetialActivity.this, jsonObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(MedalDetialActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detialBean = (MedalDetialBean) getIntent().getSerializableExtra(DETIAL_KEY);
        setContentView(R.layout.activity_medal_detial);

        medal_detial_view = (MedalDetialView) findViewById(R.id.medal_detial_view);
        parent = findViewById(R.id.parent);
        medal_detial_content = (FrameLayout) findViewById(R.id.medal_detial_content);
        icon_upper = findViewById(R.id.medal_title_icon_upper);
        icon_lower = findViewById(R.id.medal_title_icon_lower);
        //reward_status 0 为 该勋章未完成  reward_status 1 为 该勋章奖励已领取 reward_status 2 为 该勋章第一次完成领取奖励
        if ("2".equals(detialBean.getData().getReward_status())) {
            icon_upper.setVisibility(View.VISIBLE);
            icon_lower.setVisibility(View.VISIBLE);
        }
        setCameraDistance();
        medal_detial_view.setMedalDetial(detialBean);
        cardTurnover();
        medal_detial_view.setOnClickListener(this);
        parent.setOnClickListener(this);
        getMedalShareUrl(detialBean.getData().getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
//        medal_detial_view.setMedalDetial(new MedalDetialBean());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void cardTurnover() {
//            ViewHelper.setRotationY(imageViewFront, 180f);//先翻转180，转回来时就不是反转的了
        final Rotatable rotatable = new Rotatable.Builder(parent)
                .sides(R.id.medal_detial_content, R.id.medal_detial_content)
                .direction(Rotatable.ROTATE_Y)
                .build();
        rotatable.setTouchEnable(false);
        rotatable.rotate(Rotatable.ROTATE_Y, 180 * 2, 2000);
        scaleAnimationTest();
    }

    private void scaleAnimationTest() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000); //动画持续时间
        scaleAnimation.setInterpolator(new AccelerateInterpolator()); //设置为加速
        scaleAnimation.setFillAfter(true); //结束动画之后，是否停留在最后
        parent.setAnimation(scaleAnimation);
        scaleAnimation.start();
    }

    /**
     * 改变视角距离, 贴近屏幕
     */
    private void setCameraDistance() {
        int distance = 10000;
        float scale = getResources().getDisplayMetrics().density * distance;
        parent.setCameraDistance(scale);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.parent:
                finish();
                break;
            default:
                break;
        }
    }

    /***
     * 获取勋章分享链接
     */
    private void getMedalShareUrl(final String id){
        final String uid = SharedPreferencesTools.getUid(this);
        if (TextUtils.isEmpty(uid)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.shareMadal);
                    obj.put("uid", uid);
                    obj.put("id", id);
                    String result = HttpClientUtils.sendPostToGP(MedalDetialActivity.this, URLConfig.CCMTVAPP, obj.toString());
                    Message message = new Message();
                    message.what = 0;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }
}

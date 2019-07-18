package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.Carousel_figure;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

/**
 * name：医生执照认证
 * author：Larry
 * data：2016/4/27 14:24
 */
public class HasZzrzActivity extends BaseActivity {
    Context context;
    TextView activity_title_name, tv_issuccess, tv_idcard_yz_reason;
    int Str_idcard_yz;
    String Str_idcard_yz_reason, Str_idcard_imgurl;
    LinearLayout layout_renzheng, layout_renzheng_falture;
    ImageView iv_my_yszz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_has_zzrz);
        context = this;

        activity_title_name = (TextView) findViewById(R.id.activity_title_name);
        tv_issuccess = (TextView) findViewById(R.id.tv_issuccess);
        tv_idcard_yz_reason = (TextView) findViewById(R.id.tv_idcard_yz_reason);
        layout_renzheng = (LinearLayout) findViewById(R.id.layout_renzheng);
        iv_my_yszz = (ImageView) findViewById(R.id.iv_my_yszz);
        layout_renzheng_falture = (LinearLayout) findViewById(R.id.layout_renzheng_falture);
        activity_title_name.setText("医生执照认证");
        Str_idcard_yz = getIntent().getIntExtra("Str_idcard_yz", 0);
        Str_idcard_yz_reason = getIntent().getStringExtra("Str_idcard_yz_reason");
        Str_idcard_imgurl = getIntent().getStringExtra("Str_idcard_imgurl");
        if (Str_idcard_yz == 1) {
            tv_issuccess.setText("已经通过认证");
            layout_renzheng_falture.setVisibility(View.GONE);
            // loadImg(iv_my_yszz, Str_idcard_imgurl);
            new Carousel_figure(getApplicationContext()).loadImageNoCache(Str_idcard_imgurl, iv_my_yszz);  //无缓存
        } else if (Str_idcard_yz == -1) {
            tv_issuccess.setText("正在审核中");
            layout_renzheng_falture.setVisibility(View.GONE);
            // loadImg(iv_my_yszz,Str_idcard_imgurl);
            new Carousel_figure(getApplicationContext()).loadImageNoCache(Str_idcard_imgurl, iv_my_yszz);  //无缓存
        } else if (Str_idcard_yz == 0) {
            tv_issuccess.setText("审核失败");
            tv_idcard_yz_reason.setText(Str_idcard_yz_reason);
            layout_renzheng.setVisibility(View.GONE);
        }
    }

    //重新上传
    public void to_again_upload(View view) {
        Intent intent = new Intent(HasZzrzActivity.this, MyYszzrzActivity.class);
        startActivity(intent);
        HasZzrzActivity.this.finish();
    }

    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(getApplicationContext());
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
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

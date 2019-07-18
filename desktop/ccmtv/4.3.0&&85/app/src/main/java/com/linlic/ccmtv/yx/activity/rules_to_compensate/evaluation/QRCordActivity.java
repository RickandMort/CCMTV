package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.QRCodeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2019/1/18.
 */

public class QRCordActivity extends BaseActivity {
    @Bind(R.id.code_img)
    ImageView code_img;//二维码展示区域

    private Context context;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qrcord);
        context = this;
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
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

    private void initViews(){
        Bitmap mBitmap = QRCodeUtil.createQRImage(url, 680, 680);
        code_img.setImageBitmap(mBitmap);
    }
}

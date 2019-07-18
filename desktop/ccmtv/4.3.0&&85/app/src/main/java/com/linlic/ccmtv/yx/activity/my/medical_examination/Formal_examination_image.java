package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.widget.MyTouchImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2017/9/21.
 */
public class Formal_examination_image extends BaseActivity {
//    private MatrixImageView matrixImageView;
private Context context;
    private MyTouchImageView matrixImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.formal_examination_image);
        matrixImageView =(MyTouchImageView) findViewById(R.id.matrixImageView);
       ImageLoader.getInstance().displayImage(FirstLetter.getSpells(getIntent().getStringExtra("thumbnail")), matrixImageView);
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

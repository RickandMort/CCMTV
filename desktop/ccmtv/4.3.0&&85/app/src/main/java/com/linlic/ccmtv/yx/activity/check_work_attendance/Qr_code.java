package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tom on 2018/12/11.
 */

public class Qr_code extends BaseActivity{

    private Context context;

    @Bind(R.id.qr_img)
    ImageView qr_img;
    @Bind(R.id.qr_msg)
    TextView qr_msg;
    @Bind(R.id.add_layout)
    LinearLayout add_layout;

    private BaseListAdapter baseListAdapterVideo;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_code_result);
        context = this;
        ButterKnife.bind(this);
        findId();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "https://yun.ccmtv.cn/admin.php/wx/SignIn";
        super.onPause();
    }

    public void initView() {
        LogUtil.e("扫描结果",getIntent().getStringExtra("status"));
        if(getIntent().getStringExtra("status").equals("1")){
            qr_img.setImageResource(R.mipmap.qr_code_o);
            add_layout.setVisibility(View.VISIBLE);
        }else{
            add_layout.setVisibility(View.GONE);
            qr_img.setImageResource(R.mipmap.qr_code_x);
        }
        qr_msg.setText(getIntent().getStringExtra("statusMsg"));

    }

    public void  qr_list(View view){

    }
}

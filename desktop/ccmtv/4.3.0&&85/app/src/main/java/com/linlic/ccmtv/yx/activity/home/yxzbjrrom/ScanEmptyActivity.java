package com.linlic.ccmtv.yx.activity.home.yxzbjrrom;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanEmptyActivity extends BaseActivity {

    @Bind(R.id.arrow_back)
    LinearLayout arrowBack;
    @Bind(R.id.tv_scan_msg)
    TextView tvScanMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_empty);
        ButterKnife.bind(this);
        tvScanMsg.setText(getIntent().getExtras().getString("token"));
    }

    @OnClick(R.id.arrow_back)
    public void onViewClicked() {
        finish();
    }
}

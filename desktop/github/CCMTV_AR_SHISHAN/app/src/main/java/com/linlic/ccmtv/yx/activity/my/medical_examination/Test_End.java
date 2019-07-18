package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

/**
 * Created by Administrator on 2017/10/25.
 */
public class Test_End extends BaseActivity {
    private Context context;
    private String tid;
    private TextView answer_yes_num,is_yes_no,task_qualified,user_qualified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.text_end);
        context = this;
        findId();
        initdata();

    }

    @Override
    public void findId() {
        super.findId();
        answer_yes_num = (TextView) findViewById(R.id.answer_yes_num);
        is_yes_no = (TextView) findViewById(R.id.is_yes_no);
        task_qualified = (TextView) findViewById(R.id.task_qualified);
        user_qualified = (TextView) findViewById(R.id.user_qualified);
    }

    public void initdata() {
        tid = getIntent().getStringExtra("tid");
        answer_yes_num.setText(getIntent().getStringExtra("answer_yes_num"));
        is_yes_no.setText(getIntent().getStringExtra("is_yes_no"));
        task_qualified.setText(getIntent().getStringExtra("task_qualified"));
        user_qualified.setText(getIntent().getStringExtra("user_qualified"));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        super.onPause();
    }
}

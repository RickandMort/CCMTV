package com.linlic.ccmtv.yx.kzbf.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.linlic.ccmtv.yx.R;

/**
 * Created by bentley on 2018/11/7.
 */

public class InvestigationSucessPop extends PopupWindow implements View.OnClickListener {
    private int mWidth;
    private int mHeight;
    private View mContentView;
    private ListView mListView;
    private final ImageView pp_research_close;
    private final ImageView pp_research_share;
    private final Button btn_pp_check_result;
    private final KzbfSubmitSucessImageView submit_sucess_sign;

    public InvestigationSucessPop(Context context) {
        super(context);
        calWidthAndHeight(context);
        setWidth(mWidth);
        setHeight(mHeight);
        mContentView = LayoutInflater.from(context).inflate(R.layout.popup_research_results, null);
        setContentView(mContentView);
        pp_research_close = mContentView.findViewById(R.id.pp_research_close);
        pp_research_close.setOnClickListener(this);
        pp_research_share = mContentView.findViewById(R.id.pp_research_share);
        btn_pp_check_result = mContentView.findViewById(R.id.btn_pp_check_result);
        submit_sucess_sign = mContentView.findViewById(R.id.kiv_subnit_sucess);
        setTouchable(true);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //点击PopupWindow以外区域时PopupWindow消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                }
                return false;
            }
        });
        initEvent();
    }


    private void initEvent() {

    }

    /**
     * 设置PopupWindow的大小
     *
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pp_research_close:
                dismiss();
                break;
            case R.id.pp_research_share:
                break;
            default:
                break;
        }
    }

    public void setOnShareClickListener(View.OnClickListener onClickListener) {
        pp_research_share.setOnClickListener(onClickListener);
    }

    public void setOnCheckMoreResult(View.OnClickListener onClickListener) {
        btn_pp_check_result.setOnClickListener(onClickListener);
    }

    public void setSucessIntegralSign(String sign) {
        submit_sucess_sign.setSignText(sign);
    }
}
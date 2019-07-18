package com.linlic.ccmtv.yx.activity.home.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;

import cn.cc.android.sdk.util.Logger;

public class TitleFragment extends BaseFragment {
    private static final int TYPE_DISPLAY = 0;
    private static final int TYPE_EDIT = 1;

    private ImageView mEditBtn;
    private ImageView mCancelBtn;
    private ImageView mCompleteBtn;
    private TitleFragmentCallback mCallback;
    private static final String LOG_TAG = "TitleFragment";

    public TitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        Logger.i(LOG_TAG, "onAttach");
        try {
            mCallback = (TitleFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnEditBtnClickedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Logger.i(LOG_TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        Logger.i(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.header_layout, container, false);


        return rootView;
    }



    private void refreshView(int type) {
//        Logger.i(LOG_TAG, "refreshView type=" + type);
        mEditBtn.setVisibility(TYPE_DISPLAY == type ? View.VISIBLE : View.GONE);
        mCancelBtn.setVisibility(TYPE_DISPLAY == type ? View.GONE : View.VISIBLE);
        mCompleteBtn.setVisibility(TYPE_DISPLAY == type ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        Logger.i(LOG_TAG, "onCreateView");
        mListener = null;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.video_download_img:
                    onDownloadBtnClick();
                    break;

            }
        }
    };



    public void onDownloadBtnClick() {
        if (mCallback != null) {
            mCallback.onDownloadBtnClick();
        }
    }



    public interface TitleFragmentCallback {


        public void onDownloadBtnClick();
    }


}

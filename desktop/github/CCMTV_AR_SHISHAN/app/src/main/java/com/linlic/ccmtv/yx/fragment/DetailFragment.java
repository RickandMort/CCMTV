package com.linlic.ccmtv.yx.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.util.MyLogger;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import cn.cc.android.sdk.util.VideoData;

public class DetailFragment extends Fragment {
    private VideoData mVideoData;
    private Callback mCallback;
    private ImageView mActivatedView;
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();


    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Callback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DetailFragmentCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mVideoData = getArguments().getParcelable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            return rootView;
        } catch (Exception e) {
            MyLogger.w(LOG_TAG, "onActivityCreated Exception", e);
        }

        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();


    }




    public void setData(VideoData data) {
        mVideoData = data;
    }


    public interface Callback {
        public void onDownloadBtnClick();

        public void onActivatedStatusChanged(boolean status);
    }

}


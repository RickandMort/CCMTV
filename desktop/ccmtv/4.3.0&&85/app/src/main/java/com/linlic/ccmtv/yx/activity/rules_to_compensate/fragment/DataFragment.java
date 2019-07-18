package com.linlic.ccmtv.yx.activity.rules_to_compensate.fragment;

/**
 * Created by Administrator on 2018/6/19.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

/**
 * Created by Administrator on 2017/12/28.
 */

public class DataFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    private int mPage;
    private TextView textView;

    public static DataFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        DataFragment fragment = new DataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.layput_data_fragment, container, false);
        textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText("第"+mPage+"页");
        return rootView;
    }

    public void initData(){

    }
}
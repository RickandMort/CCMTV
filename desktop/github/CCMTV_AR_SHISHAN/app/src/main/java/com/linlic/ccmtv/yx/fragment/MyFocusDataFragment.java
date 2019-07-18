package com.linlic.ccmtv.yx.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyFocusDataFragment extends BaseFragment {


    @Bind(R.id.tv_nickname)
    TextView tvNickname;
    @Bind(R.id.tv_gender)
    TextView tvGender;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_zhi_name)
    TextView tvZhiName;
    @Bind(R.id.tv_hospital_name)
    TextView tvHospitalName;
    @Bind(R.id.tv_ke_shi)
    TextView tvKeShi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_focus_data, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle =this.getArguments();//得到从Activity传来的数据
        if(bundle!=null){
            tvNickname.setText(bundle.getString("nickname"));
            tvGender.setText(bundle.getString("sexName"));
            tvType.setText(bundle.getString("hyleibie"));
            tvZhiName.setText(bundle.getString("my_694"));
            tvHospitalName.setText(bundle.getString("name"));
            tvKeShi.setText(bundle.getString("keshilb"));
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

package com.linlic.ccmtv.yx.activity.my.personal_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;

/**
 * Created by Administrator on 2017/8/17.
 */
public class Personal_profile_attention extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_profile_attention, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findById();
        onclick();
    }




    public void findById() {
//        layou_toup = (LinearLayout) getActivity().findViewById(R.id.layou_toup);

    }


    public void onclick() {
    /*    //上传视频
        btn_toupcase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), Upload_case.class);
                startActivity(intent);
            }
        });


        tv_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_uploadhis.setVisibility(View.VISIBLE);
                layou_toup.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject();
                            object.put("uid", SharedPreferencesTools.getUidToLoginClose(getActivity()));
                            object.put("act", URLConfig.getUserUploadInfo);
                            String result = HttpClientUtils.sendPost(getActivity(),
                                    URLConfig.CCMTVAPP1, object.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
*/

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

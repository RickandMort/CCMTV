package com.linlic.ccmtv.yx.activity.upload;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.upload.entry.Upload;
import com.linlic.ccmtv.yx.activity.upload.new_upload.IsUpload3;
import com.linlic.ccmtv.yx.activity.upload.new_upload.Upload_case3;
import com.linlic.ccmtv.yx.activity.upload.new_upload.Upload_video3;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import java.util.List;

/**
 * name：上传主界面
 * author：MrSong
 * data：2016/3/29 21:08
 */
public class UploadFragment extends BaseFragment {

//    private LinearLayout llUploadVideo,llUploadCase;

    private TextView tvUploadVideo, tvUploadCase;
    private LinearLayout layou_toup, layout_up, layout_uploadhis;
    private RelativeLayout rl_history_nodata;
    private Button btn_login;
    private ImageView btn_toupcase;
    private ImageView btn_toupvideo;
    private ImageView img_x;
    private TextView tv_history,tvRecord;
    private View view;
    private ListView listview_uphis;
    private List<Upload> uploads;
    private UploadHisAdapter adapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        /*JSONObject result = new JSONObject(msg.obj.toString());
                        if (result.getInt("status") == 1) {// 成功
                            JSONArray data = result.getJSONArray("data");
                            uploads = new Gson().fromJson(data.toString()
                                    , new TypeToken<List<Upload>>() {
                                    }.getType());
                            listview_uphis.setVisibility(View.VISIBLE);
                            rl_history_nodata.setVisibility(View.GONE);
                            adapter = new UploadHisAdapter(getActivity(), uploads);
                            listview_uphis.setAdapter(adapter);

                        } else {                                                                        // 失败
//                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            listview_uphis.setVisibility(View.GONE);
                            rl_history_nodata.setVisibility(View.VISIBLE);
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_upload_new_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findById();
        onclick();

    }

    public void initData() {
        //view.setVisibility(View.VISIBLE);
        String uid = SharedPreferencesTools.getUids(getActivity());
        if (uid == null || ("").equals(uid)) {
            layout_up.setVisibility(View.VISIBLE);
            layou_toup.setVisibility(View.GONE);
        } else {
            layout_up.setVisibility(View.GONE);
            layou_toup.setVisibility(View.VISIBLE);
        }
    }


    public void findById() {
        tvRecord = (TextView) getActivity().findViewById(R.id.id_tv_upload_main_record);
        tvUploadVideo = (TextView) getActivity().findViewById(R.id.id_tv_to_upload_video);
        tvUploadCase = (TextView) getActivity().findViewById(R.id.id_tv_to_upload_case);
//        llUploadVideo = (LinearLayout) getActivity().findViewById(R.id.id_ll_upload_main_video);
//        llUploadCase = (LinearLayout) getActivity().findViewById(R.id.id_ll_upload_main_case);

        layou_toup = (LinearLayout) getActivity().findViewById(R.id.layou_toup);
        layout_up = (LinearLayout) getActivity().findViewById(R.id.layout_up);
        //layout_uploadhis = (LinearLayout) getActivity().findViewById(R.id.layout_uploadhis);
        btn_login = (Button) getActivity().findViewById(R.id.btn_login);
        //btn_toupcase = (ImageView) getActivity().findViewById(R.id.btn_toupcase);
        //btn_toupvideo = (ImageView) getActivity().findViewById(R.id.btn_toupvideo);
        //img_x = (ImageView) getActivity().findViewById(R.id.img_x);
        //tv_history = (TextView) getActivity().findViewById(R.id.tv_history);
        //listview_uphis = (ListView) getActivity().findViewById(R.id.listview_uphis);
        //view = getActivity().findViewById(R.id.mid_view);
        //rl_history_nodata = (RelativeLayout) getActivity().findViewById(R.id.rl_history_nodata);
    }


    public void onclick() {

        /*llUploadVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Upload_video.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        llUploadCase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadCase2.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });*/

        tvUploadVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Upload_video3.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        tvUploadCase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Upload_case3.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        tvRecord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), IsUpload3.class);
                intent1.putExtra("TAG", "Video");
                startActivity(intent1);
            }
        });

        //上传视频
        /*btn_toupcase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), Upload_case.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        btn_toupvideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), Upload_video.class);
                intent.putExtra("type", "home");
                startActivity(intent);
            }
        });

        tv_history.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_uploadhis.setVisibility(View.VISIBLE);
                layou_toup.setVisibility(View.GONE);
                view.setVisibility(View.GONE);
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
        });*/

        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("source", "up");
                startActivity(intent);
            }
        });
        /*img_x.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_uploadhis.setVisibility(View.GONE);
                layou_toup.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        });*/
    }

    @Override
    public void onResume() {
        super.onResume();
        layou_toup.setVisibility(View.VISIBLE);
        initData();
        //layout_uploadhis.setVisibility(View.GONE);
    }
}

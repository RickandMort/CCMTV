package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.MyGridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */
public class Answering_card extends BaseActivity {
    private Context context;
    private MyGridView gridview;
    private MyGridAdapter myGridAdapter;
    private String my_exams_id;
    private String my_exams_eid;
    public List<String> img_text;
    public List<Integer> imgs;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");
//                            System.out.println("进入到搜索解析页：" + dataArray);
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject customJson = dataArray.getJSONObject(i);
                                img_text.add(customJson.getString("num"));
                                switch (customJson.getInt("status")) {
                                    case 1:
                                        imgs.add(R.mipmap.answering_card_icon01);
                                        break;
                                    case 2:
                                        imgs.add(R.mipmap.answering_card_icon02);
                                        break;
                                    case 3:
                                        imgs.add(R.mipmap.answering_card_icon03);
                                        break;
                                    default:
                                        break;
                                }

                            }
                            myGridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Answering_card.this, jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.answering_card);
        context = this;
        findId();
        initdata();
        setValue2();

    }

    @Override
    public void findId() {
        super.findId();
        gridview = (MyGridView) findViewById(R.id.gridview);

    }

    public void initdata() {
        my_exams_id = getIntent().getStringExtra("my_exams_id");
        my_exams_eid = getIntent().getStringExtra("my_exams_eid");
        img_text = new ArrayList<>();
        imgs = new ArrayList<>();
        myGridAdapter = new MyGridAdapter(context, img_text, imgs);
        gridview.setAdapter(myGridAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                TextView textView = (TextView) view.findViewById(R.id.tv_item);
                Intent intent = new Intent(Answering_card.this, Check_the_answer_sheet.class);
                intent.putExtra("my_exams_id", my_exams_id);
                intent.putExtra("my_exams_eid", my_exams_eid);
                intent.putExtra("position", textView.getText().toString());

                if (textView.getText().length() > 0) {
                    startActivity(intent);
                }
            }
        });
    }

    public void setValue2() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getPaperCard);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("pid", my_exams_id);
                    obj.put("eid", my_exams_eid);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.Medical_examination, obj.toString());
//                    Log.e("看看视频数据2", result);

                    MyProgressBarDialogTools.hide();
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/exam_bank/start.html?pid=" + my_exams_id;
        super.onPause();
    }

}

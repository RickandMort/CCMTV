package com.linlic.ccmtv.yx.activity.my.medical_examination;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 考试须知
 * Created by Administrator on 2017/9/6.
 */
public class Examination_instructions2 extends BaseActivity {

    private Context context;
    private MyListView examination_instructions_list,examination_instructions_list2;
    private TextView examination_instructions_name,examination_instructions,examination_instructions_time,length_of_examination,number_of_examinations,total_score_of_examination;
    private Button examination_instructions_buttpm;
    private LinearLayout hearderViewLayout,hearderViewLayout2;
    BaseListAdapter baseListAdapter,baseListAdapter2;
    private  String aid,tid,pptid;
    private String type;
    private String my_exams_id = "";
    int reset = 0;
    private List<String> data = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONArray jsonObject =  jsonObjects.getJSONArray("describe");
                            examination_instructions_buttpm.setText(jsonObjects.getString("button_val"));
                            if(Integer.parseInt(jsonObjects.getString("is_yes"))>0){
                                examination_instructions_buttpm.setBackground(getResources().getDrawable(R.mipmap.button_03));
                                examination_instructions_buttpm.setClickable(false);
                            }else{
                                examination_instructions_buttpm.setBackground(getResources().getDrawable(R.mipmap.button_01));
                                examination_instructions_buttpm.setClickable(true);
                            }
                            for (int i = 0; i < jsonObject.length(); i++) {
                                jsonObject.getString(i);
                                data.add(jsonObject.getString(i));
                            }

                        } else {
                            Toast.makeText(Examination_instructions2.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
//                        MyProgressBarDialogTools.hide();
                        baseListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    System.out.println(R.string.post_hint1);
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
    protected void onResume() {
        if(reset>0){
            data.removeAll(data);
            setValue2();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yy.ccmtv.cn/Task/tid=" + tid;
        reset++;
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.examination_instructions2);
        context = this;
        findId();
        initdata();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        examination_instructions_list = (MyListView) findViewById(R.id.examination_instructions_list);
        examination_instructions_buttpm = (Button) findViewById(R.id.examination_instructions_buttpm);

    }

    public void initdata(){
        aid = getIntent().getStringExtra("aid");
        tid = getIntent().getStringExtra("tid");
        type = getIntent().getStringExtra("type");
        pptid = getIntent().getStringExtra("pptid");
        baseListAdapter = new BaseListAdapter(examination_instructions_list, data, R.layout.examination_instructions_list_item5) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.text, item + "");
            }
        };
        examination_instructions_list.setAdapter(baseListAdapter);


    }



    public void setValue2(){
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();

                    obj.put("act", URLConfig.getPptTestInfo);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("aid",aid);
                    obj.put("tid",tid);
//                    obj.put("pptid",pptid);
                    obj.put("pptid",aid);
//                    Log.e("看看视频数据2", obj.toString());
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
    /*进入考试*/
    public void enter_the_examination(View view){
        view.setClickable(false);
        Intent intent = new Intent(Examination_instructions2.this,Formal_examination_text.class);
        intent.putExtra("aid",aid);
        intent.putExtra("tid", tid);
        intent.putExtra("type",type);
        intent.putExtra("pptid", pptid);
        startActivity(intent);
        view.setClickable(true);
    }
}

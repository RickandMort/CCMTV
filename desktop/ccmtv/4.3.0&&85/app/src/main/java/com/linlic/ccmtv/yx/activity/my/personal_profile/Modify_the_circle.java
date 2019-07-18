package com.linlic.ccmtv.yx.activity.my.personal_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.MyAlert;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/4.
 */
public class Modify_the_circle extends BaseActivity {
    Context context;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private String TitleId;//职称id
    private String TitleName;//职称name
    private Button myprofile_title;//职称
    private EditText myprofile_academy,myprofile_degree,practice_achievement,practice_give;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            final int MyAlertWhat = msg.what;
            if (MyAlertWhat == 150 || MyAlertWhat == 188 || MyAlertWhat == 158) {//弹框
                MyAlert.Builder bu = new MyAlert.Builder(Modify_the_circle.this);
                bu.setTitle(msg.obj + "");
                bu.setMessage(list);
                bu.setPositiveButton("取消");
                bu.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                        TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
                        String id = textview.getText().toString();
                        TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
                        String name = textView.getText().toString();
                       if (MyAlertWhat == 188) {
                            TitleId = id;
                            TitleName = name;
                            myprofile_title.setText(TitleName);
                        }
                    }
                });
                bu.create().show();
            }
            switch (msg.what) {
                case 303:
                    Toast.makeText(context, msg.obj + "", Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    Toast.makeText(context, "提交成功", Toast.LENGTH_SHORT).show();
                    // if (getIntent().getStringExtra("source").equals("register")) {
                    //startActivity(new Intent(MyProfile.this, MainActivity.class));
                    Intent intent = new Intent(Modify_the_circle.this, MainActivity.class);
                    intent.putExtra("type", "register");
                    startActivity(intent);
                    MyProgressBarDialogTools.hide();
                    Modify_the_circle.this.finish();
                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.personal_profile);
        context = this;
        findId();
    }

    @Override
    public void findId() {
        super.findId();
        myprofile_title = (Button) findViewById(R.id.myprofile_title);
        myprofile_academy = (EditText) findViewById(R.id.myprofile_academy);
        myprofile_degree = (EditText) findViewById(R.id.myprofile_degree);
        practice_achievement = (EditText) findViewById(R.id.practice_achievement);
        practice_give = (EditText) findViewById(R.id.practice_give);

    }

    /**
     * name：职称选择
     * <p/>
     * author: Mr.song
     * 时间：2016-2-29 下午3:30:12
     *
     * @param view
     */
    public void getTitle(View view) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.docPositions);

                    String result = HttpClientUtils.sendPost(context,
                            URLConfig.CCMTVAPP, obj.toString());

                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        //成功
                        JSONArray array = json.getJSONArray("doc_list");
                        list.clear();//使用list前先清空list
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject object = array.getJSONObject(i);
                            map.put("id", object.getString("id"));
                            map.put("name", object.getString("name"));
                            list.add(map);
                        }
                        handler.sendMessage(handler.obtainMessage(188, "请选择职称"));
                    } else {
                        //失败
                        handler.sendMessage(handler.obtainMessage(303, json.getString("errorMessage")));
                    }
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
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }

}

package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.upload.new_upload.Upload_video3;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：上传视频
 * author：Larry
 * data：2017/3/16.
 */
public class UploadVideoActivity extends BaseActivity {
    Context context;
    private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private ListView myListview;
    BaseListAdapter baseListAdapter;
    Button bottom;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONArray keshi_list = jsonObject.getJSONArray("data");
                            if(keshi_list.length()>0){
                                data.removeAll(data);
                            }
                            for(int i = 0;i<keshi_list.length();i++){
                                JSONObject keshi =  keshi_list.getJSONObject(i);
                                Map<String, Object> map;
                                switch (keshi.getString("type")){
                                    case "1":
                                        map = new HashMap<>();
                                        map.put("title1",keshi.getString("title1"));
                                        map.put("title2",keshi.getString("title2"));
                                        map.put("type",keshi.getInt("type"));
                                        data.add(map);
                                        break;
                                    case "2":
                                        map = new HashMap<>();
                                        map.put("title3",keshi.getString("title1"));
                                        map.put("type",keshi.getInt("type"));
                                        data.add(map);
                                        break;
                                    case "3":
                                        bottom.setText(keshi.getString("title1"));
                                        break;
                                    default:
                                        break;
                                }

                            }
                            baseListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(context , jsonObject.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
//                        refreshLayout.finishRefresh( false);
                        e.printStackTrace();
                    }
                    break;

                case 500:

                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharevideo);
        context = this;
        findId();
        super.setActivity_title_name("任务详情");
        initData();
        initView();
        setmsgdb();
    }
    @Override
    public void findId() {
        super.findId();
        myListview = (ListView) findViewById(R.id.myListview);
        bottom = (Button) findViewById(R.id.bottom);
    }
    public void initData(){


        Map<String, Object> map = new HashMap<>();
        map.put("title1","步骤一");
        map.put("title2","上传视频");
        map.put("type",1);
        data.add(map);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("title1","步骤二");
        map1.put("title2","通过审核赚80积分/次");
        map1.put("type",1);
        data.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("title3","积分不限量");
        map2.put("type",2);
        data.add(map2);


    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.word);
                    obj.put("type", "4");
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("分享视频",result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    public void initView(){
        bottom.setText("我要去上传");
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toupload_video();
            }
        });
        baseListAdapter = new BaseListAdapter(myListview, data, R.layout.activity_sharevideo_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                if(Integer.parseInt(((Map) item).get("type") + "") == 1){
                    helper.setText(R.id.title1, ((Map) item).get("title1") + "");
                    helper.setText(R.id.title2, ((Map) item).get("title2") + "");
                    helper.setViewVisibility(R.id.layout1,View.VISIBLE);
                    helper.setViewVisibility(R.id.layout2,View.GONE);
                }else{
                    helper.setText(R.id.title3, ((Map) item).get("title3") + "");
                    helper.setViewVisibility(R.id.layout1,View.GONE);
                    helper.setViewVisibility(R.id.layout2,View.VISIBLE);
                }



            }
        };
        myListview.setAdapter(baseListAdapter);
    }

    public void toupload_video( ) {
        Intent intent = new Intent(UploadVideoActivity.this, Upload_video3.class);
        intent.putExtra("type","home");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}

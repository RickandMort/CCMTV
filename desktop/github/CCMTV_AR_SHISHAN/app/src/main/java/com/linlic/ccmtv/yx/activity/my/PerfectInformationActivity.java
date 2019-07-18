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
import com.linlic.ccmtv.yx.activity.pull.MyProfile;
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
 * name：完善资料
 * author：Larry
 * data：2017/3/16.
 */
public class PerfectInformationActivity extends BaseActivity {
    Context context;
    String Str_hyleibie, Str_cityname, Str_bigkeshi, Str_smallkeshi, Str_zhicheng, Str_truename, Str_sexName, Str_sex, Str_cityid, Str_address, Str_idcard, Str_danwei;
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
        getMyIntent();
        initData();
        initView();
        setmsgdb();
    }
    public void initData(){


        Map<String, Object> map = new HashMap<>();
        map.put("title1","步骤一");
        map.put("title2","填写个人资料");
        map.put("type",1);
        data.add(map);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("title1","步骤二");
        map1.put("title2","提交赚10积分");
        map1.put("type",1);
        data.add(map1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("title3","积分免费送");
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
                    obj.put("type", "7");
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
    @Override
    public void findId() {
        super.findId();
        myListview = (ListView) findViewById(R.id.myListview);
        bottom = (Button) findViewById(R.id.bottom);
    }
    public void initView(){
        bottom.setText("我要去完善");
        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toperfect();
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
    private void getMyIntent() {
        Str_hyleibie = getIntent().getStringExtra("Str_hyleibie");
        Str_cityname = getIntent().getStringExtra("Str_cityname");
        Str_bigkeshi = getIntent().getStringExtra("Str_bigkeshi");
        Str_smallkeshi = getIntent().getStringExtra("Str_smallkeshi");
        Str_zhicheng = getIntent().getStringExtra("Str_zhicheng");
        Str_truename = getIntent().getStringExtra("Str_truename");
        Str_sexName = getIntent().getStringExtra("Str_sexName");
        Str_sex = getIntent().getStringExtra("Str_sex");
        Str_cityid = getIntent().getStringExtra("Str_cityid");
        Str_address = getIntent().getStringExtra("Str_address");
        Str_idcard = getIntent().getStringExtra("Str_idcard");
        Str_danwei = getIntent().getStringExtra("Str_danwei");
    }

    public void toperfect( ) {
        Intent intent = new Intent(PerfectInformationActivity.this, MyProfile.class);
        intent.putExtra("source", "my");
        intent.putExtra("Str_hyleibie", Str_hyleibie);
        intent.putExtra("Str_cityname", Str_cityname);
        intent.putExtra("Str_bigkeshi", Str_bigkeshi);
        intent.putExtra("Str_smallkeshi", Str_smallkeshi);
        intent.putExtra("Str_zhicheng", Str_zhicheng);
        intent.putExtra("Str_truename", Str_truename);
        intent.putExtra("Str_sexName", Str_sexName);
        intent.putExtra("Str_sex", Str_sex);
        intent.putExtra("Str_cityid", Str_cityid);
        intent.putExtra("Str_address", Str_address);
        intent.putExtra("Str_idcard", Str_idcard);
        intent.putExtra("Str_danwei", Str_danwei);
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

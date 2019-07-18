package com.linlic.ccmtv.yx.activity.rules_to_compensate.evaluation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/6/20.
 */

public class The_evaluation_of_teaching_list extends BaseActivity {
    private Context context;
    private Dialog dialog;
    private View view ;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.layout_nodata)
    LinearLayout layout_nodata;
    private TextView list_buttom_text;
    private LinearLayout item_the_evaluation_of_teaching_list_top,item_the_evaluation_of_teaching_list_buttom;
    private String fid = "";
    JSONObject result, data;
    private int is = 0;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private BaseListAdapter baseListAdapterVideo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                JSONArray dateJson = dataJson.getJSONObject("data").getJSONArray("list");
                                list_buttom_text.setText(dataJson.getJSONObject("data").getString("alert_msg"));
                                if(dataJson.getJSONObject("data").getString("alert_msg").trim().length()<1){
                                    listView.removeFooterView(item_the_evaluation_of_teaching_list_buttom);
                                }
                                listData.clear();
                                if(dateJson.length()<1){
                                    layout_nodata.setVisibility(View.VISIBLE);
                                }else {
                                    layout_nodata.setVisibility(View.GONE);
                                }
                                for (int i = 0; i<dateJson.length();i++){
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("id",dataJson1.getString("id") );
                                    map.put("status",dataJson1.getString("status") );
                                    map.put("contribution",dataJson1.getString("contribution") );
                                    map.put("score",dataJson1.getString("score") );
                                    map.put("year",dataJson1.getString("year") );
                                    map.put("month",dataJson1.getString("month") );
                                    map.put("username",dataJson1.getString("username") );
                                    map.put("realname",dataJson1.getString("realname") );
                                    map.put("pop_msg",dataJson1.getString("pop_msg") );
                                    listData.add(map);
                                }
                                baseListAdapterVideo.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 2:
                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(The_evaluation_of_teaching_list.this);
                    view = inflater.inflate(R.layout.dialog_item9, null);

                    // 对话框
                    dialog = new Dialog(The_evaluation_of_teaching_list.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth()-100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(view);
                    dialog.setCancelable(false);
                    final TextView btn_sure = (TextView) view.findViewById(R.id.i_understand);// 取消
                    final TextView _item_content = (TextView) view.findViewById(R.id._item_content);// 显示内容
                    _item_content.setText(msg.obj+"");

                    btn_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            view = null;
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });


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
        setContentView(R.layout.the_evaluation_of_teaching_list);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        findId();
        initViews();
        getUrlRulest();
    }


    @Override
    protected void onResume() {
        if(is>0){
            getUrlRulest();
        }
        is++;
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Allappraise/manage_index.html";
        super.onPause();
    }

    private void initViews() {
        //增加头部
      /*  item_the_evaluation_of_teaching_list_top = (LinearLayout) View.inflate(this, R.layout.item_the_evaluation_of_teaching_list_top, null);
        listView.addHeaderView(item_the_evaluation_of_teaching_list_top);*/
        //增加底部
        item_the_evaluation_of_teaching_list_buttom = (LinearLayout) View.inflate(this, R.layout.item_the_evaluation_of_teaching_list_buttom, null);
        list_buttom_text = (TextView) item_the_evaluation_of_teaching_list_buttom.findViewById(R.id.list_buttom_text);
        listView.addFooterView(item_the_evaluation_of_teaching_list_buttom);

        //设置TabLayout点击事件
        baseListAdapterVideo = new BaseListAdapter(listView, listData, R.layout.item_the_evaluation_of_teaching_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                helper.setText(R.id._item_teaching,map.get("realname").toString()+"("+map.get("username").toString()+")");
                helper.setText(R.id._item_evaluation,map.get("score").toString());
                helper.setText(R.id._item_contribution,map.get("contribution").toString());
                helper.setTag(R.id._item_evaluation,map.get("pop_msg").toString());
                helper.setText(R.id._item_id,map.get("id").toString());


            }
        };
        listView.setAdapter(baseListAdapterVideo);
        // listview点击事件
        listView.setOnItemClickListener(new  casesharing_listListener());
    }

    /**
     * name: 点击查看某个视频的详细 author:Tom 2016-1-28下午3:42:08
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            if(arg2<listData.size()){
                Map<String,Object> map = listData.get(arg2);

                Intent intent = null;

                intent = new Intent(context, Evaluation_in_detail.class);
                intent.putExtra("fid",fid);
                intent.putExtra("detailid",map.get("id").toString());
                if(intent!=null){
                    startActivity(intent);
                }

            }

        }

    }

    public void getUrlRulest() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.allAppraiseUserList);
                    obj.put("fid",fid);
                    obj.put("gps_ids",getIntent().getStringExtra("gps_ids"));
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("月度评价总接口", result);
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

    public void view_the_score(View view){
        if(view.getTag() !=null && view.getTag().toString().length()>0){
            Message message = new Message();
            message.what = 2;
            message.obj = view.getTag().toString();
            handler.sendMessage(message);
        }
    }

}

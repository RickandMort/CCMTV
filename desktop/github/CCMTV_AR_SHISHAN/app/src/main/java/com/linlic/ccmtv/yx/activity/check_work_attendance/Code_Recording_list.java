package com.linlic.ccmtv.yx.activity.check_work_attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.DateUtil;
import com.linlic.ccmtv.yx.utils.DoubleTimeSelectDialog;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.util.MD5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**二维码扫描记录
 * Created by tom on 2018/12/11.
 */

public class Code_Recording_list extends BaseActivity {
    private Context context;

    @Bind(R.id.date_img)//日期img
    ImageView date_img;
    @Bind(R.id.time_text)
    TextView time_text;
    @Bind(R.id.recording_list)
    ListView recording_list;
    TextView _item_text4;
    private BaseListAdapter baseListAdapterRecording;
    private List<Map<String, Object>> listData = new ArrayList<Map<String, Object>>();
    private int page = 1;
    private int pages = 0;
    private DoubleTimeSelectDialog mDoubleTimeSelectDialog;
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
                                JSONArray dateJson = dataJson.getJSONArray("data") ;
                                if (page == 1) {
                                    listData.clear();
                                }
                                pages = dataJson. getInt("pages");
                                _item_text4.setText(dataJson. getString("type"));
                                for (int i = 0; i < dateJson.length(); i++) {
                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("sign_date", dataJson1.getString("sign_date"));
                                    map.put("sign_h_i", dataJson1.getString("sign_h_i"));
                                    map.put("truename", dataJson1.getString("truename"));
                                    if(i>0){
                                        map.put("is", i%2==0?0:1);
                                    }else{
                                        map.put("is", 0);
                                    }
                                    listData.add(map);
                                }
                                baseListAdapterRecording.notifyDataSetChanged();

                            } else {
                                if (page == 1) {
                                    listData.clear();
                                }
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
//                        MyProgressBarDialogTools.hide();
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
        setContentView(R.layout.code_recording_list);
        context = this;
        ButterKnife.bind(this);
        findId();
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "https://yun.ccmtv.cn/admin.php/wx/SignIn";
        super.onPause();
    }

    public void initView() {
        time_text.setText(DateUtil.dataToStr(DateUtil.getFirstDayByMonth(DateUtil.getCurrentDate("yyyy-MM-dd")),"yyyy-MM-dd")+"~"+DateUtil.getCurrDate("yyyy-MM-dd"));
        LinearLayout  hearderViewLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_recording_list_top,null);
        _item_text4 = hearderViewLayout.findViewById(R.id._item_text4);
        recording_list.addHeaderView(hearderViewLayout);

        date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                String now = sdf.format(new Date());
//                customDatePicker1.show(now);

                showCustomTimePicker(  time_text);
            }
        });

        baseListAdapterRecording = new BaseListAdapter(recording_list, listData, R.layout.item_recording_list) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map map = (Map) item;
                if(map.get("is").toString().equals("1")){
                    helper.setBackground_color(R.id._item_layout, Color.parseColor("#F0F4F7"));
                }else{
                    helper.setBackground_color(R.id._item_layout, Color.parseColor("#FFFFFF"));
                }
                helper.setText(R.id._item_text1, map.get("sign_date").toString());
                helper.setText(R.id._item_text2, map.get("sign_h_i").toString());
                helper.setText(R.id._item_text3,  map.get("truename").toString());
            }
        };
        recording_list.setAdapter(baseListAdapterRecording);
        baseListAdapterRecording.addOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                }
                if (firstVisibleItem == 0) {
                    View firstVisibleItemView = recording_list.getChildAt(0);

                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    View lastVisibleItemView = recording_list.getChildAt(recording_list.getChildCount() - 1);
                    if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == recording_list.getHeight()) {
                        if (pages>page) {
                            page += 1;
                            getQrCodeLogList();
                        }
                    }
                }
            }
        });

        if(time_text.getText().toString().trim().length()>0){
            page = 1;
            getQrCodeLogList();
        }
    }

    public void getQrCodeLogList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj_sj = new JSONObject();
                    obj_sj.put("act", URLConfig.getTimestamp);
                    String result_sj = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj_sj.toString());
                    JSONObject jsonObject = new JSONObject(result_sj);
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        if (dataJson.getInt("status") == 1) { // 成功
                            JSONObject obj = new JSONObject();
                            obj.put("act", URLConfig.getQrCodeLogList);
                            obj.put("uid", SharedPreferencesTools.getUid(context));
                            obj.put("timestamp", dataJson.getJSONObject("data").getString("timestamp"));
                            obj.put("time", time_text.getText().toString());
                            obj.put("page", page);
                            obj.put("token", MD5.md5(URLConfig.key + dataJson.getJSONObject("data").getString("timestamp") + SharedPreferencesTools.getUid(context)));

                            String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_kq, obj.toString());
                            Message message = new Message();
                            message.what = 1;
                            message.obj = result;
                            handler.sendMessage(message);
                        } else {
                            Toast.makeText(context, jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("statusMsg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }


            }
        };
        new Thread(runnable).start();
    }

    public void showCustomTimePicker(final TextView textView) {
        String beginDeadTime = "2000-01-01";
        if (mDoubleTimeSelectDialog == null) {
            mDoubleTimeSelectDialog = new DoubleTimeSelectDialog(this, beginDeadTime, DateUtil.dataToStr(DateUtil.getFirstDayByMonth(DateUtil.getCurrentDate("yyyy-MM-dd")),"yyyy-MM-dd"), DateUtil.getCurrDate("yyyy-MM-dd"));
            mDoubleTimeSelectDialog.setOnDateSelectFinished(new DoubleTimeSelectDialog.OnDateSelectFinished() {
                @Override
                public void onSelectFinished(String startTime, String endTime) {
                    textView.setText(startTime+ "~" + endTime );
                    page = 1;
                    getQrCodeLogList();
                }
            });

            mDoubleTimeSelectDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                }
            });
        }
        if (!mDoubleTimeSelectDialog.isShowing()) {
            mDoubleTimeSelectDialog.recoverButtonState();
            mDoubleTimeSelectDialog.show();
        }
    }
}

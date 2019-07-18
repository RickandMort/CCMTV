package com.linlic.ccmtv.yx.activity.rules_to_compensate.periodical_exam;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 规培 阶段性考核 学员成绩汇总界面（学员首页）
 */
public class StuCheckPeriodicalExamActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    LinearLayout llActivityTitle8;
    private TextView title_name;
    private ImageView ivActivityRight;
    private ListView lvStuCheck;
    private BaseListAdapter baseListAdapter;
    private List<Map<String, String>> stuCheckList = new ArrayList<>();
    private View contentView1;
    private PopupWindow popupWindow1;
    private String fid = "";
    private int page = 1;
    private String type_id = "";
    private List<String> typeList = new ArrayList<>();
    private List<Map<String, String>> typeMapList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapterType;
    private NodataEmptyLayout lt_nodata1;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("200")) {
                            JSONObject data = result.getJSONObject("data");
                            if (data.getInt("status") == 1) {
                                JSONObject dataObject = data.getJSONObject("info");
                                JSONArray dataArray = dataObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObjectDetail = dataArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("exam_id", dataObjectDetail.getString("exam_id"));
                                    map.put("exam_name", dataObjectDetail.getString("exam_name"));//考站
                                    map.put("type_name", dataObjectDetail.getString("type_name"));//考点
//                                    map.put("s_time", dataObjectDetail.getString("s_time"));
//                                    map.put("e_time", dataObjectDetail.getString("e_time"));
//                                    map.put("base_name", dataObjectDetail.getString("base_name"));  //地点
                                    map.put("check_score", dataObjectDetail.getString("check_score"));  //是否查看成绩    check_score（成绩）：0暂无，1查看
                                    stuCheckList.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();

//                                if (stuCheckList.size() > 0) {
//                                    lvStuCheck.setVisibility(View.VISIBLE);
//                                    lt_nodata1.setVisibility(View.GONE);
//                                } else {
//                                    lvStuCheck.setVisibility(View.GONE);
//                                    lt_nodata1.setVisibility(View.VISIBLE);
//                                }

                                typeMapList.clear();
                                typeList.clear();
                                JSONArray dataTypeArray = dataObject.getJSONArray("exam_type");
                                for (int i = 0; i < dataTypeArray.length(); i++) {
                                    JSONObject dataTypeObject = dataTypeArray.getJSONObject(i);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("type_id", dataTypeObject.getString("type_id"));
                                    map.put("name", dataTypeObject.getString("name"));
                                    typeMapList.add(map);
                                    typeList.add(dataTypeObject.getString("name"));
                                }
                                arrayAdapterType.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, data.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, result.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        MyProgressBarDialogTools.hide();
                        setResultStatus(stuCheckList.size() > 0, result.getInt("code"));
                    } catch (Exception e) {
                        MyProgressBarDialogTools.hide();
                        e.printStackTrace();
                        setResultStatus(stuCheckList.size() > 0, HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    setResultStatus(stuCheckList.size() > 0, HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };

    private void setResultStatus(boolean status, int code) {
        if (status) {
            lvStuCheck.setVisibility(View.VISIBLE);
            lt_nodata1.setVisibility(View.GONE);
        } else {
            if (HttpClientUtils.isNetConnectError(context, code)) {
                lt_nodata1.setNetErrorIcon();
            } else {
                lt_nodata1.setLastEmptyIcon();
            }
            lvStuCheck.setVisibility(View.GONE);
            lt_nodata1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_check_periodical_exam);
        context = this;
        findId();
        getIntentData();
        initListView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/StageExaminer/index.html";
        super.onPause();
    }

    private void getIntentData() {
        fid = getIntent().getStringExtra("fid");
    }

    public void findId() {
        llActivityTitle8 = findViewById(R.id.id_ll_activity_title_8);
        title_name = findViewById(R.id.activity_title_name);
        ivActivityRight = findViewById(R.id.id_iv_activity_title_8_right);
        lvStuCheck = findViewById(R.id.id_lv_stu_check_periodical_exam);
        lt_nodata1 = findViewById(R.id.lt_nodata1);

        title_name.setText("阶段性考核");
        ivActivityRight.setVisibility(View.VISIBLE);
        ivActivityRight.setImageResource(R.mipmap.ic_periodical_exam_filter);

        ivActivityRight.setOnClickListener(this);
    }

    private void initData() {
        /*for (int i = 0; i < 6; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", "2018年度考核" + i);
            map.put("subtitle", "2018年度考核" + i);
            if (i % 2 != 0) {
                map.put("isCheck", "0");
            } else {
                map.put("isCheck", "1");
            }
            stuCheckList.add(map);
        }*/

        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.stageUserIndex);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("fid", fid);
                    obj.put("page", page);
                    obj.put("type_id", type_id);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("规培阶段性考核学员成绩汇总信息数据：", result);

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

    private void initListView() {
        baseListAdapter = new BaseListAdapter(lvStuCheck, stuCheckList, R.layout.item_gp_stu_check_periodical_exam_list) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Map<String, String> map = (Map<String, String>) item;
                helper.setText(R.id.id_tv_title, map.get("exam_name").toString());
                helper.setText(R.id.id_tv_subtitle, map.get("type_name").toString());
                if (map.get("check_score").equals("0")) {        //check_score（成绩）：0暂无，1查看
                    helper.setBackground_Image(R.id.id_tv_check_score, R.drawable.anniu2);
                    helper.setTextColor2(R.id.id_tv_check_score, Color.parseColor("#666666"));
                    helper.setText(R.id.id_tv_check_score, "暂无");
                } else {
                    helper.setBackground_Image(R.id.id_tv_check_score, R.drawable.anniu18);
                    helper.setText(R.id.id_tv_check_score, "查看");
                    helper.setTextColor2(R.id.id_tv_check_score, Color.parseColor("#3897F9"));
                    helper.setCheckScoreClick(R.id.id_tv_check_score, fid, map);
                }

                helper.setTag(R.id.id_iv_detail, fid);
                helper.setDetailOnClick(R.id.id_iv_detail, map);
            }
        };

        lvStuCheck.setAdapter(baseListAdapter);

        lvStuCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        contentView1 = LayoutInflater.from(context).inflate(R.layout.popupwindow_periodical_exam_stu_filter, null, false);
        ListView lvType = contentView1.findViewById(R.id.id_lv_periodical_exam_type_select);
        arrayAdapterType = new ArrayAdapter<String>(this,
                R.layout.simple_list_item_1_center, typeList);
        lvType.setAdapter(arrayAdapterType);
        lvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, "选择类型："+typeList.get(i), Toast.LENGTH_SHORT).show();
                type_id = typeMapList.get(i).get("type_id");
                popupWindow1.dismiss();
                page = 1;
                stuCheckList.clear();
                initData();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_iv_activity_title_8_right:
                Toast.makeText(context, "点击筛选", Toast.LENGTH_SHORT).show();
                popwindow(view);
                break;
        }
    }

    public void popwindow(View view) {

        // 创建PopupWindow对象，其中：
        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
        popupWindow1 = new PopupWindow(contentView1, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);

//        setBackgroundAlpha(0.5f);//设置屏幕透明度
        // 设置PopupWindow的背景
        popupWindow1.setBackgroundDrawable(new ColorDrawable());
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow1.setOutsideTouchable(true);
        // 设置PopupWindow是否能响应点击事件
        popupWindow1.setTouchable(true);
        // 显示PopupWindow，其中：
        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
        popupWindow1.showAsDropDown(llActivityTitle8);
//        popupWindow.showAtLocation(contentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

        popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
//                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void back(View view) {
        finish();
    }

}

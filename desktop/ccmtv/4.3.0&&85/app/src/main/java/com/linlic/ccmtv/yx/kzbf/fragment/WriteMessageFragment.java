package com.linlic.ccmtv.yx.kzbf.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.kzbf.activity.ChoosePersonActivity;
import com.linlic.ccmtv.yx.kzbf.activity.MessageActivity;
import com.linlic.ccmtv.yx.kzbf.adapter.WriteMessageAdapter;
import com.linlic.ccmtv.yx.kzbf.bean.DbMedicineDetial;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 写邮件
 */
//message_drop_down_layout
public class WriteMessageFragment extends BaseFragment {
    private String title = "";           //标题
    private String content = "";         //内容
    private String addressee_uid = "";
    private PopupWindow popupWindow;
    private boolean isShow = false;
    private WriteMessageAdapter wmAdapter;
    private TextView tv_write_message_helper, message_submit;
    private List<DbMedicineDetial> list;
    private List<DbMedicineDetial> listMore = new ArrayList<>();
    private DbMedicineDetial dbMd;
    private EditText ed_message_title, edit_message_content;
    private TextView tv_helper;
    private ScrollView sv_empty;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            tv_write_message_helper.setText("");
                            ed_message_title.setText("");
                            edit_message_content.setText("");
                            tv_write_message_helper.setOnClickListener(ChoosPersonClick);
                            if (getActivity() != null) {//发送成功跳转到已发送消息界面
                                ((MessageActivity) getActivity()).jump2Outbox();
                            }
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    listMore.clear();
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getString("code").equals("0")) { // 成功

                            list = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                dbMd = new DbMedicineDetial(1);
                                dbMd.setRecommend_id(object.getString("uid"));
                                dbMd.setRecommend_title(object.getString("helper"));
                                list.add(dbMd);
                            }
                            listMore.addAll(list);
                            wmAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_write_message, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
//        initPop();
//        setPopValue();
    }

    private void initView() {
        tv_write_message_helper = (TextView) getActivity().findViewById(R.id.tv_write_message_helper);
        message_submit = (TextView) getActivity().findViewById(R.id.message_submit);
        ed_message_title = (EditText) getActivity().findViewById(R.id.ed_message_title);
        edit_message_content = (EditText) getActivity().findViewById(R.id.edit_message_content);
        sv_empty = (ScrollView) getActivity().findViewById(R.id.sv_empty);
        sv_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_message_content.requestFocus();
            }
        });

        message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageSubmit();
            }
        });
    }

    private void initData() {
        if (MessageActivity.assistant != null) {
            tv_write_message_helper.setText(MessageActivity.helper);
            addressee_uid = MessageActivity.addressee_uid;
        } else {
            tv_write_message_helper.setOnClickListener(ChoosPersonClick);
        }
    }

    private View.OnClickListener ChoosPersonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivityForResult(new Intent(getActivity(), ChoosePersonActivity.class), 0);

            //弹出一个下拉框
                /*if (isShow == false) {
                    isShow = true;
                    // 设置好参数之后再show
                    try {
                        popupWindow.showAsDropDown(tv_write_message_helper, 60, -10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    isShow = false;
                    try {
                        popupWindow.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
        }
    };

    @Override
    public void onResume() {
        initData();
        super.onResume();
    }

    private void initPop() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.message_drop_down_layout, null);
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.drop_down_recyclerview);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.PopAnimStyle);
        popupWindow.setTouchable(true); // 设置PopupWindow可触摸
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                isShow = false;
                                                return false;
                                                // 这里如果返回true的话，touch事件将被拦截
                                                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                                            }
                                        }
        );
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(
                getResources().getDrawable(R.drawable.custom_bg_gv)
        );

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        wmAdapter = new WriteMessageAdapter(getContext(), listMore);
        recyclerView.setAdapter(wmAdapter);

        /*wmAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TextView textView = (TextView) view.findViewById(R.id.item_helper);
                String str = textView.getText().toString();
                tv_write_message_helper.setText(str);
                addressee_uid = listMore.get(position).getRecommend_id();
                popupWindow.dismiss();
            }
        });*/
    }

    private void setPopValue() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.focusList);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));

                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看发消息下拉列表数据", result);

                    Message message = new Message();
                    message.what = 2;
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

    /**
     * 提交
     */
    public void MessageSubmit() {
        title = ed_message_title.getText().toString();
        content = edit_message_content.getText().toString();
        if (addressee_uid.equals("")) {
            Toast.makeText(getContext(), "接收者不能为空", Toast.LENGTH_SHORT).show();
        } else if (title.equals("")) {
            Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
        } else if (content.equals("")) {
            Toast.makeText(getContext(), "内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.postMessage);
                        obj.put("uid", SharedPreferencesTools.getUid(getContext()));
                        obj.put("addressee_uid", addressee_uid);
                        obj.put("title", title);
                        obj.put("content", content);

//                        Log.e("看看发消息上行数据", obj.toString());
                        String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                        Log.e("看看发消息数据", result);

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            if (requestCode == 0) {
                tv_write_message_helper.setText(data.getStringExtra("helper"));
                addressee_uid = data.getStringExtra("id");
            }
        }
    }
}

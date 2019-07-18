package com.linlic.ccmtv.yx.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Live_broadcast;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12.
 */

public class Direct_broadcast_list_itemAdapter extends RecyclerView.Adapter<Direct_broadcast_list_itemAdapter.Holder> {
    private Context mContext;
    public List<Live_broadcast> live_broadcasts;
    AlertDialog dlg;
    private int place = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //全屏后底部点赞view
            switch (msg.what) {
                case 1:
                    try {

                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            live_broadcasts.get(place).setBotten_text("已预约");
                            notifyItemChanged(place);
                            /*JSONObject dataArray = result.getJSONObject("data");*/
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            LayoutInflater inflater = LayoutInflater.from(mContext);
                            View views = inflater.inflate(R.layout.dialog_item2, null);
                            TextView content = (TextView) views.findViewById(R.id.dialog_text);
                            content.setText(Html.fromHtml(result.getString("errorMessage")));
                            TextView btn_sure = (TextView) views.findViewById(R.id.i_understand);
                            //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
                            final Dialog dialog = builder.create();
                            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                            lp.width = WindowManager.LayoutParams.FILL_PARENT;
                            lp.height = WindowManager.LayoutParams.FILL_PARENT;
                            dialog.getWindow().setAttributes(lp);
                            dialog.setCancelable(false);
                            dialog.show();
                            dialog.getWindow().setContentView(views);//自定义布局应该在这里添加，要在dialog.show()的后面
                            //dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                            btn_sure.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
//                                    Toast.makeText(mContext, "ok", 1).show();
                                }
                            });

                        } else {// 失败
                            Toast.makeText(mContext, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                default:

                    break;
            }
        }
    };

    public Direct_broadcast_list_itemAdapter(Context mContext, List<Live_broadcast> live_broadcasts) {

        this.mContext = mContext;
        this.live_broadcasts = live_broadcasts;
    }

    @Override
    public Direct_broadcast_list_itemAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.direct_broadcast_list_item2, null);
        // 实例化viewholder
        Direct_broadcast_list_itemAdapter.Holder viewHolder = new Direct_broadcast_list_itemAdapter.Holder(v);
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(Direct_broadcast_list_itemAdapter.Holder holder, final int position) {
        //设置title
        holder.direct_broadcast_list_item2_title.setText(live_broadcasts.get(position).getTitle());
        //设置 时间
        holder.direct_broadcast_list_item2_time.setText(Html.fromHtml(live_broadcasts.get(position).getTime()));
        switch (live_broadcasts.get(position).getBotten_text()) {
            case "已预约":
                holder.direct_broadcast_list_item2_button.setBackground(mContext.getResources().getDrawable(R.mipmap.direct_broadcast_icon17));
                break;
            default:
                holder.direct_broadcast_list_item2_button.setBackground(mContext.getResources().getDrawable(R.mipmap.direct_broadcast_icon08));
                break;
        }
        holder.direct_broadcast_list_item2_button.setText(live_broadcasts.get(position).getBotten_text());
        holder.direct_broadcast_list_item2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) v;
                switch (textView.getText().toString()) {
                    case "看回顾":
                        LogUtil.e("点击了按钮", "看回顾");
                        setmsgdb(live_broadcasts.get(position));
                        place = position;
                        break;
                    case "预约提醒":
                        LogUtil.e("点击了按钮", "预约提醒");
                        setmsgdb(live_broadcasts.get(position));
                        place = position;
                        break;
                    case "进行中":
                        LogUtil.e("点击了按钮", "进行中");
                        break;
                    case "已预约":
                        LogUtil.e("点击了按钮", "已预约");
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void setmsgdb(final Live_broadcast live_broadcast) {
        MyProgressBarDialogTools.show(mContext);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.reservation);
                    obj.put("name", live_broadcast.getTitle());
                    obj.put("hid", live_broadcast.getHid());
                    obj.put("uid", SharedPreferencesTools.getUid(mContext));

                    String result = HttpClientUtils.sendPost(mContext, URLConfig.CCMTVAPP_ccmtvapplive, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("直播预约提醒",result);

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
    public int getItemCount() {
        return live_broadcasts.size();
    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    class Holder extends RecyclerView.ViewHolder {
        TextView direct_broadcast_list_item2_title;//title
        TextView direct_broadcast_list_item2_time;//时间
        TextView direct_broadcast_list_item2_button;//button

        public Holder(View view) {
            super(view);
            direct_broadcast_list_item2_title = (TextView) view.findViewById(R.id.direct_broadcast_list_item2_title);
            direct_broadcast_list_item2_time = (TextView) view.findViewById(R.id.direct_broadcast_list_item2_time);
            direct_broadcast_list_item2_button = (TextView) view.findViewById(R.id.direct_broadcast_list_item2_button);

        }
    }

}

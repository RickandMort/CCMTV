package com.linlic.ccmtv.yx.activity.vip.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.adapter.VipVideoAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * name：首页横向滚动适配器
 * author：Larry
 * data：2017/7/13.
 */
public class VipRecyclerViewAdapter3 extends RecyclerView.Adapter<VipRecyclerViewAdapter3.ViewHolder> {
    private LayoutInflater mInflater;
    private List<Map<String ,Object>> arrays;
    private int selectIndex = -1;
    private Context context;
    private int page=1;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {
                            JSONArray dataJson = result.getJSONArray("data");
//                            List<Home_Video> home_videos=JsonUtils.fromJsonArray(dataJson.toString(),Home_Video.class);
//                            adapter.videos=home_videos;
//                            adapter.notifyDataSetChanged();
                            arrays.get(msg.arg1).put("video",dataJson);
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public VipRecyclerViewAdapter3(Context context, List<Map<String ,Object>>  arrays) {
        mInflater = LayoutInflater.from(context);
        this.arrays = arrays;
        this.context=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }
        MyGridView videoss;
        TextView video_title;//
        LinearLayout ll_turn;
    }

    @Override
    public int getItemCount() {
        return arrays.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_vip_video,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.videoss = (MyGridView) view.findViewById(R.id.videoss);
        viewHolder.video_title = (TextView) view.findViewById(R.id.video_title);
        viewHolder.ll_turn = (LinearLayout) view.findViewById(R.id.ll_turn);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.video_title.setText(arrays.get(position).get("title").toString());
       final String title=arrays.get(position).get("title").toString();
        if(title.equals("VIP今日排行")){
            viewHolder.ll_turn.setVisibility(View.GONE);
        }else {
            viewHolder.ll_turn.setVisibility(View.VISIBLE);
        }
        String my_title=arrays.get(position).get("title").toString();
        JSONArray jsonArray = (JSONArray) arrays.get(position).get("video");
        final List<Home_Video> home_videos=JsonUtils.fromJsonArray(jsonArray.toString(),Home_Video.class);
        VipVideoAdapter adapter=new VipVideoAdapter(context,home_videos,my_title);
        viewHolder.videoss.setAdapter(adapter);
        viewHolder.videoss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String uid = SharedPreferencesTools.getUidToLoginClose(context);
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if (VideoFive.isFinish == null) {
                    Intent intent = new Intent(context, VideoFive.class);
                    intent.putExtra("aid", home_videos.get(position).getAid());
                    context.startActivity(intent);
                }

            }
        });

        viewHolder.ll_turn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==position){
                    page=page+1;
                }else {
                  page=1;
                  flag=position;
                }
                turn_data(title,position);
            }
        });

    }

    private int flag=-1;


    public void setSelectIndex(int i) {
        selectIndex = i;
    }

    private void turn_data(final String type, final int position){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getanotherVideos);
                    obj.put("type",type);
                    obj.put("page",page);
                    if (SharedPreferencesTools.getUidONnull(context).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    }
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
                    LogUtil.e("换一换",result);
                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    message.arg1=position;
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

package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * name：上传列表使用
 * author：MrSong
 * data：2016/3/31.
 */
public class UploadVideoListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private List<ViewHolder> checkBoxs = new ArrayList<ViewHolder>();
    private LayoutInflater inflater;

    @Override
    public int getCount() {
        return list.size();
    }

    public UploadVideoListAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.upload_download_list_item, null);
            holder.image = (ImageView) convertView.findViewById(R.id.upload_down_item_img);
            holder.videoName = (TextView) convertView.findViewById(R.id.upload_down_item_videoname);
            holder.fileSize = (TextView) convertView.findViewById(R.id.upload_down_item_size);
            holder.speed = (TextView) convertView.findViewById(R.id.upload_down_item_speed);
            holder.prog = (TextView) convertView.findViewById(R.id.upload_down_item_prog);
            holder.videoId = (TextView) convertView.findViewById(R.id.upload_down_item_videoid);
            holder.state = (TextView) convertView.findViewById(R.id.upload_down_item_state);
            holder.select = (CheckBox) convertView.findViewById(R.id.upload_down_item_select);
            holder.select.setTag("1");
            holder.myPosition=position;
            checkBoxs.add(holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (list.size() != 0) {
            holder.videoName.setText(list.get(position).get("videoName"));
            holder.fileSize.setText(list.get(position).get("fileSize"));
            holder.speed.setText(list.get(position).get("speed"));
            holder.prog.setText(list.get(position).get("prog"));
            holder.videoId.setText(list.get(position).get("videoId"));
            holder.state.setText(list.get(position).get("state"));
            /*BitmapUtils bitmap = new BitmapUtils(context);
            bitmap.display(holder.image, FirstLetter.getSpells(list.get(position).get("picUrl")));*/
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default);
            Glide.with(context)
                    .load(list.get(position).get("picUrl"))
                    .apply(options)
                    .into(holder.image);

            //如果当前s上传已经完成，隐藏不需要的参数
            if (holder.state.getText().toString().equals("200")) {
                holder.speed.setVisibility(View.INVISIBLE);
                holder.prog.setVisibility(View.INVISIBLE);
                holder.fileSize.setText(holder.fileSize.getText().toString().substring(holder.fileSize.getText().toString().lastIndexOf("/") + 1));
            } else if (holder.state.getText().toString().equals("500")) {
                //如果当前上传失败，显示需要的逻辑
                holder.prog.setText("上传失败");
                holder.speed.setText("");
                holder.fileSize.setVisibility(View.VISIBLE);
                holder.speed.setVisibility(View.VISIBLE);
            } else if (holder.state.getText().toString().equals("100")) {
                //如果当前是上传暂停，显示需要的逻辑
                holder.prog.setText("已暂停");
                holder.fileSize.setVisibility(View.INVISIBLE);
                holder.speed.setVisibility(View.INVISIBLE);
            } else if (holder.state.getText().toString().equals("300")) {
                //如果当前是等待上传，显示需要的逻辑
                holder.prog.setText("等待上传");
                holder.fileSize.setVisibility(View.INVISIBLE);
                holder.speed.setVisibility(View.INVISIBLE);
            } else if (holder.state.getText().toString().equals("18")) {
                holder.speed.setVisibility(View.VISIBLE);
                holder.fileSize.setVisibility(View.VISIBLE);
                holder.prog.setVisibility(View.VISIBLE);
            }
        }


        return convertView;
    }


    public final class ViewHolder {
        public ImageView image;
        public TextView videoName;
        public TextView fileSize;
        public TextView speed;
        public TextView prog;
        public TextView videoId;
        public TextView state;
        public CheckBox select;
        public int myPosition;
    }
}

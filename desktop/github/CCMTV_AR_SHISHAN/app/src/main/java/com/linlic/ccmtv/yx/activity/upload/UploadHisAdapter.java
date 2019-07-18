package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.upload.entry.Upload;
import java.util.List;

/**
 * name：上传历史 适配器
 * author：Larry
 * data：2017/7/13.
 */
public class UploadHisAdapter extends BaseAdapter {
    private Context context;
    private List<Upload> uploads;

    public UploadHisAdapter(Context context, List<Upload> uploads) {
        this.context = context;
        this.uploads = uploads;
    }

    @Override
    public int getCount() {
        return uploads.size();
    }

    @Override
    public Object getItem(int position) {
        return uploads.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_uphis
                    , null);
            viewHolder.tv_uptime = (TextView) convertView.findViewById(R.id.tv_uptime);
            viewHolder.tv_upname = (TextView) convertView.findViewById(R.id.tv_upname);
            viewHolder.tv_upstate = (TextView) convertView.findViewById(R.id.tv_upstate);
            viewHolder.tv_uptype = (TextView) convertView.findViewById(R.id.tv_uptype);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_uptime.setText(uploads.get(position).getRow_add_time());
        viewHolder.tv_upname.setText(uploads.get(position).getMvtitle());
        viewHolder.tv_upstate.setText(uploads.get(position).getMvstatus());
        viewHolder.tv_uptype.setText(uploads.get(position).getStyletypeword());

        return convertView;
    }

    final static class ViewHolder {
        TextView tv_uptime;
        TextView tv_upname;
        TextView tv_upstate;
        TextView tv_uptype;
    }
}

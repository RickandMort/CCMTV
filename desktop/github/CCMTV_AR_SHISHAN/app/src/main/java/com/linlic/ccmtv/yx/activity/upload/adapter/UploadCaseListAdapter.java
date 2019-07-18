package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;

import java.util.List;
import java.util.Map;

/**
 * name：上传列表使用
 * author：MrSong
 * data：2016/3/31.
 */
public class UploadCaseListAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> list;
    private LayoutInflater inflater;

    public UploadCaseListAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
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

            convertView = inflater.inflate(R.layout.upload_item3, null);
            holder.tv_case_title = (TextView) convertView.findViewById(R.id.tv_case_title);
            holder.tv_case_prog = (TextView) convertView.findViewById(R.id.tv_case_prog);
            holder.tv_case_state = (TextView) convertView.findViewById(R.id.tv_case_state);
            holder.tv_case_id = (TextView) convertView.findViewById(R.id.tv_case_id);
            holder.tv_case_nowsize = (TextView) convertView.findViewById(R.id.tv_case_nowsize);
            holder.tv_case_totalsize = (TextView) convertView.findViewById(R.id.tv_case_totalsize);
            holder.tv_case_uptime = (TextView) convertView.findViewById(R.id.tv_case_uptime);
            holder.bnp = (ProgressBar) convertView.findViewById(R.id.numberbar5);
            holder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            holder.iv_upcase = (ImageView) convertView.findViewById(R.id.iv_upcase);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        double e = 0.0;
        if (list.size() != 0) {
            holder.tv_case_title.setText(list.get(position).get("caseTitle"));
            holder.tv_case_prog.setText(list.get(position).get("caseProg"));
            holder.tv_case_state.setText(list.get(position).get("state"));
            holder.tv_case_id.setText(list.get(position).get("case_id"));
            holder.tv_case_nowsize.setText(list.get(position).get("currentsize") + "  /");
            holder.tv_case_totalsize.setText(list.get(position).get("totalsize"));
            /*XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(context);
            xUtilsImageLoader.display(holder.iv_upcase, list.get(position).get("upfileA_1"));*/
            RequestOptions options = new RequestOptions()
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop();
            Glide.with(context)
                    .load(FirstLetter.getSpells(list.get(position).get("upfileA_1")))
                    .apply(options)
                    .into(holder.iv_upcase);
            try {
                String d = list.get(position).get("caseProg").substring(0, list.get(position).get("caseProg").lastIndexOf("%"));
                e = Double.valueOf(d);  //string 先转成double 在转成int
            } catch (Exception es) {
                es.printStackTrace();
            }
            if (holder.tv_case_state.getText().toString().equals("500")) {
                holder.tv_case_state.setText("上传失败");
                holder.tv_case_state.setVisibility(View.VISIBLE);
            } else if (holder.tv_case_state.getText().toString().equals("200")) {
                holder.tv_case_prog.setVisibility(View.GONE);
                holder.bnp.setVisibility(View.GONE);
                holder.tv_case_nowsize.setVisibility(View.GONE);
                holder.bnp.setVisibility(View.GONE);
                holder.tv_case_uptime.setVisibility(View.VISIBLE);
                holder.tv_case_uptime.setText(list.get(position).get("time"));
                holder.iv_state.setVisibility(View.GONE);
            } else if (holder.tv_case_state.getText().toString().equals("300")) {
                holder.iv_state.setImageResource(R.mipmap.ding_11);
                holder.bnp.setProgress(0);

            } else if (holder.tv_case_state.getText().toString().equals("18")) {
                holder.iv_state.setImageResource(R.mipmap.img_up);
                holder.bnp.setProgress((int) (e));
            }
        }
        return convertView;
    }


    public final class ViewHolder {
        public TextView tv_case_title;
        public TextView tv_case_prog;
        public TextView tv_case_state;
        public TextView tv_case_id;
        public TextView tv_case_nowsize;
        public TextView tv_case_totalsize;
        public TextView tv_case_uptime;
        public ProgressBar bnp;
        public ImageView iv_state;
        public ImageView iv_upcase;
    }
}

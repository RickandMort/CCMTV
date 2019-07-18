package com.linlic.ccmtv.yx.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/3/14.
 */
public class ContactAdapter extends BaseAdapter {
    private List<Video_menu_expert> list;
    private int res;
    private Context context;
    // 屏幕的高度
    int screenWidth = 0;
    // 总共可以放多少个字
    int count = 0;
    // textView全部字符的宽度
    float textTotalWidth = 0.0f;
    // textView一个字的宽度
    float textWidth = 0.0f;
    Paint paint = new Paint();

    boolean imageMeasured = false;
    private LayoutInflater layoutInflater;

    public ContactAdapter(Context context, int resource, List<Video_menu_expert> objects, int screenWidth) {
        this.screenWidth = screenWidth;
        this.res = resource;
        this.list = objects;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int item) {
        return list.get(item);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView video_menu_expert_img = null;
        TextView video_menu_expert_id = null;
        TextView video_menu_expert_name = null;
        TextView video_menu_expert_cont_right = null;
        TextView video_menu_expert_cont_bottom = null;


        if (convertView == null) {
            convertView = layoutInflater.inflate(res, null);
            video_menu_expert_img = (ImageView) convertView.findViewById(R.id.video_menu_expert_img);
            video_menu_expert_id = (TextView) convertView.findViewById(R.id.video_menu_expert_id);
            video_menu_expert_name = (TextView) convertView.findViewById(R.id.video_menu_expert_name);
            video_menu_expert_cont_right = (TextView) convertView.findViewById(R.id.video_menu_expert_cont_right);
            video_menu_expert_cont_bottom = (TextView) convertView.findViewById(R.id.video_menu_expert_cont_bottom);
            convertView.setTag(new ViewHolder(video_menu_expert_img, video_menu_expert_id, video_menu_expert_name, video_menu_expert_cont_right, video_menu_expert_cont_bottom));
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            video_menu_expert_img = holder.video_menu_expert_img;
            video_menu_expert_id = holder.video_menu_expert_id;
            video_menu_expert_name = holder.video_menu_expert_name;
            video_menu_expert_cont_right = holder.video_menu_expert_cont_right;
            video_menu_expert_cont_bottom = holder.video_menu_expert_cont_bottom;
        }

        video_menu_expert_id.setText(list.get(position).getVideo_menu_expert_id());
        video_menu_expert_name.setText(list.get(position).getVideo_menu_expert_name());
        loadImg(video_menu_expert_img, list.get(position).getVideo_menu_expert_img());

        /**
         * 获取一个字的宽度
         */
        textWidth = video_menu_expert_cont_right.getTextSize();
        paint.setTextSize(textWidth);

        LayoutParams para;
        para = video_menu_expert_img.getLayoutParams();
        int height = para.height - video_menu_expert_cont_right.getLineHeight() - video_menu_expert_name.getLineHeight();
        int width = para.width + video_menu_expert_img.getPaddingLeft() + video_menu_expert_img.getPaddingRight();
        try {
            drawImageViewDone(width, height, list.get(position).getVideo_menu_expert_cont(), video_menu_expert_cont_right, video_menu_expert_cont_bottom);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private final class ViewHolder {
        public ImageView video_menu_expert_img = null;
        public TextView video_menu_expert_id = null;
        public TextView video_menu_expert_name = null;
        public TextView video_menu_expert_cont_right = null;
        public TextView video_menu_expert_cont_bottom = null;

        public ViewHolder(ImageView video_menu_expert_img, TextView video_menu_expert_id, TextView video_menu_expert_name, TextView video_menu_expert_cont_right, TextView video_menu_expert_cont_bottom) {
            this.video_menu_expert_img = video_menu_expert_img;
            this.video_menu_expert_id = video_menu_expert_id;
            this.video_menu_expert_name = video_menu_expert_name;
            this.video_menu_expert_cont_right = video_menu_expert_cont_right;
            this.video_menu_expert_cont_bottom = video_menu_expert_cont_bottom;
        }
    }

    private void drawImageViewDone(int width, int height, String text, TextView video_menu_expert_cont_right, TextView video_menu_expert_cont_bottom) {
        text.replace("\n", "").replace("\r", "");
        // 一行字体的高度
        int lineHeight = video_menu_expert_cont_right.getLineHeight();
        // 可以放多少行
        int lineCount = (int) Math.ceil((double) height / (double) lineHeight);

        // 一行的宽度
        float rowWidth = screenWidth - width - video_menu_expert_cont_right.getPaddingLeft()
                - video_menu_expert_cont_right.getPaddingRight();
      /*  Paint paint2 = new Paint();
        float size = paint2.measureText(text);*/
        // 一行可以放多少个字
        int columnCount = (int) ((rowWidth) / textWidth);
        // 总共字体数等于 行数*每行个数
        count = lineCount * columnCount + 5;
//        count = lineCount * columnCount ;
        // 一个TextView中所有字符串的宽度和（字体数*每个字的宽度）
        textTotalWidth = (float) ((float) count * textWidth);
//        measureText(text);
        if (text.length() > count)
            video_menu_expert_cont_right.setText(text.substring(0, count));
        else
            video_menu_expert_cont_right.setText(text);
        // 检查行数是否大于设定的行数，如果大于的话，就每次减少一个字符，重新计算行数与设定的一致
       /* while (video_menu_expert_cont_right.getLineCount() > lineCount) {
//            count -= 1;
            video_menu_expert_cont_right.setText(text.substring(0, count));
        }*/
        video_menu_expert_cont_bottom.setPadding(0, lineCount * lineHeight - height, 0, 0);
        try {
            video_menu_expert_cont_bottom.setText(text.substring(count));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 测量已经填充的长度，计算其剩下的长度
     */
    private void measureText(String text) {
        String string = text.substring(0, count);
        float size = paint.measureText(string);
        int remainCount = (int) ((textTotalWidth - size) / textWidth);
        if (remainCount > 0) {
            count += remainCount;
            measureText(text);
        }
    }


    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {


        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(context);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));

    }
}

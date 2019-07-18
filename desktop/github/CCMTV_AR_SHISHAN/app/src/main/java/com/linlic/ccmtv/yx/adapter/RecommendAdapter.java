package com.linlic.ccmtv.yx.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * name：推荐Adapter
 * author：Larry
 * data：2016/9/21.
 */
public class RecommendAdapter extends BaseAdapter {
    private ArrayList<String> mList;// 数据集合
    private ArrayList<Integer> list;// 位置集合
    private Context mContext;
    List<Map<String, Object>> data = new ArrayList<>();

    public RecommendAdapter(ArrayList<String> mList, ArrayList<Integer> list, List<Map<String, Object>> data,
                            Context mContext) {
        super();
        this.mList = mList;
        this.list = list;
        this.data = data;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view = View.inflate(mContext, R.layout.recommed_gvitem, null);
        TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
        TextView tv_keshi = (TextView) view.findViewById(R.id.tv_keshi);
        ImageView iv_select = (ImageView) view.findViewById(R.id.iv_select);
        com.linlic.ccmtv.yx.widget.CircleImageView headimg = (com.linlic.ccmtv.yx.widget.CircleImageView) view.findViewById(R.id.myrecommend_myhead);
        try {
            if (data.size() != 0) {
                loadImg(headimg, data.get(position).get("icon").toString());
                tv_username.setText(data.get(position).get("username").toString());

                if ("请选择科室".equals(data.get(position).get("keshi")) || "".equals(data.get(position).get("keshi"))) {
                    tv_keshi.setText("未知科室");
                } else {
                    tv_keshi.setText(data.get(position).get("keshi").toString());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        // 选中时，改变背景色,索引要一致
        for (int i = 0; i < list.size(); i++) {
            if (position == list.get(i)) {
                // layout.setBackgroundResource(R.drawable.mainbg);
                Drawable drawable = mContext.getResources().getDrawable(
                        R.drawable.select_check);
                iv_select.setImageDrawable(drawable);
                iv_select.setSelected(true);
                iv_select.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }


    /**
     * name:使用xutils 加载图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
       /* XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(mContext);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));*/

        ImageLoader.getInstance().displayImage(FirstLetter.getSpells(path), img);
    }
}

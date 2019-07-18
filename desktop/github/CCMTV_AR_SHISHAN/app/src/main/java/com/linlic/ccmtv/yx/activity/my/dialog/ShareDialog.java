package com.linlic.ccmtv.yx.activity.my.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.widget.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * name：分享弹出框
 * author：Larry
 * data：2016/6/30 15:10
 */
public class ShareDialog {

    private AlertDialog dialog;
    private MyGridView gridView;
    private RelativeLayout cancelButton;
    private SimpleAdapter saImageItems;
    private int[] image = {R.mipmap.umeng_socialize_sina_on, R.mipmap.umeng_socialize_wechat, R.mipmap.umeng_socialize_wxcircle, R.mipmap.umeng_socialize_qq_on, R.mipmap.umeng_socialize_qzone_on, R.mipmap.umeng_socialize_bcopy};
    private String[] name = {"微博", "微信好友", "朋友圈", "QQ", "QQ空间", "复制链接"};

    public ShareDialog(Context context) {

        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 非常重要：设置对话框弹出的位置
        window.setContentView(R.layout.share_dialog);
        gridView = (MyGridView) window.findViewById(R.id.share_gridView);
        cancelButton = (RelativeLayout) window.findViewById(R.id.share_cancel);
        List<HashMap<String, Object>> shareList = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < image.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", image[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            shareList.add(map);
        }

        saImageItems = new SimpleAdapter(context, shareList, R.layout.share_item, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.imageView1, R.id.textView1});
        gridView.setAdapter(saImageItems);
    }

    public void setCancelButtonOnClickListener(OnClickListener Listener) {
        cancelButton.setOnClickListener(Listener);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }


    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }
}
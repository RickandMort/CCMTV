package com.linlic.ccmtv.yx.activity.upload.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.holder.BaseViewHolder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * name：
 * author：MrSong
 * data：2016/3/26.
 */
public class MyUploadGridAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> path;

    public MyUploadGridAdapter(Context context,ArrayList<String> path){
        this.context = context;
        this.path = path;

        //添加第一张默认图
//        String aaa = drawableToByte(context.getDrawable(R.mipmap.upload_addpic));
//        Log.d("aaaaaaaaaaaaaaaaaa:",aaa);
//        path.add();
    }
    private String convertIconToString(){
        BitmapDrawable bd = (BitmapDrawable) context.getDrawable(R.mipmap.upload_addpic);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bd.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }


    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image, null);
        }
        ImageView iv = BaseViewHolder.get(convertView, R.id.upload_image);
        new BitmapUtils(context).display(iv, path.get(position));
        return convertView;
    }

    private String drawableToByte(Drawable drawable) {
        if (drawable != null) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            int size = bitmap.getWidth() * bitmap.getHeight() * 4;

            // 创建一个字节数组输出流,流的大小为size
            ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
            // 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            // 将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();

            String icon = Base64.encodeToString(imagedata, Base64.DEFAULT);
            return icon;
        }
        return null;
    }
}

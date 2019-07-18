package com.linlic.ccmtv.yx.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.entity.Special;
import com.linlic.ccmtv.yx.activity.home.VideoFive;
import com.linlic.ccmtv.yx.activity.home.special_more;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/14.
 */
public class SpecialAdapter extends BaseAdapter {
    private List<Special> list;
    private int res;
    private Context context;
    private Activity context11;
    private LayoutInflater layoutInflater;

    public SpecialAdapter(Context context, int resource, List<Special> objects) {
        this.res = resource;
        this.list = objects;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.context11 = (Activity) context;
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
        final TextView title_max;
        LinearLayout title_max_more;
        ImageView title_max_more_img;
        TextView special_text1;
        ImageView special_img1;
        TextView special_text2;
        ImageView special_img2;
        TextView special_text3;
        ImageView special_img3;
        List<ImageView> images = new ArrayList<ImageView>();
        List<TextView> texts = new ArrayList<TextView>();

        if (convertView == null) {
            convertView = layoutInflater.inflate(res, null);
            title_max = (TextView) convertView.findViewById(R.id.title_max);
            title_max_more = (LinearLayout) convertView.findViewById(R.id.special_more);
            special_text1 = (TextView) convertView.findViewById(R.id.special_text1);
            special_img1 = (ImageView) convertView.findViewById(R.id.special_img1);
            special_text2 = (TextView) convertView.findViewById(R.id.special_text2);
            special_img2 = (ImageView) convertView.findViewById(R.id.special_img2);
            special_text3 = (TextView) convertView.findViewById(R.id.special_text3);
            special_img3 = (ImageView) convertView.findViewById(R.id.special_img3);
            images.add(special_img1);
            images.add(special_img2);
            images.add(special_img3);
            texts.add(special_text1);
            texts.add(special_text2);
            texts.add(special_text3);
            setVideoOnClick(images, texts);
            title_max_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, special_more.class);
                    intent.putExtra("fid", v.getTag().toString());
                    intent.putExtra("special_more", title_max.getText());
                    context.startActivity(intent);
                }
            });
            convertView.setTag(new ViewHolder(title_max, title_max_more, special_text1, special_img1, special_text2, special_img2, special_text3, special_img3));
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            title_max = holder.title_max;
            title_max_more = holder.title_max_more;
            special_text1 = holder.special_text1;
            special_img1 = holder.special_img1;
            special_text2 = holder.special_text2;
            special_img2 = holder.special_img2;
            special_text3 = holder.special_text3;
            special_img3 = holder.special_img3;
            images.removeAll(images);
            texts.removeAll(texts);
            images.add(special_img1);
            images.add(special_img2);
            images.add(special_img3);
            texts.add(special_text1);
            texts.add(special_text2);
            texts.add(special_text3);
        }
        System.out.println("listtitlemax:" + list.get(position).getTitle_max());
        title_max.setText(list.get(position).getTitle_max());
        title_max_more.setTag(list.get(position).getTitle_max_id());
        List<Map<String, Object>> datas = list.get(position).getSpecials();


        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> map = datas.get(i);
            images.get(i).setTag(map.get("special_aid"));
            texts.get(i).setTag(map.get("special_aid"));
            texts.get(i).setText(map.get("special_text").toString());
            loadImg(images.get(i), map.get("special_img").toString());
        }


        return convertView;
    }

    /**
     * name:用于首页四种类别 视频点击事件
     * author:Tom
     * 2016-3-2下午4:25:14
     *
     * @param imageViews 图片集合
     * @param textViews  文字集合
     */
    public void setVideoOnClick(List<ImageView> imageViews, List<TextView> textViews) {

        for (ImageView imageView : imageViews) {

            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    // TODO Auto-generated method stub
                    //获取视频信息
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String uid = SharedPreferencesTools.getUidToLoginClose(context);
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.video_play_act);
                                obj.put("uid", SharedPreferencesTools.getUid(context));
                                obj.put("aid", view.getTag());
                                String data = HttpClientUtils.sendPost(context,
                                        URLConfig.CCMTVAPP, obj.toString());
                                System.out.println("视频详细信息:" + obj + "|" + data);
                                final JSONObject result = new JSONObject(data);
                                if (result.getInt("status") == 1) {// 成功
                                    final JSONObject dataArray = result
                                            .getJSONObject("data");
                                    System.out.println("进入到视频信息解析页：" + dataArray);


                                    context11.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                //存贮对象声明(把视频ID存起来)
                                                SharedPreferences myshPreferences = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = myshPreferences.edit();
                                                editor.putString("aid", dataArray.getString("aid"));//存储ID
                                                editor.commit();
                                                //跳转页面
                                                Intent intent = new Intent(context, VideoFive.class);
                                                intent.putExtra("aid", dataArray.getString("aid"));
                                                /*intent.putExtra("hits", dataArray.getString("hits"));
                                                intent.putExtra("title", dataArray.getString("title"));
                                                intent.putExtra("money", dataArray.getString("money"));
                                                intent.putExtra("down_num", dataArray.getString("down_num"));
                                                intent.putExtra("digg_num", dataArray.getString("digg_num"));
                                                intent.putExtra("picurl", dataArray.get("picurl").toString());
                                                intent.putExtra("zanflg", dataArray.get("zanflg").toString());
                                                intent.putExtra("downflg", dataArray.get("downflg").toString());
                                                intent.putExtra("collectionflg", dataArray.get("collectionflg").toString());
                                                intent.putExtra("my_video_id", dataArray.getString("my_video_id"));
                                                intent.putExtra("mvurl", dataArray.getString("mvurl"));
                                                intent.putExtra("videoplaystyle", dataArray.getString("videoplaystyle"));
                                                intent.putExtra("smvpurl", dataArray.getString("smvpurl"));
                                                intent.putExtra("videoClass", dataArray.getString("video_class"));
                                                intent.putExtra("personalmoney", dataArray.getString("personalmoney"));//用户余额
                                                intent.putExtra("videopaymoney", dataArray.getString("videopaymoney"));//视频收费的金额（如果不为0代表改视频为收费）
                                                intent.putExtra("vipflg_str", dataArray.getString("vipflg_str"));//产品编号 （收费视频必传）
                                                intent.putExtra("subject", dataArray.getString("subject"));//产品编号 （收费视频必传）*/
                                                if (dataArray.getString("videoplaystyle").equals("noresource"))
                                                    Toast.makeText(context,
                                                            result.getString("errorMessage"),
                                                            Toast.LENGTH_SHORT).show();
                                                else
                                                    context.startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } else {// 失败
                                    Toast.makeText(context,
                                            result.getString("errorMessage"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    };
                    new Thread(runnable).start();
                }
            });


        }

        for (TextView textView : textViews) {


            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    // TODO Auto-generated method stub

                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            String uid = SharedPreferencesTools.getUidToLoginClose(context);

                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.video_play_act);
                                obj.put("uid",  SharedPreferencesTools.getUid(context));
                                obj.put("aid", view.getTag());
                                String data = HttpClientUtils.sendPost(context,
                                        URLConfig.CCMTVAPP, obj.toString());
                                System.out.println("视频详细信息:" + obj + "|" + data);
                                final JSONObject result = new JSONObject(data);
                                if (result.getInt("status") == 1) {// 成功
                                    final JSONObject dataArray = result
                                            .getJSONObject("data");
                                    System.out.println("进入到视频信息解析页：" + dataArray);


                                    context11.runOnUiThread(new Runnable() {
                                        public void run() {
                                            try {
                                                //存贮对象声明(把视频ID存起来)
                                                SharedPreferences myshPreferences = context.getSharedPreferences("ccmtv", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = myshPreferences.edit();
                                                editor.putString("aid", dataArray.getString("aid"));//存储ID
                                                editor.commit();
                                                //跳转页面
                                                Intent intent = new Intent(context, VideoFive.class);
                                                intent.putExtra("aid", dataArray.getString("aid"));
                                                /*intent.putExtra("hits", dataArray.getString("hits"));
                                                intent.putExtra("title", dataArray.getString("title"));
                                                intent.putExtra("money", dataArray.getString("money"));
                                                intent.putExtra("down_num", dataArray.getString("down_num"));
                                                intent.putExtra("digg_num", dataArray.getString("digg_num"));
                                                intent.putExtra("picurl", dataArray.get("picurl").toString());
                                                intent.putExtra("zanflg", dataArray.get("zanflg").toString());
                                                intent.putExtra("downflg", dataArray.get("downflg").toString());
                                                intent.putExtra("collectionflg", dataArray.get("collectionflg").toString());
                                                intent.putExtra("my_video_id", dataArray.getString("my_video_id"));
                                                intent.putExtra("mvurl", dataArray.getString("mvurl"));
                                                intent.putExtra("videoplaystyle", dataArray.getString("videoplaystyle"));
                                                intent.putExtra("smvpurl", dataArray.getString("smvpurl"));
                                                intent.putExtra("videoClass", dataArray.getString("video_class"));
                                                intent.putExtra("personalmoney", dataArray.getString("personalmoney"));//用户余额
                                                intent.putExtra("videopaymoney", dataArray.getString("videopaymoney"));//视频收费的金额（如果不为0代表改视频为收费）
                                                intent.putExtra("vipflg_str", dataArray.getString("vipflg_str"));//产品编号 （收费视频必传）
                                                intent.putExtra("subject", dataArray.getString("subject"));//产品编号 （收费视频必传）*/
                                                if (dataArray.getString("videoplaystyle").equals("noresource"))
                                                    Toast.makeText(context,
                                                            result.getString("errorMessage"),
                                                            Toast.LENGTH_SHORT).show();
                                                else
                                                    context.startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                } else {// 失败
                                    Toast.makeText(context,
                                            result.getString("errorMessage"),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    };
                    new Thread(runnable).start();
                }
            });
        }

    }


    private final class ViewHolder {
        private TextView title_max;
        private LinearLayout title_max_more;
        private TextView special_text1;
        private ImageView special_img1;
        private TextView special_text2;
        private ImageView special_img2;
        private TextView special_text3;
        private ImageView special_img3;

        public ViewHolder(TextView title_max, LinearLayout title_max_more, TextView special_text1, ImageView special_img1, TextView special_text2, ImageView special_img2, TextView special_text3, ImageView special_img3) {
            this.title_max = title_max;
            this.title_max_more = title_max_more;
            this.special_text1 = special_text1;
            this.special_img1 = special_img1;
            this.special_text2 = special_text2;
            this.special_img2 = special_img2;
            this.special_text3 = special_text3;
            this.special_img3 = special_img3;
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

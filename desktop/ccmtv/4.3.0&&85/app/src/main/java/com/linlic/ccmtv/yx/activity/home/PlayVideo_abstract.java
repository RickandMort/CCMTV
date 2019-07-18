package com.linlic.ccmtv.yx.activity.home;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.GalleryAdapter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.Video_menu_expert;
import com.linlic.ccmtv.yx.utils.XUtilsImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PlayVideo_abstract extends BaseActivity {
    private GalleryAdapter mAdapter;
    private RecyclerView hlv;
    private String aid;
    private WebView webView;
    private ImageView imageView2;
    private ImageView imageview_Ly_bg;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private Context context;
    //用户统计
    private String  fid;
    private List<Video_menu_expert> experts = new ArrayList<Video_menu_expert>();//专家
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //全屏后底部点赞view
            switch (msg.what) {
                case 1:
                    mAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("status") == 1) { // 成功
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            textView2.setText(dataObject.getString("title"));
                            textView3.setText("时间：" + dataObject.getString("posttime"));
                            textView4.setText(dataObject.getString("video_class") + "·" + dataObject.getString("keywords").replaceAll(" ", "·"));
                            String hits = dataObject.getString("hits");
                            if (hits.length() > 4) {
                                if (Integer.parseInt(hits.substring(hits.length() - 4, hits.length() - 3)) > 0) {
                                    hits = hits.substring(0, hits.length() - 4) + "." + hits.substring(hits.length() - 4, hits.length() - 3) + "万";
                                } else {
                                    hits = hits.substring(0, hits.length() - 4) + "万";
                                }
                            }
                            textView5.setText("播放数：" + hits);
                            loadImg(imageView2, dataObject.getString("picurl"));
                            returnBitmap(dataObject.getString("picurl"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        MyProgressBarDialogTools.hide();
                    }
                case 500:
                    MyProgressBarDialogTools.hide();
                    break;
            }
        }
    };


    private void returnBitmap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL fileUrl = new URL(FirstLetter.getSpells(url));
                    HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(is);
                    is.close();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageview_Ly_bg.setImageDrawable(new BitmapDrawable(context.getResources(), blurBitmap(bitmap)));
                            MyProgressBarDialogTools.hide();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Bitmap blurBitmap(Bitmap bitmap) {

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context.getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_menu_abstract2);
        context = this;
        super.findId();
        initDate();
        findByid();
        //加载专家
        getExpertRulest();
        loadWeb();
        getabstract();
    }

    public void initDate() {
        Intent intent = getIntent();
        aid = intent.getStringExtra("aid");
        fid = intent.getStringExtra("fid");
        MyProgressBarDialogTools.show(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/video/" + fid + "/" + aid + ".html";//拼接url
        super.onPause();
    }

    public void loadWeb() {
        webView.loadUrl("http://www.ccmtv.cn/do/ccmtvappandroid/getVideoRemark.php?" + aid);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void findByid() {
        hlv = (RecyclerView) findViewById(R.id.horizontallistview1);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        hlv.setLayoutManager(linearLayoutManager3);
        //设置适配器
        mAdapter = new GalleryAdapter(this, experts);
        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PlayVideo_abstract.this, PlayVideo_expert.class);
                intent.putExtra("aid", ((TextView) view.findViewById(R.id.department_id)).getText());
                intent.putExtra("fid", fid);
                PlayVideo_abstract.this.startActivity(intent);
            }
        });
        hlv.setAdapter(mAdapter);
        webView = (WebView) findViewById(R.id.video_abstract);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView5 = (TextView) findViewById(R.id.textView5);
        imageview_Ly_bg = (ImageView) findViewById(R.id.imageview_Ly_bg);
    }

    public void getExpertRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getVideoExpert);
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(PlayVideo_abstract.this, URLConfig.CCMTVAPP, obj.toString());

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        experts.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject data = dataArray.getJSONObject(i);
                            Video_menu_expert expert = new Video_menu_expert();
                            expert.setVideo_menu_expert_cont(data.getString("content"));
                            expert.setVideo_menu_expert_id(data.getString("aid"));
                            expert.setVideo_menu_expert_img(data.getString("picurl"));
                            expert.setVideo_menu_expert_name(data.getString("title"));
                            experts.add(expert);
                        }
                    }
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getabstract() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.introductionInfo);
                    obj.put("aid", aid);
                    String result = HttpClientUtils.sendPost(PlayVideo_abstract.this, URLConfig.CCMTVAPP, obj.toString());

                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getInt("status") == 1) {// 成功
                        Message message = new Message();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);
                    } else {//错误处理
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name:使用xutils 夹在图片 author:Tom 2016-1-7下午1:28:03
     *
     * @param img  图片控件
     * @param path 图片网络地址
     */
    public void loadImg(ImageView img, String path) {
        XUtilsImageLoader xUtilsImageLoader = new XUtilsImageLoader(PlayVideo_abstract.this);
        xUtilsImageLoader.display(img, FirstLetter.getSpells(path));
    }


}


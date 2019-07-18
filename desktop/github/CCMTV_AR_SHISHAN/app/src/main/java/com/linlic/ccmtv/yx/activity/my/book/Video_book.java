package com.linlic.ccmtv.yx.activity.my.book;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_Entity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_chapter;
import com.linlic.ccmtv.yx.activity.my.OpenMenberActivity2;
import com.linlic.ccmtv.yx.adapter.Video_book_Adapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.AutoTextView;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2018/4/11.
 * 电子书介绍页
 */

public class Video_book extends BaseActivity implements View.OnClickListener {
    Context context;
    private MyGridView horizontalListView;
    private Video_book_Adapter video_book_main_gridAdapter;
    private List<Video_book_Entity> video_book_entities = new ArrayList<>();
    private ImageView cover_chart;//封面图
    private String cover_chart_url = "";//封面图URL
    private TextView title;//点子书名
    private TextView author;//作者
    private TextView price;//价格
    private LinearLayout video_layout;//相关视频 容器
    private AutoTextView synopsis;//简介
    private Button buy_e_books;//购买电子书或观看电子书
    private Button purchase_video;//购买视频
    private TextView buy;//购买
    private int is = 1;// 0 代表未购买 1 代表购买了或兑换了书与视频 2 代表购买了书 3代表购买了视频
    private Dialog dialog;
    private View view;
    private String book_url = "";
    private String type = "0";
    private String vipflg_str = "";
    private String vipflg_video_str = "";
    private String vipflg_pdf_str = "";
    private String currently = "";//当前观看章节
    private String pageid = "";//当前观看页码
    private String moneydzs = "";//电子书价格
    private String moneyvideo = "";//视频价格
    private String moneyall = "";//总价格
    public static Video_book_Entity video_book_entity;
    Intent intent = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONObject jsonObject = jsonObjects.getJSONObject("data");
                            //封面图
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(jsonObject.getString("imgurl")), cover_chart);
                            cover_chart_url = jsonObject.getString("imgurl");
                            //电子书名
                            title.setText(jsonObject.getString("name"));
                            //作者
                            author.setText(jsonObject.getString("zz"));
                            //价格
                            price.setText(Html.fromHtml(jsonObject.getString("moneyabout")));
                            //简介
                            synopsis.setText(jsonObject.getString("jianjie") + "");
                            synopsis.setText2(jsonObject.getString("jianjie"));
                            //电子书地址
                            book_url = jsonObject.getString("pdfurl");
                            //支付所需字段
                            vipflg_str = jsonObject.getString("vipflg_str2");
                            vipflg_video_str = jsonObject.getString("vipflg_str");
                            vipflg_pdf_str = jsonObject.getString("vipflg_str1");
                            //当前观看章节
                            currently = jsonObject.getString("currently");
                            //当前观看页码
                            pageid = jsonObject.getString("pageid");
                            //电子书价格
                            moneydzs = jsonObject.getString("moneydzs");
                            //视频价格
                            moneyvideo = jsonObject.getString("moneyvideo");
                            //总价格
                            moneyall = jsonObject.getString("moneyall");

                            video_book_entity.setAuthor(jsonObject.getString("zz"));
                            video_book_entity.setCover_chart(jsonObject.getString("imgurl"));
                            video_book_entity.setName(jsonObject.getString("name"));
                            video_book_entity.setId(getIntent().getStringExtra("book_id"));
                            video_book_entity.setPageid(jsonObject.getString("pageid"));
                            video_book_entity.setCurrently(jsonObject.getString("currently"));

                            List<Video_book_chapter> video_book_chapters = new ArrayList<>();
                            JSONArray pdfUrlJsonArr = jsonObject.getJSONArray("pdfurl");
                            for (int i = 0; i < pdfUrlJsonArr.length(); i++) {
                                JSONObject pdfUrlJson = pdfUrlJsonArr.getJSONObject(i);
                                Video_book_chapter video_book_chapter = new Video_book_chapter();
                                video_book_chapter.setName(pdfUrlJson.getString("title"));
                                video_book_chapter.setPdfStr(pdfUrlJson.getString("pdfurl"));
                                video_book_chapter.setId(pdfUrlJson.getString("cid"));
                                video_book_chapter.setPosition(i);
                                if (jsonObject.getString("currently").equals(pdfUrlJson.getString("cid"))) {
                                    video_book_entity.setCurrent_position(i);
                                }
                                video_book_chapters.add(video_book_chapter);
                            }
                            video_book_entity.setBook_url(video_book_chapters);

                            if (!jsonObject.getString("ifpay").equals("0")) {
                                //控制各种按钮的状态
                                // 0 代表未购买 1 代表购买了或兑换了书与视频 2 代表购买了书 3代表购买了视频
                                is = jsonObject.getInt("paystyle");
                                switch (is) {
                                    case 0:// 0 代表未购买
                                        buy_e_books.setVisibility(View.GONE);
                                        purchase_video.setVisibility(View.GONE);
                                        buy.setVisibility(View.VISIBLE);
                                        break;
                                    case 1:// 代表购买了书
                                        buy_e_books.setVisibility(View.VISIBLE);
                                        buy_e_books.setText("阅读");
                                        buy_e_books.setBackground(getResources().getDrawable(R.mipmap.button_03));
                                        purchase_video.setVisibility(View.VISIBLE);
                                        purchase_video.setText("购买");
                                        purchase_video.setBackground(getResources().getDrawable(R.mipmap.button_01));
                                        buy.setVisibility(View.GONE);
                                        break;
                                    case 2://2 代表购买了视频
                                        buy_e_books.setVisibility(View.VISIBLE);
                                        buy_e_books.setText("购买");
                                        buy_e_books.setBackground(getResources().getDrawable(R.mipmap.button_01));
                                        purchase_video.setVisibility(View.GONE);
                                        buy.setVisibility(View.GONE);
                                        break;
                                    case 3://3 代表购买了或兑换了书与视频
                                        buy_e_books.setVisibility(View.VISIBLE);
                                        buy_e_books.setText("阅读");
                                        buy_e_books.setBackground(getResources().getDrawable(R.mipmap.button_03));
                                        purchase_video.setVisibility(View.GONE);
                                        buy.setVisibility(View.GONE);
                                        break;
                                    default:
                                        buy_e_books.setVisibility(View.GONE);
                                        purchase_video.setVisibility(View.GONE);
                                        buy.setVisibility(View.VISIBLE);
                                        break;
                                }
                            } else {
                                buy_e_books.setVisibility(View.GONE);
                                purchase_video.setVisibility(View.GONE);
                                buy.setVisibility(View.VISIBLE);
                            }

                            //判断接口中是否传输了相关视频字段 如果没有该字段 则界面中隐藏其模块
                            if (jsonObject.has("aboutvideo")) {
                                video_layout.setVisibility(View.VISIBLE);
                                video_book_entities.removeAll(video_book_entities);
                                JSONArray videos = jsonObject.getJSONArray("aboutvideo");
                                for (int i = 0; i < videos.length(); i++) {
                                    JSONObject video = videos.getJSONObject(i);
                                    Video_book_Entity video_book_entity = new Video_book_Entity();
                                    video_book_entity.setId(video.getString("aid"));
                                    video_book_entity.setName(video.getString("title"));
                                    video_book_entity.setEstate(video.getString("vtime"));
                                    video_book_entity.setCover_chart(video.getString("picurl"));
                                    video_book_entities.add(video_book_entity);
                                }
                            } else {
                                video_layout.setVisibility(View.GONE);
                            }
                            video_book_main_gridAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Video_book.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:

                    // 弹出自定义dialog
                    LayoutInflater inflater = LayoutInflater.from(Video_book.this);
                    view = inflater.inflate(R.layout.dialog_item4, null);

                    // 对话框
                    dialog = new Dialog(Video_book.this);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                    // 设置宽度为屏幕的宽度
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                    lp.width = (int) (display.getWidth() - 100); // 设置宽度
                    dialog.getWindow().setAttributes(lp);
                    dialog.getWindow().setContentView(view);
                    dialog.setCancelable(false);
                    final TextView btn_sure = (TextView) view.findViewById(R.id.i_understand);// 取消
                    final TextView name = (TextView) view.findViewById(R.id.name);// title
                    name.setText(title.getText().toString());
                    final ImageView cover_chart2 = (ImageView) view.findViewById(R.id.cover_chart);// 封面图
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(cover_chart_url), cover_chart2);
                    final TextView submit = (TextView) view.findViewById(R.id.i_understand2);//确定
                    final LinearLayout covering_layer = (LinearLayout) view.findViewById(R.id.covering_layer);//遮盖层
                    final RadioGroup application_scheme_Group = (RadioGroup) view.findViewById(R.id.application_scheme_Group);//选择组
                    final VerificationCodeView icv_1 = (VerificationCodeView) view.findViewById(R.id.icv_1);//选择组
                    application_scheme_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton radioButton = (RadioButton) view.findViewById(application_scheme_Group.getCheckedRadioButtonId());
                            switch (radioButton.getText().toString()) {
                                case "电子书":
                                    covering_layer.setVisibility(View.VISIBLE);
                                    icv_1.clearInputContent();
                                    break;
                                case "视频":
                                    covering_layer.setVisibility(View.VISIBLE);
                                    icv_1.clearInputContent();
                                    break;
                                case "电子书+视频":
                                    covering_layer.setVisibility(View.VISIBLE);
                                    icv_1.clearInputContent();
                                    break;
                                case "序列号":
                                    covering_layer.setVisibility(View.GONE);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    btn_sure.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            dialog = null;
                            view = null;
//                                    Toast.makeText(mContext, "ok", 1).show();
                        }
                    });
                    submit.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            double money = 0.01;
                            switch (((RadioButton) view.findViewById(application_scheme_Group.getCheckedRadioButtonId())).getText().toString()) {
                                case "电子书":
                                    intent = new Intent(context,
                                            OpenMenberActivity2.class);
//                                    money = Double.valueOf("60.00");
                                    money = Double.valueOf(moneydzs);
                                    intent.putExtra("viptitle", "《" + title.getText().toString() + "》电子书");
                                    intent.putExtra("vip_time", "永久");
                                    intent.putExtra("payfor", "book");//
                                    intent.putExtra("vipflg_Str", vipflg_pdf_str);//
                                    intent.putExtra("money", money);
                                    intent.putExtra("aid", getIntent().getStringExtra("book_id"));
                                    setpayDzs(money + "", "1");

                                    break;
                                case "视频":
                                    intent = new Intent(context,
                                            OpenMenberActivity2.class);
//                                    money = Double.valueOf("30.00");
                                    money = Double.valueOf(moneyvideo);
                                    intent.putExtra("viptitle", "《" + title.getText().toString() + "》电子书");
                                    intent.putExtra("vip_time", "永久");
                                    intent.putExtra("payfor", "book");//
                                    intent.putExtra("vipflg_Str", vipflg_video_str);//
                                    intent.putExtra("money", money);
                                    intent.putExtra("aid", getIntent().getStringExtra("book_id"));
                                    setpayDzs(money + "", "2");
                                    break;
                                case "电子书+视频":
                                    intent = new Intent(context,
                                            OpenMenberActivity2.class);
//                                    money = Double.valueOf("78.00");
                                    money = Double.valueOf(moneyall);
                                    intent.putExtra("viptitle", "《" + title.getText().toString() + "》电子书");
                                    intent.putExtra("vip_time", "永久");
                                    intent.putExtra("payfor", "book");//
                                    intent.putExtra("vipflg_Str", vipflg_str);//
                                    intent.putExtra("money", money);
                                    intent.putExtra("aid", getIntent().getStringExtra("book_id"));
                                    setpayDzs(money + "", "3");
                                    break;
                                case "序列号":
//                                    Toast.makeText(context, icv_1.getInputContent(), 1).show();
                                    setcodeGetDzs(icv_1.getInputContent());
                                    break;
                                default:
                                    break;
                            }


                        }
                    });
                    break;
                case 3:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            dialog.dismiss();
                            dialog = null;
                            view = null;
                            buy_e_books.setVisibility(View.VISIBLE);
                            buy_e_books.setText("阅读");
                            buy_e_books.setBackground(getResources().getDrawable(R.mipmap.button_03));
                            purchase_video.setVisibility(View.GONE);
                            buy.setVisibility(View.GONE);
                            Toast.makeText(Video_book.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Video_book.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    MyProgressBarDialogTools.hide();
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            startActivity(intent);
                            dialog.dismiss();
                            dialog = null;
                            view = null;
                        } else {
                            Toast.makeText(Video_book.this, jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_book);
        context = this;
        video_book_entity = new Video_book_Entity();
        SharedPreferencesTools.getUid(context);
        findId();
        initData();
        initView();
//        setmsgdb();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setmsgdb();
    }

    /***
     * 模拟测试数据
     */
    public void initData() {
        type = getIntent().getStringExtra("type");
//        Log.e("type数据",type);
//        Log.e("type数据",getIntent().getStringExtra("type"));
//        Log.e("book_id数据",getIntent().getStringExtra("book_id"));

    }

    public void initView() {
        buy_e_books.setOnClickListener(this);
        purchase_video.setOnClickListener(this);
        buy.setOnClickListener(this);
        video_book_main_gridAdapter = new Video_book_Adapter(context, video_book_entities);
        horizontalListView.setAdapter(video_book_main_gridAdapter);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                ImageView textView = (ImageView) view.findViewById(R.id.cover_chart);
                if (buy.getVisibility() == View.GONE && purchase_video.getVisibility() == View.GONE) {
                    Intent intent = new Intent(context, Video_book_Five.class);
                    intent.putExtra("aid", textView.getTag().toString());
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(context, "请先购买！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void findId() {
        super.findId();
        horizontalListView = (MyGridView) findViewById(R.id.horizontalListView);
        cover_chart = (ImageView) findViewById(R.id.cover_chart);
        title = (TextView) findViewById(R.id.title);
        author = (TextView) findViewById(R.id.author);
        price = (TextView) findViewById(R.id.price);
        buy = (TextView) findViewById(R.id.buy);
        synopsis = (AutoTextView) findViewById(R.id.synopsis);
        buy_e_books = (Button) findViewById(R.id.buy_e_books);
        purchase_video = (Button) findViewById(R.id.purchase_video);
        video_layout = (LinearLayout) findViewById(R.id.video_layout);
    }


    public void onclickPDF() {
        Intent intent = new Intent(context, Book_PDFViewerActivity.class);
//        intent.putExtra("video_book_entity",  video_book_entity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        video_book_entity = null;
        super.finish();
    }

    @Override
    public void onBackPressed() {
        //TODO something
        if (type.equals("1")) {
            startActivity(new Intent(Video_book.this, MainActivity.class));
        }
        super.onBackPressed();
    }

    @Override
    public void back(View view) {
        if (type.equals("1")) {
            startActivity(new Intent(Video_book.this, MainActivity.class));
        }
        super.back(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_e_books://购买电子书或阅读电子书
                if (buy_e_books.getText().toString().equals("购买")) {
                    Toast.makeText(context, "购买电子书", Toast.LENGTH_SHORT).show();
                    intent = new Intent(context,
                            OpenMenberActivity2.class);
                    double money = 0.01;
                    money = Double.valueOf(moneydzs);
                    intent.putExtra("viptitle", "《" + title.getText().toString() + "》电子书");
                    intent.putExtra("vip_time", "永久");
                    intent.putExtra("payfor", "book");//
                    intent.putExtra("vipflg_Str", vipflg_pdf_str);//
                    intent.putExtra("money", money);
                    intent.putExtra("aid", getIntent().getStringExtra("book_id"));
                    setpayDzs(money + "", "1");
                } else if (buy_e_books.getText().toString().equals("阅读")) {
                    Toast.makeText(context, "阅读", Toast.LENGTH_SHORT).show();
                    onclickPDF();
                }
                break;
            case R.id.buy://购买
                Toast.makeText(context, "购买", Toast.LENGTH_SHORT).show();
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
                break;
            case R.id.purchase_video://购买视频
                Toast.makeText(context, "购买视频", Toast.LENGTH_SHORT).show();
                intent = new Intent(context,
                        OpenMenberActivity2.class);
                double money = 0.01;
                money = Double.valueOf(moneyvideo);
                intent.putExtra("viptitle", "《" + title.getText().toString() + "》电子书");
                intent.putExtra("vip_time", "永久");
                intent.putExtra("payfor", "book");//
                intent.putExtra("vipflg_Str", vipflg_video_str);//
                intent.putExtra("money", money);
                intent.putExtra("aid", getIntent().getStringExtra("book_id"));
                setpayDzs(money + "", "2");
                break;
            default:
                break;
        }
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getDzsDetial);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", getIntent().getStringExtra("book_id"));
//                    obj.put("id", "1");

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("电子书",result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setcodeGetDzs(final String xlhnum) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.codeGetDzs);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", getIntent().getStringExtra("book_id"));
//                    obj.put("id", "1");
                    obj.put("xlhnum", xlhnum);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("电子书",result);

                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }


    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setpayDzs(final String money, final String paystyle) {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.payDzs);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", getIntent().getStringExtra("book_id"));
//                    obj.put("id", "1");
                    obj.put("money", money);
                    obj.put("paystyle", paystyle);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());
//                    MyProgressBarDialogTools.hide();
//                    LogUtil.e("电子书",result);

                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}

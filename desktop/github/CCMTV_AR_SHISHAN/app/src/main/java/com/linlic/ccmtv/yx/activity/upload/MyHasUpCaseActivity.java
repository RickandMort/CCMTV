package com.linlic.ccmtv.yx.activity.upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * name：已经上传病例查看(病例详情)
 * author：Larry
 * data：2016/6/14.
 */
public class MyHasUpCaseActivity extends BaseActivity implements View.OnClickListener {
    TextView tv_case_title;
    private ImageView iv_upfileA_1, iv_upfileA_2, iv_upfileA_3;

    private ImageView iv_upfileB_1, iv_upfileB_2, iv_upfileB_3;

    private ImageView iv_upfileC_1, iv_upfileC_2, iv_upfileC_3;

    private ImageView iv_upfileD_1, iv_upfileD_2, iv_upfileD_3;

    private ImageView iv_upfileE_1, iv_upfileE_2, iv_upfileE_3;

    private ImageView iv_upfileF_1, iv_upfileF_2, iv_upfileF_3;

    private ImageView iv_upfileG_1, iv_upfileG_2, iv_upfileG_3;

    private ImageView iv_upfileH_1, iv_upfileH_2, iv_upfileH_3;

    private TextView tv_Anumber, tv_Bnumber, tv_Cnumber, tv_Dnumber, tv_Enumber, tv_Fnumber, tv_Gnumber, tv_Hnumber;
    private LinearLayout layout_A, layout_B, layout_C, layout_D, layout_E, layout_F, layout_G, layout_H;
    private String caseTitle;//病例标题
    JSONArray ListupfileA;
    JSONArray ListupfileB;
    JSONArray ListupfileC;
    JSONArray ListupfileD;
    JSONArray ListupfileE;
    JSONArray ListupfileF;
    JSONArray ListupfileG;
    JSONArray ListupfileH;
    Context context;

    private ArrayList urls_huanzhexinxi = new ArrayList();
    private ArrayList urls_bingshi = new ArrayList();
    private ArrayList urls_linchuangbiaoxin = new ArrayList();
    private ArrayList urls_fuzhujiancha = new ArrayList();
    private ArrayList urls_teshujiancha = new ArrayList();
    private ArrayList urls_zhenduan = new ArrayList();
    private ArrayList urls_zhiliao = new ArrayList();
    private ArrayList urls_shoushu = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhasupcase);
        context = this;
        findId();
        initData();
        setText();
        setOnClick();
    }

    private void setOnClick() {
        iv_upfileA_1.setOnClickListener(this);
        iv_upfileA_2.setOnClickListener(this);
        iv_upfileA_3.setOnClickListener(this);

        iv_upfileB_1.setOnClickListener(this);
        iv_upfileB_2.setOnClickListener(this);
        iv_upfileB_3.setOnClickListener(this);

        iv_upfileC_1.setOnClickListener(this);
        iv_upfileC_2.setOnClickListener(this);
        iv_upfileC_3.setOnClickListener(this);

        iv_upfileD_1.setOnClickListener(this);
        iv_upfileD_2.setOnClickListener(this);
        iv_upfileD_3.setOnClickListener(this);

        iv_upfileE_1.setOnClickListener(this);
        iv_upfileE_2.setOnClickListener(this);
        iv_upfileE_3.setOnClickListener(this);

        iv_upfileF_1.setOnClickListener(this);
        iv_upfileF_2.setOnClickListener(this);
        iv_upfileF_3.setOnClickListener(this);

        iv_upfileG_1.setOnClickListener(this);
        iv_upfileG_2.setOnClickListener(this);
        iv_upfileG_3.setOnClickListener(this);

        iv_upfileH_1.setOnClickListener(this);
        iv_upfileH_2.setOnClickListener(this);
        iv_upfileH_3.setOnClickListener(this);
    }

    public void findId() {
        super.findId();
        tv_case_title = (TextView) findViewById(R.id.tv_case_title);
        iv_upfileA_1 = (ImageView) findViewById(R.id.iv_upfileA_1);
        iv_upfileA_2 = (ImageView) findViewById(R.id.iv_upfileA_2);
        iv_upfileA_3 = (ImageView) findViewById(R.id.iv_upfileA_3);

        iv_upfileB_1 = (ImageView) findViewById(R.id.iv_upfileB_1);
        iv_upfileB_2 = (ImageView) findViewById(R.id.iv_upfileB_2);
        iv_upfileB_3 = (ImageView) findViewById(R.id.iv_upfileB_3);

        iv_upfileC_1 = (ImageView) findViewById(R.id.iv_upfileC_1);
        iv_upfileC_2 = (ImageView) findViewById(R.id.iv_upfileC_2);
        iv_upfileC_3 = (ImageView) findViewById(R.id.iv_upfileC_3);

        iv_upfileD_1 = (ImageView) findViewById(R.id.iv_upfileD_1);
        iv_upfileD_2 = (ImageView) findViewById(R.id.iv_upfileD_2);
        iv_upfileD_3 = (ImageView) findViewById(R.id.iv_upfileD_3);

        iv_upfileE_1 = (ImageView) findViewById(R.id.iv_upfileE_1);
        iv_upfileE_2 = (ImageView) findViewById(R.id.iv_upfileE_2);
        iv_upfileE_3 = (ImageView) findViewById(R.id.iv_upfileE_3);

        iv_upfileF_1 = (ImageView) findViewById(R.id.iv_upfileF_1);
        iv_upfileF_2 = (ImageView) findViewById(R.id.iv_upfileF_2);
        iv_upfileF_3 = (ImageView) findViewById(R.id.iv_upfileF_3);

        iv_upfileG_1 = (ImageView) findViewById(R.id.iv_upfileG_1);
        iv_upfileG_2 = (ImageView) findViewById(R.id.iv_upfileG_2);
        iv_upfileG_3 = (ImageView) findViewById(R.id.iv_upfileG_3);

        iv_upfileH_1 = (ImageView) findViewById(R.id.iv_upfileH_1);
        iv_upfileH_2 = (ImageView) findViewById(R.id.iv_upfileH_2);
        iv_upfileH_3 = (ImageView) findViewById(R.id.iv_upfileH_3);

        tv_Anumber = (TextView) findViewById(R.id.tv_Anumber);
        tv_Bnumber = (TextView) findViewById(R.id.tv_Bnumber);
        tv_Cnumber = (TextView) findViewById(R.id.tv_Cnumber);
        tv_Dnumber = (TextView) findViewById(R.id.tv_Dnumber);
        tv_Enumber = (TextView) findViewById(R.id.tv_Enumber);
        tv_Fnumber = (TextView) findViewById(R.id.tv_Fnumber);
        tv_Gnumber = (TextView) findViewById(R.id.tv_Gnumber);
        tv_Hnumber = (TextView) findViewById(R.id.tv_Hnumber);
        //layout_A, layout_B, layout_C, layout_D, layout_E, layout_F, layout_G, layout_H;
        layout_A = (LinearLayout) findViewById(R.id.layout_A);
        layout_B = (LinearLayout) findViewById(R.id.layout_B);
        layout_C = (LinearLayout) findViewById(R.id.layout_C);
        layout_D = (LinearLayout) findViewById(R.id.layout_D);
        layout_E = (LinearLayout) findViewById(R.id.layout_E);
        layout_F = (LinearLayout) findViewById(R.id.layout_F);
        layout_G = (LinearLayout) findViewById(R.id.layout_G);
        layout_H = (LinearLayout) findViewById(R.id.layout_H);
    }


    private void initData() {
        RequestOptions options =new RequestOptions().placeholder(R.mipmap.img_default)
                .error(R.mipmap.img_default);
        Bundle bundle = getIntent().getExtras();
        try {
            String obj = bundle.getString("obj");
            //将字符串转换成jsonObject对象
            JSONObject object = new JSONObject(obj);
            caseTitle = object.getString("mvtitle");
            if (object.has("upfileA")) {
                ListupfileA = object.getJSONArray("upfileA");

                if (ListupfileA.length() > 0) {
                    for (int i = 0; i < ListupfileA.length(); i++) {
                        urls_huanzhexinxi.add(i, ListupfileA.get(i).toString());

                        try {
                            if (i == 0) {
                                //Glide.with(context).load(list.get(position).get("picUrl")).into(holder.img);
                                Glide.with(context)
                                        .load(ListupfileA.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileA_1);

                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileA.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileA_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileA.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileA_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Anumber.setText(ListupfileA.length() + "张照片");
                }
            } else {
                layout_A.setVisibility(View.GONE);
                //    tv_upfileA.setVisibility(View.VISIBLE);
            }
            if (object.has("upfileB")) {
                ListupfileB = object.getJSONArray("upfileB");

                if (ListupfileB.length() > 0) {
                    for (int i = 0; i < ListupfileB.length(); i++) {
                        urls_bingshi.add(i, ListupfileB.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileB.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileB_1);

                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileB.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileB_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileB.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileB_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Bnumber.setText(ListupfileB.length() + "张照片");
                }

            } else {
                layout_B.setVisibility(View.GONE);
                //   tv_upfileB.setVisibility(View.VISIBLE);
            }
            if (object.has("upfileC")) {
                ListupfileC = object.getJSONArray("upfileC");

                if (ListupfileC.length() > 0) {
                    for (int i = 0; i < ListupfileC.length(); i++) {
                        urls_linchuangbiaoxin.add(i, ListupfileC.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileC.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileC_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileC.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileC_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileC.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileC_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Cnumber.setText(ListupfileC.length() + "张照片");
                }
            } else {
                layout_C.setVisibility(View.GONE);
                //   tv_upfileC.setVisibility(View.VISIBLE);
            }
            if (object.has("upfileD")) {
                ListupfileD = object.getJSONArray("upfileD");
                if (ListupfileD.length() > 0) {
                    for (int i = 0; i < ListupfileD.length(); i++) {
                        urls_fuzhujiancha.add(i, ListupfileD.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileD.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileD_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileD.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileD_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileD.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileD_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Dnumber.setText(ListupfileD.length() + "张照片");
                }

            } else {
                layout_D.setVisibility(View.GONE);
            }
            if (object.has("upfileE")) {
                ListupfileE = object.getJSONArray("upfileE");
                if (ListupfileE.length() > 0) {
                    for (int i = 0; i < ListupfileE.length(); i++) {
                        urls_teshujiancha.add(i, ListupfileE.get(i));
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileE.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileE_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileE.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileE_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileE.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileE_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Enumber.setText(ListupfileE.length() + "张照片");
                }

            } else {
                layout_E.setVisibility(View.GONE);
            }
            if (object.has("upfileF")) {
                ListupfileF = object.getJSONArray("upfileF");

                if (ListupfileF.length() > 0) {
                    for (int i = 0; i < ListupfileF.length(); i++) {
                        urls_zhenduan.add(i, ListupfileF.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileF.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileF_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileF.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileF_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileF.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileF_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Fnumber.setText(ListupfileF.length() + "张照片");
                }
            } else {
                layout_F.setVisibility(View.GONE);
            }
            if (object.has("upfileG")) {
                ListupfileG = object.getJSONArray("upfileG");
                if (ListupfileG.length() > 0) {
                    for (int i = 0; i < ListupfileG.length(); i++) {
                        urls_zhiliao.add(i, ListupfileG.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileG.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileG_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileG.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileG_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileG.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileG_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Gnumber.setText(ListupfileG.length() + "张照片");
                }
            } else {
                layout_G.setVisibility(View.GONE);
            }
            if (object.has("upfileH")) {
                ListupfileH = object.getJSONArray("upfileH");

                if (ListupfileH.length() > 0) {
                    for (int i = 0; i < ListupfileH.length(); i++) {
                        urls_shoushu.add(i, ListupfileH.get(i).toString());
                        try {
                            if (i == 0) {
                                Glide.with(context)
                                        .load(ListupfileH.get(0).toString())
                                        .apply(options)
                                        .into(iv_upfileH_1);
                            } else if (i == 1) {
                                Glide.with(context)
                                        .load(ListupfileH.get(1).toString())
                                        .apply(options)
                                        .into(iv_upfileH_2);
                            } else if (i == 2) {
                                Glide.with(context)
                                        .load(ListupfileH.get(2).toString())
                                        .apply(options)
                                        .into(iv_upfileH_3);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tv_Hnumber.setText(ListupfileH.length() + "张照片");
                }
            } else {
                layout_H.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setText() {
        super.findId();
        super.setActivity_title_name("病例详情");
        tv_case_title.setText(caseTitle);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, PicViewerActivity.class);
        switch (v.getId()) {
            case R.id.iv_upfileA_1:

                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", 0);

                break;

            case R.id.iv_upfileA_2:
                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", 1);
                break;


            case R.id.iv_upfileA_3:
                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", 2);
                break;


            case R.id.iv_upfileB_1:
                intent.putExtra("urls_case", urls_bingshi);
                intent.putExtra("current_index", 0);
                break;

            case R.id.iv_upfileB_2:
                intent.putExtra("urls_case", urls_bingshi);
                intent.putExtra("current_index", 1);
                break;

            case R.id.iv_upfileB_3:
                intent.putExtra("urls_case", urls_bingshi);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileC_1:
                intent.putExtra("urls_case", urls_linchuangbiaoxin);
                intent.putExtra("current_index", 0);
                break;


            case R.id.iv_upfileC_2:
                intent.putExtra("urls_case", urls_linchuangbiaoxin);
                intent.putExtra("current_index", 1);
                break;


            case R.id.iv_upfileC_3:
                intent.putExtra("urls_case", urls_linchuangbiaoxin);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileD_1:
                intent.putExtra("urls_case", urls_fuzhujiancha);
                intent.putExtra("current_index", 0);
                break;


            case R.id.iv_upfileD_2:
                intent.putExtra("urls_case", urls_fuzhujiancha);
                intent.putExtra("current_index", 1);
                break;


            case R.id.iv_upfileD_3:
                intent.putExtra("urls_case", urls_fuzhujiancha);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileE_1:
                intent.putExtra("urls_case", urls_teshujiancha);
                intent.putExtra("current_index", 0);
                break;

            case R.id.iv_upfileE_2:
                intent.putExtra("urls_case", urls_teshujiancha);
                intent.putExtra("current_index", 1);
                break;

            case R.id.iv_upfileE_3:
                intent.putExtra("urls_case", urls_teshujiancha);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileF_1:
                intent.putExtra("urls_case", urls_zhenduan);
                intent.putExtra("current_index", 0);
                break;

            case R.id.iv_upfileF_2:
                intent.putExtra("urls_case", urls_zhenduan);
                intent.putExtra("current_index", 1);
                break;

            case R.id.iv_upfileF_3:
                intent.putExtra("urls_case", urls_zhenduan);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileG_1:
                intent.putExtra("urls_case", urls_zhiliao);
                intent.putExtra("current_index", 0);
                break;

            case R.id.iv_upfileG_2:
                intent.putExtra("urls_case", urls_zhiliao);
                intent.putExtra("current_index", 1);
                break;

            case R.id.iv_upfileG_3:
                intent.putExtra("urls_case", urls_zhiliao);
                intent.putExtra("current_index", 2);
                break;

            case R.id.iv_upfileH_1:
                intent.putExtra("urls_case", urls_shoushu);
                intent.putExtra("current_index", 0);
                break;

            case R.id.iv_upfileH_2:
                intent.putExtra("urls_case", urls_shoushu);
                intent.putExtra("current_index", 1);
                break;

            case R.id.iv_upfileH_3:
                intent.putExtra("urls_case", urls_shoushu);
                intent.putExtra("current_index", 2);
                break;
            default:
                break;
        }
        intent.putExtra("type", "home");
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn";
        super.onPause();
    }
}

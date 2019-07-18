package com.linlic.ccmtv.yx.activity.upload.new_upload;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.DbUploadCase;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.activity.upload.adapter.MyCaseGridViewAdapter;
import com.linlic.ccmtv.yx.activity.upload.entry.UploadCaseDetail;
import com.linlic.ccmtv.yx.activity.upload.entry.UploadCaseDetail3;
import com.linlic.ccmtv.yx.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * name：我的病例
 * author：Larry
 * data：2016/5/5 15:57
 */
public class MyCaseActivity3 extends BaseActivity implements View.OnClickListener {
    DbUploadCase dbUploadCase;
    private List<UploadCaseDetail3> uploadCaseBeanList = new ArrayList<>();
    UploadCaseDetail3 uploadCaseDetail;
    TextView tv_case_title;
    private List<ImageView> ivListA=new ArrayList<>();
    private List<ImageView> ivListB=new ArrayList<>();
    private List<ImageView> ivListC=new ArrayList<>();
    private List<ImageView> ivListD=new ArrayList<>();
    private List<ImageView> ivListE=new ArrayList<>();
    private List<ImageView> ivListF=new ArrayList<>();
    private List<ImageView> ivListG=new ArrayList<>();
    private List<ImageView> ivListH=new ArrayList<>();

    private MyGridView gvMyCaseA;
    private MyGridView gvMyCaseB;
    private MyGridView gvMyCaseC;
    private MyGridView gvMyCaseD;
    private MyGridView gvMyCaseE;
    private MyGridView gvMyCaseF;
    private MyGridView gvMyCaseG;
    private MyGridView gvMyCaseH;
    private ImageView iv_upfileA_1, iv_upfileA_2, iv_upfileA_3;

    private ImageView iv_upfileB_1, iv_upfileB_2, iv_upfileB_3;

    private ImageView iv_upfileC_1, iv_upfileC_2, iv_upfileC_3;

    private ImageView iv_upfileD_1, iv_upfileD_2, iv_upfileD_3;

    private ImageView iv_upfileE_1, iv_upfileE_2, iv_upfileE_3;

    private ImageView iv_upfileF_1, iv_upfileF_2, iv_upfileF_3;

    private ImageView iv_upfileG_1, iv_upfileG_2, iv_upfileG_3;

    private ImageView iv_upfileH_1, iv_upfileH_2, iv_upfileH_3;

    //private TextView tv_upfileA, tv_upfileB, tv_upfileC, tv_upfileD, tv_upfileE, tv_upfileF, tv_upfileG, tv_upfileH;
    private TextView tv_infoA, tv_infoB, tv_infoC, tv_infoD, tv_infoE, tv_infoF, tv_infoG, tv_infoH;
    private List<TextView> tvInfoList = new ArrayList<>();
    private LinearLayout layout_A, layout_B, layout_C, layout_D, layout_E, layout_F, layout_G, layout_H;
    Context context;

    private ArrayList urls_huanzhexinxi = new ArrayList();
    private ArrayList urls_bingshi = new ArrayList();
    private ArrayList urls_linchuangbiaoxin = new ArrayList();
    private ArrayList urls_fuzhujiancha = new ArrayList();
    private ArrayList urls_teshujiancha = new ArrayList();
    private ArrayList urls_zhenduan = new ArrayList();
    private ArrayList urls_zhiliao = new ArrayList();
    private ArrayList urls_shoushu = new ArrayList();
    private List<UploadCaseDetail.DataBean> uploadCaseList;
    private List<List<String>> urlTotalList = new ArrayList<>();
    private List<String> uploadFileAList = new ArrayList<>();
    private List<String> uploadFileBList = new ArrayList<>();
    private List<String> uploadFileCList = new ArrayList<>();
    private List<String> uploadFileDList = new ArrayList<>();
    private List<String> uploadFileEList = new ArrayList<>();
    private List<String> uploadFileFList = new ArrayList<>();
    private List<String> uploadFileGList = new ArrayList<>();
    private List<String> uploadFileHList = new ArrayList<>();
    private List<MyCaseGridViewAdapter> adapterTotalList = new ArrayList<>();
    private MyCaseGridViewAdapter adapter1;
    private MyCaseGridViewAdapter adapter2;
    private MyCaseGridViewAdapter adapter3;
    private MyCaseGridViewAdapter adapter4;
    private MyCaseGridViewAdapter adapter5;
    private MyCaseGridViewAdapter adapter6;
    private MyCaseGridViewAdapter adapter7;
    private MyCaseGridViewAdapter adapter8;
    private String mvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_case3);
        context = this;
        findId();
        initData();
        setText();
        setOnClick();
    }

    private void setOnClick() {
       /* iv_upfileA_1.setOnClickListener(this);
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
        iv_upfileH_3.setOnClickListener(this);*/
    }

    public void findId() {
        super.findId();
        tv_case_title = (TextView) findViewById(R.id.tv_case_title);
        /*iv_upfileA_1 = (ImageView) findViewById(R.id.iv_upfileA_1);
        iv_upfileA_2 = (ImageView) findViewById(R.id.iv_upfileA_2);
        iv_upfileA_3 = (ImageView) findViewById(R.id.iv_upfileA_3);
        ivListA.add(iv_upfileA_1);
        ivListA.add(iv_upfileA_2);
        ivListA.add(iv_upfileA_3);

        iv_upfileB_1 = (ImageView) findViewById(R.id.iv_upfileB_1);
        iv_upfileB_2 = (ImageView) findViewById(R.id.iv_upfileB_2);
        iv_upfileB_3 = (ImageView) findViewById(R.id.iv_upfileB_3);
        ivListB.add(iv_upfileB_1);
        ivListB.add(iv_upfileB_2);
        ivListB.add(iv_upfileB_3);

        iv_upfileC_1 = (ImageView) findViewById(R.id.iv_upfileC_1);
        iv_upfileC_2 = (ImageView) findViewById(R.id.iv_upfileC_2);
        iv_upfileC_3 = (ImageView) findViewById(R.id.iv_upfileC_3);
        ivListC.add(iv_upfileC_1);
        ivListC.add(iv_upfileC_2);
        ivListC.add(iv_upfileC_3);

        iv_upfileD_1 = (ImageView) findViewById(R.id.iv_upfileD_1);
        iv_upfileD_2 = (ImageView) findViewById(R.id.iv_upfileD_2);
        iv_upfileD_3 = (ImageView) findViewById(R.id.iv_upfileD_3);
        ivListD.add(iv_upfileD_1);
        ivListD.add(iv_upfileD_2);
        ivListD.add(iv_upfileD_3);

        iv_upfileE_1 = (ImageView) findViewById(R.id.iv_upfileE_1);
        iv_upfileE_2 = (ImageView) findViewById(R.id.iv_upfileE_2);
        iv_upfileE_3 = (ImageView) findViewById(R.id.iv_upfileE_3);
        ivListE.add(iv_upfileE_1);
        ivListE.add(iv_upfileE_2);
        ivListE.add(iv_upfileE_3);

        iv_upfileF_1 = (ImageView) findViewById(R.id.iv_upfileF_1);
        iv_upfileF_2 = (ImageView) findViewById(R.id.iv_upfileF_2);
        iv_upfileF_3 = (ImageView) findViewById(R.id.iv_upfileF_3);
        ivListF.add(iv_upfileF_1);
        ivListF.add(iv_upfileF_2);
        ivListF.add(iv_upfileF_3);

        iv_upfileG_1 = (ImageView) findViewById(R.id.iv_upfileG_1);
        iv_upfileG_2 = (ImageView) findViewById(R.id.iv_upfileG_2);
        iv_upfileG_3 = (ImageView) findViewById(R.id.iv_upfileG_3);
        ivListG.add(iv_upfileG_1);
        ivListG.add(iv_upfileG_2);
        ivListG.add(iv_upfileG_3);

        iv_upfileH_1 = (ImageView) findViewById(R.id.iv_upfileH_1);
        iv_upfileH_2 = (ImageView) findViewById(R.id.iv_upfileH_2);
        iv_upfileH_3 = (ImageView) findViewById(R.id.iv_upfileH_3);
        ivListF.add(iv_upfileF_1);
        ivListF.add(iv_upfileF_2);
        ivListF.add(iv_upfileF_3);*/


       /* tv_upfileA = (TextView) findViewById(R.id.tv_upfileA);
        tv_upfileB = (TextView) findViewById(R.id.tv_upfileB);
        tv_upfileC = (TextView) findViewById(R.id.tv_upfileC);
        tv_upfileD = (TextView) findViewById(R.id.tv_upfileD);
        tv_upfileE = (TextView) findViewById(R.id.tv_upfileE);
        tv_upfileF = (TextView) findViewById(R.id.tv_upfileF);
        tv_upfileG = (TextView) findViewById(R.id.tv_upfileG);
        tv_upfileH = (TextView) findViewById(R.id.tv_upfileH);*/

        tv_infoA = (TextView) findViewById(R.id.tv_info_A);
        tv_infoB = (TextView) findViewById(R.id.tv_info_B);
        tv_infoC = (TextView) findViewById(R.id.tv_info_C);
        tv_infoD = (TextView) findViewById(R.id.tv_info_D);
        tv_infoE = (TextView) findViewById(R.id.tv_info_E);
        tv_infoF = (TextView) findViewById(R.id.tv_info_F);
        tv_infoG = (TextView) findViewById(R.id.tv_info_G);
        tv_infoH = (TextView) findViewById(R.id.tv_info_H);

        layout_A = (LinearLayout) findViewById(R.id.layout_A);
        layout_B = (LinearLayout) findViewById(R.id.layout_B);
        layout_C = (LinearLayout) findViewById(R.id.layout_C);
        layout_D = (LinearLayout) findViewById(R.id.layout_D);
        layout_E = (LinearLayout) findViewById(R.id.layout_E);
        layout_F = (LinearLayout) findViewById(R.id.layout_F);
        layout_G = (LinearLayout) findViewById(R.id.layout_G);
        layout_H = (LinearLayout) findViewById(R.id.layout_H);

        gvMyCaseA = (MyGridView) findViewById(R.id.id_gv_mycase_a);
        gvMyCaseB = (MyGridView) findViewById(R.id.id_gv_mycase_b);
        gvMyCaseC = (MyGridView) findViewById(R.id.id_gv_mycase_c);
        gvMyCaseD = (MyGridView) findViewById(R.id.id_gv_mycase_d);
        gvMyCaseE = (MyGridView) findViewById(R.id.id_gv_mycase_e);
        gvMyCaseF = (MyGridView) findViewById(R.id.id_gv_mycase_f);
        gvMyCaseG = (MyGridView) findViewById(R.id.id_gv_mycase_g);
        gvMyCaseH = (MyGridView) findViewById(R.id.id_gv_mycase_h);

        tvInfoList.add(tv_infoA);
        tvInfoList.add(tv_infoB);
        tvInfoList.add(tv_infoC);
        tvInfoList.add(tv_infoD);
        tvInfoList.add(tv_infoE);
        tvInfoList.add(tv_infoF);
        tvInfoList.add(tv_infoG);
        tvInfoList.add(tv_infoH);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        //dbUploadCase = (DbUploadCase) bundle.getSerializable("dbUploadCase");
        uploadCaseBeanList = (List<UploadCaseDetail3>) bundle.getSerializable("uploadCaseDetail");
        mvTitle = bundle.getString("mvTitle");

        /*uploadFileAList = uploadCaseList.get(0).getDate();
        uploadFileBList = uploadCaseList.get(1).getDate();
        uploadFileCList = uploadCaseList.get(2).getDate();
        uploadFileDList = uploadCaseList.get(3).getDate();
        uploadFileEList = uploadCaseList.get(4).getDate();
        uploadFileFList = uploadCaseList.get(5).getDate();
        uploadFileGList = uploadCaseList.get(6).getDate();
        uploadFileHList = uploadCaseList.get(7).getDate();*/

        urlTotalList.add(uploadFileAList);
        urlTotalList.add(uploadFileBList);
        urlTotalList.add(uploadFileCList);
        urlTotalList.add(uploadFileDList);
        urlTotalList.add(uploadFileEList);
        urlTotalList.add(uploadFileFList);
        urlTotalList.add(uploadFileGList);
        urlTotalList.add(uploadFileHList);

        adapter1 = new MyCaseGridViewAdapter(this,uploadFileAList);
        adapter2 = new MyCaseGridViewAdapter(this,uploadFileBList);
        adapter3 = new MyCaseGridViewAdapter(this,uploadFileCList);
        adapter4 = new MyCaseGridViewAdapter(this,uploadFileDList);
        adapter5 = new MyCaseGridViewAdapter(this,uploadFileEList);
        adapter6 = new MyCaseGridViewAdapter(this,uploadFileFList);
        adapter7 = new MyCaseGridViewAdapter(this,uploadFileGList);
        adapter8 = new MyCaseGridViewAdapter(this,uploadFileHList);

        adapterTotalList.add(adapter1);
        adapterTotalList.add(adapter2);
        adapterTotalList.add(adapter3);
        adapterTotalList.add(adapter4);
        adapterTotalList.add(adapter5);
        adapterTotalList.add(adapter6);
        adapterTotalList.add(adapter7);
        adapterTotalList.add(adapter8);

        for (int i = 0; i < uploadCaseBeanList.size(); i++) {
            UploadCaseDetail3 uploadCaseDetail3 = uploadCaseBeanList.get(i);
            tvInfoList.get(i).setText(uploadCaseDetail3.getInfo());
            urlTotalList.get(i).clear();
            urlTotalList.get(i).addAll(uploadCaseDetail3.getUrlList());
            adapterTotalList.get(i).notifyDataSetChanged();
        }

        gvMyCaseA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_huanzhexinxi);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseB.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_bingshi);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseC.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_zhiliao);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_linchuangbiaoxin);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseE.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_fuzhujiancha);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseF.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_teshujiancha);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_zhenduan);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });

        gvMyCaseH.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, PicViewerActivity.class);
                intent.putExtra("type", "my_case");
                intent.putExtra("urls_case", urls_shoushu);
                intent.putExtra("current_index", position);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    public void setText() {
        super.setActivity_title_name("病例详情");
        tv_case_title.setText(mvTitle);

        gvMyCaseA.setAdapter(adapter1);
        gvMyCaseB.setAdapter(adapter2);
        gvMyCaseC.setAdapter(adapter3);
        gvMyCaseD.setAdapter(adapter4);
        gvMyCaseE.setAdapter(adapter5);
        gvMyCaseF.setAdapter(adapter6);
        gvMyCaseG.setAdapter(adapter7);
        gvMyCaseH.setAdapter(adapter8);
        for (int i=0;i<uploadFileAList.size();i++){
            urls_huanzhexinxi.add(i, uploadFileAList.get(i));
            /*if (i<ivListA.size()){
                if (!TextUtils.isEmpty(uploadFileAList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileAList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListA.get(i));
                    urls_huanzhexinxi.add(i, uploadFileAList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileBList.size();i++){
            urls_bingshi.add(i, uploadFileBList.get(i));
            /*if (i<ivListB.size()){
                if (!TextUtils.isEmpty(uploadFileBList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileBList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListB.get(i));
                    urls_bingshi.add(i, uploadFileBList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileCList.size();i++){
            urls_zhiliao.add(i, uploadFileCList.get(i));
            /*if (i<ivListC.size()){
                if (!TextUtils.isEmpty(uploadFileCList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileCList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListC.get(i));
                    urls_linchuangbiaoxin.add(i, uploadFileCList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileDList.size();i++){
            urls_linchuangbiaoxin.add(i, uploadFileDList.get(i));
            /*if (i<ivListD.size()){
                if (!TextUtils.isEmpty(uploadFileDList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileDList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListD.get(i));
                    urls_fuzhujiancha.add(i, uploadFileDList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileEList.size();i++){
            urls_fuzhujiancha.add(i, uploadFileEList.get(i));
            /*if (i<ivListE.size()){
                if (!TextUtils.isEmpty(uploadFileEList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileEList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListE.get(i));
                    urls_teshujiancha.add(i, uploadFileEList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileFList.size();i++){
            urls_teshujiancha.add(i, uploadFileFList.get(i));
            /*if (i<ivListF.size()){
                if (!TextUtils.isEmpty(uploadFileFList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileFList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListF.get(i));
                    urls_zhenduan.add(i, uploadFileFList.get(i));
                }
            }*/
        }

        for (int i=0;i<uploadFileGList.size();i++){
            urls_zhenduan.add(i, uploadFileGList.get(i));
            /*if (i<ivListG.size()){
                if (!TextUtils.isEmpty(uploadFileGList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileGList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListG.get(i));
                    urls_zhiliao.add(i, uploadFileGList.get(i));
                }
            }*/
        }


        for (int i=0;i<uploadFileHList.size();i++){
            urls_shoushu.add(i, uploadFileHList.get(i));
            /*if (i<ivListH.size()){
                if (!TextUtils.isEmpty(uploadFileHList.get(i))) {
                    Glide.with(context)
                            .load(uploadFileHList.get(i))
                            .centerCrop()
                            .placeholder(R.mipmap.img_default)
                            .error(R.mipmap.img_default)
                            .into(ivListH.get(i));
                    urls_shoushu.add(i, uploadFileHList.get(i));
                }
            }*/
        }

        /*if (!TextUtils.isEmpty(uploadFileAList.get(0))) {
            Glide.with(context)
                    .load(uploadFileAList.get(0))
                    .centerCrop()
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .into(ivListA.get(0));
            urls_huanzhexinxi.add(0, "file://" + uploadFileAList.get(0));
        }
        if (!TextUtils.isEmpty(uploadFileAList.get(1))) {
            Glide.with(context)
                    .load(uploadFileAList.get(1))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileA_2);
            urls_huanzhexinxi.add(1, "file://" + uploadFileAList.get(1));
        }
        if (!TextUtils.isEmpty(uploadFileAList.get(2))) {
            Glide.with(context)
                    .load(uploadFileAList.get(2))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileA_3);
            urls_huanzhexinxi.add(2, "file://" + uploadFileAList.get(2));
        }*/

        /*if (!TextUtils.isEmpty(uploadFileBList.get(0))) {
            Glide.with(context)
                    .load(uploadFileBList.get(0))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_1);
            urls_bingshi.add(0, "file://" + uploadFileBList.get(0));
        }
        if (!TextUtils.isEmpty(uploadFileBList.get(1))) {
            Glide.with(context)
                    .load(uploadFileBList.get(1))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_2);
            urls_bingshi.add(1, "file://" + uploadFileBList.get(1));
        }
        if (!TextUtils.isEmpty(uploadFileBList.get(2))) {
            Glide.with(context)
                    .load(uploadFileBList.get(2))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_3);
            urls_bingshi.add(2, "file://" + uploadFileBList.get(2));
        }


        if (!TextUtils.isEmpty(uploadFileCList.get(0))) {
            Glide.with(context)
                    .load(uploadFileCList.get(0))
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_1);
            urls_linchuangbiaoxin.add(0, "file://" + uploadFileCList.get(0));
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileC_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileC_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_2);
            urls_linchuangbiaoxin.add(1, "file://" + dbUploadCase.getUpfileC_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileC_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileC_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_3);
            urls_linchuangbiaoxin.add(2, "file://" + dbUploadCase.getUpfileC_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_1);
            urls_fuzhujiancha.add(0, "file://" + dbUploadCase.getUpfileD_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_2);
            urls_fuzhujiancha.add(1, "file://" + dbUploadCase.getUpfileD_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_3);
            urls_fuzhujiancha.add(2, "file://" + dbUploadCase.getUpfileD_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileE_1);
            urls_teshujiancha.add(0, "file://" + dbUploadCase.getUpfileE_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileE_2);
            urls_teshujiancha.add(1, "file://" + dbUploadCase.getUpfileE_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_3())
                    .centerCrop()
                    .into(iv_upfileE_3);
            urls_teshujiancha.add(2, "file://" + dbUploadCase.getUpfileE_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_1);
            urls_zhenduan.add(0, "file://" + dbUploadCase.getUpfileF_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_2);
            urls_zhenduan.add(1, "file://" + dbUploadCase.getUpfileF_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_3);
            urls_zhenduan.add(2, "file://" + dbUploadCase.getUpfileF_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_1);
            urls_zhiliao.add(0, "file://" + dbUploadCase.getUpfileG_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_2);
            urls_zhiliao.add(1, "file://" + dbUploadCase.getUpfileG_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_3);
            urls_zhiliao.add(2, "file://" + dbUploadCase.getUpfileG_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_1);
            urls_shoushu.add(0, "file://" + dbUploadCase.getUpfileH_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_2);
            urls_shoushu.add(1, "file://" + dbUploadCase.getUpfileH_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_3);
            urls_shoushu.add(2, "file://" + dbUploadCase.getUpfileH_3());
        }*/

        if (urls_huanzhexinxi.size() == 0) {
            //  tv_upfileA.setVisibility(View.VISIBLE);
            layout_A.setVisibility(View.GONE);
        }
        if (urls_bingshi.size() == 0) {
            layout_B.setVisibility(View.GONE);
            //   tv_upfileB.setVisibility(View.VISIBLE);
        }
        if (urls_zhiliao.size() == 0) {
            layout_C.setVisibility(View.GONE);
            //   tv_upfileC.setVisibility(View.VISIBLE);
        }
        if (urls_linchuangbiaoxin.size() == 0) {
            layout_D.setVisibility(View.GONE);
            //  tv_upfileD.setVisibility(View.VISIBLE);
        }
        if (urls_fuzhujiancha.size() == 0) {
            layout_E.setVisibility(View.GONE);
            //  tv_upfileE.setVisibility(View.VISIBLE);
        }
        if (urls_teshujiancha.size() == 0) {
            layout_F.setVisibility(View.GONE);
            //  tv_upfileF.setVisibility(View.VISIBLE);
        }
        if (urls_zhenduan.size() == 0) {
            layout_G.setVisibility(View.GONE);
            //   tv_upfileG.setVisibility(View.VISIBLE);
        }
        if (urls_shoushu.size() == 0) {
            layout_H.setVisibility(View.GONE);
            //   tv_upfileH.setVisibility(View.VISIBLE);
        }
    }

    /*public void setText() {
        super.setActivity_title_name("病例详情");
        tv_case_title.setText(dbUploadCase.getCaseTitle());
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileA_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileA_1())
                    .centerCrop()
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .into(iv_upfileA_1);
            urls_huanzhexinxi.add(0, "file://" + dbUploadCase.getUpfileA_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileA_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileA_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileA_2);
            urls_huanzhexinxi.add(1, "file://" + dbUploadCase.getUpfileA_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileA_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileA_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileA_3);
            urls_huanzhexinxi.add(2, "file://" + dbUploadCase.getUpfileA_3());
        }

        if (!TextUtils.isEmpty(dbUploadCase.getUpfileB_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileB_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_1);
            urls_bingshi.add(0, "file://" + dbUploadCase.getUpfileB_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileB_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileB_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_2);
            urls_bingshi.add(1, "file://" + dbUploadCase.getUpfileB_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileB_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileB_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileB_3);
            urls_bingshi.add(2, "file://" + dbUploadCase.getUpfileB_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileC_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileC_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_1);
            urls_linchuangbiaoxin.add(0, "file://" + dbUploadCase.getUpfileC_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileC_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileC_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_2);
            urls_linchuangbiaoxin.add(1, "file://" + dbUploadCase.getUpfileC_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileC_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileC_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileC_3);
            urls_linchuangbiaoxin.add(2, "file://" + dbUploadCase.getUpfileC_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_1);
            urls_fuzhujiancha.add(0, "file://" + dbUploadCase.getUpfileD_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_2);
            urls_fuzhujiancha.add(1, "file://" + dbUploadCase.getUpfileD_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileD_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileD_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileD_3);
            urls_fuzhujiancha.add(2, "file://" + dbUploadCase.getUpfileD_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileE_1);
            urls_teshujiancha.add(0, "file://" + dbUploadCase.getUpfileE_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileE_2);
            urls_teshujiancha.add(1, "file://" + dbUploadCase.getUpfileE_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileE_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileE_3())
                    .centerCrop()
                    .into(iv_upfileE_3);
            urls_teshujiancha.add(2, "file://" + dbUploadCase.getUpfileE_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_1);
            urls_zhenduan.add(0, "file://" + dbUploadCase.getUpfileF_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_2);
            urls_zhenduan.add(1, "file://" + dbUploadCase.getUpfileF_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileF_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileF_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileF_3);
            urls_zhenduan.add(2, "file://" + dbUploadCase.getUpfileF_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_1);
            urls_zhiliao.add(0, "file://" + dbUploadCase.getUpfileG_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_2);
            urls_zhiliao.add(1, "file://" + dbUploadCase.getUpfileG_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileG_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileG_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileG_3);
            urls_zhiliao.add(2, "file://" + dbUploadCase.getUpfileG_3());
        }


        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_1())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_1())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_1);
            urls_shoushu.add(0, "file://" + dbUploadCase.getUpfileH_1());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_2())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_2())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_2);
            urls_shoushu.add(1, "file://" + dbUploadCase.getUpfileH_2());
        }
        if (!TextUtils.isEmpty(dbUploadCase.getUpfileH_3())) {
            Glide.with(context)
                    .load(dbUploadCase.getUpfileH_3())
                    .placeholder(R.mipmap.img_default)
                    .error(R.mipmap.img_default)
                    .centerCrop()
                    .into(iv_upfileH_3);
            urls_shoushu.add(2, "file://" + dbUploadCase.getUpfileH_3());
        }

        if (urls_huanzhexinxi.size() == 0) {
            //  tv_upfileA.setVisibility(View.VISIBLE);
            layout_A.setVisibility(View.GONE);
        }
        if (urls_bingshi.size() == 0) {
            layout_B.setVisibility(View.GONE);
            //   tv_upfileB.setVisibility(View.VISIBLE);
        }
        if (urls_linchuangbiaoxin.size() == 0) {
            layout_C.setVisibility(View.GONE);
            //   tv_upfileC.setVisibility(View.VISIBLE);
        }
        if (urls_fuzhujiancha.size() == 0) {
            layout_D.setVisibility(View.GONE);
            //  tv_upfileD.setVisibility(View.VISIBLE);
        }
        if (urls_teshujiancha.size() == 0) {
            layout_E.setVisibility(View.GONE);
            //  tv_upfileE.setVisibility(View.VISIBLE);
        }
        if (urls_zhenduan.size() == 0) {
            layout_F.setVisibility(View.GONE);
            //  tv_upfileF.setVisibility(View.VISIBLE);
        }
        if (urls_zhiliao.size() == 0) {
            layout_G.setVisibility(View.GONE);
            //   tv_upfileG.setVisibility(View.VISIBLE);
        }
        if (urls_shoushu.size() == 0) {
            layout_H.setVisibility(View.GONE);
            //   tv_upfileH.setVisibility(View.VISIBLE);
        }
    }*/

    @Override
    public void onClick(View v) {
        /*Intent intent = new Intent(context, PicViewerActivity.class);
        intent.putExtra("type", "my_case");//判断从上传病例进入
        switch (v.getId()) {
            case R.id.iv_upfileA_1:

                int[] location = new int[2];
                // 获得当前item的位置x与y
                iv_upfileA_1.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
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
        startActivity(intent);*/
    }
}

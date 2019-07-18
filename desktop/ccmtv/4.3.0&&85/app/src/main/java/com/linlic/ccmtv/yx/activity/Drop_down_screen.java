package com.linlic.ccmtv.yx.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选页
 *@author tom.li
 *create at 2016/3/21 11:26
 */
public class Drop_down_screen extends BaseActivity {
    Context context;
    private List<View> views = new ArrayList<View>();
    //肿瘤
    private LinearLayout drop_down_screen_tumour_max;
    private LinearLayout drop_down_screen_tumour_min;
    private TextView drop_down_screen_tumour_min_text1;
    private TextView drop_down_screen_tumour_min_text2;
    private TextView drop_down_screen_tumour_min_text3;
    private TextView drop_down_screen_tumour_min_text4;
    private TextView drop_down_screen_tumour_min_text5;
    private TextView drop_down_screen_tumour_min_text6;
    private TextView drop_down_screen_tumour_min_text7;
    private TextView drop_down_screen_tumour_min_text8;
    private TextView drop_down_screen_tumour_min_text9;
    private TextView drop_down_screen_tumour_min_text10;
    private TextView drop_down_screen_tumour_min_text11;
    private TextView drop_down_screen_tumour_min_text12;
    private TextView drop_down_screen_tumour_min_text13;
    private TextView drop_down_screen_tumour_min_text14;
    private TextView drop_down_screen_tumour_min_text15;
    private TextView drop_down_screen_tumour_min_text16;
    private TextView drop_down_screen_tumour_min_text17;
    private TextView drop_down_screen_tumour_min_text18;
    private TextView drop_down_screen_tumour_min_text19;
    private TextView drop_down_screen_tumour_min_text20;
    private TextView drop_down_screen_tumour_min_text21;
    private TextView drop_down_screen_tumour_min_text22;
    private TextView drop_down_screen_tumour_min_text23;
    private TextView drop_down_screen_tumour_min_text24;
    private TextView drop_down_screen_tumour_min_text25;
    private TextView drop_down_screen_tumour_min_text26;
    //血管
    private LinearLayout drop_down_screen_blood_vessel_max;
    private LinearLayout drop_down_screen_blood_vessel_min;
    private TextView drop_down_screen_blood_vessel_min_text1;
    private TextView drop_down_screen_blood_vessel_min_text2;
    private TextView drop_down_screen_blood_vessel_min_text3;
    private TextView drop_down_screen_blood_vessel_min_text4;
    private TextView drop_down_screen_blood_vessel_min_text5;
    private TextView drop_down_screen_blood_vessel_min_text6;
    private TextView drop_down_screen_blood_vessel_min_text7;
    private TextView drop_down_screen_blood_vessel_min_text8;
    private TextView drop_down_screen_blood_vessel_min_text9;
    //骨运动
    private LinearLayout drop_down_screen_bone_movement_max;
    private LinearLayout drop_down_screen_bone_movement_min;
    private TextView drop_down_screen_bone_movement_min_text1;
    private TextView drop_down_screen_bone_movement_min_text2;
    private TextView drop_down_screen_bone_movement_min_text3;
    private TextView drop_down_screen_bone_movement_min_text4;
    private TextView drop_down_screen_bone_movement_min_text5;
    private TextView drop_down_screen_bone_movement_min_text6;
    private TextView drop_down_screen_bone_movement_min_text7;
    private TextView drop_down_screen_bone_movement_min_text8;
    private TextView drop_down_screen_bone_movement_min_text9;
    private TextView drop_down_screen_bone_movement_min_text10;
    //呼吸
    private LinearLayout drop_down_screen_breathing_max;
    private LinearLayout drop_down_screen_breathing_min;
    private TextView drop_down_screen_breathing_min_text1;
    private TextView drop_down_screen_breathing_min_text2;
    private TextView drop_down_screen_breathing_min_text3;
    private TextView drop_down_screen_breathing_min_text4;
    private TextView drop_down_screen_breathing_min_text5;
    private TextView drop_down_screen_breathing_min_text6;
    private TextView drop_down_screen_breathing_min_text7;
    private TextView drop_down_screen_breathing_min_text8;
    private TextView drop_down_screen_breathing_min_text9;
    private TextView drop_down_screen_breathing_min_text10;
    private TextView drop_down_screen_breathing_min_text11;
    private TextView drop_down_screen_breathing_min_text12;
    //心血管
    private LinearLayout drop_down_screen_cardiovascular_max;
    private LinearLayout drop_down_screen_cardiovascular_min;
    private TextView drop_down_screen_cardiovascular_min_text1;
    private TextView drop_down_screen_cardiovascular_min_text2;
    private TextView drop_down_screen_cardiovascular_min_text3;
    private TextView drop_down_screen_cardiovascular_min_text4;
    private TextView drop_down_screen_cardiovascular_min_text5;
    private TextView drop_down_screen_cardiovascular_min_text6;
    private TextView drop_down_screen_cardiovascular_min_text7;
    private TextView drop_down_screen_cardiovascular_min_text8;
    private TextView drop_down_screen_cardiovascular_min_text9;
    private TextView drop_down_screen_cardiovascular_min_text10;
    private TextView drop_down_screen_cardiovascular_min_text11;
    //消化
    private LinearLayout drop_down_screen_digestion_max;
    private LinearLayout drop_down_screen_digestion_min;
    private TextView drop_down_screen_digestion_min_text1;
    private TextView drop_down_screen_digestion_min_text2;
    private TextView drop_down_screen_digestion_min_text3;
    private TextView drop_down_screen_digestion_min_text4;
    private TextView drop_down_screen_digestion_min_text5;
    private TextView drop_down_screen_digestion_min_text6;
    private TextView drop_down_screen_digestion_min_text7;
    private TextView drop_down_screen_digestion_min_text8;
    private TextView drop_down_screen_digestion_min_text9;
    private TextView drop_down_screen_digestion_min_text10;
    private TextView drop_down_screen_digestion_min_text11;
    private TextView drop_down_screen_digestion_min_text12;
    private TextView drop_down_screen_digestion_min_text13;
    private TextView drop_down_screen_digestion_min_text14;
    private TextView drop_down_screen_digestion_min_text15;
    private TextView drop_down_screen_digestion_min_text16;
    //内分泌
    private LinearLayout drop_down_screen_endocrine_max;
    private LinearLayout drop_down_screen_endocrine_min;
    private TextView drop_down_screen_endocrine_min_text1;
    private TextView drop_down_screen_endocrine_min_text2;
    private TextView drop_down_screen_endocrine_min_text3;
    private TextView drop_down_screen_endocrine_min_text4;
    private TextView drop_down_screen_endocrine_min_text5;
    private TextView drop_down_screen_endocrine_min_text6;
    private TextView drop_down_screen_endocrine_min_text7;
    private TextView drop_down_screen_endocrine_min_text8;
    //肝病
    private LinearLayout drop_down_screen_hepatopathy_max;
    private LinearLayout drop_down_screen_hepatopathy_min;
    private TextView drop_down_screen_hepatopathy_min_text1;
    private TextView drop_down_screen_hepatopathy_min_text2;
    private TextView drop_down_screen_hepatopathy_min_text3;
    private TextView drop_down_screen_hepatopathy_min_text4;
    private TextView drop_down_screen_hepatopathy_min_text5;
    private TextView drop_down_screen_hepatopathy_min_text6;
    private TextView drop_down_screen_hepatopathy_min_text7;
    private TextView drop_down_screen_hepatopathy_min_text8;
    private TextView drop_down_screen_hepatopathy_min_text9;
    //神经
    private LinearLayout drop_down_screen_nerve_max;
    private LinearLayout drop_down_screen_nerve_min;
    private TextView drop_down_screen_nerve_min_text1;
    private TextView drop_down_screen_nerve_min_text2;
    private TextView drop_down_screen_nerve_min_text3;
    private TextView drop_down_screen_nerve_min_text4;
    private TextView drop_down_screen_nerve_min_text5;
    private TextView drop_down_screen_nerve_min_text6;
    private TextView drop_down_screen_nerve_min_text7;
    private TextView drop_down_screen_nerve_min_text8;
    //泌尿
    private LinearLayout drop_down_screen_urinary_max;
    private LinearLayout drop_down_screen_urinary_min;
    private TextView drop_down_screen_urinary_min_text1;
    private TextView drop_down_screen_urinary_min_text2;
    private TextView drop_down_screen_urinary_min_text3;
    private TextView drop_down_screen_urinary_min_text4;
    private TextView drop_down_screen_urinary_min_text5;
    private TextView drop_down_screen_urinary_min_text6;
    private TextView drop_down_screen_urinary_min_text7;
    private TextView drop_down_screen_urinary_min_text8;
    private TextView drop_down_screen_urinary_min_text9;
    private TextView jtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drop_down_screen);
        context = this;
        findId();
        addList();
        setMinTextOnClick(views);
        setOnclick();
    }

    /**
     * 将控件加入到list中用于for循环增加单击事件
     *@author tom.li
     *create at 2016/3/21 15:02
     */
    private void addList() {
        views.add(drop_down_screen_blood_vessel_min_text1);
        views.add(drop_down_screen_blood_vessel_min_text2);
        views.add(drop_down_screen_blood_vessel_min_text3);
        views.add(drop_down_screen_blood_vessel_min_text4);
        views.add(drop_down_screen_blood_vessel_min_text5);
        views.add(drop_down_screen_blood_vessel_min_text6);
        views.add(drop_down_screen_blood_vessel_min_text7);
        views.add(drop_down_screen_blood_vessel_min_text8);
        views.add(drop_down_screen_blood_vessel_min_text9);

        views.add(drop_down_screen_bone_movement_min_text1);
        views.add(drop_down_screen_bone_movement_min_text2);
        views.add(drop_down_screen_bone_movement_min_text3);
        views.add(drop_down_screen_bone_movement_min_text4);
        views.add(drop_down_screen_bone_movement_min_text5);
        views.add(drop_down_screen_bone_movement_min_text6);
        views.add(drop_down_screen_bone_movement_min_text7);
        views.add(drop_down_screen_bone_movement_min_text8);
        views.add(drop_down_screen_bone_movement_min_text9);
        views.add(drop_down_screen_bone_movement_min_text10);

        views.add(drop_down_screen_breathing_min_text1);
        views.add(drop_down_screen_breathing_min_text2);
        views.add(drop_down_screen_breathing_min_text3);
        views.add(drop_down_screen_breathing_min_text4);
        views.add(drop_down_screen_breathing_min_text5);
        views.add(drop_down_screen_breathing_min_text6);
        views.add(drop_down_screen_breathing_min_text7);
        views.add(drop_down_screen_breathing_min_text8);
        views.add(drop_down_screen_breathing_min_text9);
        views.add(drop_down_screen_breathing_min_text10);
        views.add(drop_down_screen_breathing_min_text11);
        views.add(drop_down_screen_breathing_min_text12);

        views.add(drop_down_screen_cardiovascular_min_text1);
        views.add(drop_down_screen_cardiovascular_min_text2);
        views.add(drop_down_screen_cardiovascular_min_text3);
        views.add(drop_down_screen_cardiovascular_min_text4);
        views.add(drop_down_screen_cardiovascular_min_text5);
        views.add(drop_down_screen_cardiovascular_min_text6);
        views.add(drop_down_screen_cardiovascular_min_text7);
        views.add(drop_down_screen_cardiovascular_min_text8);
        views.add(drop_down_screen_cardiovascular_min_text9);
        views.add(drop_down_screen_cardiovascular_min_text10);
        views.add(drop_down_screen_cardiovascular_min_text11);

        views.add(drop_down_screen_digestion_min_text1);
        views.add(drop_down_screen_digestion_min_text2);
        views.add(drop_down_screen_digestion_min_text3);
        views.add(drop_down_screen_digestion_min_text4);
        views.add(drop_down_screen_digestion_min_text5);
        views.add(drop_down_screen_digestion_min_text6);
        views.add(drop_down_screen_digestion_min_text7);
        views.add(drop_down_screen_digestion_min_text8);
        views.add(drop_down_screen_digestion_min_text9);
        views.add(drop_down_screen_digestion_min_text10);
        views.add(drop_down_screen_digestion_min_text11);
        views.add(drop_down_screen_digestion_min_text12);
        views.add(drop_down_screen_digestion_min_text13);
        views.add(drop_down_screen_digestion_min_text14);
        views.add(drop_down_screen_digestion_min_text15);
        views.add(drop_down_screen_digestion_min_text16);

        views.add(drop_down_screen_endocrine_min_text1);
        views.add(drop_down_screen_endocrine_min_text2);
        views.add(drop_down_screen_endocrine_min_text3);
        views.add(drop_down_screen_endocrine_min_text4);
        views.add(drop_down_screen_endocrine_min_text5);
        views.add(drop_down_screen_endocrine_min_text6);
        views.add(drop_down_screen_endocrine_min_text7);
        views.add(drop_down_screen_endocrine_min_text8);

        views.add(drop_down_screen_hepatopathy_min_text1);
        views.add(drop_down_screen_hepatopathy_min_text2);
        views.add(drop_down_screen_hepatopathy_min_text3);
        views.add(drop_down_screen_hepatopathy_min_text4);
        views.add(drop_down_screen_hepatopathy_min_text5);
        views.add(drop_down_screen_hepatopathy_min_text6);
        views.add(drop_down_screen_hepatopathy_min_text7);
        views.add(drop_down_screen_hepatopathy_min_text8);
        views.add(drop_down_screen_hepatopathy_min_text9);

        views.add(drop_down_screen_nerve_min_text1);
        views.add(drop_down_screen_nerve_min_text2);
        views.add(drop_down_screen_nerve_min_text3);
        views.add(drop_down_screen_nerve_min_text4);
        views.add(drop_down_screen_nerve_min_text5);
        views.add(drop_down_screen_nerve_min_text6);
        views.add(drop_down_screen_nerve_min_text7);
        views.add(drop_down_screen_nerve_min_text8);

        views.add(drop_down_screen_tumour_min_text1);
        views.add(drop_down_screen_tumour_min_text2);
        views.add(drop_down_screen_tumour_min_text3);
        views.add(drop_down_screen_tumour_min_text4);
        views.add(drop_down_screen_tumour_min_text5);
        views.add(drop_down_screen_tumour_min_text6);
        views.add(drop_down_screen_tumour_min_text7);
        views.add(drop_down_screen_tumour_min_text8);
        views.add(drop_down_screen_tumour_min_text9);
        views.add(drop_down_screen_tumour_min_text10);
        views.add(drop_down_screen_tumour_min_text11);
        views.add(drop_down_screen_tumour_min_text12);
        views.add(drop_down_screen_tumour_min_text13);
        views.add(drop_down_screen_tumour_min_text14);
        views.add(drop_down_screen_tumour_min_text15);
        views.add(drop_down_screen_tumour_min_text16);
        views.add(drop_down_screen_tumour_min_text17);
        views.add(drop_down_screen_tumour_min_text18);
        views.add(drop_down_screen_tumour_min_text19);
        views.add(drop_down_screen_tumour_min_text20);
        views.add(drop_down_screen_tumour_min_text21);
        views.add(drop_down_screen_tumour_min_text22);
        views.add(drop_down_screen_tumour_min_text23);
        views.add(drop_down_screen_tumour_min_text24);
        views.add(drop_down_screen_tumour_min_text25);
        views.add(drop_down_screen_tumour_min_text26);

        views.add(drop_down_screen_urinary_min_text1);
        views.add(drop_down_screen_urinary_min_text2);
        views.add(drop_down_screen_urinary_min_text3);
        views.add(drop_down_screen_urinary_min_text4);
        views.add(drop_down_screen_urinary_min_text5);
        views.add(drop_down_screen_urinary_min_text6);
        views.add(drop_down_screen_urinary_min_text7);
        views.add(drop_down_screen_urinary_min_text8);
        views.add(drop_down_screen_urinary_min_text9);
    }

    @Override
    public void back(View view) {
        Drop_down_screen.this.setResult(1, new Intent());
        finish();
    }

    @Override
    public void findId() {
        super.findId();

        drop_down_screen_tumour_max = (LinearLayout) findViewById(R.id.drop_down_screen_tumour_max);
        drop_down_screen_tumour_min = (LinearLayout) findViewById(R.id.drop_down_screen_tumour_min);
        drop_down_screen_tumour_min_text1 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text1);
        drop_down_screen_tumour_min_text2 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text2);
        drop_down_screen_tumour_min_text3 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text3);
        drop_down_screen_tumour_min_text4 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text4);
        drop_down_screen_tumour_min_text5 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text5);
        drop_down_screen_tumour_min_text6 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text6);
        drop_down_screen_tumour_min_text7 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text7);
        drop_down_screen_tumour_min_text8 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text8);
        drop_down_screen_tumour_min_text9 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text9);
        drop_down_screen_tumour_min_text10 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text10);
        drop_down_screen_tumour_min_text11 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text11);
        drop_down_screen_tumour_min_text12 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text12);
        drop_down_screen_tumour_min_text13 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text13);
        drop_down_screen_tumour_min_text14 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text14);
        drop_down_screen_tumour_min_text15 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text15);
        drop_down_screen_tumour_min_text16 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text16);
        drop_down_screen_tumour_min_text17 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text17);
        drop_down_screen_tumour_min_text18 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text18);
        drop_down_screen_tumour_min_text19 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text19);
        drop_down_screen_tumour_min_text20 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text20);
        drop_down_screen_tumour_min_text21 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text21);
        drop_down_screen_tumour_min_text22 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text22);
        drop_down_screen_tumour_min_text23 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text23);
        drop_down_screen_tumour_min_text24 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text24);
        drop_down_screen_tumour_min_text25 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text25);
        drop_down_screen_tumour_min_text26 = (TextView) findViewById(R.id.drop_down_screen_tumour_min_text26);

        drop_down_screen_blood_vessel_max = (LinearLayout) findViewById(R.id.drop_down_screen_blood_vessel_max);
        drop_down_screen_blood_vessel_min = (LinearLayout) findViewById(R.id.drop_down_screen_blood_vessel_min);
        drop_down_screen_blood_vessel_min_text1 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text1);
        drop_down_screen_blood_vessel_min_text2 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text2);
        drop_down_screen_blood_vessel_min_text3 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text3);
        drop_down_screen_blood_vessel_min_text4 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text4);
        drop_down_screen_blood_vessel_min_text5 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text5);
        drop_down_screen_blood_vessel_min_text6 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text6);
        drop_down_screen_blood_vessel_min_text7 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text7);
        drop_down_screen_blood_vessel_min_text8 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text8);
        drop_down_screen_blood_vessel_min_text9 = (TextView) findViewById(R.id.drop_down_screen_blood_vessel_min_text9);

        drop_down_screen_bone_movement_max = (LinearLayout) findViewById(R.id.drop_down_screen_bone_movement_max);
        drop_down_screen_bone_movement_min = (LinearLayout) findViewById(R.id.drop_down_screen_bone_movement_min);
        drop_down_screen_bone_movement_min_text1 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text1);
        drop_down_screen_bone_movement_min_text2 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text2);
        drop_down_screen_bone_movement_min_text3 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text3);
        drop_down_screen_bone_movement_min_text4 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text4);
        drop_down_screen_bone_movement_min_text5 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text5);
        drop_down_screen_bone_movement_min_text6 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text6);
        drop_down_screen_bone_movement_min_text7 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text7);
        drop_down_screen_bone_movement_min_text8 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text8);
        drop_down_screen_bone_movement_min_text9 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text9);
        drop_down_screen_bone_movement_min_text10 = (TextView) findViewById(R.id.drop_down_screen_bone_movement_min_text10);

        drop_down_screen_breathing_max = (LinearLayout) findViewById(R.id.drop_down_screen_breathing_max);
        drop_down_screen_breathing_min = (LinearLayout) findViewById(R.id.drop_down_screen_breathing_min);
        drop_down_screen_breathing_min_text1 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text1);
        drop_down_screen_breathing_min_text2 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text2);
        drop_down_screen_breathing_min_text3 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text3);
        drop_down_screen_breathing_min_text4 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text4);
        drop_down_screen_breathing_min_text5 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text5);
        drop_down_screen_breathing_min_text6 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text6);
        drop_down_screen_breathing_min_text7 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text7);
        drop_down_screen_breathing_min_text8 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text8);
        drop_down_screen_breathing_min_text9 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text9);
        drop_down_screen_breathing_min_text10 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text10);
        drop_down_screen_breathing_min_text11 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text11);
        drop_down_screen_breathing_min_text12 = (TextView) findViewById(R.id.drop_down_screen_breathing_min_text12);

        drop_down_screen_cardiovascular_max = (LinearLayout) findViewById(R.id.drop_down_screen_cardiovascular_max);
        drop_down_screen_cardiovascular_min = (LinearLayout) findViewById(R.id.drop_down_screen_cardiovascular_min);
        drop_down_screen_cardiovascular_min_text1 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text1);
        drop_down_screen_cardiovascular_min_text2 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text2);
        drop_down_screen_cardiovascular_min_text3 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text3);
        drop_down_screen_cardiovascular_min_text4 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text4);
        drop_down_screen_cardiovascular_min_text5 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text5);
        drop_down_screen_cardiovascular_min_text6 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text6);
        drop_down_screen_cardiovascular_min_text7 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text7);
        drop_down_screen_cardiovascular_min_text8 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text8);
        drop_down_screen_cardiovascular_min_text9 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text9);
        drop_down_screen_cardiovascular_min_text10 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text10);
        drop_down_screen_cardiovascular_min_text11 = (TextView) findViewById(R.id.drop_down_screen_cardiovascular_min_text11);

        drop_down_screen_digestion_max = (LinearLayout) findViewById(R.id.drop_down_screen_digestion_max);
        drop_down_screen_digestion_min = (LinearLayout) findViewById(R.id.drop_down_screen_digestion_min);
        drop_down_screen_digestion_min_text1 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text1) ;
        drop_down_screen_digestion_min_text2 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text2) ;
        drop_down_screen_digestion_min_text3 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text3) ;
        drop_down_screen_digestion_min_text4 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text4) ;
        drop_down_screen_digestion_min_text5 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text5) ;
        drop_down_screen_digestion_min_text6 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text6) ;
        drop_down_screen_digestion_min_text7 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text7) ;
        drop_down_screen_digestion_min_text8 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text8) ;
        drop_down_screen_digestion_min_text9 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text9) ;
        drop_down_screen_digestion_min_text10 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text10) ;
        drop_down_screen_digestion_min_text11 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text11) ;
        drop_down_screen_digestion_min_text12 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text12) ;
        drop_down_screen_digestion_min_text13 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text13) ;
        drop_down_screen_digestion_min_text14 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text14) ;
        drop_down_screen_digestion_min_text15 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text15) ;
        drop_down_screen_digestion_min_text16 = (TextView) findViewById(R.id.drop_down_screen_digestion_min_text16) ;

        drop_down_screen_endocrine_max = (LinearLayout) findViewById(R.id.drop_down_screen_endocrine_max);
        drop_down_screen_endocrine_min = (LinearLayout) findViewById(R.id.drop_down_screen_endocrine_min);
        drop_down_screen_endocrine_min_text1 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text1);
        drop_down_screen_endocrine_min_text2 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text2);
        drop_down_screen_endocrine_min_text3 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text3);
        drop_down_screen_endocrine_min_text4 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text4);
        drop_down_screen_endocrine_min_text5 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text5);
        drop_down_screen_endocrine_min_text6 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text6);
        drop_down_screen_endocrine_min_text7 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text7);
        drop_down_screen_endocrine_min_text8 = (TextView) findViewById(R.id.drop_down_screen_endocrine_min_text8);

        drop_down_screen_hepatopathy_max = (LinearLayout) findViewById(R.id.drop_down_screen_hepatopathy_max);
        drop_down_screen_hepatopathy_min = (LinearLayout) findViewById(R.id.drop_down_screen_hepatopathy_min);
        drop_down_screen_hepatopathy_min_text1 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text1);
        drop_down_screen_hepatopathy_min_text2 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text2);
        drop_down_screen_hepatopathy_min_text3 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text3);
        drop_down_screen_hepatopathy_min_text4 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text4);
        drop_down_screen_hepatopathy_min_text5 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text5);
        drop_down_screen_hepatopathy_min_text6 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text6);
        drop_down_screen_hepatopathy_min_text7 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text7);
        drop_down_screen_hepatopathy_min_text8 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text8);
        drop_down_screen_hepatopathy_min_text9 = (TextView) findViewById(R.id.drop_down_screen_hepatopathy_min_text9);

        drop_down_screen_nerve_max = (LinearLayout) findViewById(R.id.drop_down_screen_nerve_max);
        drop_down_screen_nerve_min = (LinearLayout) findViewById(R.id.drop_down_screen_nerve_min);
        drop_down_screen_nerve_min_text1 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text1);
        drop_down_screen_nerve_min_text2 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text2);
        drop_down_screen_nerve_min_text3 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text3);
        drop_down_screen_nerve_min_text4 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text4);
        drop_down_screen_nerve_min_text5 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text5);
        drop_down_screen_nerve_min_text6 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text6);
        drop_down_screen_nerve_min_text7 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text7);
        drop_down_screen_nerve_min_text8 = (TextView) findViewById(R.id.drop_down_screen_nerve_min_text8);

        drop_down_screen_urinary_max = (LinearLayout) findViewById(R.id.drop_down_screen_urinary_max);
        drop_down_screen_urinary_min = (LinearLayout) findViewById(R.id.drop_down_screen_urinary_min);
        drop_down_screen_urinary_min_text1 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text1);
        drop_down_screen_urinary_min_text2 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text2);
        drop_down_screen_urinary_min_text3 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text3);
        drop_down_screen_urinary_min_text4 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text4);
        drop_down_screen_urinary_min_text5 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text5);
        drop_down_screen_urinary_min_text6 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text6);
        drop_down_screen_urinary_min_text7 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text7);
        drop_down_screen_urinary_min_text8 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text8);
        drop_down_screen_urinary_min_text9 = (TextView) findViewById(R.id.drop_down_screen_urinary_min_text9);

    }

    /**
     * 设置小选项的点击事件
     *@author tom.li
     *create at 2016/3/21 14:58
     */
    public void setMinTextOnClick(List<View> views){

        for (View view:views){

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;
                    setText_bg(textView);
                }
            });
        }

    }

    public void setText_bg(TextView textView){

        if(jtextView!=null){
            jtextView.setTextColor(Color.rgb(112,112,112));
            textView.setTextColor(Color.rgb(67,146,217));
            jtextView = textView;

        }else{
            textView.setTextColor(Color.rgb(67,146,217));
           jtextView =  textView;
        }

        if (jtextView!=null){
            Intent intent = new Intent(Drop_down_screen.this, CustomActivity.class);
            intent.putExtra("type", "home");
            intent.putExtra("disease_class",jtextView.getTag().toString() );
            intent.putExtra("keywords",jtextView.getText() );
            Drop_down_screen.this.setResult(1, intent);
            finish();
        }

    }

    public void setOnclick(){
        drop_down_screen_tumour_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_tumour_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_tumour_min.setVisibility(View.GONE);
                else
                    drop_down_screen_tumour_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_blood_vessel_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_blood_vessel_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_blood_vessel_min.setVisibility(View.GONE);
                else
                    drop_down_screen_blood_vessel_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_bone_movement_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_bone_movement_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_bone_movement_min.setVisibility(View.GONE);
                else
                    drop_down_screen_bone_movement_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_breathing_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_breathing_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_breathing_min.setVisibility(View.GONE);
                else
                    drop_down_screen_breathing_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_cardiovascular_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_cardiovascular_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_cardiovascular_min.setVisibility(View.GONE);
                else
                    drop_down_screen_cardiovascular_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_digestion_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_digestion_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_digestion_min.setVisibility(View.GONE);
                else
                    drop_down_screen_digestion_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_endocrine_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_endocrine_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_endocrine_min.setVisibility(View.GONE);
                else
                    drop_down_screen_endocrine_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_hepatopathy_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_hepatopathy_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_hepatopathy_min.setVisibility(View.GONE);
                else
                    drop_down_screen_hepatopathy_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_nerve_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_nerve_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_nerve_min.setVisibility(View.GONE);
                else
                    drop_down_screen_nerve_min.setVisibility(View.VISIBLE);
            }
        });

        drop_down_screen_urinary_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drop_down_screen_urinary_min.getVisibility() == View.VISIBLE)
                    drop_down_screen_urinary_min.setVisibility(View.GONE);
                else
                    drop_down_screen_urinary_min.setVisibility(View.VISIBLE);
            }
        });
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

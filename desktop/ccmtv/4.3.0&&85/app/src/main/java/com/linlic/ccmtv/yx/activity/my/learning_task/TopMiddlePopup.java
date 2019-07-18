package com.linlic.ccmtv.yx.activity.my.learning_task;

/**
 * Author:  Niklaus
 * Date:    2017/11/6
 * Description: 顶部弹窗，筛选菜单
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.ArrayList;

public class TopMiddlePopup extends PopupWindow implements View.OnClickListener {

    private Context myContext;
    private int myWidth;
    private int myHeight;
    //    private int myType;
    private int myPosition;
    private String type = "";
    private String qualified = "";
    private SharedPreferencesTools sp;
    private LayoutInflater inflater = null;
    private View myMenuView;
    private boolean isfirst = true;
    private LinearLayout ll_screen, ll_qualified, ll_type;
    private TextView qualified1, qualified2, qualified3, type1, type2, type3, type4, type5, screen_reset, screen_yes, tv_type, tv_qualified;

    public TopMiddlePopup(Context context) {
        // TODO Auto-generated constructor stub
    }

    public TopMiddlePopup(Context context, int width, int height, int position) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myMenuView = inflater.inflate(R.layout.screen_pop, null);

        this.myContext = context;
//        this.myType = type;
        this.myPosition = position;
        this.myWidth = width;
        this.myHeight = height;

        if (isfirst) {
            sp.saveType(context, "1");
            sp.saveType1(context, "11");
            sp.saveType2(context, "111");
            sp.saveQualified(context, "1");
            isfirst = false;
        }

        sp.getType(myContext);
        sp.getQualified(myContext);

        initView();
//        initWidget();
        setPopup();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        ll_screen = (LinearLayout) myMenuView.findViewById(R.id.ll_screen);
        ll_type = (LinearLayout) myMenuView.findViewById(R.id.ll_type);
        ll_qualified = (LinearLayout) myMenuView.findViewById(R.id.ll_qualified);
        tv_type = (TextView) myMenuView.findViewById(R.id.tv_type);
        tv_qualified = (TextView) myMenuView.findViewById(R.id.tv_qualified);
        type1 = (TextView) myMenuView.findViewById(R.id.type1);
        type2 = (TextView) myMenuView.findViewById(R.id.type2);
        type3 = (TextView) myMenuView.findViewById(R.id.type3);
        type4 = (TextView) myMenuView.findViewById(R.id.type4);
        type5 = (TextView) myMenuView.findViewById(R.id.type5);
        qualified1 = (TextView) myMenuView.findViewById(R.id.qualified1);
        qualified2 = (TextView) myMenuView.findViewById(R.id.qualified2);
        qualified3 = (TextView) myMenuView.findViewById(R.id.qualified3);
        screen_reset = (TextView) myMenuView.findViewById(R.id.screen_reset);
        screen_yes = (TextView) myMenuView.findViewById(R.id.screen_yes);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        type3.setOnClickListener(this);
        type4.setOnClickListener(this);
        type5.setOnClickListener(this);
        qualified1.setOnClickListener(this);
        qualified2.setOnClickListener(this);
        qualified3.setOnClickListener(this);
        screen_reset.setOnClickListener(this);
        screen_yes.setOnClickListener(this);
    }

    /**
     * 设置popup的样式
     */
    private void setPopup() {
        // 设置AccessoryPopup的view
        this.setContentView(myMenuView);
        // 设置AccessoryPopup弹出窗体的宽度
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体的高度
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体可点击
        this.setFocusable(true);
        // 设置AccessoryPopup弹出窗体的动画效果
        //this.setAnimationStyle(R.style.AnimTop);
//        this.setAnimationStyle(R.style.AnimTopMiddle);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        initData();

        myMenuView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = ll_screen.getBottom();
                int left = ll_screen.getLeft();
                int right = ll_screen.getRight();
//                Log.e("点击位置1：", "--popupLL.getBottom()--:" + ll_screen.getBottom());
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || x < left || x > right) {
//                        Log.e("点击位置2：", "---点击位置在列表下方--");
                        sp.saveType(myContext, "1");
                        sp.saveType1(myContext, "11");
                        sp.saveType2(myContext, "111");
                        sp.saveQualified(myContext, "1");
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 显示弹窗界面
     *
     * @param view
     */
    public void show(View view) {
        type = "";
        qualified = "";
        if (myPosition == 0) {
            tv_qualified.setVisibility(View.GONE);
            ll_qualified.setVisibility(View.GONE);
            checkType();
        } else if (myPosition == 1) {
            tv_qualified.setVisibility(View.GONE);
            ll_qualified.setVisibility(View.GONE);
            checkType();
        } else if (myPosition == 2) {
            checkType();
            checkQualified();
        }
        showAsDropDown(view, 0, 0);
    }

    private void initData() {
        type1.setBackgroundResource(R.mipmap.screen2);
        type1.setTextColor(Color.parseColor("#3997F9"));
        qualified1.setBackgroundResource(R.mipmap.screen2);
        qualified1.setTextColor(Color.parseColor("#3997F9"));
    }

    //任务类型回显
    public void checkType() {
        String type = sp.getType(myContext);
        String type11 = sp.getType1(myContext);
        String type22 = sp.getType2(myContext);
        if (myPosition == 0) {
            switch (type) {
                case "2":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen2);
                    type2.setTextColor(Color.parseColor("#3997F9"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "3":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen2);
                    type3.setTextColor(Color.parseColor("#3997F9"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "4":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen2);
                    type4.setTextColor(Color.parseColor("#3997F9"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "5":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen2);
                    type5.setTextColor(Color.parseColor("#3997F9"));
                    break;
                default:
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
            }
        } else if (myPosition == 1) {
            switch (type11) {
                case "22":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen2);
                    type2.setTextColor(Color.parseColor("#3997F9"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "33":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen2);
                    type3.setTextColor(Color.parseColor("#3997F9"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "44":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen2);
                    type4.setTextColor(Color.parseColor("#3997F9"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "55":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen2);
                    type5.setTextColor(Color.parseColor("#3997F9"));
                    break;
                default:
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
            }
        } else if (myPosition == 2) {
            switch (type22) {
                case "222":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen2);
                    type2.setTextColor(Color.parseColor("#3997F9"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "333":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen2);
                    type3.setTextColor(Color.parseColor("#3997F9"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "444":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen2);
                    type4.setTextColor(Color.parseColor("#3997F9"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
                case "555":
                    type1.setBackgroundResource(R.mipmap.screen1);
                    type1.setTextColor(Color.parseColor("#7A7A7A"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen2);
                    type5.setTextColor(Color.parseColor("#3997F9"));
                    break;
                default:
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    break;
            }
        }
    }

    public void checkQualified() {
        String qualified = sp.getQualified(myContext);
        switch (qualified) {
            case "2":
                qualified1.setBackgroundResource(R.mipmap.screen1);
                qualified1.setTextColor(Color.parseColor("#7A7A7A"));
                qualified2.setBackgroundResource(R.mipmap.screen2);
                qualified2.setTextColor(Color.parseColor("#3997F9"));
                qualified3.setBackgroundResource(R.mipmap.screen1);
                qualified3.setTextColor(Color.parseColor("#7A7A7A"));
                break;
            case "3":
                qualified1.setBackgroundResource(R.mipmap.screen1);
                qualified1.setTextColor(Color.parseColor("#7A7A7A"));
                qualified2.setBackgroundResource(R.mipmap.screen1);
                qualified2.setTextColor(Color.parseColor("#7A7A7A"));
                qualified3.setBackgroundResource(R.mipmap.screen2);
                qualified3.setTextColor(Color.parseColor("#3997F9"));
                break;
            default:
                qualified1.setBackgroundResource(R.mipmap.screen2);
                qualified1.setTextColor(Color.parseColor("#3997F9"));
                qualified2.setBackgroundResource(R.mipmap.screen1);
                qualified2.setTextColor(Color.parseColor("#7A7A7A"));
                qualified3.setBackgroundResource(R.mipmap.screen1);
                qualified3.setTextColor(Color.parseColor("#7A7A7A"));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.type1://不限选中
                type1.setBackgroundResource(R.mipmap.screen2);
                type1.setTextColor(Color.parseColor("#3997F9"));
                type2.setBackgroundResource(R.mipmap.screen1);
                type2.setTextColor(Color.parseColor("#7A7A7A"));
                type3.setBackgroundResource(R.mipmap.screen1);
                type3.setTextColor(Color.parseColor("#7A7A7A"));
                type4.setBackgroundResource(R.mipmap.screen1);
                type4.setTextColor(Color.parseColor("#7A7A7A"));
                type5.setBackgroundResource(R.mipmap.screen1);
                type5.setTextColor(Color.parseColor("#7A7A7A"));
                /*if (myPosition == 0) {
                    sp.saveType(myContext, "11");
                } else if (myPosition == 1) {
                    sp.saveType1(myContext, "11");
                } else if (myPosition == 2) {
                    sp.saveType2(myContext, "11");
                }*/
                break;
            case R.id.type2://PPT选中
                type1.setBackgroundResource(R.mipmap.screen1);
                type1.setTextColor(Color.parseColor("#7A7A7A"));
                type2.setBackgroundResource(R.mipmap.screen2);
                type2.setTextColor(Color.parseColor("#3997F9"));
                type3.setBackgroundResource(R.mipmap.screen1);
                type3.setTextColor(Color.parseColor("#7A7A7A"));
                type4.setBackgroundResource(R.mipmap.screen1);
                type4.setTextColor(Color.parseColor("#7A7A7A"));
                type5.setBackgroundResource(R.mipmap.screen1);
                type5.setTextColor(Color.parseColor("#7A7A7A"));
                if (myPosition == 0) {
                    sp.saveType(myContext, "2");
                    type = "5";
                } else if (myPosition == 1) {
                    sp.saveType1(myContext, "22");
                    type = "5";
                } else if (myPosition == 2) {
                    sp.saveType2(myContext, "222");
                    type = "5";
                }
                break;
            case R.id.type3://视频选中
                type1.setBackgroundResource(R.mipmap.screen1);
                type1.setTextColor(Color.parseColor("#7A7A7A"));
                type2.setBackgroundResource(R.mipmap.screen1);
                type2.setTextColor(Color.parseColor("#7A7A7A"));
                type3.setBackgroundResource(R.mipmap.screen2);
                type3.setTextColor(Color.parseColor("#3997F9"));
                type4.setBackgroundResource(R.mipmap.screen1);
                type4.setTextColor(Color.parseColor("#7A7A7A"));
                type5.setBackgroundResource(R.mipmap.screen1);
                type5.setTextColor(Color.parseColor("#7A7A7A"));
                if (myPosition == 0) {
                    sp.saveType(myContext, "3");
                    type = "1";
                } else if (myPosition == 1) {
                    sp.saveType1(myContext, "33");
                    type = "1";
                } else if (myPosition == 2) {
                    sp.saveType2(myContext, "333");
                    type = "1";
                }
                break;
            case R.id.type4://图文选中
                type1.setBackgroundResource(R.mipmap.screen1);
                type1.setTextColor(Color.parseColor("#7A7A7A"));
                type2.setBackgroundResource(R.mipmap.screen1);
                type2.setTextColor(Color.parseColor("#7A7A7A"));
                type3.setBackgroundResource(R.mipmap.screen1);
                type3.setTextColor(Color.parseColor("#7A7A7A"));
                type4.setBackgroundResource(R.mipmap.screen2);
                type4.setTextColor(Color.parseColor("#3997F9"));
                type5.setBackgroundResource(R.mipmap.screen1);
                type5.setTextColor(Color.parseColor("#7A7A7A"));
                if (myPosition == 0) {
                    sp.saveType(myContext, "4");
                    type = "3";
                } else if (myPosition == 1) {
                    sp.saveType1(myContext, "44");
                    type = "3";
                } else if (myPosition == 2) {
                    sp.saveType2(myContext, "444");
                    type = "3";
                }
                break;
            case R.id.type5://音频选中
                type1.setBackgroundResource(R.mipmap.screen1);
                type1.setTextColor(Color.parseColor("#7A7A7A"));
                type2.setBackgroundResource(R.mipmap.screen1);
                type2.setTextColor(Color.parseColor("#7A7A7A"));
                type3.setBackgroundResource(R.mipmap.screen1);
                type3.setTextColor(Color.parseColor("#7A7A7A"));
                type4.setBackgroundResource(R.mipmap.screen2);
                type4.setTextColor(Color.parseColor("#7A7A7A"));
                type5.setBackgroundResource(R.mipmap.screen2);
                type5.setTextColor(Color.parseColor("#3997F9"));
                if (myPosition == 0) {
                    sp.saveType(myContext, "5");
                    type = "6";
                } else if (myPosition == 1) {
                    sp.saveType1(myContext, "55");
                    type = "6";
                } else if (myPosition == 2) {
                    sp.saveType2(myContext, "555");
                    type = "6";
                }
                break;
            case R.id.qualified1://不限选中
                qualified1.setBackgroundResource(R.mipmap.screen2);
                qualified1.setTextColor(Color.parseColor("#3997F9"));
                qualified2.setBackgroundResource(R.mipmap.screen1);
                qualified2.setTextColor(Color.parseColor("#7A7A7A"));
                qualified3.setBackgroundResource(R.mipmap.screen1);
                qualified3.setTextColor(Color.parseColor("#7A7A7A"));
                sp.saveQualified(myContext, "1");
                break;
            case R.id.qualified2://合格选中
                qualified1.setBackgroundResource(R.mipmap.screen1);
                qualified1.setTextColor(Color.parseColor("#7A7A7A"));
                qualified2.setBackgroundResource(R.mipmap.screen2);
                qualified2.setTextColor(Color.parseColor("#3997F9"));
                qualified3.setBackgroundResource(R.mipmap.screen1);
                qualified3.setTextColor(Color.parseColor("#7A7A7A"));
                sp.saveQualified(myContext, "2");
                qualified = "3";
                break;
            case R.id.qualified3://不合格选中
                qualified1.setBackgroundResource(R.mipmap.screen1);
                qualified1.setTextColor(Color.parseColor("#7A7A7A"));
                qualified2.setBackgroundResource(R.mipmap.screen1);
                qualified2.setTextColor(Color.parseColor("#7A7A7A"));
                qualified3.setBackgroundResource(R.mipmap.screen2);
                qualified3.setTextColor(Color.parseColor("#3997F9"));
                sp.saveQualified(myContext, "3");
                qualified = "4";
                break;
            case R.id.screen_reset://点击重置
                if (myPosition == 0) {
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    sp.saveType(myContext, "1");
                    type = "";
                } else if (myPosition == 1) {
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    sp.saveType1(myContext, "11");
                    type = "";
                } else if (myPosition == 2) {
                    type1.setBackgroundResource(R.mipmap.screen2);
                    type1.setTextColor(Color.parseColor("#3997F9"));
                    type2.setBackgroundResource(R.mipmap.screen1);
                    type2.setTextColor(Color.parseColor("#7A7A7A"));
                    type3.setBackgroundResource(R.mipmap.screen1);
                    type3.setTextColor(Color.parseColor("#7A7A7A"));
                    type4.setBackgroundResource(R.mipmap.screen1);
                    type4.setTextColor(Color.parseColor("#7A7A7A"));
                    type5.setBackgroundResource(R.mipmap.screen1);
                    type5.setTextColor(Color.parseColor("#7A7A7A"));
                    sp.saveType2(myContext, "111");
                    qualified1.setBackgroundResource(R.mipmap.screen2);
                    qualified1.setTextColor(Color.parseColor("#3997F9"));
                    qualified2.setBackgroundResource(R.mipmap.screen1);
                    qualified2.setTextColor(Color.parseColor("#7A7A7A"));
                    qualified3.setBackgroundResource(R.mipmap.screen1);
                    qualified3.setTextColor(Color.parseColor("#7A7A7A"));
                    sp.saveQualified(myContext, "1");
                    type = "";
                    qualified = "";
                }
                break;
            case R.id.screen_yes://点击确定
                Intent intent = new Intent();
                if (myPosition == 0) {
                    intent.setAction("first");
                } else if (myPosition == 1) {
                    intent.setAction("second");
                } else if (myPosition == 2) {
                    intent.setAction("third");
                    intent.putExtra("popqualified", qualified);
                }
                intent.putExtra("poptype", type);
                myContext.sendBroadcast(intent);
                dismiss();
                break;
            default:
                break;
        }

    }
}

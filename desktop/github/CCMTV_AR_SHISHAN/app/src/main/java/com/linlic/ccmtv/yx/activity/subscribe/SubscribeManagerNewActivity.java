package com.linlic.ccmtv.yx.activity.subscribe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.MyDragAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.MyGridViewAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.adapter.OtherAdapter;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Followks;
import com.linlic.ccmtv.yx.activity.subscribe.entiy.Illness;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.MyDragGridView;
import com.linlic.ccmtv.yx.widget.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * name：订阅管理
 * author：Larry
 * data：2017/6/16.
 */
public class SubscribeManagerNewActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private MyGridView mOtherGv;
    private MyDragGridView mUserGv;
    private ImageView iv_back1;
    private List<Followks> followkses = new ArrayList<>();
    private List<Followks> nfollowkses = new ArrayList<>();
    private OtherAdapter mOtherAdapter;
    private MyDragAdapter mUserAdapter;
    private List<Followks> followksList;
    private List<Illness> illnessList = new ArrayList<>();
    private List<String> illness = null;
    private JSONArray jsonArray;
    private ArrayList<JSONObject> fk = new ArrayList<>();
    private JSONObject jo;
    private Dialog dialog;
    private ImageView moveImageView;
    Context context;
    private int mPosition;
    private View mView;
    private AdapterView<?> mParent;
    private String ksid;

    private ArrayList dt = new ArrayList();
    private ArrayList jb;
    private ArrayList fks;

    private Intent intent;
    private Bundle bundle;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(SubscribeManagerNewActivity.this, "已取消订阅", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubscribeManagerNewActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            Toast.makeText(SubscribeManagerNewActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SubscribeManagerNewActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        JSONObject object = new JSONObject(msg.obj.toString());
                        if (object.getInt("status") == 1) {
                            JSONArray dataArray = object.getJSONArray("data");
                            Illness illness;
                            for (int i = 0; i < dataArray.length(); i++) {
                                illness = new Illness();
                                JSONObject customJson = dataArray.getJSONObject(i);
                                illness.setIllness(customJson.getString("name"));
                                illness.setIllnessid(customJson.getString("id"));
                                illness.setChecked(false);
                                illnessList.add(illness);
                            }
//                            Log.e("jibinglist", illnessList.toString());
                            if (moveImageView != null) {
//                                Log.e("illnesslist", illnessList.size() + "");
                                if (illnessList.size() == 0) {
                                    OthersToUsers();
                                } else {
                                    changeUsers(mParent, mView, mPosition);
                                    dialog.show();
                                }
                            }
                        } else {
                            OthersToUsers();
                            Toast.makeText(SubscribeManagerNewActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
//                            Toast.makeText(SubscribeManagerNewActivity.this, object.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        MyProgressBarDialogTools.hide();
                    }
                    break;
                case 500:
                    Toast.makeText(SubscribeManagerNewActivity.this, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_manager);
        context = this;
        initView();
        initData();
    }

    public void initView() {
        mUserGv = (MyDragGridView) findViewById(R.id.userGridView);
        mOtherGv = (MyGridView) findViewById(R.id.otherGridView);
        iv_back1 = (ImageView) findViewById(R.id.iv_back1);
    }

    private void initData() {
        followkses = (List<Followks>) getIntent().getSerializableExtra("followkses");
        nfollowkses = (List<Followks>) getIntent().getSerializableExtra("nfollowkses");
        try {
            dt = (ArrayList) getIntent().getSerializableExtra("dt");
            //遍历回传的ArrayList集合，转换成我们需要的json格式
            for (int i = 0; i < dt.size(); i++) {
                //拿到回传的子集合转换成HashMap
                HashMap km = (HashMap) dt.get(i);
                Iterator iter = km.entrySet().iterator();
                Map.Entry entry = (Map.Entry) iter.next();
                ksid = (String) entry.getKey();
                jb = new ArrayList();
                jb = (ArrayList) entry.getValue();
                jsonArray = new JSONArray();
                //按照json形式转换
                for (int j = 0; j < jb.size(); j++) {
                    jsonArray.put(jb.get(j));
                }
                jo = new JSONObject();
                jo.put(ksid, jsonArray);
                fk.add(jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mUserAdapter = new MyDragAdapter(this, followkses, true);
        mOtherAdapter = new OtherAdapter(this, nfollowkses, false);
        mUserGv.setAdapter(mUserAdapter);
        mOtherGv.setAdapter(mOtherAdapter);
        mUserGv.setOnItemClickListener(this);
        mOtherGv.setOnItemClickListener(this);
        iv_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //将拖拽后的列表回传
                intent = new Intent();
                bundle = new Bundle();
                bundle.putSerializable("followkses", (Serializable) followkses);
                bundle.putSerializable("nfollowkses", (Serializable) nfollowkses);
                fks = new ArrayList();
                try {
                    //将拖拽后的列表重新排序，上传服务器
                    for (int i = 0; i < followkses.size(); i++) {
                        String id = followkses.get(i).getId();
                        for (int j = 0; j < fk.size(); j++) {
                            if (fk.get(j).has(id)) {
                                JSONArray ja = (JSONArray) fk.get(j).get(id);
                                if (ja.length() > 0) {
                                    JSONObject object = new JSONObject();
                                    object.put(id, ja);
                                    fks.add(object);
                                } else if (ja.length() == 0) {
                                    JSONObject object = new JSONObject();
                                    object.put(id, new JSONArray());
                                    fks.add(object);
                                }
                            }
                        }
                    }
//                    Log.e("fk", followkses.toString());
//                    Log.e("fks", fks.toString());
                    MyProgressBarDialogTools.show(context);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.doSubscribeKsnew);
                                obj.put("uid", SharedPreferencesTools.getUidToLoginClose(SubscribeManagerNewActivity.this));

                                final ArrayList<String> arry = new ArrayList<>();
                                for (int i = 0; i < mUserAdapter.getCount(); i++) {
                                    arry.add(i, mUserAdapter.getItem(i).getId());
                                }

                                obj.put("kidstr", fks.toString());
//                                Log.e("kidstr", obj.toString());
                                String result = HttpClientUtils.sendPost(SubscribeManagerNewActivity.this, URLConfig.CCMTVAPP1, obj.toString());
                                MyProgressBarDialogTools.hide();
                                intent.putExtras(bundle);
                                setResult(4, intent);
                                SubscribeManagerNewActivity.this.finish();
//                                Log.e("kidstr", result.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProgressBarDialogTools.hide();
                        }
                    });
                }
//                Log.e("followkses123", followkses.toString());
//                Log.e("followkses123456", fk.toString());
            }
        });
    }

    /**
     * 获取点击的Item的对应View，
     * 因为点击的Item已经有了自己归属的父容器MyGridView，所有我们要是有一个ImageView来代替Item移动
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     * 用于存放我们移动的View
     */
    private ViewGroup getMoveViewGroup() {
        //window中最顶层的view
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final Followks followks,
                          final GridView clickGridView, final boolean isUser) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(1L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // 判断点击的是DragGrid还是OtherGridView
                if (isUser) {
                    mOtherAdapter.setVisible(true);
                    mOtherAdapter.notifyDataSetChanged();
                    mUserAdapter.setListDate(followksList);
                    mUserAdapter.notifyDataSetChanged();
                    //  mUserAdapter.remove();
                } else {
                    mUserAdapter.setVisible(true);
                    mUserAdapter.notifyDataSetChanged();
                    mOtherAdapter.remove();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0，1 的不可以进行任何操作
                /*if (position != 0 && position != 1) {*/
                moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final Followks followks = ((MyDragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                    mOtherAdapter.setVisible(false);
                    //添加到最后一个
                    mOtherAdapter.addItem(followks);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                final ArrayList<String> arry = new ArrayList<>();
                                followksList = mUserAdapter.getChannnelLst();
                                followksList.remove(position);
//                                Log.i("kidstr", "followksList" + followksList.toString());
                                for (int i = 0; i < followksList.size(); i++) {
                                    arry.add(i, followksList.get(i).getId());
                                }
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                mOtherGv.getChildAt(mOtherGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, followks, mUserGv, true);
                                // mUserAdapter.setRemove(position);
                                //已订阅变为未订阅
                                new Thread(new Runnable() {
                                    @SuppressLint("NewApi")
                                    @Override
                                    public void run() {
                                        try {
                                            JSONObject obj = new JSONObject();
                                            obj.put("act", URLConfig.doSubscribeKsnew);
                                            obj.put("uid", SharedPreferencesTools.getUidToLoginClose(SubscribeManagerNewActivity.this));
//                                            obj.put("kidstr", arry.toString());
//                                            followksesNew.remove(position);
                                            fk.remove(position);
                                            obj.put("kidstr", fk.toString());
                                            String result = HttpClientUtils.sendPost(SubscribeManagerNewActivity.this,
                                                    URLConfig.CCMTVAPP1, obj.toString());
                                            Message message = new Message();
                                            message.what = 1;
                                            message.obj = result;
                                            handler.sendMessage(message);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            handler.sendEmptyMessage(500);
                                        }
                                    }
                                }).start();
                            } catch (Exception localException) {
                                localException.printStackTrace();
                            }
                        }
                    }, 1L);


                }
              /*  }*/
                break;
            case R.id.otherGridView:
                //清空疾病列表
                illnessList.clear();
                //访问疾病接口
                setIllness(position);
                moveImageView = getView(view);
                mView = view;
                mParent = parent;
                mPosition = position;
                break;
            default:
                break;
        }
    }

    private void setIllness(final int position) {
        //访问疾病接口
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getJbKs);
                    obj.put("id", mOtherAdapter.getItem(position).getId());
//                    Log.e("resultobj", obj.toString());
                    String result = HttpClientUtils.sendPost(SubscribeManagerNewActivity.this, URLConfig.CCMTVAPP1, obj.toString());
                    MyProgressBarDialogTools.hide();
//                    Log.e("result555", result.toString());
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        }).start();
    }

    //未订阅变为已订阅
    //有疾病，弹出疾病选择框
    private void changeUsers(AdapterView<?> parent, View view, final int position) {
        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
        final int[] startLocation = new int[2];
        newTextView.getLocationInWindow(startLocation);
        final Followks followks = ((OtherAdapter) parent.getAdapter()).getItem(position);

        //选择急病dialog
        dialog = new Dialog(SubscribeManagerNewActivity.this, R.style.ActionSheetDialogStyle);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view1 = inflater.inflate(R.layout.dialog_subscribe, null);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);
        TextView tv_confirm = (TextView) view1.findViewById(R.id.tv_confirm);
        TextView tv_canal = (TextView) view1.findViewById(R.id.tv_canal);

        final GridView goodsGridView = (GridView) view1.findViewById(R.id.single_choice_view_goods);
        final MyGridViewAdapter mAdapter = new MyGridViewAdapter(getApplicationContext());
        mAdapter.setData(illnessList);//传数组, 不指定默认值
        goodsGridView.setAdapter(mAdapter);
        goodsGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        illness = new ArrayList<>();
        jsonArray = new JSONArray();
        goodsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position1, long arg3) {
                mAdapter.setSeclection(position1);//传值更新
                String illnessid = illnessList.get(position1).getIllnessid();
                if (illnessList.get(position1).isChecked()) {
                    illnessList.get(position1).setChecked(false);
                    //移除
                    if (illness.contains(illnessid)) {
                        illness.remove(illnessid);
                    }
                    jsonArray.remove(position1);
                } else {
                    illnessList.get(position1).setChecked(true);
                    //添加
                    illness.add(illnessid);
                    jsonArray.put(illnessid);
                }
//                Log.e("illness", illness + "");
                mAdapter.notifyDataSetChanged();
//                Log.e("jkl", "position===" + position1);
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断选中疾病是否大于1
                if (jsonArray.length() == 0) {
                    Toast.makeText(getApplicationContext(), "至少选择一种疾病", Toast.LENGTH_SHORT).show();
                } else {
                    mUserAdapter.setVisible(false);
                    //添加到最后一个
                    mUserAdapter.addItem(followks);
                    ksid = mOtherAdapter.getItem(position).getId();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                mUserGv.getChildAt(mUserGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, followks, mOtherGv, false);
                                mOtherAdapter.setRemove(position);
                            } catch (Exception localException) {
                                localException.printStackTrace();
                            }
                        }
                    }, 1L);
                    //未订阅变为已订阅
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("act", URLConfig.doSubscribeKsnew);
                                obj.put("uid", SharedPreferencesTools.getUidToLoginClose(SubscribeManagerNewActivity.this));
                                jo = new JSONObject();
                                jo.put(ksid, jsonArray);
//                                Log.e("followksesNew111", fk + "");
//                                followksesNew.put(jo);
                                fk.add(jo);

                                final ArrayList<String> arry = new ArrayList<>();
                                for (int i = 0; i < mUserAdapter.getCount(); i++) {
                                    arry.add(i, mUserAdapter.getItem(i).getId());
                                }

                                obj.put("kidstr", fk.toString());
//                                Log.e("kidstr", obj.toString());
                                String result = HttpClientUtils.sendPost(SubscribeManagerNewActivity.this, URLConfig.CCMTVAPP1, obj.toString());
//                                Log.e("kidstr1", result.toString());
                                Message message = new Message();
                                message.what = 2;
                                message.obj = result;
                                handler.sendMessage(message);
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(500);
                            }
                        }
                    }).start();
                    dialog.dismiss();
                }
            }
        });
        tv_canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //未订阅变为已订阅
    //无疾病，不弹出疾病选择框
    private void OthersToUsers() {
        moveImageView = getView(mView);
        if (moveImageView != null) {
            TextView newTextView = (TextView) mView.findViewById(R.id.text_item);
            final int[] startLocation = new int[2];
            newTextView.getLocationInWindow(startLocation);
            final Followks followks = ((OtherAdapter) mParent.getAdapter()).getItem(mPosition);
            mUserAdapter.setVisible(false);
            //添加到最后一个
            mUserAdapter.addItem(followks);
            ksid = mOtherAdapter.getItem(mPosition).getId();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        int[] endLocation = new int[2];
                        //获取终点的坐标
                        mUserGv.getChildAt(mUserGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                        MoveAnim(moveImageView, startLocation, endLocation, followks, mOtherGv, false);
                        mOtherAdapter.setRemove(mPosition);
                    } catch (Exception localException) {
                        localException.printStackTrace();
                    }
                }
            }, 1L);
            //未订阅变为已订阅
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject obj = new JSONObject();
                        obj.put("act", URLConfig.doSubscribeKsnew);
                        obj.put("uid", SharedPreferencesTools.getUidToLoginClose(SubscribeManagerNewActivity.this));
                        final ArrayList<String> arry = new ArrayList<>();
                        for (int i = 0; i < mUserAdapter.getCount(); i++) {
                            arry.add(i, mUserAdapter.getItem(i).getId());
                        }
                        jo = new JSONObject();
                        jo.put(ksid, new JSONArray());
//                        followksesNew.put(jo);
                        fk.add(jo);
                        obj.put("kidstr", fk.toString());
//                        Log.e("kidstr", obj.toString());
                        String result = HttpClientUtils.sendPost(SubscribeManagerNewActivity.this,
                                URLConfig.CCMTVAPP1, obj.toString());
                        Message message = new Message();
                        message.what = 2;
                        message.obj = result;
                        handler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.sendEmptyMessage(500);
                    }
                }
            }).start();
        }
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

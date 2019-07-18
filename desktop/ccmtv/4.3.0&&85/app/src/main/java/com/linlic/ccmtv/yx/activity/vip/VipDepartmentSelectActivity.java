package com.linlic.ccmtv.yx.activity.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.linlic.ccmtv.yx.R.id.id_gv_vip_special_area_department_select_internal;
import static com.linlic.ccmtv.yx.R.id.id_gv_vip_special_area_department_select_other;
import static com.linlic.ccmtv.yx.R.id.id_gv_vip_special_area_department_select_surgery;
import static com.linlic.ccmtv.yx.R.id.id_gv_vip_special_area_department_select_women_children;

public class VipDepartmentSelectActivity extends BaseActivity {

    private TextView title_name;
    private ImageView ivTitleIconRight;
    private ImageView ivTitleIconLeft;
    private GridView gvInternal, gvSurgery, gvWomenAndChildren, gvOther;
    private BaseListAdapter baseListAdapterInternal, baseListAdapterSurgery, baseListAdapterWomenAndChildren, baseListAdapterOther;
    private View view1, view2, view3;
    private List<VipDepartmentBean> listInternal = new ArrayList<>();
    private List<VipDepartmentBean> listSurgery = new ArrayList<>();
    private List<VipDepartmentBean> listWomenAndChildren = new ArrayList<>();
    private List<VipDepartmentBean> listOther = new ArrayList<>();
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_department_select);

        context = this;
        findId();
        initAdapter();
    }

    public void findId() {
        title_name = (TextView) findViewById(R.id.activity_title_name);
        ivTitleIconRight = (ImageView) findViewById(R.id.id_iv_activity_title_8_right);
        ivTitleIconLeft = (ImageView) findViewById(R.id.id_iv_activity_title_8);
        gvInternal = (GridView) findViewById(id_gv_vip_special_area_department_select_internal);
        gvSurgery = (GridView) findViewById(id_gv_vip_special_area_department_select_surgery);
        gvWomenAndChildren = (GridView) findViewById(id_gv_vip_special_area_department_select_women_children);
        gvOther = (GridView) findViewById(id_gv_vip_special_area_department_select_other);
        view1 = findViewById(R.id.id_view1);
        view2 = findViewById(R.id.id_view2);
        view3 = findViewById(R.id.id_view3);

        title_name.setText("全部科室");
        ivTitleIconLeft.setVisibility(View.GONE);
        ivTitleIconRight.setVisibility(View.VISIBLE);
        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivTitleIconRight.setImageResource(R.mipmap.conference_issue_selece_close);
        ivTitleIconRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> strings=new ArrayList<>();
                for (int i=0; i<listInternal.size(); i++){
                    strings.add(listInternal.get(i).getDepartmentName() + listInternal.get(i).getIsSelect());
                }
                Intent intent=new Intent(VipDepartmentSelectActivity.this,VipSpecialAreaActivity.class);
                intent.putStringArrayListExtra("select",strings);
                VipDepartmentSelectActivity.this.setResult(1,intent);
                finish();
            }
        });
    }

    private void initAdapter() {
        for (int i = 0; i < 10; i++) {
            VipDepartmentBean bean = new VipDepartmentBean();
            bean.setDepartmentName("中医" + i);
            bean.setIsSelect(0);
            listInternal.add(bean);
            /*listSurgery.add(bean);
            listWomenAndChildren.add(bean);
            listOther.add(bean);*/
        }

        for (int i = 0; i < 10; i++) {
            VipDepartmentBean bean = new VipDepartmentBean();
            bean.setDepartmentName("中医" + i);
            bean.setIsSelect(0);
//            listInternal.add(bean);
            listSurgery.add(bean);
//            listWomenAndChildren.add(bean);
//            listOther.add(bean);
        }

        baseListAdapterInternal = new BaseListAdapter(gvInternal, listInternal, R.layout.vip_department_select_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_vip_special_area_video, ((VipDepartmentBean) item).getDepartmentName());
//                helper.setImageBitmapGlide(context, R.id.id_iv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoPicUrl(), 100, 80);
                if (((VipDepartmentBean) item).getIsSelect()==1) {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_selected);
                } else {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_unselected);
                }
                helper.setImageResource(R.id.id_iv_item_vip_special_area_video, R.mipmap.activity_my_08);
            }
        };

        baseListAdapterSurgery = new BaseListAdapter(gvSurgery, listSurgery, R.layout.vip_department_select_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_vip_special_area_video, ((VipDepartmentBean) item).getDepartmentName());
                //                helper.setImageBitmapGlide(context, R.id.id_iv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoPicUrl(), 100, 80);
                helper.setImageResource(R.id.id_iv_item_vip_special_area_video, R.mipmap.activity_my_08);
                if (((VipDepartmentBean) item).getIsSelect()==1) {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_selected);
                } else {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_unselected);
                }
            }
        };

        baseListAdapterWomenAndChildren = new BaseListAdapter(gvWomenAndChildren, listWomenAndChildren, R.layout.vip_department_select_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_vip_special_area_video, ((VipDepartmentBean) item).getDepartmentName());
                //                helper.setImageBitmapGlide(context, R.id.id_iv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoPicUrl(), 100, 80);
                helper.setImageResource(R.id.id_iv_item_vip_special_area_video, R.mipmap.activity_my_08);
                if (((VipDepartmentBean) item).getIsSelect()==1) {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_selected);
                } else {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_unselected);
                }
            }
        };

        baseListAdapterOther = new BaseListAdapter(gvOther, listOther, R.layout.vip_department_select_item_video) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.id_tv_item_vip_special_area_video, ((VipDepartmentBean) item).getDepartmentName());
                //                helper.setImageBitmapGlide(context, R.id.id_iv_item_vip_special_area_video, ((VipSpecialAreaVideoBean)item).getVideoPicUrl(), 100, 80);
                helper.setImageResource(R.id.id_iv_item_vip_special_area_video, R.mipmap.activity_my_08);
                if (((VipDepartmentBean) item).getIsSelect()==1) {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_selected);
                } else {
                    helper.setImageResource(R.id.id_iv_item_vip_special_area_select_status, R.mipmap.vip_department_unselected);
                }
            }
        };


        gvInternal.setAdapter(baseListAdapterInternal);
        gvSurgery.setAdapter(baseListAdapterSurgery);
        gvWomenAndChildren.setAdapter(baseListAdapterWomenAndChildren);
        gvOther.setAdapter(baseListAdapterOther);

        gvInternal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((listInternal.get(position)).getIsSelect()==0) {
                    listInternal.get(position).setIsSelect(1);
                } else {
                    listInternal.get(position).setIsSelect(0);
                }
                baseListAdapterInternal.notifyDataSetChanged();
            }
        });

        gvSurgery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((listSurgery.get(position)).getIsSelect()==0) {
                    listSurgery.get(position).setIsSelect(1);
                } else {
                    listSurgery.get(position).setIsSelect(0);
                }
                baseListAdapterSurgery.notifyDataSetChanged();
            }
        });

        gvWomenAndChildren.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((listWomenAndChildren.get(position)).getIsSelect()==0) {
                    listWomenAndChildren.get(position).setIsSelect(1);
                } else {
                    listWomenAndChildren.get(position).setIsSelect(0);
                }
                baseListAdapterWomenAndChildren.notifyDataSetChanged();
            }
        });

        gvOther.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ((listOther.get(position)).getIsSelect()==0) {
                    listOther.get(position).setIsSelect(1);
                } else {
                    listOther.get(position).setIsSelect(0);
                }
                baseListAdapterOther.notifyDataSetChanged();
            }
        });


        if (listInternal.size()<=0){
            view1.setVisibility(View.GONE);
        }

        if (listSurgery.size()<=0){
            view2.setVisibility(View.GONE);
        }

        if (listWomenAndChildren.size()<=0){
            view3.setVisibility(View.GONE);
        }

    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ArrayList<String> strings=new ArrayList<>();
            for (int i=0; i<listInternal.size(); i++){
                strings.add(listInternal.get(i).getDepartmentName() + listInternal.get(i).getIsSelect());
            }
            Intent intent=new Intent(VipDepartmentSelectActivity.this,VipSpecialAreaActivity.class);
            intent.putStringArrayListExtra("select",strings);
            VipDepartmentSelectActivity.this.setResult(1,intent);
            finish();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        ArrayList<String> strings=new ArrayList<>();
        for (int i=0; i<listInternal.size(); i++){
            strings.add(listInternal.get(i).getDepartmentName() + listInternal.get(i).getIsSelect());
        }
        Intent intent=new Intent(VipDepartmentSelectActivity.this,VipSpecialAreaActivity.class);
        intent.putStringArrayListExtra("select",strings);
        VipDepartmentSelectActivity.this.setResult(1,intent);
        finish();
    }*/

    @Override
    protected void onPause() {
        super.onPause();
    }
}

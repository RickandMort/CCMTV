package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Files_info;
import com.linlic.ccmtv.yx.activity.upload.PicViewerActivity;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.New_teaching_activities.coursewares;

/**
 * Created by tom on 2019/2/19.
 * 课件
 */

public class Event_Details_Accompanying_notes extends BaseActivity{
    private Context context;
    private String http ="";
      List<Files_info> coursewares2 = new ArrayList<>();//课件

    @Bind(R.id.courseware)
    GridView courseware;//文件列表

    private BaseListAdapter  baseListAdapterCoursewares;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                for (com.linlic.ccmtv.yx.activity.entity.Courseware courseware_ben : coursewares) {
                                    if (dataJson.getString("dataList").equals(courseware_ben.getFile_path())) {
                                        coursewares.remove(courseware_ben);
                                    }
                                }
                                baseListAdapterCoursewares.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case 500:
                    Toast.makeText(context
                            , R.string.post_hint1,
                            Toast.LENGTH_SHORT).show();
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.event_details_accompanying_notes);
        context = this;
        ButterKnife.bind(this);
        initView();


    }

    public void initView(){
        http = getIntent().getStringExtra("http");

            List<Files_info> rs = (List<Files_info>) getIntent().getSerializableExtra("list");//获取list方式
//                    LogUtil.e("返回的数据",residents.toString());
            for (Files_info courseware : rs) {
                coursewares2.add(courseware);
            }


        baseListAdapterCoursewares = new BaseListAdapter(courseware,  coursewares2, R.layout.item_coursewares) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                Files_info map = (Files_info) item;
                helper.setText(R.id._item_text, map.getUrl_name());
                helper.setTag(R.id._item_img, map.getUrl());
                helper.setVisibility(R.id._item_img, View.GONE);

            }
        };
        courseware.setAdapter(baseListAdapterCoursewares);
        courseware.setSelector(new ColorDrawable(Color.TRANSPARENT));
        courseware.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Files_info courseware_bean = coursewares2.get(position);
                ArrayList urls_huanzhexinxi = null;
                Intent intent = null;
                LogUtil.e("文件地址",http + "" + courseware_bean.getUrl());
                    switch (courseware_bean.getUrl_name().substring(courseware_bean.getUrl_name().lastIndexOf(".") + 1, courseware_bean.getUrl_name().length())) {
                        case "png":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getUrl());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getUrl());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "jpeg":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getUrl());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        case "bmp":
                            urls_huanzhexinxi = new ArrayList();
                            urls_huanzhexinxi.add(http + "" + courseware_bean.getUrl());
                            intent = new Intent(context, PicViewerActivity.class);
                            intent.putExtra("type", "my_case");
                            intent.putExtra("urls_case", urls_huanzhexinxi);
                            intent.putExtra("current_index", 0);
                            startActivity(intent);
                            break;
                        default:
                            Intent intent1 = new Intent(context, File_down.class);
                            intent1.putExtra("file_path", http + "" + courseware_bean.getUrl());
                            intent1.putExtra("file_name", courseware_bean.getUrl().substring(courseware_bean.getUrl().lastIndexOf("/") + 1, courseware_bean.getUrl().length()));
                            startActivity(intent1);
                            break;
                    }


            }
        });

    }


}

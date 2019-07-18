package com.linlic.ccmtv.yx.activity.my.download;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.db.MyDbUtils;
import com.linlic.ccmtv.yx.activity.entity.DbDownloadVideo;
import com.linlic.ccmtv.yx.activity.upload.VideoPlayerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：已完成下载列表
 * author：MrSong
 * data：2016/6/28.
 */
public class DownloadOKbackup extends BaseActivity {
    private TextView title;//顶部title
    private LinearLayout download_bottom;//底部的全选和删除按钮
    private ImageView download_topRightImg;//右上角删除图标
    private String topTitle;
    private Context context;
    private List<Map<String, String>> list = new ArrayList<>();//数据list
    private ListView listview;
    private SimpleAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_ok);
        context = this;

        findViewById();

        //获取传输过来的参数
        getIntents();

        //查询当前类型下所有已完成的视频
        findDownlodVideoOK();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = (TextView) view.findViewById(R.id.upload_down_item_videoname);
                TextView filePath = (TextView) view.findViewById(R.id.upload_down_item_state);
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("videoTitle", name.getText().toString());
                intent.putExtra("videoPath", filePath.getText().toString());
                startActivity(intent);
            }
        });

        /*listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                *//*DeleteDialog deleteDialog = new DeleteDialog(context, R.style.mystyle, R.layout.download_video_delete_dialog);
                deleteDialog.show();*//*


                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 200);

                aaaaaaaaaaaaaaaaaa();
                return true;
            }
        });*/
    }

    /*private void aaaaaaaaaaaaaaaaaa() {
        //自定义底部弹框
        DeleteDialog deleteDialog = new DeleteDialog(context, R.style.transparentFrameWindowStyle, R.layout.photo_choose_dialog);
        deleteDialog.setCanceledOnTouchOutside(true);
//        deleteDialog.show();
        Dialog dialogs = new Dialog(context,R.style.progress_dialog);
        View views = getLayoutInflater().inflate(R.layout.aaaaaaaaaabbbbbb, null);
        dialogs.setContentView(views, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialogs.show();



        View view = getLayoutInflater().inflate(R.layout.aaaaaaaaaabbbbbb, null);
        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画R.style.main_menu_animstyle
        window.setWindowAnimations(R.style.main_menu_animstyle);
        android.view.WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();

    }*/

    private void findDownlodVideoOK() {
        list.clear();
        List<DbDownloadVideo> downlodVideoOK = MyDbUtils.findDownlodVideoOK(context, topTitle);
        if (downlodVideoOK != null) {
            if (downlodVideoOK.size() != 0) {
                for (DbDownloadVideo video : downlodVideoOK) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("picUrl", video.getPicUrl());
                    map.put("videoName", video.getVideoName());
                    map.put("total", video.getTotal());
                    map.put("filePath", video.getFilePath());
                    map.put("videoId", video.getVideoId());
                    list.add(map);
                }
                if (adapter == null) {
                    adapter = new SimpleAdapter(context, list, R.layout.upload_download_list_item,
                            new String[]{"picUrl", "videoName", "total", "filePath", "videoId"},
                            new int[]{R.id.upload_down_item_img, R.id.upload_down_item_videoname,
                                    R.id.upload_down_item_size, R.id.upload_down_item_state,
                                    R.id.upload_down_item_videoid});
                    adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                        @Override
                        public boolean setViewValue(View view, Object data, String textRepresentation) {
                            if (view instanceof ImageView) {
                                Glide.with(context).load(data.toString()).into((ImageView) view);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }
                listview.setAdapter(adapter);
            }
        }
    }

    /**
     * name：查询所有ID
     * author：MrSong
     * data：2016/6/28 17:02
     */
    private void findViewById() {
        title = (TextView) findViewById(R.id.activity_title_name);
        listview = (ListView) findViewById(R.id.download_listview);
        download_bottom = (LinearLayout) findViewById(R.id.download_bottom);
        download_topRightImg = (ImageView) findViewById(R.id.download_topRightImg);
    }

    /**
     * name：获取传输过来的参数
     * author：MrSong
     * data：2016/6/28 17:04
     */
    private void getIntents() {
        topTitle = getIntent().getExtras().getString("topTitle");
        title.setText(topTitle);
    }

    /**
     * name：右上角删除按钮
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void selectDelete(View view) {
        download_bottom.setVisibility(View.VISIBLE);
        download_topRightImg.setSelected(true);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    /**
     * name：全选
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void selectAllCheck(View view) {

    }

    /**
     * name：删除
     * author：MrSong
     * data：2016/7/5 17:10
     */
    public void deleteVideo(View view) {

    }

}



/*

*//**
 * name：自定义dialog
 * author：MrSong
 * data：2016/6/29 17:37
 *//*
class DeleteDialog extends Dialog {
    private int view;

    public DeleteDialog(Context context, int theme, int view) {
        super(context, theme);
        this.view = view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View views = getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        setContentView(views, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) );
    }
}*/
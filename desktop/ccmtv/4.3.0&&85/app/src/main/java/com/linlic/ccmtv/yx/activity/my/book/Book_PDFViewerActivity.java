package com.linlic.ccmtv.yx.activity.my.book;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.entity.Video_book_chapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.HttpURLConnHelper;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;

import org.json.JSONObject;

import java.util.Collection;

public class Book_PDFViewerActivity extends BaseActivity implements OnPageChangeListener {
    Context context;
    private PDFView pdfView;
    private TextView pageNum;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private DrawerLayout drawerLayout;
    private ListView chapter;
    private BaseListAdapter baseListAdapter;
    //    private Video_book_Entity video_book_entity ;
    private String selectName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pdfviewer);
        context = this;
        findId();
        setActivity_title_name(Video_book.video_book_entity.getName());
        pdfView = (PDFView) findViewById(R.id.pdf_view);
        pageNum = (TextView) findViewById(R.id.pdf_pageNum);
        selectName = Video_book.video_book_entity.getBook_url().get(Video_book.video_book_entity.getCurrent_position()).getName();
        initPDF(Video_book.video_book_entity.getBook_url().get(Video_book.video_book_entity.getCurrent_position()).getPdfStr());
    }

    @Override
    public void findId() {
        super.findId();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        chapter = (ListView) findViewById(R.id.chapter);
        baseListAdapter = new BaseListAdapter(chapter, Video_book.video_book_entity.getBook_url(), R.layout.chapter_item) {

            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling) {
                super.convert(helper, item, isScrolling);
                helper.setText(R.id.name, ((Video_book_chapter) item).getName());
                helper.setText(R.id.pdf_url, ((Video_book_chapter) item).getPdfStr());
                helper.setText(R.id.pdf_id, ((Video_book_chapter) item).getId());
                helper.setText(R.id.position, ((Video_book_chapter) item).getPosition() + "");


            }
        };
        chapter.setAdapter(baseListAdapter);
        // listview点击事件
        chapter.setOnItemClickListener(new casesharing_listListener());
        ((TextView) findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);


            }
        });
    }

    /**
     * name: 点击查看某个PDF文件
     */
    private class casesharing_listListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                long arg3) {
            TextView textView = (TextView) view
                    .findViewById(R.id.pdf_url);
            TextView position = (TextView) view
                    .findViewById(R.id.position);
            TextView pdf_id = (TextView) view
                    .findViewById(R.id.pdf_id);
            TextView titleView = (TextView) view
                    .findViewById(R.id.name);

            selectName = titleView.getText().toString();
            Video_book.video_book_entity.setCurrently(pdf_id.getText().toString());
            Video_book.video_book_entity.setCurrent_position(Integer.parseInt(position.getText().toString()));
            initPDF2(textView.getText().toString());
            drawerLayout.closeDrawer(Gravity.RIGHT);
        }

    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNum.setText(page + "/" + pageCount);
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

    public void initPDF(final String url) {
        //获取动态权限
        getPermission();
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int myPage = Integer.parseInt(Video_book.video_book_entity.getPageid());
                    //选择pdf
                    pdfView.fromBytes(HttpURLConnHelper.loadByteFromURL(url))
//                .pages(0, 2, 3, 4, 5); // 把0 , 2 , 3 , 4 , 5 过滤掉
                            //是否允许翻页，默认是允许翻页
                            .enableSwipe(true)
                            //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                            .swipeHorizontal(false)
                            //
                            .enableDoubletap(true)
                            //设置默认显示第0页
                            .defaultPage(myPage)
                            //允许在当前页面上绘制一些内容，通常在屏幕中间可见。
//                .onDraw(onDrawListener)
//                // 允许在每一页上单独绘制一个页面。只调用可见页面
//                .onDrawAll(onDrawListener)
                            //设置加载监听
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    MyProgressBarDialogTools.hide();
                                }
                            })
                            //设置翻页监听
                            .onPageChange(new OnPageChangeListener() {

                                @Override
                                public void onPageChanged(int page, int pageCount) {

                                    Video_book.video_book_entity.setPageid(page + "");
                                    setrecordDzsRead();
                                }
                            })
                            //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
                            // 首次提交文档后调用。
//                .onRender(onRenderListener)
                            // 渲染风格（就像注释，颜色或表单）
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            // 改善低分辨率屏幕上的渲染
                            .enableAntialiasing(true)
                            // 页面间的间距。定义间距颜色，设置背景视图
                            .spacing(0)
                            .load();

//                    MyProgressBarDialogTools.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MyProgressBarDialogTools.hide();
                }
            }
        }).start();
    }

    public void initPDF2(final String url) {
        //获取动态权限
        getPermission();
        MyProgressBarDialogTools.show(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final int myPage = 0;
                    //选择pdf
                    pdfView.fromBytes(HttpURLConnHelper.loadByteFromURL(url))
//                .pages(0, 2, 3, 4, 5); // 把0 , 2 , 3 , 4 , 5 过滤掉
                            //是否允许翻页，默认是允许翻页
                            .enableSwipe(true)
                            //pdf文档翻页是否是垂直翻页，默认是左右滑动翻页
                            .swipeHorizontal(false)
                            //
                            .enableDoubletap(true)
                            //设置默认显示第0页
                            .defaultPage(myPage)
                            //允许在当前页面上绘制一些内容，通常在屏幕中间可见。
//                .onDraw(onDrawListener)
//                // 允许在每一页上单独绘制一个页面。只调用可见页面
//                .onDrawAll(onDrawListener)
                            //设置加载监听
                            .onLoad(new OnLoadCompleteListener() {
                                @Override
                                public void loadComplete(int nbPages) {
                                    MyProgressBarDialogTools.hide();
                                }
                            })
                            //设置翻页监听
                            .onPageChange(new OnPageChangeListener() {

                                @Override
                                public void onPageChanged(int page, int pageCount) {

                                    Video_book.video_book_entity.setPageid(page + "");
                                    setrecordDzsRead();
                                }
                            })
                            //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
//                .onError(onErrorListener)
                            // 首次提交文档后调用。
//                .onRender(onRenderListener)
                            // 渲染风格（就像注释，颜色或表单）
                            .enableAnnotationRendering(false)
                            .password(null)
                            .scrollHandle(null)
                            // 改善低分辨率屏幕上的渲染
                            .enableAntialiasing(true)
                            // 页面间的间距。定义间距颜色，设置背景视图
                            .spacing(0)
                            .load();

//                    MyProgressBarDialogTools.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MyProgressBarDialogTools.hide();
                }
            }
        }).start();
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private void getPermission() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(Book_PDFViewerActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(Book_PDFViewerActivity.this,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            ActivityCompat.requestPermissions(Book_PDFViewerActivity.this,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        while ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
        }
    }

    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setrecordDzsRead() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.recordDzsRead);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", Video_book.video_book_entity.getId());
                    obj.put("cid", Video_book.video_book_entity.getCurrently());
                    obj.put("pageid", Video_book.video_book_entity.getPageid());
                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP1, obj.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }
}

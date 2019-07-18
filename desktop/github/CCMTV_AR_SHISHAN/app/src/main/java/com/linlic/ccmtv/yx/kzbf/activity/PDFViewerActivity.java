package com.linlic.ccmtv.yx.kzbf.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.HttpURLConnHelper;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;

import java.io.File;

public class PDFViewerActivity extends BaseActivity implements OnPageChangeListener {
    Context context;
    private String name;
    private String pdfurl;
    private PDFView pdfView;
    private TextView pageNum;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        context = this;

        name = getIntent().getStringExtra("name");
        pdfurl = getIntent().getStringExtra("url");

        pdfView = (PDFView) findViewById(R.id.pdf_view);
        pageNum = (TextView) findViewById(R.id.pdf_pageNum);


        if (pdfurl != null && !pdfurl.isEmpty()) {
            initPDF(pdfurl);
        } else {
            initFilePDF();
        }


    }

    private void initFilePDF() {
        if (name != null && !name.isEmpty()) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/article/" + name);
            pdfView.fromFile(file)
                    .defaultPage(0)//默认展示第一页
                    .onPageChange(this)//监听页面切换
                    .load();
        } else {
            Toast.makeText(this, "无可加载的数据", Toast.LENGTH_SHORT).show();
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
                            /*.onPageChange(new OnPageChangeListener() {

                                @Override
                                public void onPageChanged(int page, int pageCount) {
                                    pageTv1.setText(page + "/");
                                }
                            })*/
                            //设置页面滑动监听
//                .onPageScroll(onPageScrollListener)
                            .onError(new OnErrorListener() {
                                @Override
                                public void onError(Throwable t) {
                                    t.printStackTrace();
                                    Toast.makeText(PDFViewerActivity.this, "加载网络PDF出错" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    MyProgressBarDialogTools.hide();
                                }
                            })
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
                    MyProgressBarDialogTools.hide();
                    initFilePDF();
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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(PDFViewerActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(PDFViewerActivity.this,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            ActivityCompat.requestPermissions(PDFViewerActivity.this,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        while ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
        }
    }
}

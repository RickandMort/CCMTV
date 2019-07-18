package com.linlic.ccmtv.yx.activity.my.our_video;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.HttpURLConnHelper;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.util.Locale;

public class MyOurResourcesDisplayActivity2 extends BaseActivity {

    private TextView title_name;
    private FrameLayout webView;
    private PDFView pdfView;
    private Context context;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String resourcesTitle;
    private String resourcesUrl;

    /**声明各种类型文件的dataType**/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";


    private String aid;
    private String typeId;
    private String downFilepath;
    private String downFileName;
    private TbsReaderView readerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_our_resources_display);
        context = this;
        readerView = new TbsReaderView(context, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {

            }
        });
        //下载路径
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/");
        title_name = (TextView) findViewById(R.id.activity_title_name);
        webView = (FrameLayout) findViewById(R.id.id_webview_our_resources_display);
        pdfView = (PDFView) findViewById(R.id.id_pdf_view_our_resources_display);

        title_name.setText("查看文档");

        //typeid  0：其他     1：视频     2：PDF     3：TXT
        typeId = getIntent().getStringExtra("typeid");
        aid = getIntent().getStringExtra("aid");
        resourcesUrl = aid;
        downFilepath  =aid;
        downFileName = resourcesUrl.substring(resourcesUrl.lastIndexOf("/") + 1, resourcesUrl.length());
        if (typeId.equals("2")) {
            initPDF(resourcesUrl);
        } else if (typeId.equals("3")||typeId.equals("4")||typeId.equals("5")||typeId.equals("6")) {
//                                    initWeb(resourcesUrl);
            isdown(downFileName);
        }

//        initWeb("https://view.officeapps.live.com/op/view.aspx?src="+"http://192.168.30.201/upload_files/new_upload_files/pro_sh/test/wordtest.docx");
        //initPDF("http://192.168.30.201/upload_files/new_upload_files/pro_sh/test/pdftest.pdf");

    }



    public void isdown(String file_name) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/" + file_name);
        if (!file.exists()) {
            MyProgressBarDialogTools.show(context);
            //没有该文件
            //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
            GetRequest<File> request = OkGo.<File>get(downFilepath);
            //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
            OkDownload.request(downFilepath, request)//
                    .fileName(file_name)
                    .save()//
                    .register(new com.lzy.okserver.download.DownloadListener("0") {
                        @Override
                        public void onStart(Progress progress) {

                        }

                        @Override
                        public void onProgress(Progress progress) {
                            LogUtil.e("下载", progress.fraction + "");
                        }

                        @Override
                        public void onError(Progress progress) {
                            MyProgressBarDialogTools.hide();
                            Toast.makeText(getApplicationContext(), "文件下载失败，请重新下载！", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFinish(File file, Progress progress) {
                            LogUtil.e("下载", progress.status + "");
                            switch (progress.status) {
                                case 0://无状态

                                    break;
                                case 1://等待下载

                                    break;
                                case 2://下载中

                                    break;
                                case 3://暂停

                                    break;
                                case 4://错误
                                    MyProgressBarDialogTools.hide();
                                    Toast.makeText(getApplicationContext(), "文件下载失败，请重新下载！", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case 5://完成
                                    webView.setVisibility(View.VISIBLE);
                                    pdfView.setVisibility(View.GONE);
                                    MyProgressBarDialogTools.hide();
                                    openFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/" + downFileName);
                                    break;
                            }
                        }

                        @Override
                        public void onRemove(Progress progress) {

                        }
                    })//
                    .start();
        } else {
            webView.setVisibility(View.VISIBLE);
            pdfView.setVisibility(View.GONE);
            //有该文件
            openFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/" + file_name);
        }
    }

    /**
     * 打开文件
     */
    private void openFile(String path) {
        if (webView.getChildCount() < 1) {
            //通过bundle把文件传给x5,打开的事情交由x5处理
            Bundle bundle = new Bundle();
            //传递文件路径
            bundle.putString("filePath", path);
            //加载插件保存的路径
            bundle.putString("tempPath", Environment.getExternalStorageDirectory() + File.separator + "temp");
            //加载文件前的初始化工作,加载支持不同格式的插件
            boolean b = readerView.preOpen(getFileType(path), false);
            if (b) {
                readerView.openFile(bundle);
            }else{
                //tbs 加载失败时候使用手机自带的软件去打开该文件
                openFile2(path);
            }

            webView.addView(readerView);
        }
    }
    /**
     * 打开文件
     * @param filePath 文件的全路径，包括到文件名
     */
    private   void openFile2(String filePath) {
        File file = new File(filePath);
        if (!file.exists()){
            //如果文件不存在
            Toast.makeText(context, "打开失败，原因：文件已经被移动或者删除", Toast.LENGTH_SHORT).show();
            return;
        }
    /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
    /* 依扩展名的类型决定MimeType */
        Intent intent = null;
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            intent =  generateVideoAudioIntent(filePath,DATA_TYPE_AUDIO);
        } else if (end.equals("3gp") || end.equals("mp4")) {
            intent = generateVideoAudioIntent(filePath,DATA_TYPE_VIDEO);
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_IMAGE);
        } else if (end.equals("apk")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_APK);
        }else if (end.equals("html") || end.equals("htm")){
            intent = generateHtmlFileIntent(filePath);
        } else if (end.equals("ppt")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_PPT);
        } else if (end.equals("xls")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_EXCEL);
        } else if (end.equals("doc")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_WORD);
        } else if (end.equals("pdf")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_PDF);
        } else if (end.equals("chm")) {
            intent = generateCommonIntent(filePath,DATA_TYPE_CHM);
        } else if (end.equals("txt")) {
            intent = generateCommonIntent(filePath, DATA_TYPE_TXT);
        } else {
            intent = generateCommonIntent(filePath,DATA_TYPE_ALL);
        }
        Toast.makeText(context, intent+" ", Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }
    /***
     * 获取文件类型
     *
     * @param path 文件路径
     * @return 文件的格式
     */
    private String getFileType(String path) {
        String str = "";

        if (TextUtils.isEmpty(path)) {
            return str;
        }
        int i = path.lastIndexOf('.');
        if (i <= -1) {
            return str;
        }
        str = path.substring(i + 1);
        return str;
    }

 /*   public void initWeb(String url) {
        webView.setVisibility(View.VISIBLE);
        pdfView.setVisibility(View.GONE);

        webView.getSettings().setJavaScriptEnabled(true);//支持javascript
        // webView.requestFocus();//触摸焦点起作用
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);//取消滚动条

        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        //WebView加载web资源
        webView.loadUrl(url);
//     webView.loadData(HttpURLConnHelper.loadByteFromURL(url).toString(),"text/html", "UTF-8");
    }*/

    public void initPDF(final String url) {
        webView.setVisibility(View.GONE);
        pdfView.setVisibility(View.VISIBLE);

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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MyOurResourcesDisplayActivity2.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MyOurResourcesDisplayActivity2.this,
                        PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

            ActivityCompat.requestPermissions(MyOurResourcesDisplayActivity2.this,
                    PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }

        while ((ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)) != PackageManager.PERMISSION_GRANTED) {
        }
    }
    /**
     * 产生打开网页文件的Intent
     * @param filePath 文件路径
     * @return
     */
    private   Intent generateHtmlFileIntent(String filePath) {
        Uri uri = Uri.parse(filePath)
                .buildUpon()
                .encodedAuthority("com.android.htmlfileprovider")
                .scheme("content")
                .encodedPath(filePath)
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, DATA_TYPE_HTML);
        return intent;
    }
    /**
     * 产生打开视频或音频的Intent
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private   Intent generateVideoAudioIntent(String filePath, String dataType){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        File file = new File(filePath);
        intent.setDataAndType(getUri(intent,file), dataType);
        return intent;
    }

    /**
     * 产生除了视频、音频、网页文件外，打开其他类型文件的Intent
     * @param filePath 文件路径
     * @param dataType 文件类型
     * @return
     */
    private   Intent generateCommonIntent(String filePath, String dataType) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(filePath);
        Uri uri = getUri(intent, file);
        intent.setDataAndType(uri, dataType);
        return intent;
    }
    /**
     * 获取对应文件的Uri
     * @param intent 相应的Intent
     * @param file 文件对象
     * @return
     */
    private   Uri getUri(Intent intent, File file) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判断版本是否在7.0以上
            uri =
                    FileProvider.getUriForFile(context,"fileprovider",
                            file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
    public void back(View view) {
        finish();
    }

    @Override
    public void finish() {
        readerView.onStop();
        webView.removeAllViews();
        super.finish();
    }
}

package com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.utils.CustomizedProgressBar2;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/9/21.
 */

public class File_down  extends BaseActivity {
    private Context context;
    @Bind(R.id.down_file)
    TextView down_file;
    @Bind(R.id.open_file)
    TextView open_file;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.down_progress_layout)
    LinearLayout down_progress_layout;
    @Bind(R.id.close_icon)
    ImageView close_icon;
    @Bind(R.id._item_progressbar)
    CustomizedProgressBar2 _item_progressbar;
    @Bind(R.id.fl)
    FrameLayout fl;

    private String file_path,file_name;
    TbsReaderView readerView = null;

    /**声明各种类型文件的dataType**/
    private static final String DATA_TYPE_ALL = "*/*";//未指定明确的文件类型，不能使用精确类型的工具打开，需要用户选择
    private static final String DATA_TYPE_APK = "application/vnd.android.package-archive";
    private static final String DATA_TYPE_VIDEO = "video/*";
    private static final String DATA_TYPE_AUDIO = "audio/*";
    private static final String DATA_TYPE_HTML = "text/html";
    private static final String DATA_TYPE_IMAGE = "image/*";
    private static final String DATA_TYPE_PPT = "application/vnd.ms-powerpoint";
    private static final String DATA_TYPE_EXCEL = "application/vnd.ms-excel";
    private static final String DATA_TYPE_CSV = "text/xml";
    private static final String DATA_TYPE_WORD = "application/msword";
    private static final String DATA_TYPE_CHM = "application/x-chm";
    private static final String DATA_TYPE_TXT = "text/plain";
    private static final String DATA_TYPE_PDF = "application/pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.file_down);
        context = this;

        readerView = new TbsReaderView(context, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {

            }
        });
        ButterKnife.bind(this);
        //下载路径
        OkDownload.getInstance().setFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/");
        file_name = getIntent().getStringExtra("file_name");
        file_path = getIntent().getStringExtra("file_path");
        findId();
        initView();
    }

    public void initView( ){
        close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( OkDownload.getInstance().hasTask(file_path)){
                   OkDownload.getInstance().getTask(file_path).remove();
                   down_file.setVisibility(View.VISIBLE);
                   open_file.setVisibility(View.GONE);
                   down_progress_layout.setVisibility(View.GONE);
                   Toast.makeText(getApplicationContext(), "已取消下载！", Toast.LENGTH_SHORT).show();
               }
            }
        });
        setActivity_title_name(file_name);
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/"+file_name);
        if(!file.exists()){
            down_file.setVisibility(View.VISIBLE);
            open_file.setVisibility(View.GONE);
            down_progress_layout.setVisibility(View.GONE);
        }else{
            down_file.setVisibility(View.GONE);
            open_file.setVisibility(View.VISIBLE);
            down_progress_layout.setVisibility(View.GONE);
        }

        down_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down_file.setVisibility(View.GONE);
                down_progress_layout.setVisibility(View.VISIBLE);

                //这里只是演示，表示请求可以传参，怎么传都行，和okgo使用方法一样
                GetRequest<File> request = OkGo.<File>get(file_path);
                //这里第一个参数是tag，代表下载任务的唯一标识，传任意字符串都行，需要保证唯一,我这里用url作为了tag
                OkDownload.request(file_path, request)//
                        .fileName(file_name)
                        .save()//
                        .register(new DownloadListener("0") {
                            @Override
                            public void onStart(Progress progress) {

                            }

                            @Override
                            public void onProgress(Progress progress) {
                                LogUtil.e("下载",progress.fraction+"" );
                                _item_progressbar.setCurrentCount(progress.fraction*100);
                            }

                            @Override
                            public void onError(Progress progress) {
                                down_file.setVisibility(View.VISIBLE);
                                open_file.setVisibility(View.GONE);
                                down_progress_layout.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "文件下载失败，请重新下载！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                LogUtil.e("下载",progress.status+"" );
                                down_progress_layout.setVisibility(View.GONE);
                                switch (progress.status){
                                    case 0://无状态

                                        break;
                                    case 1://等待下载

                                        break;
                                    case 2://下载中

                                        break;
                                    case 3://暂停

                                        break;
                                    case 4://错误
                                        down_file.setVisibility(View.VISIBLE);
                                        open_file.setVisibility(View.GONE);
                                        break;
                                    case 5://完成
                                        down_file.setVisibility(View.GONE);
                                        open_file.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }

                            @Override
                            public void onRemove(Progress progress) {

                            }
                        })//
                        .start();
            }
        });

        open_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_file.setClickable(false);
                LogUtil.e("点击打开文件","点击打开文件");
                File_down.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fl.setVisibility(View.VISIBLE);
                        openFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ccmtvCache/training/"+file_name);
                        Toast.makeText(getApplicationContext(), "点击打开文件！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }


    /**
     * 打开文件
     */
    private void openFile(String path) {
        if(fl.getChildCount()<1){
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
            fl.addView(readerView);
        }else{
            //tbs 加载失败时候使用手机自带的软件去打开该文件
            openFile2(path);
        }
            Toast.makeText(getApplicationContext(), "点击打开文件！", Toast.LENGTH_SHORT).show();

        }
        open_file.setClickable(true);
    }

    @Override
    public void finish() {
        readerView.onStop();
        fl.removeAllViews();
        super.finish();
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

    /**
     * @param context
     * @param uri     文件的uri
     * @return 文件的路径
     */
    public String getPathFromUri(final Context context, Uri uri) {
        // 选择的图片路径
        String selectPath = null;

        final String scheme = uri.getScheme();
        if (uri != null && scheme != null) {
            if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                // content://开头的uri
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    // 取出文件路径
                    selectPath = cursor.getString(columnIndex);

                    // Android 4.1 更改了SD的目录，sdcard映射到/storage/sdcard0
                    if (!selectPath.startsWith("/storage") && !selectPath.startsWith("/mnt")) {
                        // 检查是否有"/mnt"前缀
                        selectPath = "/mnt" + selectPath;
                    }
                    //关闭游标
                    cursor.close();
                }
            } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {// file:///开头的uri
                // 替换file://
                selectPath = uri.toString().replace("file://", "");
                int index = selectPath.indexOf("/sdcard");
                selectPath = index == -1 ? selectPath : selectPath.substring(index);
                if (!selectPath.startsWith("/mnt")) {
                    // 加上"/mnt"头
                    selectPath = "/mnt" + selectPath;
                }
            }
        }
        return selectPath;
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
        }else if (end.equals("csv")) {
//            intent = generateCommonIntent(filePath,DATA_TYPE_CSV);
            Toast.makeText(getApplicationContext(), "暂不支持查看此类型文件~", Toast.LENGTH_SHORT).show();
            return;
        }  else if (end.equals("doc")) {
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
}

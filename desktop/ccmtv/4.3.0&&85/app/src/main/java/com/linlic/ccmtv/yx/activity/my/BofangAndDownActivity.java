package com.linlic.ccmtv.yx.activity.my;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.my.dialog.DownTypeCustomDialog;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FileSizeUtil;
import com.linlic.ccmtv.yx.utils.SDCardUtils;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.getSdcardPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * name：播放和下载
 * author：Larry
 * data：2016/6/1.
 */
public class BofangAndDownActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    ImageView iv_phonecache_check, iv_everyremind_check, iv_oneremind_check, iv_sdcache_check;
    LinearLayout layout_everyremind_check, layout_oneremind_check, layout_phonecache_check, layout_sdcache_check;
    LinearLayout layout_downtype, layout_onlywifi;
    TextView tv_downtype, tv_phonememory, tv_sdcardmemory;
    ImageView iv_opendown;
    public static final String TAG = "Memory";
    public String ccmtvapp_sdcardpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bifanganddown);
        context = this;

        findId();
        onClick();
        setText();
        updateMemoryStatus();
    }

    private void setText() {

        boolean Iswifi = SharedPreferencesTools.getOnlyWifiDown(BofangAndDownActivity.this);
        if (Iswifi) {
            iv_opendown.setSelected(true);
        } else {
            iv_opendown.setSelected(false);
        }
        //非wifi播放每次都提醒
        boolean IsEveryReminder = SharedPreferencesTools.getIsEveryReminder(BofangAndDownActivity.this);
        //非wifi播放每次都提醒
        boolean IsOneReminder = SharedPreferencesTools.getIsOneReminder(BofangAndDownActivity.this);

        if (IsEveryReminder) {
            iv_oneremind_check.setVisibility(View.INVISIBLE);
            iv_everyremind_check.setVisibility(View.VISIBLE);
            SharedPreferencesTools.saveIsEveryReminder(BofangAndDownActivity.this, true);
            SharedPreferencesTools.saveIsOneReminder(BofangAndDownActivity.this, false);
        } else {
            iv_everyremind_check.setVisibility(View.INVISIBLE);
            iv_oneremind_check.setVisibility(View.VISIBLE);
            SharedPreferencesTools.saveIsEveryReminder(BofangAndDownActivity.this, false);
            SharedPreferencesTools.saveIsOneReminder(BofangAndDownActivity.this, true);
        }

        int Clarity = SharedPreferencesTools.getClarity(BofangAndDownActivity.this);
        if (Clarity == 0) {
            tv_downtype.setText("流畅");
        } else if (Clarity == 1) {
            tv_downtype.setText("标清");
        } else if (Clarity == 2) {
            tv_downtype.setText("高清");
        }
        if (SDCardScanner.getExtSDCardPaths().size() > 1) {
            layout_sdcache_check.setVisibility(View.VISIBLE);

            File dirs = new File(getSdcardPath.getStoragePath(BofangAndDownActivity.this, true) + "/Android/data/com.linlic.ccmtv.yx");
            if (!dirs.exists())
                dirs.mkdirs();
            ccmtvapp_sdcardpath = getSdcardPath.getStoragePath(BofangAndDownActivity.this, true) + "/Android/data/com.linlic.ccmtv.yx";
           // Log.i("getCachePath", "存在外置");
            tv_sdcardmemory.setText("总量" + FileSizeUtil.FormetFileSize(getAllSize()) + "/" + "剩余" + FileSizeUtil.FormetFileSize(getAvailableExternalMemorySize()));
        } else {
            layout_sdcache_check.setVisibility(View.GONE);
            iv_phonecache_check.setSelected(true);
          //  Log.i("getCachePath", "不存在外置");
        }
/*        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            layout_sdcache_check.setVisibility(View.VISIBLE);
        } else {
            layout_sdcache_check.setVisibility(View.GONE);
        }*/
        String CachePath = SharedPreferencesTools.getCachePath(BofangAndDownActivity.this);
       // Log.i("getCachePath", CachePath);
        if (CachePath.contains("emulated")) {
            iv_phonecache_check.setVisibility(View.VISIBLE);
            iv_sdcache_check.setVisibility(View.INVISIBLE);
            //SharedPreferencesTools.saveCachePath(BofangAndDownActivity.this, getFilesDir() + "/ccmtvCache");
            SharedPreferencesTools.saveCachePath(BofangAndDownActivity.this, URLConfig.ccmtvapp_basesdcardpath);
          //  Log.i("getCachePath", "手机内存" + URLConfig.ccmtvapp_basesdcardpath);
        } else {
            iv_sdcache_check.setVisibility(View.VISIBLE);
            iv_phonecache_check.setVisibility(View.INVISIBLE);
            SharedPreferencesTools.saveCachePath(BofangAndDownActivity.this, ccmtvapp_sdcardpath);
        //    Log.i("getCachePath", "SD卡" + ccmtvapp_sdcardpath);
        }
    }


    public void findId() {
        super.findId();
        iv_phonecache_check = (ImageView) findViewById(R.id.iv_phonecache_check);
        iv_everyremind_check = (ImageView) findViewById(R.id.iv_everyremind_check);
        iv_oneremind_check = (ImageView) findViewById(R.id.iv_oneremind_check);
        iv_sdcache_check = (ImageView) findViewById(R.id.iv_sdcache_check);
        iv_opendown = (ImageView) findViewById(R.id.iv_opendown);
        layout_everyremind_check = (LinearLayout) findViewById(R.id.layout_everyremind_check);
        layout_oneremind_check = (LinearLayout) findViewById(R.id.layout_oneremind_check);
        layout_phonecache_check = (LinearLayout) findViewById(R.id.layout_phonecache_check);
        layout_sdcache_check = (LinearLayout) findViewById(R.id.layout_sdcache_check);
        layout_downtype = (LinearLayout) findViewById(R.id.layout_downtype);
        layout_onlywifi = (LinearLayout) findViewById(R.id.layout_onlywifi);
        tv_downtype = (TextView) findViewById(R.id.tv_downtype);
        tv_phonememory = (TextView) findViewById(R.id.tv_phonememory);
        tv_sdcardmemory = (TextView) findViewById(R.id.tv_sdcardmemory);
    }

    private void onClick() {
        super.setActivity_title_name("播放和下载");
        layout_everyremind_check.setOnClickListener(this);
        layout_oneremind_check.setOnClickListener(this);
        layout_phonecache_check.setOnClickListener(this);
        layout_sdcache_check.setOnClickListener(this);
        layout_downtype.setOnClickListener(this);
        layout_onlywifi.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_downtype:

                DownTypeCustomDialog dialog_down = new DownTypeCustomDialog(BofangAndDownActivity.this,
                        R.style.mystyle, R.layout.downtypecustomdialog, new DownTypeCustomDialog.OnChooseDownTypeListener() {
                    @Override
                    public void ChooseDownType(String type) {
//                        Toast.makeText(BofangAndDownActivity.this, "type" + type, Toast.LENGTH_SHORT).show();
                        tv_downtype.setText(type);
                        if ("流畅".equals(type)) {
                            SharedPreferencesTools.saveClarity(BofangAndDownActivity.this, 0);
                        } else if ("标清".equals(type)) {
                            SharedPreferencesTools.saveClarity(BofangAndDownActivity.this, 1);
                        } else if ("高清".equals(type)) {
                            SharedPreferencesTools.saveClarity(BofangAndDownActivity.this, 2);
                        }
                    }
                });
                dialog_down.show();
                break;
            case R.id.layout_everyremind_check:
                iv_oneremind_check.setVisibility(View.INVISIBLE);
                iv_everyremind_check.setVisibility(View.VISIBLE);
                SharedPreferencesTools.saveIsEveryReminder(BofangAndDownActivity.this, true);
                SharedPreferencesTools.saveIsOneReminder(BofangAndDownActivity.this, false);
                break;
            case R.id.layout_oneremind_check:
                iv_everyremind_check.setVisibility(View.INVISIBLE);
                iv_oneremind_check.setVisibility(View.VISIBLE);
                SharedPreferencesTools.saveIsEveryReminder(BofangAndDownActivity.this, false);
                SharedPreferencesTools.saveIsOneReminder(BofangAndDownActivity.this, true);
                break;
            case R.id.layout_phonecache_check:
                iv_phonecache_check.setVisibility(View.VISIBLE);
                iv_sdcache_check.setVisibility(View.INVISIBLE);
                SharedPreferencesTools.saveCachePath(BofangAndDownActivity.this, URLConfig.ccmtvapp_basesdcardpath);
                break;
            case R.id.layout_sdcache_check:
                iv_sdcache_check.setVisibility(View.VISIBLE);
                iv_phonecache_check.setVisibility(View.INVISIBLE);
                SharedPreferencesTools.saveCachePath(BofangAndDownActivity.this, ccmtvapp_sdcardpath);
                break;
            case R.id.layout_onlywifi:
                if (iv_opendown.isSelected()) {
                    iv_opendown.setSelected(false);
                    SharedPreferencesTools.saveOnlyWifiDown(BofangAndDownActivity.this, false);
                } else {
                    iv_opendown.setSelected(true);
                    SharedPreferencesTools.saveOnlyWifiDown(BofangAndDownActivity.this, true);
                }
                break;
            default:
                break;
        }
    }

    private void updateMemoryStatus() {
        try {
            tv_phonememory.setText("总量" + FileSizeUtil.FormetFileSize(SDCardUtils.getTotalInternalMemorySize()) + "/" + "剩余" + FileSizeUtil.FormetFileSize(SDCardUtils.getAvailableInternalMemorySize()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取所有大小
    public long getAllSize() {


        StatFs stat = new StatFs(getSdcardPath.getStoragePath(BofangAndDownActivity.this, true));

        long blockSize = stat.getBlockSize();


        long availableBlocks = stat.getBlockCount();


        return availableBlocks * blockSize;

    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public long getAvailableExternalMemorySize() {
        StatFs stat = new StatFs(getSdcardPath.getStoragePath(BofangAndDownActivity.this, true));
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    //获取sdk版本
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }


}

/**
 * name：获取内置，外置SD卡路径  返回List  用于判断是否存在外置SD卡，存在的显示选择SD卡路径布局  不存在则隐藏SD卡路径布局
 * author：Larry
 * data：2016/6/24 11:09
 */
class SDCardScanner {
    /*
     * avoid initializations of tool classes
     *http://www.cnblogs.com/littlepanpc/p/3868369.html
     */
    private SDCardScanner() {
    }

    /**
     * @return List<String>
     * @throws IOException
     * @Title: getExtSDCardPaths
     * @Description: to obtain storage paths, the first path is theoretically
     * the returned value of
     * Environment.getExternalStorageDirectory(), namely the
     * primary external storage. It can be the storage of internal
     * device, or that of external sdcard. If paths.size() >1,
     * basically, the current device contains two type of storage:
     * one is the storage of the device itself, one is that of
     * external sdcard. Additionally, the paths is directory.
     */
    public static List<String> getExtSDCardPaths() {
        List<String> paths = new ArrayList<String>();
        String extFileStatus = Environment.getExternalStorageState();
        File extFile = Environment.getExternalStorageDirectory();
        if (extFileStatus.equals(Environment.MEDIA_MOUNTED)
                && extFile.exists() && extFile.isDirectory()
                && extFile.canWrite()) {
            paths.add(extFile.getAbsolutePath());
        }
        try {
            // obtain executed result of command line code of 'mount', to judge
            // whether tfCard exists by the result
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("mount");
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            int mountPathIndex = 1;
            try {
                while ((line = br.readLine()) != null) {
                    // format of sdcard file system: vfat/fuse
                    if ((!line.contains("fat") && !line.contains("fuse") && !line
                            .contains("storage"))
                            || line.contains("secure")
                            || line.contains("asec")
                            || line.contains("firmware")
                            || line.contains("shell")
                            || line.contains("obb")
                            || line.contains("legacy") || line.contains("data")) {
                        continue;
                    }
                    String[] parts = line.split(" ");
                    int length = parts.length;
                    if (mountPathIndex >= length) {
                        continue;
                    }
                    String mountPath = parts[mountPathIndex];
                    if (!mountPath.contains("/") || mountPath.contains("data")
                            || mountPath.contains("Data")) {
                        continue;
                    }
                    File mountRoot = new File(mountPath);
                    if (!mountRoot.exists() || !mountRoot.isDirectory()
                            || !mountRoot.canWrite()) {
                        continue;
                    }
                    boolean equalsToPrimarySD = mountPath.equals(extFile
                            .getAbsolutePath());
                    if (equalsToPrimarySD) {
                        continue;
                    }
                    paths.add(mountPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return paths;
    }
}

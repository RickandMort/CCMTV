package com.linlic.ccmtv.yx.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.CustomActivity;
import com.linlic.ccmtv.yx.activity.MainActivity;
import com.linlic.ccmtv.yx.activity.Popular_search;
import com.linlic.ccmtv.yx.activity.base.BaseFragment;
import com.linlic.ccmtv.yx.activity.base.LocalApplication;
import com.linlic.ccmtv.yx.activity.channel.Channel_selection;
import com.linlic.ccmtv.yx.activity.conference.ConferenceDetailActivity;
import com.linlic.ccmtv.yx.activity.conference.ConferenceMainActivity;
import com.linlic.ccmtv.yx.activity.entity.Home_Video;
import com.linlic.ccmtv.yx.activity.entity.Icon;
import com.linlic.ccmtv.yx.activity.home.adapter.HomeNewRecyclerViewAdapter;
import com.linlic.ccmtv.yx.activity.home.apricotcup.ApricotActivity;
import com.linlic.ccmtv.yx.activity.home.hxsl.BreatheActivity;
import com.linlic.ccmtv.yx.activity.home.willowcup.WillowCupActivity;
import com.linlic.ccmtv.yx.activity.home.yxzbjrrom.MedicalLiveRoomActivity;
import com.linlic.ccmtv.yx.activity.hospital_training.Hospital_training_entrance;
import com.linlic.ccmtv.yx.activity.login.LoginActivity;
import com.linlic.ccmtv.yx.activity.login.Qr_code_to_log_in;
import com.linlic.ccmtv.yx.activity.medical_database.Medical_database_Main;
import com.linlic.ccmtv.yx.activity.my.MyInvitationFriend;
import com.linlic.ccmtv.yx.activity.my.book.Video_book_Main;
import com.linlic.ccmtv.yx.activity.my.dialog.ReportDialog;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.training_management.Event_Details2;
import com.linlic.ccmtv.yx.activity.user_statistics.StudyRecordListActivity;
import com.linlic.ccmtv.yx.activity.vip.adapter.OnRecyclerviewItemClickListener;
import com.linlic.ccmtv.yx.adapter.home_videos_GridAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.kzbf.MedicineMessageActivity;
import com.linlic.ccmtv.yx.kzbf.SkyProtocolActivity;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.GlideImageLoader;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyGridView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.utils.ToastUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.linlic.ccmtv.yx.widget.Util.dip2px;

/**
 * Created by Administrator on 2018/5/9.
 */

public class HomeFragment_new extends BaseFragment implements View.OnClickListener {
    private int REQUEST_CODE = 1;
    private ImageView browsingRecord, personalCenter, scanning;
    private RelativeLayout search;
    private Banner banner;
    //    private HorizontalListView horizontalListView;
    private RecyclerView recyclerView;
    //    Home_horizontalListviewAdapter home_horizontalListviewAdapter;
    HomeNewRecyclerViewAdapter homeNewRecyclerViewAdapter;
    private List<String> images = new ArrayList<>();
    private List<Icon> icons = new ArrayList<>();
    private JSONArray indexInfoArray;
    private LinearLayout medicine_layout, surgery_layout, children_and_women_layout, other_layout, layout_gone;
    private ImageView medicine_img, surgery_img, children_and_women_img, other_img;
    private ImageView banner1;
    private MyGridView features_gridview;
    private TextView medicine_title, surgery_title, children_and_women_title, other_title;
    private TextView newest_hottest, super_access, lecture_room, famous_view_of_famous_artists;
    private View newest_hottest_bg, super_access_bg, lecture_room_bg, famous_view_of_famous_artists_bg;
    private TextView newest_hottest1, super_access1, lecture_room1, famous_view_of_famous_artists1;
    private View newest_hottest_bg1, super_access_bg1, lecture_room_bg1, famous_view_of_famous_artists_bg1;
    private MyGridView videos;//视频集合
    //    private MyGridView videos;//视频集合
    private home_videos_GridAdapter home_videos_gridAdapter;
    public List<Home_Video> videodata = new ArrayList<>();
    private int page;
    private boolean is = true;
    private NestedScrollView nestedScrollView;
    private int keyid = 0;
    private String token;
    private boolean is_show_red;
    BaseListAdapter baseListAdapter;
    private List<Map<String, Object>> features_data = new ArrayList<Map<String, Object>>();
    private String urlDefaultResult = "{\"status\":\"1\",\"data\":{\"ccmtvactivity\":[{\"id\":\"1297\",\"title\":\"\\u7b2c\\u4e00\\u5c4a\\u4e0a\\u6d77\\u80c3\\u764c\\u534f\\u4f5c\\u521b\\u65b0\\u8bba\\u575b\",\"picurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/special\\/_20180503110500_qjq0d.jpg\",\"urlflg\":2,\"activityurl\":\"\"},{\"id\":\"1292\",\"title\":\"2018\\u5e74\\u957f\\u4e09\\u89d2\\u809d\\u75c5MDTL\\u8bba\\u575b\",\"picurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/special\\/_20180503110506_1ks6f.jpg\",\"urlflg\":2,\"activityurl\":\"\"},{\"id\":\"1288\",\"title\":\"2018\\u7b2c\\u4e03\\u5c4a\\u56fd\\u9645\\u5e15\\u91d1\\u68ee\\u75c5\\u53ca\\u8fd0\\u52a8\\u969c\\u788d\\u5b66\\u672f\\u4f1a\\u8bae\\u897f\\u6e56\\u8bba\\u575b\",\"picurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/special\\/_20180424150406_um5wo.jpg\",\"urlflg\":2,\"activityurl\":\"\"},{\"id\":\"1283\",\"title\":\"2018\\u5e74\\u91cd\\u6027\\u7cbe\\u795e\\u969c\\u788d\\u9ad8\\u5cf0\\u8bba\\u575b\",\"picurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/special\\/_20180416130412_stfnd.jpg\",\"urlflg\":2,\"activityurl\":\"\"},{\"picurl\":\"http:\\/\\/shgme.ccmtv.cn\\/images\\/mob\\/banner.jpg\",\"id\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/2018guipei\\/mobile\\/index.php\",\"title\":\"\\u4e0a\\u6d77\\u8bba\\u575b2018GME\\u6bd5\\u4e1a\\u540e\\u533b\\u5b66\\u6559\\u80b2\\u4ea4\\u6d41\\u5927\\u4f1a\",\"urlflg\":1,\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/2018guipei\\/mobile\\/index.php\"}],\"indexInfo\":[{\"id\":\"9999\",\"title\":\"\\u9080\\u8bf7\\u597d\\u53cb\",\"picurl\":\"http:\\/\\/www.ccmtv.cn\\/do\\/ccmtvappandroid\\/dayicon\\/yaoqinghaoyou.jpg\",\"urlflg\":0,\"activityurl\":\"\"}],\"iconArr\":[{\"title\":\"\\u624b\\u672f\\u6f14\\u793a\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/shoushu.png\",\"isnet\":0,\"type\":\"1\",\"tzurl\":\"\"},{\"title\":\"\\u75c5\\u4f8b\\u8ba8\\u8bba\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/bingli.png\",\"isnet\":0,\"type\":\"2\",\"tzurl\":\"\"},{\"title\":\"\\u8d85\\u7ea7\\u8bbf\\u95ee\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/chaoji.png\",\"isnet\":0,\"type\":\"3\",\"tzurl\":\"\"},{\"title\":\"\\u767e\\u5bb6\\u8bb2\\u575b\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/baijia.png\",\"isnet\":0,\"type\":\"4\",\"tzurl\":\"\"},{\"title\":\"\\u533b\\u5b66\\u76f4\\u64ad\\u95f4\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/yixue.png\",\"isnet\":0,\"type\":\"5\",\"tzurl\":\"\"},{\"title\":\"\\u6267\\u4e1a\\u8003\\u8bd5\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/zhiye.png\",\"isnet\":0,\"type\":\"6\",\"tzurl\":\"\"},{\"title\":\"AR\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/indexicon\\/ar.png\",\"isnet\":0,\"type\":\"7\",\"tzurl\":\"\"},{\"title\":\"\\u7a7a\\u4e2d\\u62dc\\u8bbf\",\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/do\\/ccmtvappandroid\\/newicon\\/kzbf.png\",\"isnet\":0,\"type\":\"8\",\"tzurl\":\"\"}],\"icon\":[{\"id\":\"1\",\"kid\":\"1\",\"kname\":\"\\u5185\\u79d1\",\"icon\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/neike.png\"},{\"id\":\"2\",\"kid\":\"2\",\"kname\":\"\\u5916\\u79d1\",\"icon\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/waike.png\"},{\"id\":\"3\",\"kid\":\"3\",\"kname\":\"\\u5987\\u513f\\u79d1\",\"icon\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/fuerke.png\"},{\"id\":\"4\",\"kid\":\"4\",\"kname\":\"\\u5176\\u4ed6\",\"icon\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/qita.png\"}],\"meet_one\":[{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/banner2.jpg\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/pro_sh\\/2018_occ_east\\/m.php\",\"activitytitle\":\"OCC2018\\u4e1c\\u65b9PCI\\u5f71\\u9662\",\"urlflg\":1}],\"meet_two\":[{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/z2.png\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/\\/upload_files\\/new_upload_files\\/pro_sh\\/ccmtvhy\\/m1136.php\",\"activitytitle\":\"\\u7b2c\\u5341\\u5c4a\\u4e2d\\u56fd\\u533b\\u5e08\\u534f\\u4f1a\\u5916\\u79d1\\u533b\\u5e08\\u5e74\\u4f1a\\uff08CCS2017\\uff09\",\"urlflg\":0},{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/z3.png\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/\\/upload_files\\/new_upload_files\\/pro_sh\\/ccmtvhy\\/m1112.php\",\"activitytitle\":\"\\u4e0a\\u6d77\\u5e02\\u7b2c\\u516b\\u5c4a\\u98df\\u7ba1\\u80c3\\u9759\\u8109\\u66f2\\u5f20\\u5b66\\u672f\\u4f1a\\u8bae\",\"urlflg\":0},{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/pdicon\\/z4.png\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/\\/upload_files\\/new_upload_files\\/pro_sh\\/ccmtvhy\\/m1124.php\",\"activitytitle\":\"\\u4e0a\\u6d77\\u5e02\\u533b\\u5b66\\u4f1a\\u521b\\u4f24\\u9aa8\\u79d1\\u4f1a\\u66a8\\u7b2c\\u4e94\\u5c4a\\u957f\\u6d77\\u521b\\u4f24\\u9aa8\\u79d1\\u7126\\u70b9\\u8bba\\u575b\",\"urlflg\":0}],\"meet_three\":[{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/label\\/_20170426170403_qfyfw.jpg\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/pro_sh\\/jishuitan\\/m.php\",\"activitytitle\":\"\\u79ef\\u6c34\\u6f6d\\u9aa8\\u79d1\\u8bfe\\u7a0b\",\"urlflg\":1},{\"imgurl\":\"http:\\/\\/www.ccmtv.cn\\/upload_files\\/new_upload_files\\/pro_sh\\/ccmtvhy\\/img\\/m1112.jpg\",\"activityurl\":\"http:\\/\\/www.ccmtv.cn\\/\\/upload_files\\/new_upload_files\\/pro_sh\\/ccmtvhy\\/m1112.php\",\"activitytitle\":\"\\u4e0a\\u6d77\\u5e02\\u7b2c\\u516b\\u5c4a\\u98df\\u7ba1\\u80c3\\u9759\\u8109\\u66f2\\u5f20\\u5b66\\u672f\\u4f1a\\u8bae\",\"urlflg\":1}]},\"errorMessage\":\"\\u9996\\u9875\\u6570\\u636e\"}";
    private String videoDefaultResult = "{\"status\":\"1\",\"data\":[{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u672f_3877_10010623.jpg!350x197.jpg\",\"title\":\"\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u672f\",\"aid\":\"45342\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u674e\\u767d\\uff1a\\u524d\\u5217\\u817a\\u89e3\\u5256\\u6027\\u525c\\u9664\\u672f_3857_10068545.jpg!350x197.jpg\",\"title\":\"\\u674e\\u767d\\uff1a\\u524d\\u5217\\u817a\\u89e3\\u5256\\u6027\\u525c\\u9664\\u672f\",\"aid\":\"45288\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/article\\/3220\\/tangml_20180515110535_tqvsq.jpg!350x197.jpg\",\"title\":\"\\u53f8\\u5f92\\u5347\\uff1a\\u9611\\u5c3e\\u8113\\u80bf\\u7684\\u8179\\u8154\\u955c\\u624b\\u672f\",\"aid\":\"45533\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u502a\\u7530\\u6839\\uff1a\\u5168\\u4e73\\u6655\\u5165\\u8def\\u8154\\u955c\\u5de6\\u4fa7\\u7532\\u72b6\\u817a\\u817a\\u53f6+\\u5ce1\\u90e8\\u5207\\u9664\\u672f+\\u5de6\\u4fa7\\u4e2d\\u592e\\u533a\\u6dcb\\u5df4\\u7ed3\\u6e05\\u626b_3954_8586.jpg!350x197.jpg\",\"title\":\"\\u502a\\u7530\\u6839\\uff1a\\u5168\\u4e73\\u6655\\u5165\\u8def\\u8154\\u955c\\u5de6\\u4fa7\\u7532\\u72b6\\u817a\\u817a\\u53f6+\\u5ce1\\u90e8\\u5207\\u9664\\u672f+\\u5de6\\u4fa7\\u4e2d\\u592e\\u533a\\u6dcb\\u5df4\\u7ed3\\u6e05\\u626b\",\"aid\":\"46352\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u708e\\u4e4b\\u9006\\u884c\\u5207\\u9664_3998_10010623.jpg!350x197.jpg\",\"title\":\"\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u708e\\u4e4b\\u9006\\u884c\\u5207\\u9664\",\"aid\":\"46535\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u672f\\u8377\\u5305\\u7f1d\\u5408_3943_10010623.jpg!350x197.jpg\",\"title\":\"\\u5218\\u6d2a\\u6ce2\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u672f\\u8377\\u5305\\u7f1d\\u5408\",\"aid\":\"46201\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u9648\\u6c38\\u80dc\\uff1a\\u8179\\u8154\\u955c\\u4e0b\\u8179\\u80a1\\u6c9f\\u6dcb\\u5df4\\u7ed3\\u6e05\\u626b_3856_10008262.jpg!350x197.jpg\",\"title\":\"\\u9648\\u6c38\\u80dc\\uff1a\\u8179\\u8154\\u955c\\u4e0b\\u8179\\u80a1\\u6c9f\\u6dcb\\u5df4\\u7ed3\\u6e05\\u626b\",\"aid\":\"45279\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u674e\\u98de\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u8377\\u5305\\u7f1d\\u5408\\u5305\\u57cb_4012_5556000.jpg!350x197.jpg\",\"title\":\"\\u674e\\u98de\\uff1a\\u8179\\u8154\\u955c\\u9611\\u5c3e\\u5207\\u9664\\u8377\\u5305\\u7f1d\\u5408\\u5305\\u57cb\",\"aid\":\"46595\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/article\\/3271\\/tangml_20180515110509_dxnmn.jpg!350x197.jpg\",\"title\":\"\\u5168\\u8eab\\u4f53\\u683c\\u68c0\\u67e5\",\"aid\":\"45538\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"},{\"picurl\":\"http:\\/\\/img.ccmtv.cn\\/upload_files\\/2018_upload_files\\/20180103sug\\/\\u7a0b\\u4e91\\u751f\\uff1a\\u8179\\u8154\\u955c\\u53f3\\u534a\\u7ed3\\u80a0\\u764c\\uff08D2\\uff09\\u6839\\u6cbb\\u672f_3879_5556313.jpg!350x197.jpg\",\"title\":\"\\u7a0b\\u4e91\\u751f\\uff1a\\u8179\\u8154\\u955c\\u53f3\\u534a\\u7ed3\\u80a0\\u764c\\uff08D2\\uff09\\u6839\\u6cbb\\u672f\",\"aid\":\"45344\",\"flag\":\"0\",\"videopaymoney\":0,\"money\":\"5\"}],\"errorMessage\":\"\\u83b7\\u53d6\\u89c6\\u9891\\u6570\\u636e\"}";

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject data = result.getJSONObject("data");
                            //清空轮播图数据地址
                            images.clear();
                            indexInfoArray = new JSONArray();
                            //处理轮播图数据
                            JSONArray indexInfo = new JSONArray(data.getString("indexInfo"));
                            JSONArray activityArray = new JSONArray(data.getString("ccmtvactivity"));


                            for (int i = 0; i < activityArray.length(); i++) {
                                JSONObject activityObj = new JSONObject(activityArray.get(i).toString());
                                activityObj.put("picurl", activityObj.getString("picurl"));
                                images.add(activityObj.getString("picurl"));
                                activityObj.put("aid", activityObj.getString("id"));
                                activityObj.put("title", activityObj.getString("title"));
                                switch (activityObj.getString("urlflg")) {//
                                    case "0":
                                        activityObj.put("isActivity", 0);//isActivity 0为邀请好友等，1为网页链接，2为会议专题
                                        break;
                                    case "1":
                                        activityObj.put("isActivity", 1);//isActivity 0为邀请好友等，1为网页链接，2为会议专题
                                        activityObj.put("aid", activityObj.getString("activityurl"));//如果是网页，参数aid重新赋值为url地址
                                        break;
                                    case "2":
                                        activityObj.put("isActivity", 2);//isActivity 0为邀请好友等，1为网页链接，2为会议专题
                                        break;
                                    default:
                                        activityObj.put("isActivity", 1);//isActivity 0为邀请好友等，1为网页链接，2为会议专题
                                        activityObj.put("aid", activityObj.getString("activityurl"));//如果是网页，参数aid重新赋值为url地址
                                        break;

                                }

                                indexInfoArray.put(activityObj);
                            }

                            for (int i = 0; i < indexInfo.length(); i++) {
                                JSONObject activityObj = new JSONObject(indexInfo.get(i).toString());
                                activityObj.put("picurl", activityObj.getString("picurl"));
                                images.add(activityObj.getString("picurl"));
                                activityObj.put("aid", activityObj.getString("id"));
                                activityObj.put("title", activityObj.getString("title"));


                                switch (activityObj.getString("urlflg")) {//urlflg = 0的时候是专题的id,urlflg=1的时id为网页链接
                                    case "0":
                                        activityObj.put("isActivity", 0);//isActivity 0为邀请好友等，1为网页链接，2为会议专题
                                        break;
                                    case "1":
                                        activityObj.put("isActivity", 1);
                                        activityObj.put("aid", activityObj.getString("activityurl"));
                                        break;
                                    case "2":
                                        activityObj.put("isActivity", 2);
                                        break;
                                    default:
                                        activityObj.put("isActivity", 1);
                                        activityObj.put("aid", activityObj.getString("activityurl"));//如果是网页，参数aid重新赋值为url地址
                                        break;

                                }
                                indexInfoArray.put(activityObj);
                            }
                            //初始化轮播图
                            initBanner();
                            //获取ICON集合
                            JSONArray iconArrs = data.getJSONArray("icon");
                            //循环取出ICON
                            for (int i = 0; i < iconArrs.length(); i++) {
                                JSONObject json = iconArrs.getJSONObject(i);
//                                private ImageView medicine_img,surgery_img,children_and_women_img,other_img;
//                                private TextView medicine_title,surgery_title,children_and_women_title,other_title;
                                switch (i) {
                                    case 0:
                                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(json.getString("icon")), medicine_img);
                                        medicine_img.setTag(json.getString("id"));
                                        medicine_title.setText(json.getString("kname"));
                                        medicine_title.setTag(json.getString("kid"));
                                        break;
                                    case 1:
                                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(json.getString("icon")), surgery_img);
                                        surgery_img.setTag(json.getString("id"));
                                        surgery_title.setText(json.getString("kname"));
                                        surgery_title.setTag(json.getString("kid"));
                                        break;
                                    case 2:
                                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(json.getString("icon")), children_and_women_img);
                                        children_and_women_img.setTag(json.getString("id"));
                                        children_and_women_title.setText(json.getString("kname"));
                                        children_and_women_title.setTag(json.getString("kid"));
                                        break;
                                    case 3:
                                        com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(json.getString("icon")), other_img);
                                        other_img.setTag(json.getString("id"));
                                        other_title.setText(json.getString("kname"));
                                        other_title.setTag(json.getString("kid"));
                                        break;
                                    default:
                                        break;
                                }

                            }

                            //初始化8个ICON
                            icons.clear();
                            JSONArray iconArr = new JSONArray(data.getString("iconArr"));
                            for (int i = 0; i < iconArr.length(); i++) {
                                JSONObject iconJson = iconArr.getJSONObject(i);
                                icons.add(new Icon(iconJson.getString("imgurl"), iconJson.getString("title"), iconJson.getString("type"), iconJson.getString("tzurl")));
                            }
                            //初始化8个ICON
                            initRecyclerView();
                            getKzbfIsShowRedDot();

                            //初始化一张banner图
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(data.getJSONArray("meet_one").getJSONObject(0).getString("imgurl")), banner1);
                            banner1.setTag(data.getJSONArray("meet_one").getString(0));
                            //年终报告
                            Log.d("mason", "数据------" + data.getJSONArray("meet_one"));
                            JSONObject meetOneData = new JSONObject(data.getJSONArray("meet_one").getString(0));
                            if (meetOneData.has("summary")) {
                                if ("1".equals(meetOneData.getString("summary"))) {
                                    //1为年终总结
                                    showReportDialog(data.getJSONArray("meet_one").getString(0));
                                }
                            }
                            features_data.clear();
                            JSONArray featuresArr = new JSONArray(data.getString("newmeeting"));
                            if (featuresArr.length() != 0) {
                                for (int i = 0; i < featuresArr.length(); i++) {
                                    Map<String, Object> map = new HashMap<>();
                                    JSONObject featuresJson = featuresArr.getJSONObject(i);
                                    map.put("imgurl", featuresJson.getString("imgurl"));
                                    map.put("activityurl", featuresJson.getString("activityurl"));
                                    map.put("activitytitle", featuresJson.getString("activitytitle"));
                                    map.put("urlflg", featuresJson.getString("urlflg"));
                                    features_data.add(map);
                                }
                                baseListAdapter.notifyDataSetChanged();
                            }
                        } else {                                                                        // 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataArray = result.getJSONObject("data");
                            Intent intent = new Intent(getActivity(), VideoFive.class);
                            intent.putExtra("aid", dataArray.getString("aid"));
                            if (dataArray.getString("videoplaystyle").equals("noresource")) {
                                Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(intent);
                            }
                        } else {// 失败
                            MyProgressBarDialogTools.hide();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            Intent intent = new Intent(getActivity(), Qr_code_to_log_in.class);
//                            intent.putExtra("token", token);
                            startActivity(intent);
                        } else {// 失败
                            Toast.makeText(getActivity(), result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("0")) {// 成功
                            if (result.getString("sub").equals("1")) {//已签署
                                startActivity(new Intent(getActivity(), MedicineMessageActivity.class));
                            } else {// 未签署
                                startActivity(new Intent(getActivity(), SkyProtocolActivity.class));
                            }
                        } else {// 失败
                            Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            if (page == 1) {
                                videodata.clear();
                            }
                            JSONArray data = result.getJSONArray("data");
                            if (data.length() < 10) {
                                is = false;
                            }
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject videoJson = data.getJSONObject(i);
                                videodata.add(new Home_Video(videoJson.getString("aid"), videoJson.getString("title"), videoJson.getString("picurl"), videoJson.getString("flag"), videoJson.getString("videopaymoney"), videoJson.getString("money")));
                            }
                            home_videos_gridAdapter.notifyDataSetChanged();

                        } else {// 失败
                            Toast.makeText(getContext(), result.getString("msg"), Toast.LENGTH_SHORT).show();
                            is = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        JSONObject result = new JSONObject(msg.obj + "");
                        if (result.getString("code").equals("0")) {
                            if (result.getString("is_show_red").equals("1")) {
                                //is_show_red    1显示 0隐藏
                                is_show_red = true;
                            } else {
                                is_show_red = false;
                            }
                            homeNewRecyclerViewAdapter.setIsShowRedDot(is_show_red);
                            homeNewRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 8:
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");

                            if (dataJson.getInt("status") == 1) { // 成功
                                Toast.makeText(getContext(), dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                                Intent intent = null;
                                intent = new Intent(getContext(), Event_Details2.class);
                                intent.putExtra("id", dataJson.getJSONObject("dataList").getString("id"));
                                intent.putExtra("fid", dataJson.getJSONObject("dataList").getString("fid"));
                                if (intent != null) {
                                    startActivity(intent);
                                }

                            } else {
                                Toast.makeText(getContext(), dataJson.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                            }
//
                        } else {
                            Toast.makeText(getContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 500:
                    Toast.makeText(getActivity(), R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    break;
                default:
                    break;
            }
        }
    };
    private ReportDialog reportDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // 解决当程序crash，切换fragment无效的问题
        if (savedInstanceState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_new, container, false);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        page = 1;
        initVideos();
        setOnclicks();
        ImageGalleryAdapter(getContext());

        setDefaultResult();
        getUrlRulest();
        getVideoRulest();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (homeNewRecyclerViewAdapter != null) {
            getKzbfIsShowRedDot();
        }
    }

    public void ImageGalleryAdapter(Context c) {
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels;
        float screenHeight = dm.heightPixels;
        float sss = screenHeight / screenWidth;
        if (sss > 1.80) {


            Banner.LayoutParams paramsBanner = (Banner.LayoutParams) banner.getLayoutParams();
            //获取当前控件的布局对象
            paramsBanner.height = dip2px(getContext(), 165);
            //设置当前控件布局的高度
            banner.setLayoutParams(paramsBanner);//将设置好的布局参数应用到控件中
        } else {
            Banner.LayoutParams paramsBanner = (Banner.LayoutParams) banner.getLayoutParams();
            //获取当前控件的布局对象
            paramsBanner.height = dip2px(getContext(), 140);
            //设置当前控件布局的高度
            banner.setLayoutParams(paramsBanner);//将设置好的布局参数应用到控件中
        }
    }

    private void setDefaultResult() {
        try {
            Message message = new Message();
            message.what = 1;
            message.obj = urlDefaultResult;
            handler.sendMessage(message);

            Message message2 = new Message();
            message2.what = 6;
            message2.obj = videoDefaultResult;
            handler.sendMessage(message2);
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(500);
        }
    }

    public void initViews() {


        videos = (MyGridView) getActivity().findViewById(R.id.videos);


        medicine_img = (ImageView) getActivity().findViewById(R.id.medicine_img);
        surgery_img = (ImageView) getActivity().findViewById(R.id.surgery_img);
        children_and_women_img = (ImageView) getActivity().findViewById(R.id.children_and_women_img);
        other_img = (ImageView) getActivity().findViewById(R.id.other_img);
        medicine_title = (TextView) getActivity().findViewById(R.id.medicine_title);
        surgery_title = (TextView) getActivity().findViewById(R.id.surgery_title);
        children_and_women_title = (TextView) getActivity().findViewById(R.id.children_and_women_title);
        other_title = (TextView) getActivity().findViewById(R.id.other_title);
        newest_hottest = (TextView) getActivity().findViewById(R.id.newest_hottest);
        super_access = (TextView) getActivity().findViewById(R.id.super_access);
        lecture_room = (TextView) getActivity().findViewById(R.id.lecture_room);
        famous_view_of_famous_artists = (TextView) getActivity().findViewById(R.id.famous_view_of_famous_artists);
        newest_hottest_bg = getActivity().findViewById(R.id.newest_hottest_bg);
        super_access_bg = getActivity().findViewById(R.id.super_access_bg);
        lecture_room_bg = getActivity().findViewById(R.id.lecture_room_bg);
        famous_view_of_famous_artists_bg = getActivity().findViewById(R.id.famous_view_of_famous_artists_bg);
        newest_hottest1 = (TextView) getActivity().findViewById(R.id.newest_hottest1);
        super_access1 = (TextView) getActivity().findViewById(R.id.super_access1);
        lecture_room1 = (TextView) getActivity().findViewById(R.id.lecture_room1);
        famous_view_of_famous_artists1 = (TextView) getActivity().findViewById(R.id.famous_view_of_famous_artists1);
        newest_hottest_bg1 = getActivity().findViewById(R.id.newest_hottest_bg1);
        super_access_bg1 = getActivity().findViewById(R.id.super_access_bg1);
        lecture_room_bg1 = getActivity().findViewById(R.id.lecture_room_bg1);
        famous_view_of_famous_artists_bg1 = getActivity().findViewById(R.id.famous_view_of_famous_artists_bg1);
        banner1 = (ImageView) getActivity().findViewById(R.id.banner1);
        features_gridview = (MyGridView) getActivity().findViewById(R.id.features_gridview);
//        horizontalListView = (HorizontalListView) getActivity().findViewById(R.id.horizontalListView);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.id_recyclerview_home_new);
        browsingRecord = (ImageView) getActivity().findViewById(R.id.browsingRecord);
        search = (RelativeLayout) getActivity().findViewById(R.id.search);
        personalCenter = (ImageView) getActivity().findViewById(R.id.personalCenter);
        scanning = (ImageView) getActivity().findViewById(R.id.scanning);
        medicine_layout = (LinearLayout) getActivity().findViewById(R.id.medicine_layout);
        surgery_layout = (LinearLayout) getActivity().findViewById(R.id.surgery_layout);
        children_and_women_layout = (LinearLayout) getActivity().findViewById(R.id.children_and_women_layout);
        other_layout = (LinearLayout) getActivity().findViewById(R.id.other_layout);
        layout_gone = (LinearLayout) getActivity().findViewById(R.id.layout_gone);
        banner = (Banner) getActivity().findViewById(R.id.banner);

    }

    public void setOnclicks() {
        medicine_layout.setOnClickListener(this);
        surgery_layout.setOnClickListener(this);
        children_and_women_layout.setOnClickListener(this);
        other_layout.setOnClickListener(this);
        scanning.setOnClickListener(this);
        personalCenter.setOnClickListener(this);
        browsingRecord.setOnClickListener(this);
        search.setOnClickListener(this);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject json = new JSONObject(v.getTag().toString());
                    Intent intent = null;
                    if (json.getString("urlflg").equals("1")) {
                        intent = new Intent(getActivity(), ActivityWebActivity.class);
                        intent.putExtra("title", json.getString("activitytitle"));
                    } else {
                        if ("邀请好友".equals(json.getString("activitytitle"))) {
                            intent = new Intent(getActivity(), MyInvitationFriend.class);
                            intent.putExtra("type", "home");
                        } else if ("呼吸时令".equals(json.getString("activitytitle"))) {
                            intent = new Intent(getActivity(), BreatheActivity.class);
                        } else if (json.getString("activitytitle").contains("柳叶杯")) {
                            intent = new Intent(getActivity(), WillowCupActivity.class);
                            intent.putExtra("title", json.getString("activitytitle"));
                        } else if (json.getString("activitytitle").contains("杏林杯")) {
                            intent = new Intent(getActivity(), ApricotActivity.class);
                            intent.putExtra("title", json.getString("activitytitle"));
                        } else {
                            intent = new Intent(getActivity(), SpecialActivity.class);
                        }
                    }
                    if (json.has("summary")) {
                        if ("1".equals(json.getString("summary"))) {
                            //1为年终总结，拼接uid
                            intent.putExtra("aid", json.getString("activityurl") + "?uid=" + SharedPreferencesTools.getUidONnull(getActivity()));
                            if (json.has("shareimgurl")) {
                                intent.putExtra("shareimgurl", json.getString("shareimgurl"));
                            }
                            if (json.has("activitycontent")) {
                                intent.putExtra("activitycontent", json.getString("activitycontent"));
                            }
                        }
                    } else {
                        intent.putExtra("aid", json.getString("activityurl"));
                    }
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        newest_hottest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 0) {
                    page = 1;
                    keyid = 0;
                    newest_hottest_bg.setVisibility(View.VISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);

                    newest_hottest_bg1.setVisibility(View.VISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);

                    getVideoRulest();
                }
            }
        });
        super_access.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 1) {
                    page = 1;
                    keyid = 1;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.VISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.VISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);
                    getVideoRulest();
                }
            }
        });
        lecture_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 3) {
                    page = 1;
                    keyid = 3;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.VISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.VISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);
                    getVideoRulest();
                }
            }
        });
        famous_view_of_famous_artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 2) {
                    page = 1;
                    keyid = 2;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.VISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.VISIBLE);
                    getVideoRulest();
                }
            }
        });
        newest_hottest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 0) {
                    page = 1;
                    keyid = 0;
                    newest_hottest_bg.setVisibility(View.VISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);

                    newest_hottest_bg1.setVisibility(View.VISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);

                    getVideoRulest();
                }
            }
        });
        super_access1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 1) {
                    page = 1;
                    keyid = 1;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.VISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.VISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);
                    getVideoRulest();
                }
            }
        });
        lecture_room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 3) {
                    page = 1;
                    keyid = 3;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.VISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.INVISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.VISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.INVISIBLE);
                    getVideoRulest();
                }
            }
        });
        famous_view_of_famous_artists1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyid != 2) {
                    page = 1;
                    keyid = 2;
                    newest_hottest_bg.setVisibility(View.INVISIBLE);
                    super_access_bg.setVisibility(View.INVISIBLE);
                    lecture_room_bg.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg.setVisibility(View.VISIBLE);
                    newest_hottest_bg1.setVisibility(View.INVISIBLE);
                    super_access_bg1.setVisibility(View.INVISIBLE);
                    lecture_room_bg1.setVisibility(View.INVISIBLE);
                    famous_view_of_famous_artists_bg1.setVisibility(View.VISIBLE);
                    getVideoRulest();
                }
            }
        });

        videos.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色
        videos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView video_aid = (TextView) view.findViewById(R.id.video_aid);
                final String uid = SharedPreferencesTools.getUidToLoginClose(getContext());
                if (uid == null || ("").equals(uid)) {
                    return;
                }
                if (VideoFive.isFinish == null) {
                    Intent intent = new Intent(getContext(), VideoFive.class);
                    intent.putExtra("aid", video_aid.getText().toString());
                    getActivity().startActivity(intent);
                }
            }
        });
        nestedScrollView = (NestedScrollView) getActivity().findViewById(R.id.nestedScrollView);
        //视频列表 底部加载更多
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int x, int y, int oldScrollX, int oldScrollY) {
                int scrollY = v.getScrollY();//顶端以及滑出去的距离
                int height = v.getHeight();//界面的高度
                int scrollViewMeasuredHeight = v.getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                int scrollViewMeasuredHeight2 = ((LinearLayout) v.getChildAt(0)).getChildAt(0).getMeasuredHeight();//scrollview所占的高度
                if (scrollY == 0) {//在顶端的时候
//                    Toast.makeText(getContext(),"在顶端的时候  ", Toast.LENGTH_SHORT).show();
//                    LogUtil.e("滑动监听","在顶端的时候");
                    if (layout_gone.getVisibility() == View.VISIBLE) {
                        layout_gone.setVisibility(View.GONE);
                    }

                } else if ((scrollY + height) == scrollViewMeasuredHeight) {//当在底部的时候
//                    LogUtil.e("滑动监听","当在底部的时候");
//                    Toast.makeText(getContext(),"当在底部的时候    ", Toast.LENGTH_SHORT).show();
                    if (is) {
                        page++;
                        getVideoRulest();
                    }
                } else {
                    if (scrollY > scrollViewMeasuredHeight2) {
                        //属于 滑动出第一个VIEW
//                        LogUtil.e("滑动监听","滑动出第一个VIEW");
                        if (layout_gone.getVisibility() == View.GONE) {
                            layout_gone.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if (layout_gone.getVisibility() == View.VISIBLE) {
                            layout_gone.setVisibility(View.GONE);
                        }
                    }

                }
            }
        });


    }

    public void initVideos() {
        home_videos_gridAdapter = new home_videos_GridAdapter(getActivity(), videodata);
        videos.setAdapter(home_videos_gridAdapter);
//        videos.addOn

        baseListAdapter = new BaseListAdapter(features_gridview, features_data, R.layout.item_features) {
            @Override
            public void refresh(Collection datas) {
                super.refresh(datas);
            }

            @Override
            public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                super.convert(helper, item, isScrolling, position);
                //  1:网页专题    2：医院培训    3：医学专题    4：医学资料库    5：书架
                helper.setImageBitmapGlide(getContext(), R.id._item_img, ((Map) item).get("imgurl") + "");

            }
        };

        features_gridview.setAdapter(baseListAdapter);

        features_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Map<String, Object> map = features_data.get(position);
                Intent intent = null;
                switch (map.get("urlflg").toString()) {
                    case "1"://1:网页专题
                        intent = new Intent(getActivity(), ActivityWebActivity.class);
                        intent.putExtra("title", map.get("activitytitle").toString());
                        break;
                    case "2":// 2：医院培训
                        intent = new Intent(getActivity(), Hospital_training_entrance.class);
                        break;
                    case "3"://3：医学专题
                        intent = new Intent(getActivity(), ConferenceMainActivity.class);
                        break;
                    case "4":// 4：医学资料库
                        String uid = SharedPreferencesTools.getUid(getActivity());
                        if (uid == null || "".equals(uid)) {
                            intent = (new Intent(getActivity(), LoginActivity.class));
                        } else {
                            intent = new Intent(getActivity(), Medical_database_Main.class);
                        }
                        break;
                    case "5":// 5：书架
                        String uid2 = SharedPreferencesTools.getUid(getActivity());
                        if (uid2 == null || "".equals(uid2)) {
                            intent = (new Intent(getActivity(), LoginActivity.class));
                        } else {
                            intent = new Intent(getActivity(), Video_book_Main.class);
                        }
                        break;
                }

                intent.putExtra("aid", map.get("activityurl").toString());
                getActivity().startActivity(intent);
            }
        });

    }


    public void initRecyclerView() {
        /*home_horizontalListviewAdapter =new  Home_horizontalListviewAdapter(getActivity(),icons);
        horizontalListView.setAdapter(home_horizontalListviewAdapter);*/

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        homeNewRecyclerViewAdapter = new HomeNewRecyclerViewAdapter(getActivity(), icons, onRecyclerviewItemClickListener);
        recyclerView.setAdapter(homeNewRecyclerViewAdapter);
    }

    private OnRecyclerviewItemClickListener onRecyclerviewItemClickListener = new OnRecyclerviewItemClickListener() {
        @Override
        public void onItemClickListener(View v, int position) {
            TextView home_horizontallistview_item_title = (TextView) v.findViewById(R.id.home_horizontallistview_item_title);
            Intent intent = null;
            switch (home_horizontallistview_item_title.getText().toString()) {
                case "手术演示":
                    intent = new Intent(getActivity(), CustomActivity.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("video_class", "手术演示");
                    intent.putExtra("mode", "5");
                    break;
                case "病例讨论":
                    intent = new Intent(getActivity(), CustomActivity.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("video_class", "病例讨论");
                    intent.putExtra("mode", "5");
                    break;
                case "超级访问":
                    intent = new Intent(getActivity(), CustomActivity.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("video_class", "超级访问");
                    intent.putExtra("mode", "5");
                    break;
                case "百家讲坛":
                    intent = new Intent(getActivity(), CustomActivity.class);
                    intent.putExtra("type", "home");
                    intent.putExtra("video_class", "百家讲坛");
                    intent.putExtra("mode", "5");
                    break;
                case "医学直播间":
                    intent = new Intent(getActivity(), MedicalLiveRoomActivity.class);  //跳转至医学直播间
//                intent = new Intent(getActivity(), Direct_broadcast_main.class);
                    break;
                case "执业考试":
                    intent = new Intent(getActivity(), Practicing_physician_examination.class);
                    break;
                case "职业考试":
                    intent = new Intent(getActivity(), Practicing_physician_examination.class);
                    break;
                case "AR":
                    //intent = new Intent(getActivity(), VideoAR.class);
                    intent = new Intent(getActivity(), ScanActivity.class);
                    intent.putExtra("defaultType", 1);
                    intent.putExtra("isClicked", true);
                    break;
                case "领取积分":
                    intent = new Intent(getActivity(), Hospital_training_entrance.class);
                    break;
                case "空中拜访":
                    IsSignAgreement();
                    //测试使用
//                intent = new Intent(getActivity(), Channel_selection.class);
//                        intent = new Intent(getActivity(), HomeFragment_new.class);
                    break;
                default:
                    break;
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
    };


    public void initBanner() {

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
//                Log.e("位置", position + "  =================");
                try {
                    JSONObject json = indexInfoArray.getJSONObject(position);
                    Intent intent = null;
                    /*if (json.getBoolean("isActivity")) {
                        intent = new Intent(getActivity(), ActivityWebActivity.class);
                        intent.putExtra("title", json.getString("title"));
                    } else {
                        if ("邀请好友".equals(json.getString("title"))) {
                            intent = new Intent(getActivity(), MyInvitationFriend.class);
                            intent.putExtra("type","home");
                        } else if ("呼吸时令".equals(json.getString("title"))) {
                            intent = new Intent(getActivity(), BreatheActivity.class);
                        } else if (json.getString("title").contains("柳叶杯")) {
                            intent = new Intent(getActivity(), WillowCupActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else if (json.getString("title").contains("杏林杯")) {
                            intent = new Intent(getActivity(), ApricotActivity.class);
                            intent.putExtra("title", json.getString("title"));
                        } else {
                            intent = new Intent(getActivity(), SpecialActivity.class);
                        }
                    }*/

                    //isActivity 0为邀请好友等，1为网页链接，2为会议专题
                    switch (json.getInt("isActivity")) {
                        case 0:
                            if ("邀请好友".equals(json.getString("title"))) {
                                intent = new Intent(getActivity(), MyInvitationFriend.class);
                                intent.putExtra("type", "home");
                            } else if ("呼吸时令".equals(json.getString("title"))) {
                                intent = new Intent(getActivity(), BreatheActivity.class);
                            } else if (json.getString("title").contains("柳叶杯")) {
                                intent = new Intent(getActivity(), WillowCupActivity.class);
                                intent.putExtra("title", json.getString("title"));
                            } else if (json.getString("title").contains("杏林杯")) {
                                intent = new Intent(getActivity(), ApricotActivity.class);
                                intent.putExtra("title", json.getString("title"));
                            } else {
                                intent = new Intent(getActivity(), SpecialActivity.class);
                            }
                            break;
                        case 1:
                            intent = new Intent(getActivity(), ActivityWebActivity.class);
                            intent.putExtra("title", json.getString("title"));
                            break;
                        case 2:
                            intent = new Intent(getActivity(), ConferenceDetailActivity.class);
                            intent.putExtra("conferenceId", json.getString("aid"));
                            break;
                        default:
                            intent = new Intent(getActivity(), ActivityWebActivity.class);
                            intent.putExtra("title", json.getString("title"));
                            break;
                    }
                    if (json.getString("urlflg").equals("999")) {
                        if (SharedPreferencesTools.getUidONnull(getContext()).equals("")) {
                            getActivity().startActivity(new Intent(getContext(), LoginActivity.class).putExtra("source", ""));
                            LocalApplication.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LocalApplication.getAppContext(), "账户未登录，请先登录", Toast.LENGTH_SHORT).show();
                                }
                            });

                            return;
                        } else {
                            intent.putExtra("aid", json.getString("aid") + "?uid=" + SharedPreferencesTools.getUid(getContext()));

                        }

                    } else {
                        intent.putExtra("aid", json.getString("aid"));
                    }
                    getActivity().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    /***
     * 刷新数据 MainAcitvity会调用
     */
    public void initdata() {

        page = 1;
//        keyid = 0;
        is = true;
        getUrlRulest();
        getVideoRulest();
    }

    public void getUrlRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.newIndex);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    obj.put("version", getVersionCode(getContext()) + "");
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.PDCCMTVAPP, obj.toString());
                    LogUtil.e("首页数据", result);

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getVideoRulest() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getVideo);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    obj.put("page", page);
                    obj.put("keyid", keyid);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.PDCCMTVAPP, obj.toString());
//                     LogUtil.e("首页视频请求数据", result);

                    Message message = new Message();
                    message.what = 6;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void getKzbfIsShowRedDot() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.indexIsShowRed);
                    if (SharedPreferencesTools.getUidONnull(getContext()).length() > 0) {
                        obj.put("uid", SharedPreferencesTools.getUidONnull(getContext()));
                    }
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.Skyvisit, obj.toString());
                    LogUtil.e("首页空中拜访是否显示小红点数据", result);

                    Message message = new Message();
                    message.what = 7;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        LogUtil.e("处理二维码扫描结果","home");
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    token = result;
                    if (token.contains("activities")) {
                        SharedPreferencesTools.getUid(getContext());
                        //访问签到接口
                        activitiesSign(result);
                    } else {

                    }
                    scan_valid();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(getActivity(), "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void activitiesSign(final String url) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.activitiesSign);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
//                    obj.put("fid", fid);
                    obj.put("url", url);

                    String result = HttpClientUtils.sendPostToGP(getContext(), URLConfig.CCMTVAPP_GpApi, obj.toString());
                    LogUtil.e("扫描签到", result);
                    Message message = new Message();
                    message.what = 3;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    public void scan_valid() {
        final String uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    token = token.substring(token.indexOf("token=") + 6, token.length());
                    obj.put("action", URLConfig.scan_valid);
                    obj.put("token", token);
                    obj.put("uid", uid);
                    String result = HttpClientUtils.sendPost(getActivity(), URLConfig.QR_CCMTVAPP, obj.toString());

                    Message message = new Message();
                    message.what = 4;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    //是否签署协议
    private void IsSignAgreement() {
        if (TextUtils.isEmpty(SharedPreferencesTools.getUid(getContext()))) {
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.isSubscribe);
                    obj.put("uid", SharedPreferencesTools.getUid(getContext()));
//                    Log.e("看看协议数据", obj.toString());
                    String result = HttpClientUtils.sendPost(getContext(), URLConfig.Skyvisit, obj.toString());
//                    Log.e("看看协议数据", result);

                    Message message = new Message();
                    message.what = 5;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(500);
                }
            }
        };
        new Thread(runnable).start();
    }

    private void showReportDialog(String data) {
        Log.d("mason", "isReportDialogShow----" + SharedPreferencesTools.isReportDialogShow(getActivity()));
        Log.d("mason", "isReportDialogHasShow----" + SharedPreferencesTools.isReportDialogHasShow(getActivity()));
        if (SharedPreferencesTools.isReportDialogShow(getActivity())) {
            if (SharedPreferencesTools.isReportDialogHasShow(getActivity())) {
//                reportDialog.show();
                reportDialog = new ReportDialog(getActivity(), data);
                SharedPreferencesTools.saveIsReportDialogHasShow(getActivity(), false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.search:
                intent = new Intent(getActivity(), Popular_search.class);
                intent.putExtra("mode", "1");//方式 1 表示从首页title-img 进入到热门搜索页 2 表示从搜索也进入到搜索页
                break;
            case R.id.personalCenter:
                String Str_Uid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid)) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.ToFragment(4);
                    mainActivity.refresh();
                }
                break;
            case R.id.browsingRecord:
                String browUid = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(browUid)) {
                    intent = new Intent(getActivity(), StudyRecordListActivity.class);
                    intent.putExtra("type", "video");
                }
                break;
            case R.id.scanning:                                                     //打开默认二维码扫描界面
//                Intent intent1 = new Intent(getActivity(), CaptureActivity.class);
//                startActivityForResult(intent1, REQUEST_CODE);


                Intent intent1 = new Intent(getActivity(), ScanActivity.class);     //二维码和AR扫描界面
                intent1.putExtra("defaultType", 0);
                startActivity(intent1);

                //todo 权限统一处理

                //  requestCameraPermission();
                break;
            case R.id.medicine_layout:
                String Str_Uid1 = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid1)) {
                    intent = new Intent(getActivity(), Channel_selection.class);
                    intent.putExtra("kid", medicine_title.getTag().toString());
                }
                break;
            case R.id.surgery_layout:
                String Str_Uid2 = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid2)) {
                    intent = new Intent(getActivity(), Channel_selection.class);
                    intent.putExtra("kid", surgery_title.getTag().toString());
                }
                break;
            case R.id.children_and_women_layout:
                String Str_Uid3 = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid3)) {
                    intent = new Intent(getActivity(), Channel_selection.class);
                    intent.putExtra("kid", children_and_women_title.getTag().toString());
                }
                break;
            case R.id.other_layout:
                String Str_Uid4 = SharedPreferencesTools.getUidToLoginClose(getActivity());
                if (!TextUtils.isEmpty(Str_Uid4)) {
                    intent = new Intent(getActivity(), Channel_selection.class);
                    intent.putExtra("kid", other_title.getTag().toString());
                }
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    private final int PREMISSON_REQUEST_CODE = 2;//请求码

    public void requestCameraPermission() {
        //API <23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Intent intent1 = new Intent(getActivity(), ScanActivity.class);     //二维码和AR扫描界面
            intent1.putExtra("defaultType", 0);
            startActivity(intent1);
        } else {
            //API >=23
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtils.makeText(getActivity(), "您已经拒绝过一次");
                }
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PREMISSON_REQUEST_CODE);
            } else {//有权限直接调用系统相机拍照
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    ToastUtils.makeText(getActivity(), "您已经拒绝过一次");
                }
                Intent intent1 = new Intent(getActivity(), ScanActivity.class);     //二维码和AR扫描界面
                intent1.putExtra("defaultType", 0);
                startActivity(intent1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PREMISSON_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent1 = new Intent(getActivity(), ScanActivity.class);     //二维码和AR扫描界面
                    intent1.putExtra("defaultType", 0);
                    startActivity(intent1);
                } else {

                    ToastUtils.makeText(getActivity(), "请允许打开相机~");
                }
                break;
            }
        }
    }
}

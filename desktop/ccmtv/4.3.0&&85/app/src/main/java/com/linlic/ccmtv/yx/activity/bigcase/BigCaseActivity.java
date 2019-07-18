package com.linlic.ccmtv.yx.activity.bigcase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.holder.BaseListAdapter;
import com.linlic.ccmtv.yx.holder.ListHolder;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.JsonUtils;
import com.linlic.ccmtv.yx.utils.LogUtil;
import com.linlic.ccmtv.yx.utils.MyListView;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by bentley on 2018/12/18.
 */

public class BigCaseActivity extends BaseActivity {
    //每页显示多少条
    private static final int PAGE_LIMIT = 20;

    @Bind(R.id.listView)
    XRecyclerView xRecyclerView;
    @Bind(R.id.entry_month_nodata)
    NodataEmptyLayout emptyView;
    @Bind(R.id.activity_title_name)
    TextView activityTitleName;


    private Context context;
    private BigCaseYearNonthAdapter bigCaseYearNonthAdapter;
    private List<ListInfo> listData;
    public int currPage = 1;
    private static String is_type;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://刷新
                    try {
                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
                        if (jsonObject.getInt("code") == 200) {
                            JSONObject dataJson = jsonObject.getJSONObject("data");
                            if (dataJson.getInt("status") == 1) {
                                // 成功  1存在数据 0不存在数据
                                JSONArray dateJson = dataJson.getJSONArray("data");
                                is_type=dataJson.getString("is_type");
                                 listData = JsonUtils.fromJsonArray(dateJson.toString(), ListInfo.class);
                                if(listData.size()!=0){
                                    bigCaseYearNonthAdapter = new BigCaseYearNonthAdapter(listData,BigCaseActivity.this);
                                    xRecyclerView.setAdapter(bigCaseYearNonthAdapter);
                                }
                                if (currPage == 1 && dateJson.length() == 0) {
                                    //刷新无数据
                                    listData.clear();
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                    xRecyclerView.loadMoreComplete();
                                } else if (dateJson.length() > 0) {
                                    xRecyclerView.setVisibility(View.VISIBLE);
                                    emptyView.setVisibility(View.GONE);
                                    currPage++;
                                    //bigCaseYearNonthAdapter.refresh(listData);
                                    xRecyclerView.loadMoreComplete();
                                }
                                //bigCaseYearNonthAdapter.notifyDataSetChanged();
                            } else {

                               String  statusMsg=dataJson.getString("statusMsg");
                                if(statusMsg.equals("暂无数据")){
                                    xRecyclerView.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                }else {
                                    Toast.makeText(context, statusMsg, Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        MyProgressBarDialogTools.hide();
//                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        MyProgressBarDialogTools.hide();
                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
                    }
                    break;
//                case 2://加载更多
//                    try {
//                        final JSONObject jsonObject = new JSONObject(msg.obj + "");
//                        if (jsonObject.getInt("code") == 200) {
//                            JSONObject dataJson = jsonObject.getJSONObject("data");
//                            if (dataJson.getInt("status") == 1) {
//                                // 成功  1存在数据 0不存在数据
//                                JSONArray dateJson = dataJson.getJSONArray("data");
////                                listData.clear();
//                                for (int i = 0; i < dateJson.length(); i++) {
//                                    JSONObject dataJson1 = dateJson.getJSONObject(i);
//                                    BigCaseYearMonthEntrty bigCaseYearMonthEntrty = new BigCaseYearMonthEntrty();
//                                    bigCaseYearMonthEntrty.setCase_id(dataJson1.getString("case_id"));
//                                    if (!dataJson1.isNull("year")) {
//                                        bigCaseYearMonthEntrty.setYear(dataJson1.getString("year"));
//                                    }
//                                    if (!dataJson1.isNull("month")) {
//                                        bigCaseYearMonthEntrty.setMonth(dataJson1.getString("month"));
//                                    }
//                                    if (!dataJson1.isNull("title")) {
//                                        bigCaseYearMonthEntrty.setTitle(dataJson1.getString("title"));
//                                    }
//                                    if (!dataJson1.isNull("type")) {
//                                        bigCaseYearMonthEntrty.setType(dataJson1.getString("type"));
//                                    }
//                                    if (!dataJson1.isNull("count")) {
//                                        bigCaseYearMonthEntrty.setCount(dataJson1.getString("count"));
//                                    }
//                                    // listData.add(bigCaseYearMonthEntrty);
//                                }
//                                if (dateJson.length() > 0) {
//                                    currPage++;
//                                    // bigCaseYearNonthAdapter.addData(listData);
//                                    bigCaseYearNonthAdapter.notifyDataSetChanged();
//                                }
//                                xRecyclerView.loadMoreComplete();
//                            } else {
//                                Toast.makeText(context, dataJson.getString("statusMsg"), Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        }
//
//                        MyProgressBarDialogTools.hide();
////                        setResultStatus(listData.size() > 0, jsonObject.getInt("code"));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        MyProgressBarDialogTools.hide();
//                        setResultStatus(HttpClientUtils.UNKONW_EXCEPTION_CODE);
//                    }
//                    break;
                case 500:
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    MyProgressBarDialogTools.hide();
                    setResultStatus(HttpClientUtils.NET_ERROT_CODE_SENDPOSTTOGP);
                    break;
                default:
                    break;
            }
        }
    };
    private static String fid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.big_case_activity);
        context = this;
        ButterKnife.bind(this);
        fid = getIntent().getStringExtra("fid");
        activityTitleName.setText(getIntent().getStringExtra("name"));
        initViews();
        getBigCaseYearMonthList();
    }

    private void initViews() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                currPage = 1;
                getBigCaseYearMonthList();
                xRecyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                getBigCaseYearMonthList();
            }
        });



    }

    /**
     * 设置空界面
     *
     * @param code
     */
    private void setResultStatus(int code) {
        if (HttpClientUtils.isNetConnectError(context, code)) {
            emptyView.setNetErrorIcon();
        } else {
            emptyView.setLastEmptyIcon();
        }
        xRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    /***
     * 刷新大病历最外层列表
     */
    private void getBigCaseYearMonthList() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.getBigCaseYearMonthList);
                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
                    obj.put("page", currPage);
                    obj.put("limit", PAGE_LIMIT);
                    //fid   222员工   123学员
                    obj.put("fid", fid);
                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());

                    LogUtil.e("大病历最外层列表", result);

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

    /***
     * 加载更多大病历最外层列表
     */
//    private void getMoreBigCaseYearMonthList() {
//        MyProgressBarDialogTools.show(context);
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    JSONObject obj = new JSONObject();
//                    obj.put("act", URLConfig.getBigCaseYearMonthList);
//                    obj.put("uid", SharedPreferencesTools.getUidONnull(context));
//                    obj.put("page", currPage);
//                    obj.put("limit", PAGE_LIMIT);
//                    obj.put("fid", fid);
//                    String result = HttpClientUtils.sendPostToGP(context, URLConfig.CCMTVAPP_GpApi, obj.toString());
//
//                    LogUtil.e("大病历最外层列表", result);
//
//                    Message message = new Message();
//                    message.what = 2;
//                    message.obj = result;
//                    handler.sendMessage(message);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    handler.sendEmptyMessage(500);
//                }
//            }
//        };
//        new Thread(runnable).start();
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://yun.ccmtv.cn/admin.php/wx/Bigcase/index.html";
        super.onPause();
    }


    static class BigCaseYearNonthAdapter extends RecyclerView.Adapter<BigCaseYearNonthAdapter.ViewHolder> implements View.OnClickListener {
        private List<ListInfo> data = new ArrayList<>();
        private Context mContext;
        private OnItemClickListener mItemClickListener;
        private BaseListAdapter baseListAdapter;

        public BigCaseYearNonthAdapter(List<ListInfo> data, Context mContext) {
            this.data = data;
            this.mContext = mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bigcase, parent, false);
            view.setOnClickListener(this);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ListInfo entrty = data.get(position);
            holder.itemView.setTag(position);
            holder.tv_title.setText(entrty.year);
            final List<Info> infos=entrty.list;
            baseListAdapter=new BaseListAdapter(holder.my_listview,infos,R.layout.item_big_case_yearmonth) {
                @Override
                public void refresh(Collection datas) {
                    super.refresh(datas);
                }

                @Override
                public void convert(ListHolder helper, Object item, boolean isScrolling, int position) {
                    super.convert(helper, item, isScrolling, position);
                    helper.setText(R.id.tv_month,((Info)item).month);
                    helper.setText(R.id._item_title,((Info)item).title);
                    helper.setText(R.id.item_num,((Info)item).count);
                }
            };
            holder.my_listview.setAdapter(baseListAdapter);

            holder.my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //如果 有type为1的话    点击查看 访问 getBigCaseUserList人员列表接口
                                        Intent intent = null;
                                        if (infos.get(i).type.equals("1")) {
                                            intent = new Intent(mContext, BigcaseUserListActivity.class);
                                            intent.putExtra("year", entrty.year);
                                            intent.putExtra("month", infos.get(i).month);
                                        } else {
                                            intent = new Intent(mContext, MonthBigCaseActivity.class);
                                        }
                                        intent.putExtra("fid", fid);
                                        intent.putExtra("case_id", infos.get(i).case_id);
                                        intent.putExtra("realname",infos.get(i).title);
                                        mContext.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void refresh(List<ListInfo> newDatas) {
            data.clear();
            data.addAll(newDatas);
            notifyDataSetChanged();
        }

        public void clearData() {
            data.clear();
            notifyDataSetChanged();
        }

        public void addData(List<ListInfo> newDatas) {
            if (newDatas != null) {
                int size = newDatas.size();
                data.addAll(newDatas);
                notifyItemRangeInserted(getItemCount(), size);
            }
        }

        public interface OnItemClickListener {
            void onItemClick(int position, ListInfo entrty);
        }

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick((Integer) v.getTag(), data.get((Integer) v.getTag()));
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_title;
            MyListView my_listview;

            ViewHolder(View view) {
                super(view);
                //ButterKnife.bind(this, view);
                tv_title=(TextView)view.findViewById(R.id.tv_title);
                my_listview=(MyListView)view.findViewById(R.id.my_listview);

            }
        }
    }



    static class ListInfo {
        private String year;
        private List<Info> list;
    }

    static class Info {
        private String title;
        private String type;
        private String case_id;
        private String month;
        private String count;
    }


}

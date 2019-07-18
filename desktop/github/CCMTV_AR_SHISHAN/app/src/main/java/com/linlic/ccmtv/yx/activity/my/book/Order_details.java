package com.linlic.ccmtv.yx.activity.my.book;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.activity.rules_to_compensate.gp_personal_information.GpPersonalInformationActivity4;
import com.linlic.ccmtv.yx.adapter.BaseRecyclerViewAdapter;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.MyProgressBarDialogTools;
import com.linlic.ccmtv.yx.utils.SharedPreferencesTools;
import com.linlic.ccmtv.yx.widget.NodataEmptyLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**订单详情 实体书
 * Created by admin on 2019/7/5.
 */

public class Order_details extends BaseActivity {
    Context context;
    @Bind(R.id.activity_title_name)
    TextView activity_title_name;//
    @Bind(R.id.name)
    TextView name;//
    @Bind(R.id.phone)
    TextView phone;//
    @Bind(R.id.address)
    TextView address;//
    @Bind(R.id.book_name)
    TextView book_name;
    @Bind(R.id.book_Press)
    TextView book_Press;
    @Bind(R.id.text_01)
    TextView text_01;
    @Bind(R.id.text_02)
    TextView text_02;
    @Bind(R.id.text_03)
    TextView text_03;
    @Bind(R.id.text_04)
    TextView text_04;
    @Bind(R.id.order_information)
    TextView order_information;
    @Bind(R.id.book_img)
    ImageView book_img;//
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        final JSONObject jsonObjects = new JSONObject(msg.obj + "");
                        if (jsonObjects.getInt("status") == 1) { // 成功
                            JSONObject data = jsonObjects.getJSONObject("data");
                            name.setText(data.getString("recipient"));
                            phone.setText(data.getString("tel"));
                            address.setText(data.getString("address"));
                            book_Press.setText(data.getString("publishing"));
                            book_name.setText(data.getString("title"));
                            text_01.setText("￥"+data.getString("money"));
                            text_02.setText("X"+data.getString("num"));
                            text_03.setText("运费 ￥"+data.getString("freight"));
                            text_04.setText(Html.fromHtml("实付款：<font color='red'>￥"+data.getString("payment")+"</fong>"));
                            try{
                                RequestOptions options = new RequestOptions();
                                RequestOptions error = options.placeholder(R.mipmap.img_default)
                                        .error(R.mipmap.img_default);
                                Glide.with(context)
                                        .load(FirstLetter.getSpells(data.getString("banner")).toString())
                                    .apply(options)
                                        .into(book_img);
                            }catch (Exception e){
                                e.printStackTrace();
                            }


                            order_information.setText("快递单号："+(data.getString("logistics").length()>0?data.getString("logistics"):"无")+"\n" +
                                    "\n" +
                                    "订单号： "+data.getString("oid")+"\n" +
                                    "\n" +
                                    "付款时间："+data.getString("addtime")+"\n" +
                                    "\n" +
                                    "发货时间："+(data.getString("deliverytime").length()>0?data.getString("deliverytime"):"无"));

                        } else {
                            Toast.makeText(context , jsonObjects.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case 500:
                    MyProgressBarDialogTools.hide();
                    Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_order_details);
        context = this;
        ButterKnife.bind(this);
        SharedPreferencesTools.getUid(context);
        findId();
        initView();
        setmsgdb();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    public void initView() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    /**
     * name:设置listview中的值 author:Tom 2016-1-28下午3:42:47
     */
    public void setmsgdb() {
        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.booksOrderDetails);
                    obj.put("uid", SharedPreferencesTools.getUid(context));
                    obj.put("id", getIntent().getStringExtra("id"));

                    String result = HttpClientUtils.sendPost(context, URLConfig.book_url, obj.toString());
                    MyProgressBarDialogTools.hide();

                    Message message = new Message();
                    message.what = 1;
                    message.obj = result;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(runnable).start();
    }

}
package com.linlic.ccmtv.yx.activity.integral_mall;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.FirstLetter;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/12/5.
 */

public class Order_details extends BaseActivity {
    private String id;
    private Context context;
    private ImageView imageView;
    private TextView order_details_name,integral,convert,order_number,order_date,consumption_integral,order_status,receiving_address,logistics_information;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject result = null;
                    try {
                        result = new JSONObject(msg.obj + "");
                        if (result.getInt("status") == 1) {// 成功
                            JSONObject dataJson = result.getJSONObject("data");
                            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(FirstLetter.getSpells(dataJson.getJSONArray("imgs").get(0).toString()), imageView);
                            order_details_name.setText(dataJson.getString("productname"));
                            integral.setText(dataJson.getString("money"));
                            convert.setText(dataJson.getString("status"));
                            order_number.setText(dataJson.getString("order_num"));
                            order_date.setText(dataJson.getString("createdate"));
                            consumption_integral.setText(dataJson.getString("money"));
                            order_status.setText(dataJson.getString("status"));
                            receiving_address.setText(dataJson.getString("address"));
                            logistics_information.setText(dataJson.getString("Express_name"));
                        } else {                                                                        // 失败
                            Toast.makeText(Order_details.this, result.getString("errorMessage"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        setContentView(R.layout.order_details);
        context = this;
        findId();
        init();
        setValue2();
    }

    @Override
    public void findId() {
        super.findId();
        imageView = (ImageView) findViewById(R.id.img);
        order_details_name = (TextView) findViewById(R.id.order_details_name);
        integral = (TextView) findViewById(R.id.integral);
        convert = (TextView) findViewById(R.id.convert);
        order_number = (TextView) findViewById(R.id.order_number);
        order_date = (TextView) findViewById(R.id.order_date);
        consumption_integral = (TextView) findViewById(R.id.consumption_integral);
        order_status = (TextView) findViewById(R.id.order_status);
        receiving_address = (TextView) findViewById(R.id.receiving_address);
        logistics_information = (TextView) findViewById(R.id.logistics_information);
    }

    public void init(){
        id=getIntent().getStringExtra("id");
    }

    public void setValue2(){
//        MyProgressBarDialogTools.show(context);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("act", URLConfig.exchangeDetail);
                    obj.put("id", id);

                    String result = HttpClientUtils.sendPost(context, URLConfig.CCMTVAPP_Commodity, obj.toString());

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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        enterUrl = "http://www.ccmtv.cn/Member/Index.html";
        super.onPause();
    }

}

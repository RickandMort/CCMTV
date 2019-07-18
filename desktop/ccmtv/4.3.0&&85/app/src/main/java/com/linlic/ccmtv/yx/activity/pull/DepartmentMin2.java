package com.linlic.ccmtv.yx.activity.pull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 科室小类
 * 
 * 第二级
 * @author Administrator
 *
 */
public class DepartmentMin2 extends BaseActivity {
	private TextView activity_title_name;//顶部title
	private ListView listview;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.hospital);
		context = this;
		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		final String Maxid = bundle.getString("departmentMaxId");
		//departmentMaxName
		final String departmentMaxId = getIntent().getStringExtra("departmentMaxId");
		final String departmentMaxName = getIntent().getStringExtra("departmentMaxName");
		listview = (ListView) findViewById(R.id.hospital_list);
		activity_title_name = (TextView) findViewById(R.id.activity_title_name);
		//设置顶部导航文字
		activity_title_name.setText(R.string.depa_min);
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("act", URLConfig.hosDepartment_o);
					obj.put("id", Maxid);
					String result = HttpClientUtils.sendPost(
							context, URLConfig.CCMTVAPP,
							obj.toString());
					
					JSONObject json = new JSONObject(result);
					if (json.getInt("status")==1) {
						//成功
						JSONArray array = json.getJSONArray("depto_list");
						for (int i = 0; i < array.length(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							JSONObject object = array.getJSONObject(i);
							map.put("id", object.getString("id"));
							map.put("name", object.getString("name"));
							list.add(map);
						}
						handler.sendEmptyMessage(200);
					}else {
						//失败
						handler.sendMessage(handler.obtainMessage(303, json.getString("errorMessage")));
					}
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(500);
				}
			}
		};
		new Thread(runnable).start();
		
		
		// 点击跳转到 市
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,long arg3) {
				TextView viewID = (TextView) view.findViewById(R.id.hospital_list_id);
				TextView viewName = (TextView) view.findViewById(R.id.hospital_list_name);
				Intent intent = new Intent();
				intent.putExtra("departmentMaxId", departmentMaxId);
				intent.putExtra("departmentMaxName", departmentMaxName);
				intent.putExtra("departmentMinId", viewID.getText().toString());
				intent.putExtra("departmentMinName", viewName.getText().toString());
				setResult(20, intent);
				finish();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle bundle = data.getExtras();
		if (bundle!=null) {
			setResult(resultCode, data);
			finish();
		}
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 200:
				SimpleAdapter adapter = new SimpleAdapter(
						DepartmentMin2.this, list, R.layout.hospital_item,
						new String[] { "id", "name" }, new int[] {
								R.id.hospital_list_id,
								R.id.hospital_list_name });
				listview.setAdapter(adapter);
				break;
			case 303:
				Toast.makeText(context, msg.obj+"", Toast.LENGTH_SHORT).show();
				break;
			case 500:
				Toast.makeText(context, R.string.post_hint1, Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	public void onBackPressed() {
		setResult(1,new Intent());  
		super.onBackPressed();
	};
	
	@Override
	public void back(View view) {
		setResult(1, new Intent());
		finish();
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

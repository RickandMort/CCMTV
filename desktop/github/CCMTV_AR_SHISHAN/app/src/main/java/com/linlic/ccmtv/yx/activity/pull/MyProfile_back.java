package com.linlic.ccmtv.yx.activity.pull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;
import com.linlic.ccmtv.yx.adapter.MyAlert;
import com.linlic.ccmtv.yx.config.URLConfig;
import com.linlic.ccmtv.yx.utils.Base64utils;
import com.linlic.ccmtv.yx.utils.HttpClientUtils;
import com.linlic.ccmtv.yx.utils.SDCardUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * name：我的资料(注册第二页)
 *
 * author: Mr.song 
 * 时间：2016-2-23 下午1:01:01
 * @author Administrator
 *
 */
public class MyProfile_back extends BaseActivity{
	private TextView activity_title_name;//顶部title
	private ImageView myprofile_img;//头像
	private Button myprofile_department;//科室
	private Button myprofile_title;//职称
	private Button myprofile_viptype;//职称
	private Button myprofile_sex;//性别
	private Button myprofile_hospital;//单位
	private EditText myprofile_truename;//真实姓名
	private EditText myprofile_address;//地址
	private EditText myprofile_idcard;//身份证号码
	//传入数组或list<String>
	private List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
	private String DepartmentMaxId;//科室大类id
	private String DepartmentMaxName;//科室大类name
	private String DepartmentMinId;//科室大类id
	private String DepartmentMinName;//科室大类name
	private String TitleId;//职称id
	private String TitleName;//职称name
	private String VipTypeId;//会员类别id
	private String VipTypeName;//会员类别name
	private String SexId;//性别id
	private String SexName;//性别name
	private String provinceId;//省 id
	private String provinceName;//省 name
	private String cityId;//市 id
	private String cityName;//市 name
	private String hospitalId;//医院 id
	private String hospitalName;//医院 name
	private File file;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myprofile);
		context = this;
		findViewById();
		
		setViewText();
	}

	private void setViewText() {
		//设置顶部导航文字
		activity_title_name.setText(R.string.myprofile_title_name);
	}

	private void findViewById() {
		activity_title_name = (TextView) findViewById(R.id.activity_title_name);
		myprofile_department = (Button) findViewById(R.id.myprofile_department);
		myprofile_title = (Button) findViewById(R.id.myprofile_title);
		myprofile_viptype = (Button) findViewById(R.id.myprofile_viptype);
		myprofile_sex = (Button) findViewById(R.id.myprofile_sex);
		myprofile_hospital = (Button) findViewById(R.id.myprofile_hospital);
		myprofile_img = (ImageView) findViewById(R.id.myprofile_img);
		myprofile_truename = (EditText) findViewById(R.id.myprofile_truename);
		myprofile_address = (EditText) findViewById(R.id.myprofile_address);
		myprofile_idcard = (EditText) findViewById(R.id.myprofile_idcard);
	}
	
	/**
	 * name：科室选择
	 *
	 * author: Mr.song 
	 * 时间：2016-2-25 下午7:00:12
	 * @param view
	 */
	public void getDepartment(View view){
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("act", URLConfig.hosDepartment);

					String result = HttpClientUtils.sendPost(context,
							URLConfig.CCMTVAPP, obj.toString());

					JSONObject json = new JSONObject(result);
					if (json.getInt("status")==1) {
						//成功
						JSONArray array = json.getJSONArray("dept_list");
						list.clear();//使用list前先清空list
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
	}
	/**
	 * name：职称选择
	 *
	 * author: Mr.song 
	 * 时间：2016-2-29 下午3:30:12
	 * @param view
	 */
	public void getTitle(View view){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("act", URLConfig.docPositions);

					String result = HttpClientUtils.sendPost(context,
							URLConfig.CCMTVAPP, obj.toString());

					JSONObject json = new JSONObject(result);
					if (json.getInt("status")==1) {
						//成功
						JSONArray array = json.getJSONArray("doc_list");
						list.clear();//使用list前先清空list
						for (int i = 0; i < array.length(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							JSONObject object = array.getJSONObject(i);
							map.put("id", object.getString("id"));
							map.put("name", object.getString("name"));
							list.add(map);
						}
						handler.sendEmptyMessage(188);
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
	}
	/**
	 * name：会员类型选择
	 *
	 * author: Mr.song 
	 * 时间：2016-2-29 下午3:30:12
	 * @param view
	 */
	public void getVipType(View view){
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject obj = new JSONObject();
					obj.put("act", URLConfig.memberStates);

					String result = HttpClientUtils.sendPost(context,
							URLConfig.CCMTVAPP, obj.toString());

					JSONObject json = new JSONObject(result);
					if (json.getInt("status")==1) {
						//成功
						JSONArray array = json.getJSONArray("member_list");
						list.clear();//使用list前先清空list
						for (int i = 0; i < array.length(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							JSONObject object = array.getJSONObject(i);
							map.put("id", object.getString("id"));
							map.put("name", object.getString("name"));
							list.add(map);
						}
						handler.sendEmptyMessage(150);
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
	}
	/**
	 * name：性别选择
	 *
	 * author: Mr.song 
	 * 时间：2016-3-1 上午10:05:30
	 * @param view
	 */
	public void getSex(View view){
		list.clear();//使用list前先清空list
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("id", "1");
		map1.put("name", "男");
		list.add(map1);
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("id", "0");
		map2.put("name", "女");
		list.add(map2);
		
		MyAlert.Builder bu = new MyAlert.Builder(MyProfile_back.this);
		bu.setTitle("请选择性别");
		bu.setMessage(list);
		bu.setPositiveButton("取消");
		bu.setItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				TextView textId = (TextView) view.findViewById(R.id.alert_dialog_items_id);
				TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
				String id = textId.getText().toString();
				String name = textView.getText().toString();
				SexName = name;
				SexId = id;
				//设置文本
				myprofile_sex.setText(SexName);
			}
		});
		bu.create().show();
	}
	/**
	 * name：单位选择
	 *
	 * author: Mr.song 
	 * 时间：2016-3-1 上午10:05:30
	 * @param view
	 */
	public void getHospital(View view){
		startActivityForResult(new Intent(MyProfile_back.this, HospitalProvince.class), 18);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				
				break;
			case 150:
				MyAlert.Builder bu = new MyAlert.Builder(MyProfile_back.this);
				bu.setTitle("请选择会员类别");
				bu.setMessage(list);
				bu.setPositiveButton("取消");
				bu.setItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
						TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
						String id = textview.getText().toString();
						VipTypeId = id;
						TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
						String name = textView.getText().toString();
						VipTypeName = name;
						//设置文本
						myprofile_viptype.setText(VipTypeName);
					}
				});
				bu.create().show();
				break;
			case 188:
				MyAlert.Builder buil = new MyAlert.Builder(MyProfile_back.this);
				buil.setTitle("请选择职称");
				buil.setMessage(list);
				buil.setPositiveButton("取消");
				buil.setItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
						TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
						String id = textview.getText().toString();
						TitleId = id;
						TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
						String name = textView.getText().toString();
						TitleName = name;
						//设置文本
						myprofile_title.setText(TitleName);
					}
				});
				buil.create().show();
				break;
			case 200:
				MyAlert.Builder builder = new MyAlert.Builder(MyProfile_back.this);
				builder.setTitle("请选择大科室类别");
				builder.setMessage(list);
				builder.setPositiveButton("取消");
				builder.setItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
						TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
						String id = textview.getText().toString();
						DepartmentMaxId = id;
						TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
						String name = textView.getText().toString();
						DepartmentMaxName = name;
						handler.sendEmptyMessage(201);
					}
				});
				builder.create().show();
				break;
			case 201:
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject obj = new JSONObject();
							obj.put("act", URLConfig.hosDepartment_o);
							obj.put("id", DepartmentMaxId);

							String result = HttpClientUtils.sendPost(context,
									URLConfig.CCMTVAPP, obj.toString());

							JSONObject json = new JSONObject(result);
							if (json.getInt("status")==1) {
								//成功
								JSONArray array = json.getJSONArray("depto_list");
								list.clear();//使用list前先清空list
								for (int i = 0; i < array.length(); i++) {
									Map<String, Object> map = new HashMap<String, Object>();
									JSONObject object = array.getJSONObject(i);
									map.put("id", object.getString("id"));
									map.put("name", object.getString("name"));
									list.add(map);
								}
								handler.sendEmptyMessage(288);
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
				break;
			case 288:
				MyAlert.Builder builder1 = new MyAlert.Builder(MyProfile_back.this);
				builder1.setTitle("请选择小科室类别");
				builder1.setMessage(list);
				builder1.setPositiveButton("取消");
				builder1.setItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
						TextView textview = (TextView) view.findViewById(R.id.alert_dialog_items_id);
						String id = textview.getText().toString();
						DepartmentMinId = id;
						TextView textView = (TextView) view.findViewById(R.id.alert_dialog_items_name);
						String name = textView.getText().toString();
						DepartmentMinName = name;
						//设置文本
						myprofile_department.setText(DepartmentMaxName+DepartmentMinName);
					}
				});
				builder1.create().show();
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
		};
	};
	/**
	 * name：上传头像
	 *
	 * author: Mr.song 
	 * 时间：2016-3-1 下午7:07:27
	 * @param view
	 */
	public void myprofile_head(View view) {
		new AlertDialog.Builder(MyProfile_back.this)
		.setTitle("提示")
		.setMessage("请选择上传方式")
		.setPositiveButton("相机", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				/**  压缩版 */
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, 20);
				
			}
		})
		.setNegativeButton("图库", new OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				/**  压缩版 */
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setType("image/*");
				startActivityForResult(intent, 10);
				
			}
		})
		.create().show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			switch (requestCode) {
			case 18://单位
				Bundle bundle = data.getExtras();
				if (bundle!=null) {
					provinceName = data.getStringExtra("provinceName");
					cityName = data.getStringExtra("cityName");
					hospitalName = data.getStringExtra("hospitalName");
					provinceId = data.getStringExtra("provinceId");
					cityId = data.getStringExtra("cityId");
					hospitalId = data.getStringExtra("hospitalId");
					myprofile_hospital.setText(hospitalName);
				}
				break;
			case 20://相机

				/**  没有压缩版 
				 Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(getContentResolver(), 
						   file.getAbsolutePath(), null, null));
				 myprofile_img.setImageURI(u);
				 */
				
				
				/**  压缩版 */
				Bitmap bitmap =data.getParcelableExtra("data");//Extras().getParcelable("data");
				//saveImgForSD(bitmap);
				
				Uri uri = SDCardUtils.saveBitmap(context, bitmap);
				startImageZoom(uri);//图像裁剪
				
				break;
			case 10://图库
				if (data!=null) {

					/**  压缩版 */
					Uri ur = data.getData();
					startImageZoom(convertUri(ur));//图像裁剪
					
				}
				break;
			case 30://图像裁剪
				if (data!=null) {
					if (data.getExtras()!=null) {
						Bitmap bit =data.getExtras().getParcelable("data");
						SDCardUtils.saveBitmap(context, bit);
						myprofile_img.setImageBitmap(bit);
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	};


	private Uri convertUri(Uri uri) {
		InputStream is = null;
		try {
			is = getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			return SDCardUtils.saveBitmap(context, bitmap);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * name：图像裁剪(方形)
	 *
	 * author: Mr.song 
	 * 时间：2016-3-1 下午9:03:25
	 * @param uri
	 */
	private void startImageZoom(Uri uri){
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		//小米问题就卡在这
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 30);
	}
	/**
	 * name：提交
	 *
	 * author: Mr.song 
	 * 时间：2016-3-1 下午6:58:17
	 * @param view
	 * @throws JSONException 
	 */
	public void MyprofileSubmit(View view) throws JSONException{
		Toast.makeText(context, "click", 0).show();
		String truename = myprofile_truename.getText().toString();
		String address = myprofile_address.getText().toString();
		String idcard = myprofile_idcard.getText().toString();
		if (TextUtils.isEmpty(truename)) {
			Toast.makeText(context, "真实姓名不可为空", Toast.LENGTH_SHORT).show();
			myprofile_truename.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(address)) {
			Toast.makeText(context, "联系地址不可为空", Toast.LENGTH_SHORT).show();
			myprofile_address.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(idcard)) {
			Toast.makeText(context, "身份证号码不可为空", Toast.LENGTH_SHORT).show();
			myprofile_idcard.requestFocus();
			return;
		}
		
		JSONObject object = new JSONObject();
		object.put("uid", "5556477");
		object.put("idcard", idcard);//身份证号码
		object.put("act", URLConfig.completeUserinfo);
		object.put("hyleibie", VipTypeName);//会员类别
		object.put("cityid", hospitalId);//医院ID
		object.put("keshilb", DepartmentMaxName);//大科室
		object.put("keshi", DepartmentMinName);//小科室
		object.put("my_694", TitleName);//职称
		object.put("truename", truename);//真实姓名
		object.put("sex", SexId);//性别 0女 1男
		object.put("address", address);//联系地址
		object.put("idcard", idcard);//身份证号码
		
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		// 加密二次
		String strBase64 = Base64utils.getBase64(Base64utils.getBase64(object.toString()));
		params.addBodyParameter("data", strBase64);
		params.addBodyParameter("fileSource", file);
		httpUtils.send(HttpMethod.POST, "http://192.168.123.84/do/ccmtvapp/ccmtvapp_UserInfo.php", params,
				new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}
			@Override
			public void onSuccess(ResponseInfo<String> info) {
				try {
					String base64 = Base64utils.getFromBase64(Base64utils.getFromBase64(info.result));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

package com.linlic.ccmtv.yx.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;

import java.util.List;
import java.util.Map;

public class MyAlert extends Dialog {

	public MyAlert(Context context) {
		super(context);
	}

	public MyAlert(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private List<Map<String, Object>> message;
		private String positiveButtonText;
		private View contentView;
		private OnClickListener positiveButtonClickListener;
		private OnItemClickListener itemClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(List<Map<String, Object>> message) {
			this.message = message;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText) {
			this.positiveButtonText = positiveButtonText;
			return this;
		}

		public Builder setItemClickListener(
				OnItemClickListener itemClickListener) {
			this.itemClickListener = itemClickListener;
			return this;
		}

		public MyAlert create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final MyAlert dialog = new MyAlert(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.alert_dialog, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			((TextView) layout.findViewById(R.id.title)).setText(title);
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				// if (positiveButtonClickListener != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								// positiveButtonClickListener.onClick(dialog,
								// DialogInterface.BUTTON_POSITIVE);
								dialog.dismiss();
							}
						});
				// }
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}
			if (message != null) {
				ListView listView = (ListView) layout
						.findViewById(R.id.listmsg);
				SimpleAdapter adapter = new SimpleAdapter(context, message,
						R.layout.alert_dialog_items, new String[] { "id",
								"name" }, new int[] {
								R.id.alert_dialog_items_id,
								R.id.alert_dialog_items_name });
				listView.setAdapter(adapter);
				if (itemClickListener != null) {
					listView.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0, View view,
								int position, long arg3) {
							itemClickListener.onItemClick(arg0, view, position,
									arg3);
							TextView text = (TextView) view.findViewById(R.id.alert_dialog_items_name);
							text.setBackgroundResource(R.color.alert_dialog_items_name_background);
							text.setTextColor(Color.rgb(255, 255, 255));
							dialog.dismiss();
						}
					});
				} else {
					System.out.println("itemClickListener is null  !!!");
				}
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}

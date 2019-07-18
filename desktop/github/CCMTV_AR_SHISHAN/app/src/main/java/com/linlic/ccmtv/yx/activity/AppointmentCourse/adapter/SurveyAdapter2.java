package com.linlic.ccmtv.yx.activity.AppointmentCourse.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.AppointmentCourse.entity.FromEntity;

import java.util.List;

/**
 * Created by bentley on 2019/1/21.
 */

public class SurveyAdapter2 extends BaseAdapter implements  View.OnTouchListener, View.OnFocusChangeListener {
    private int selectedEditTextPosition = -1;
    private List<FromEntity.Config> daily_exam_of_item_list;
    private Context mContext;

    public SurveyAdapter2(List<FromEntity.Config> mList,Context mContext) {
        this.mContext=mContext;
        this.daily_exam_of_item_list = mList;
    }

    @Override
    public int getCount() {
        return daily_exam_of_item_list.size();
    }

    @Override
    public FromEntity.Config getItem(int position) {
        return daily_exam_of_item_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_yk_survey_config, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.editText.setOnTouchListener(this); // 正确写法
        vh.editText.setOnFocusChangeListener(this);
        vh.editText.setTag(position);

        if (selectedEditTextPosition != -1 && position == selectedEditTextPosition) { // 保证每个时刻只有一个EditText能获取到焦点
            vh.editText.requestFocus();
        } else {
            vh.editText.clearFocus();
        }

        vh.tvConfigTitle.setText(getItem(position).getName());
        vh.tvReson.setText(getItem(position).getDeduct_marks_account());
//        if (isAddScore) {
//            vh.tvMarking.setText("得分：");
//        } else {
//            vh.tvMarking.setText("扣分：");
//        }
//        if (needReson) {
//            vh.editText.setVisibility(View.VISIBLE);
//
//            if (canEditable) {
//                vh.editText.setEnabled(true);
//            } else {
//                vh.editText.setHint("");
//                vh.editText.setEnabled(false);
//            }
//            if (!editTexts.contains(vh.editText)) {
//                editTexts.add(vh.editText);
//            }
//        } else {
//            vh.editText.setVisibility(View.GONE);
//        }
//        helper.setSpinnerYKSurvey(R.id._item_grade, tvScore, allConfiglist, congig,
//                canEditable, R.id.tv_value, R.id.id_iv_spinner_arrow, isAddScore);

        vh.editText.setSelection(vh.editText.length());

        convertView.setTag(R.id.item_root, position); // 应该在这里让convertView绑定position
//            convertView.setOnClickListener(this);
//            convertView.setOnLongClickListener(this);
        return convertView;
    }
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (selectedEditTextPosition != -1) {
                Log.w("MyEditAdapter", "onTextPosiotion " + selectedEditTextPosition);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            EditText editText = (EditText) v;
            selectedEditTextPosition = (int) editText.getTag();
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EditText editText = (EditText) v;
        if (hasFocus) {
            editText.addTextChangedListener(mTextWatcher);
        } else {
            editText.removeTextChangedListener(mTextWatcher);
        }
    }

//        @Override
//        public void onClick(View view) {
//            if (view.getId() == R.id.item_root) {
//                int position = (int) view.getTag(R.id.item_root);
//                Toast.makeText(mContext, "点击第 " + position + " 个item", Toast.LENGTH_SHORT).show();
//            }
//        }

//        @Override
//        public boolean onLongClick(View view) {
//            if (view.getId() == R.id.item_root) {
//                int position = (int) view.getTag(R.id.item_root);
//                Toast.makeText(mContext, "长按第 " + position + " 个item", Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }

    public class ViewHolder {
        EditText editText;
        TextView tvConfigTitle;
        TextView tvReson;
        TextView tvMarking;

        public ViewHolder(View convertView) {
            editText = (EditText) convertView.findViewById(R.id.ed_reson);
            tvConfigTitle = (TextView) convertView.findViewById(R.id.tv_item_config_tittle);
            tvReson = (TextView) convertView.findViewById(R.id.tv_reson);
            tvMarking = (TextView) convertView.findViewById(R.id.tv_marking);
        }
    }
}

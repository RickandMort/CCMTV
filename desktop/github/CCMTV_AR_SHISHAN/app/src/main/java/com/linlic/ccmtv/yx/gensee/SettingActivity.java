package com.linlic.ccmtv.yx.gensee;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.linlic.ccmtv.yx.R;
import com.linlic.ccmtv.yx.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    public static final String SETTING_PREFERENCES_NAME = "SETTING_PREFERENCES_NAME";
    public static final String GS_DOC = "gs_doc";
    public static final String GS_CHAT = "gs_chat";
    public static final String GS_QA = "gs_qa";
    public static final String GS_INTRO = "gs_intro";
    public static final String GS_PIP = "gs_PIP";
    public static final String GS_HAND = "gs_hand";
    public static final String GS_RATE = "gs_rate";
    public static final String GS_NET = "gs_net";
    public static final String GS_SKIN = "gs_skin";
    public static final String GS_DANMU = "gs_danmu";
    public static final String GS_CLOSE_VIDEO = "gs_close_video";
    public static final int SKIN_TYPE_NIGHT = 0;
    public static final int SKIN_TYPE_DAY = 1;
    private CheckBox cbDoc;
    private CheckBox cbChat;
    private CheckBox cbQa;
    private CheckBox cbIntro;
    private CheckBox cbPIP;
    private CheckBox cbHand;
    private CheckBox cbRate;
    private CheckBox cbNet;
    private Spinner spSkinner;
    private SharedPreferences preferences;
    private CheckBox cbDanmu;
    private CheckBox cbCloseVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gensee_setting);
        preferences = getSharedPreferences(SETTING_PREFERENCES_NAME, MODE_PRIVATE);
        cbDoc = (CheckBox) findViewById(R.id.cb_doc);
        cbDoc.setChecked(getFromPrefrences(GS_DOC));
        cbDoc.setOnCheckedChangeListener(this);
        cbChat = (CheckBox) findViewById(R.id.cb_chat);
        cbChat.setChecked(getFromPrefrences(GS_CHAT));
        cbChat.setOnCheckedChangeListener(this);
        cbQa = (CheckBox) findViewById(R.id.cb_qa);
        cbQa.setChecked(getFromPrefrences(GS_QA));
        cbQa.setOnCheckedChangeListener(this);
        cbIntro = (CheckBox) findViewById(R.id.cb_intro);
        cbIntro.setChecked(getFromPrefrences(GS_INTRO));
        cbIntro.setOnCheckedChangeListener(this);
        cbPIP = (CheckBox) findViewById(R.id.cb_PIP);
        cbPIP.setChecked(getFromPrefrences(GS_PIP));
        cbPIP.setOnCheckedChangeListener(this);
        cbHand = (CheckBox) findViewById(R.id.cb_hand);
        cbHand.setChecked(getFromPrefrences(GS_HAND));
        cbHand.setOnCheckedChangeListener(this);
        cbRate = (CheckBox) findViewById(R.id.cb_cbRate);
        cbRate.setChecked(getFromPrefrences(GS_RATE));
        cbRate.setOnCheckedChangeListener(this);
        cbNet = (CheckBox) findViewById(R.id.cb_net);
        cbNet.setChecked(getFromPrefrences(GS_NET));
        cbNet.setOnCheckedChangeListener(this);
        cbDanmu = (CheckBox) findViewById(R.id.cb_danmu);
        cbDanmu.setChecked(getFromPrefrences(GS_DANMU));
        cbDanmu.setOnCheckedChangeListener(this);
        cbCloseVideo = (CheckBox) findViewById(R.id.cb_close_video);
        cbCloseVideo.setChecked(getFromPrefrences(GS_CLOSE_VIDEO));
        cbCloseVideo.setOnCheckedChangeListener(this);
        spSkinner = (Spinner) findViewById(R.id.sp_skinner);
        List<String> list2 = new ArrayList<String>();
        list2.add("[换肤]黑夜");
        list2.add("[换肤]白天");
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSkinner.setAdapter(adapter2);
        int defSkin = preferences.getInt(GS_SKIN,0);
        if(defSkin == SKIN_TYPE_NIGHT){
            spSkinner.setSelection(0);
        }else if(defSkin == SKIN_TYPE_DAY){
            spSkinner.setSelection(1);
        }
        spSkinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                switch (arg2) {
                    case 0:
                        saveToPrefrences(GS_SKIN,SKIN_TYPE_NIGHT);
                        break;
                    case 1:
                        saveToPrefrences(GS_SKIN,SKIN_TYPE_DAY);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        findViewById(R.id.gs_bnt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_doc:
                saveToPrefrences(GS_DOC, isChecked);
                break;
            case R.id.cb_chat:
                saveToPrefrences(GS_CHAT, isChecked);
                break;
            case R.id.cb_qa:
                saveToPrefrences(GS_QA, isChecked);
                break;
            case R.id.cb_intro:
                saveToPrefrences(GS_INTRO, isChecked);
                break;
            case R.id.cb_PIP:
                saveToPrefrences(GS_PIP, isChecked);
                break;
            case R.id.cb_hand:
                saveToPrefrences(GS_HAND, isChecked);
                break;
            case R.id.cb_cbRate:
                saveToPrefrences(GS_RATE, isChecked);
                break;
            case R.id.cb_net:
                saveToPrefrences(GS_NET, isChecked);
                break;
            case R.id.cb_danmu:
                saveToPrefrences(GS_DANMU, isChecked);
            case R.id.cb_close_video:
                saveToPrefrences(GS_CLOSE_VIDEO, isChecked);
                break;
        }
    }

    private void saveToPrefrences(String key, boolean value) {
        preferences.edit().putBoolean(key, value).commit();
    }

    private void saveToPrefrences(String key, int value) {
        preferences.edit().putInt(key, value).commit();
    }

    private boolean getFromPrefrences(String key) {
        return preferences.getBoolean(key,true);
    }
}

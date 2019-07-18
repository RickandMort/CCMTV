package com.linlic.ccmtv.yx.gensee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by pc on 2017/4/18.
 */

public class StartupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,GenseeMainActivity.class));
        finish();
    }
}

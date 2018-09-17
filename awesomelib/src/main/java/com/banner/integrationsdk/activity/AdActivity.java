package com.banner.integrationsdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.banner.integrationsdk.util.AdUtil;

public class AdActivity extends Activity {
    Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AdUtil.getInstance(this).InterstitialAD(null);
    }




}

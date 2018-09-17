package com.banner.integrationsdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.banner.integrationsdk.R;
import com.banner.integrationsdk.util.AdUtil;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.comm.util.AdError;

public class AdActivity extends Activity {
    Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AdUtil.getInstance(this).InterstitialAD(null);
    }




}

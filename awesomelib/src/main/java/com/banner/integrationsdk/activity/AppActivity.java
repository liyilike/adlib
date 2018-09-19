package com.banner.integrationsdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.banner.integrationsdk.R;
import com.banner.integrationsdk.update.UpUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class AppActivity extends AppCompatActivity {
    Context mContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        UMConfigure.init(mContext, UMConfigure.DEVICE_TYPE_PHONE, getString(R.string.youmeng));
        new UpUtil(mContext).init();
//        AdUtil.getInstance((Activity) mContext).showAD();
    }


    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }


}

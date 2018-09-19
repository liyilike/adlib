package com.banner.integrationsdk.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banner.integrationsdk.R;
import com.banner.integrationsdk.myad.MyAdBanner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;

import java.util.Locale;

public class ExitActivity extends AppCompatActivity {
    private static final int DEFAULT_RADIUS = 6;
    TextView mPositiveBtn;
    View llBtnGroup;
    int click = 3;
    View moretv;
    Context mContext;
    FrameLayout adsRl;
    ViewGroup banner;
    int time = 4000;
    int qqstate = 0, bdstate = 0;
    private static final int MSG_UPDATE_TEXT = 1;
    private String mStrContent = null;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    mPositiveBtn.setText(click + getString(R.string.ad_ban_exit_activity));
                    click--;
                    if (click != 0) {
                        handler.sendEmptyMessageDelayed(0, 1400);
                    } else {
                        mPositiveBtn.setText(getString(R.string.ad_ban_exittv));
                        llBtnGroup.setEnabled(true);
                        setBtnBackground2(mPositiveBtn);
                    }
                }
                break;
//                case MainData.BDMES:
//                    if (bdstate == 1) {         //有百度广告
//                        banner.setVisibility(View.GONE);
//                        adsRl.setVisibility(View.VISIBLE);
//                        handler.sendEmptyMessageDelayed(MainData.GDMES, time);   //有百度广告显示2秒后
//                    } else {
//                        banner.setVisibility(View.VISIBLE);
//                        adsRl.setVisibility(View.VISIBLE);
//                        handler.sendEmptyMessageDelayed(MainData.BDMES, time);  //没百度广告就2秒后判断是否有百度广告
//                    }
//                    break;
//                case MainData.GDMES:
//                    if (qqstate == 1) {    //有广点通广告
//                        banner.setVisibility(View.VISIBLE);
//                        adsRl.setVisibility(View.VISIBLE);
//                        handler.sendEmptyMessageDelayed(MainData.BDMES, time);   //有广点通广告显示2秒后
//                    } else {     //没广点通广告
//                        banner.setVisibility(View.GONE);
//                        adsRl.setVisibility(View.VISIBLE);
//                        handler.sendEmptyMessageDelayed(MainData.GDMES, time);   //没广点通广告显示2秒后
//                    }
//                    break;
                default:
                    break;
            }
        }
    };
    private LinearLayout layout, loading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
        mContext = this;
        initview();
//        qqban();
    }


    public void setView() {
        int view = R.layout.view_exit;
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (!language.equals("zh")) {
            view = R.layout.view_exit2;
        }
        setContentView(view);
    }


    public void more(View view) {
        mContext.startActivity(new Intent().setClass(mContext, MyAdBanner.class));
    }

    public void initview() {
        moretv = (View) findViewById(R.id.moretv);
        layout = (LinearLayout) findViewById(R.id.exit_lin);
        mPositiveBtn = (TextView) findViewById(R.id.btnPositive);
        loading = (LinearLayout) findViewById(R.id.loading);
        llBtnGroup = findViewById(R.id.llBtnGroup);
        setBtnBackground(mPositiveBtn);
        setBottomCorners(llBtnGroup);
        handler.obtainMessage(0).sendToTarget();
        llBtnGroup.setEnabled(false);
        llBtnGroup.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {
                                              MobclickAgent.onKillProcess(mContext);
                                              finish();
                                              int currentVersion = Build.VERSION.SDK_INT;
                                              if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                                                  Intent startMain = new Intent(Intent.ACTION_MAIN);
                                                  startMain.addCategory(Intent.CATEGORY_HOME);
                                                  startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //清除上一步缓存
                                                  startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                  startActivity(startMain);
                                                  System.exit(0);
                                              } else {// android2.1
                                                  ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                                  am.restartPackage(getPackageName());
                                              }
                                          }
                                      }
        );
        layout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getString(R.string.ad_ban_tip), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }


    public void qqban() {
//        AdUtil.getInstance(this).QQBan( (ViewGroup) findViewById(R.id.qqban),null);
//        AdUtil.getInstance(this).AdmobBan((AdView) findViewById(R.id.adView));
        banner = (ViewGroup) findViewById(R.id.qqban);
        BannerView bv = new BannerView(this, ADSize.BANNER, getString(R.string.qq_ad_id), getString(R.string.qq_ban_key));
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(AdError adError) {
                qqstate = 0;
            }

            public void onADReceiv() {
                qqstate = 1;
            }
        });
        banner.addView(bv);
        bv.loadAD();

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_ban));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    private void setBtnBackground(final TextView btnOk) {
        btnOk.setTextColor(createColorStateList(ContextCompat.getColor(mContext, R.color.colorAccent), ContextCompat.getColor(mContext, R.color.colorAccent)));
        btnOk.setBackgroundResource(R.drawable.sel_btn);
    }

    private void setBtnBackground2(final TextView btnOk) {
        btnOk.setTextColor(createColorStateList(ContextCompat.getColor(mContext, R.color.colorAccent), ContextCompat.getColor(mContext, R.color.colorAccent)));
        btnOk.setBackgroundResource(R.drawable.sel_btn);
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void setBottomCorners(View llBtnGroup) {
        int radius = dp2px(this, DEFAULT_RADIUS);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            llBtnGroup.setBackground(shapeDrawable);
        } else {
            llBtnGroup.setBackgroundDrawable(shapeDrawable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TEXT:
                    if (mStrContent != null)
//                        mTv1.setText(mStrContent);
                        break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    }

}

package com.banner.integrationsdk.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;

import com.banner.integrationsdk.Interface.ADCallBackInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.liyi.R;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.ads.interstitial.AbstractInterstitialADListener;
import com.qq.e.ads.interstitial.InterstitialAD;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;

import java.util.Locale;

public class AdUtil {
    private static AdUtil Instance = null;
    private static Activity mContext;
    private static InterstitialAD iad;
    private static InterstitialAd interstitialAd;
    private static SplashAD splashAD;
    private static String qqID = null, qq_IAD_KEY = null, qq_BAN_KEY = null, qq_SP_KEY = null, admob_BAN = null, admob_IAD = null;

    public static AdUtil getInstance(Activity Context) {
        mContext = Context;
        if (Instance == null) {
            Instance = new AdUtil();
            init();

            return Instance;
        }
        return Instance;
    }

    public static void init() {
        qqID = mContext.getString(R.string.qq_ad_id);
        qq_IAD_KEY = mContext.getString(R.string.qq_iad_key);
        qq_BAN_KEY = mContext.getString(R.string.qq_ban_key);
        qq_SP_KEY = mContext.getString(R.string.qq_sp_key);
        admob_IAD = mContext.getString(R.string.admob_iad);
        admob_BAN = mContext.getString(R.string.admob_ban);
        //初始化谷歌广告的id
        MobileAds.initialize(mContext, mContext.getString(R.string.admob_id));


    }




    public void showAD() {
        Locale locale = mContext.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if(!language.equals("zh")){
           InterstitialGoogleAD(new ADCallBackInterface() {
                @Override
                public void onADReceive() {

                }

                @Override
                public void onNoAD() {
                    InterstitialAD(null);
                }

                @Override
                public void onADClicked() {

                }

                @Override
                public void onADClosed() {
                }
            });
            return;
        }



        InterstitialAD(new ADCallBackInterface() {
            @Override
            public void onADReceive() {

            }

            @Override
            public void onNoAD() {
                InterstitialGoogleAD(null);
            }

            @Override
            public void onADClicked() {

            }

            @Override
            public void onADClosed() {

            }
        });

    }



    public static void QQBan(ViewGroup banner, final ADCallBackInterface adCallBackInterface) {
        BannerView bv = new BannerView(mContext, ADSize.BANNER, qqID, qq_BAN_KEY);
        bv.setRefresh(30);
        bv.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onNoAD();
                }
            }

            public void onADReceiv() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADReceive();
                }
            }
        });
        banner.addView(bv);
        bv.loadAD();
    }

    public static void AdmobBan(AdView mAdView) {
        MobileAds.initialize(mContext.getApplicationContext(), admob_BAN);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void InterstitialAD(final ADCallBackInterface adCallBackInterface) {

        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                | ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            AdUtil.getInstance(mContext).InterstitialGoogleAD(null);
            return;
        }

        if (iad == null) {
            iad = new InterstitialAD(mContext, qqID, qq_IAD_KEY);
        }
        iad.setADListener(new AbstractInterstitialADListener() {
            @Override
            public void onADReceive() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADReceive();
                }
                iad.show();
            }

            @Override
            public void onNoAD(AdError adError) {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onNoAD();
                }
            }

            @Override
            public void onADClosed() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClosed();
                }
                super.onADClosed();
            }

            @Override
            public void onADClicked() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClicked();
                }
                super.onADClicked();
            }
        });
        iad.loadAD();

    }


    public static void InterstitialGoogleAD(final ADCallBackInterface adCallBackInterface) {
        interstitialAd = new InterstitialAd(mContext);
        AdRequest adRequest = new AdRequest.Builder().setRequestAgent("android_studio:ad_template").build();
        interstitialAd.setAdUnitId(admob_IAD);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADReceive();
                }
                interstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onNoAD();
                }
            }

            @Override
            public void onAdClosed() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClosed();
                }

            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClicked();
                }
            }
        });
        interstitialAd.loadAd(adRequest);
    }

    public static void SplashAD(ViewGroup adContainer, View skipContainer, int fetchDelay, final ADCallBackInterface adCallBackInterface) {

//      splashAD = new SplashAD(mContext, adContainer, skipContainer, mContext.getString(R.string.gdt_ID),mContext.getString(R.string.gdt_Splash), new SplashADListener() {
        splashAD = new SplashAD(mContext, adContainer, skipContainer,qqID, qq_SP_KEY, new SplashADListener() {
            @Override
            public void onADDismissed() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClosed();
                }
            }
            @Override
            public void onNoAD(AdError adError) {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onNoAD();
                }
            }

            @Override
            public void onADPresent() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADReceive();
                }
            }

            @Override
            public void onADClicked() {
                if (adCallBackInterface != null) {
                    adCallBackInterface.onADClicked();
                }
            }

            @Override
            public void onADTick(long l) {
            }

            @Override
            public void onADExposure() {

            }
        }, fetchDelay);
    }



}




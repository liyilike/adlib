package com.banner.integrationsdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.banner.integrationsdk.R;
import com.banner.integrationsdk.myad.MyAdBanner;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class AdDialog extends Dialog  {

    Context mContext;
    private NativeAD nativeAD;
    private NativeADDataRef adItem;
    private ViewGroup adImg;
    private View layout;
    View left,right,cancel,adimg;
    private NativeExpressADView nativeExpressADView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_interstitial_zh);
        setCanceledOnTouchOutside(true);
        initView();
        loadAD();
    }


    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        //downTime += 2500;
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
//          view.onTouchEvent(downEvent);
//          view.onTouchEvent(upEvent);
        view.dispatchTouchEvent(downEvent);
        view.dispatchTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    public AdDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext= context;
    }

    //初始化并加载广告
    public void initView() {
        layout = (View) findViewById(R.id.exit_lin);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        adImg = (ViewGroup) findViewById(R.id.adimg);
//        adImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (adItem!=null){
////                    adItem.onClicked(view);
////                }else{
////                    mContext.startActivity(new Intent(mContext, MyAdBanner.class));
////                }
//                dismiss();
//            }
//        });
        left= (View) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (nativeExpressADView!=null) {
                    setSimulateClick(nativeExpressADView, 0, 0);
                }else{
                    mContext.startActivity(new Intent(mContext, MyAdBanner.class));
                }
//                if (adItem!=null){
//                    adItem.onClicked(view);
//                }else{
//                    mContext.startActivity(new Intent(mContext, MyAdBanner.class));
//                }

            }
        });
        cancel= (View) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    //初始化并加载广告
    public void loadAD() {
        NativeExpressAD nativeExpressAD = new NativeExpressAD(mContext, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), mContext.getString(R.string.qq_ad_id), mContext.getString(R.string.qq_native_key), new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(AdError adError) {
//                Log.i("gg412","GG");
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> adList) {
//                Log.i("gg41",adList.size()+"GG");
                // 释放前一个展示的NativeExpressADView的资源
                if (nativeExpressADView != null) {
                    nativeExpressADView.destroy();
                }

                if (adImg.getChildCount() > 0) {
                    adImg.removeAllViews();
                }

                nativeExpressADView = adList.get(0);
                // 广告可见才会产生曝光，否则将无法产生收益。
                adImg.addView(nativeExpressADView);
                nativeExpressADView.render();

            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

            }
        });
        // 这里的Context必须为Activity

        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
                .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
                .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置
        nativeExpressAD.loadAD(1);

    }





//        if (nativeAD == null) {
//            this.nativeAD = new NativeAD(mContext, "1104948517", "5000530404252601", new NativeAD.NativeAdListener() {
//                @Override
//                public void onADLoaded(List<NativeADDataRef> list) {
//                    Toast.makeText(mContext, "22", Toast.LENGTH_LONG).show();
//                    if (list.size() > 0) {
//                        adItem = list.get(0);
//                        showAD();
//                        Toast.makeText(mContext, "原生广告加载成功", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(mContext, "NOADReturn", Toast.LENGTH_LONG).show();
//                        Log.i("AD_DEMO2", "NOADReturn");
//                    }
//                }
//
//                @Override
//                public void onNoAD(AdError adError) {
//                    Log.i("AD_DEMO3", adError.getErrorMsg()+"ggg"+adError.getErrorCode());
//                }
//
//                @Override
//                public void onADStatusChanged(NativeADDataRef nativeADDataRef) {
//                    Log.i("AD_DEMO4", nativeADDataRef.getTitle()+"ggg"+nativeADDataRef.getDesc());
//                }
//
//                @Override
//                public void onADError(NativeADDataRef nativeADDataRef, AdError adError) {
//                    Log.i("AD_DEMO5", nativeADDataRef.getTitle()+"ggg"+nativeADDataRef.getDesc());
//                }
//            });
//        }
//        int count = 1; // 一次拉取的广告条数：范围1-10
//        nativeAD.loadAD(count);


//    public void showAD() {
////        if (adItem.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
////        } else if (adItem.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
////            adImg.setImageURI(Uri.parse(adItem.getImgUrl()));
////        } else if (adItem.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT) {
////        }
////        adItem.onExposured(adImg); // 需要先调用曝光接口
//    }


}




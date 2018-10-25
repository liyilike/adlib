package com.banner.integrationsdk.dialog;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banner.integrationsdk.R;
import com.banner.integrationsdk.myad.MyAdBanner;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.umeng.analytics.MobclickAgent;

import java.lang.ref.WeakReference;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class ExitDialog extends Dialog {

    private Context mContext;
    private NativeADDataRef adItem;
    private ViewGroup adImg;
    private View layout, myban;
    private View left, right;
    private TextView text;
    private NativeExpressADView nativeExpressADView;
    private Handler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_exit_zh);
        initView();
        loadAD();
        mHandler.sendEmptyMessageDelayed(0, 4000);
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        mHandler.removeMessages(0);
    }

    public ExitDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }


    public void timeTask() {
        if (adImg.getVisibility() == View.VISIBLE) {
            adImg.setVisibility(View.GONE);
            text.setText(mContext.getString(R.string.ad_ban_more));
        } else {
            adImg.setVisibility(View.VISIBLE);
            text.setText(mContext.getString(R.string.ad_ban_please));
        }
        mHandler.sendEmptyMessageDelayed(0, 4000);
    }

    //初始化并加载广告
    public void initView() {
        myban = (View) findViewById(R.id.myban);
        myban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, MyAdBanner.class));
            }
        });
        text = (TextView) findViewById(R.id.tv);
        layout = (View) findViewById(R.id.exit_lin);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        adImg = (ViewGroup) findViewById(R.id.adimg);
        adImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adItem != null) {
                    adItem.onClicked(view);
                } else {
                    mContext.startActivity(new Intent(mContext, MyAdBanner.class));
                }
//                dismiss();
            }
        });
        left = (View) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dismiss();
                if (adImg.getVisibility() == View.VISIBLE) {
                    if (nativeExpressADView != null) {
                        setSimulateClick(nativeExpressADView, 0, 0);
                    } else {
                        mContext.startActivity(new Intent(mContext, MyAdBanner.class));
                    }
                } else {
                    adImg.setVisibility(View.GONE);
                    mContext.startActivity(new Intent(mContext, MyAdBanner.class));
                }

            }
        });
        right = (View) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobclickAgent.onKillProcess(mContext);
//                mContext.finish();
                int currentVersion = Build.VERSION.SDK_INT;
                if (currentVersion > Build.VERSION_CODES.ECLAIR_MR1) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //清除上一步缓存
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(startMain);
                    System.exit(0);
                } else {// android2.1
                    ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                    am.restartPackage(mContext.getPackageName());
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dismiss();
        return true;
    }

    private void setSimulateClick(View view, float x, float y) {
        long downTime = SystemClock.uptimeMillis();
        final MotionEvent downEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        final MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,
                MotionEvent.ACTION_UP, x, y, 0);
        view.dispatchTouchEvent(downEvent);
        view.dispatchTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
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
//                Log.i("gg4123","GG");
            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
//                Log.i("gg4124","GG");
            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {
//                Log.i("gg4125","GG");
            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {
//                Log.i("gg4126","GG");
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


    static class MyHandler extends Handler {
        WeakReference<ExitDialog> mActivity;

        public MyHandler(ExitDialog activity) {
            mActivity = new WeakReference<ExitDialog>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ExitDialog Activity = mActivity.get();
            switch (msg.what) {
                case 0:
                    Activity.timeTask();
                    break;
            }
        }

    }


}




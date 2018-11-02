package com.banner.integrationsdk.myad;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;

import com.banner.integrationsdk.update.InstallUtils;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * 后台下载
 */
public class MyAdService extends Service {
    static Context mContext;
    static String downUrl = null;
    static String fileUrl = null;
    public static boolean runFlag = false;
    private Handler handler = new MyHandler(this);
    private long current, total;

    public void onCreate() {
        super.onCreate();
        runFlag = true;
        mContext = this;
        downUrl = MyAdBanner.downUrl;
        fileUrl = InstallUtils.getCachePath(mContext) + "/myapk.apk";
        File file = new File(fileUrl);
        if (file.exists()) {
            file.delete();
        }
        downLoadFile(downUrl);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runFlag = false;
    }

//    public  static void install() {
//        InstallUtils.installAPK(mContext, fileUrl, mContext.getPackageName() + ".fileprovider", new InstallUtils.InstallCallBack() {
//                @Override
//                public void onSuccess() {
//                    Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
//
//                }
//                @Override
//                public void onFail(Exception e) {
//                }
//            });
//
//    }


    InstallUtils.DownloadCallBack DownloadCallBack = new InstallUtils.DownloadCallBack() {
        @Override
        public void onStart() {
//            Log.i("fff11", "22sss");
        }

        @Override
        public void onComplete(String path) {
//            AndroidUtil.viewout(MyAdBanner.downLin);
            handler.obtainMessage(1).sendToTarget();
            runFlag = false;
            InstallUtils.installAPK(mContext, fileUrl, mContext.getPackageName() + ".fileprovider", new InstallUtils.InstallCallBack() {
                @Override
                public void onSuccess() {
//                    Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Exception e) {
//                    Toast.makeText(mContext, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            stopSelf();
        }

        @Override
        public void onLoading(long tol, long cur) {
            if (MyAdBanner.downProgress == null) {
                return;
            }
            current = cur;
            total = tol;
//            int ss= (int) (current/total*100);
//            Log.i("gas",(int)current+"gg"+(int)total+"gg"+((float)(current/total*100))+"%");
            double result = current * 100 / total;
//            Log.i("gas",(int)(current * 100 / total)+"%");
            handler.obtainMessage(0).sendToTarget();

//            AndroidUtil.viewin(MyAdBanner.downLin);
//            AndroidUtil.setText((int) (current * 100 / total) + "%", MyAdBanner.mun);
//            MyAdBanner.downProgress.setMax((int) total);
//            MyAdBanner.downProgress.setProgress((int) current);
//            Log.i(getString(R.string.app_tags), "InstallUtilstotal:" + total + ",current:" + current);
        }

        @Override
        public void onFail(Exception e) {
            handler.obtainMessage(1).sendToTarget();

//            AndroidUtil.viewout(MyAdBanner.downLin);
////            Log.i("gas",e.getMessage());
//            AndroidUtil.setToast(mContext, "下载失败");
            runFlag = false;
            stopSelf();
        }
    };


    public void downLoadFile(String url) {
//        Log.i("gas",url);
        new InstallUtils(mContext, url, "myapk", DownloadCallBack).downloadAPK();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                MyAdBanner.downLin.setVisibility(View.VISIBLE);
//                String num =(int) (current * 100 / total) + "%";
                MyAdBanner.mun.setText((int) (current * 100 / total) + "%");
                MyAdBanner.downProgress.setMax((int) total);
                MyAdBanner.downProgress.setProgress((int) current);
//                AndroidUtil.viewin(MyAdBanner.downLin);
//                AndroidUtil.setText((int) (current * 100 / total) + "%", MyAdBanner.mun);
                break;
            case 1:
                MyAdBanner.downLin.setVisibility(View.GONE);
//                Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
//                AndroidUtil.viewout(MyAdBanner.downLin);
//            Log.i("gas",e.getMessage());
//                AndroidUtil.setToast(mContext,"下载失败");
                break;
            default:
                break;
        }

    }

    static class MyHandler extends Handler {
        WeakReference<MyAdService> mActivity;

        public MyHandler(MyAdService activity) {
            mActivity = new WeakReference<MyAdService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyAdService Activity = mActivity.get();
            Activity.handleMessage(msg);
        }
    }


}

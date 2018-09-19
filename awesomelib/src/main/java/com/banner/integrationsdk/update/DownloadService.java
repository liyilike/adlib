package com.banner.integrationsdk.update;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;


import com.banner.integrationsdk.R;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 后台下载
 */
public class DownloadService extends Service {
    Context mContext;
    public void onCreate() {
        super.onCreate();
        mContext=this;
        File file = new File(UpUtil.getFilePath(mContext));
        if (file.exists()) {
            file.delete();
        }
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            public void run() {
                downLoadFile();
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 返回自定义的DownloadBinder实例
        return null;
    }


    InstallUtils.DownloadCallBack DownloadCallBack =    new InstallUtils.DownloadCallBack() {
        @Override
        public void onStart() {
//            Log.i("fff11", "22sss");
        }

        @Override
        public void onComplete(String path) {
//            Log.i("fff11", "sss");
            startActivity(new Intent(mContext, UpActivity.class));
            stopSelf();
        }

        @Override
        public void onLoading(long total, long current) {
//            Log.i(getString(R.string.app_tags) ,"InstallUtilstotal:" + total + ",current:" + current);
        }

        @Override
        public void onFail(Exception e) {
//            Log.i("fff11", "341");
            stopSelf();
        }
    };

    public void downLoadFile() {
        String httpUrl = mContext.getString(R.string.apk_update_down)+mContext.getPackageName()+".apk";
//        new InstallUtils(mContext,mContext.getString(R.string.apk_update_down), "update", DownloadCallBack).downloadAPK();
        new InstallUtils(mContext,httpUrl, "update", DownloadCallBack).downloadAPK();
    }




}

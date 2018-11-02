package com.banner.integrationsdk.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.banner.integrationsdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpUtil {
    Context mContext;
    String archiveFilePath, describe, apkversion, webStr;
    int versionData;
    public static String ApkPreferences = "apkup";
    private Handler mHandler = new MyHandler(this);

    public UpUtil(Context mContext) {
        this.mContext = mContext;
    }


    public void init() {
//        Log.i("ggg123", "gg");
        archiveFilePath = getFilePath(mContext);//安装包路径
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            public void run() {
//                Log.i("ggg123", "22gg");
                checkUp();
            }
        });
    }

    public String webData() {
        StringBuilder resultData = new StringBuilder("");
        URL url = null;
        try {
            url = new URL(mContext.getString(R.string.apk_update_web));
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setRequestProperty("Content-type", "text/html");
            urlConn.setRequestProperty("Accept-Charset", "utf-8");
            urlConn.setRequestProperty("contentType", "utf-8");
            //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer
            //getInputStream是真正去连接网络获取数据
            InputStreamReader isr = new InputStreamReader(urlConn.getInputStream());
            //使用缓冲一行行的读入，加速InputStreamReader的速度
            BufferedReader buffer = new BufferedReader(isr);
            String inputLine = null;
            while ((inputLine = buffer.readLine()) != null) {
                resultData.append(inputLine);
                resultData.append("\n");
            }
//            Log.i("ggg123", resultData.toString());
            buffer.close();
            isr.close();
            urlConn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultData.toString();
    }


    public void checkUp() {
        webStr = webData();
        if (TextUtils.isEmpty(webStr)) {
            return;
        }
        //解析json
        JSONObject packJson = null;
        try {
            packJson = new JSONObject(webStr).getJSONObject(mContext.getPackageName());
            versionData = packJson.getInt("versioncode");
            describe = packJson.getString("describe");

            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = null;
            pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            int versionCode = pi.versionCode;     //自己apk版本code
//            Log.i("ggg123", versionCode + "!" + versionData);
            if (versionCode < versionData) {   //本apk版本小于网络版
//                Log.i("fff11", "本apk版本小于网络版");
//                Log.i("ggg123", "0");
//        记录网络的describe信息
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(ApkPreferences, Context.MODE_PRIVATE); // 私有数据
                SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
                editor.putString("describe", describe);
                editor.commit();// 提交修改

                File f = new File(archiveFilePath);
                //如果本地下载过apk更新文件存在
                if (f.exists()) {
//                    Log.i("ggg123", "11");
                    PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
                    //检测到apk包是完整的apk包
                    if (info != null) {
//                        Log.i("ggg123", "22");
                        apkversion = info.versionName;       //得到安装包版本信息
                        int apkcode = info.versionCode;       //得到安装包版本信息
                        if (apkcode < versionData) {  //已下载的安装包小于网络的apk版本
//                            Log.i("ggg123", "1");
                            downservice();
                        } else {
//                            Log.i("ggg123", "2");
                            //已下载的安装包大于或等于网络的apk版本弹出安装提示
                            mHandler.obtainMessage(1234).sendToTarget();
                        }
                    } else {
//                        Log.i("ggg123", "3");
                        //如果不是完整apk包就重新下载
                        downservice();
                    }
                } else {
                    downservice();
//                    Log.i("ggg123", "4");
                }
            } else {
//                Log.i("ggg123", "5");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void downservice() {
        mContext.startService(new Intent(mContext, DownloadService.class));
    }


//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1234:
//                    File file = new File(archiveFilePath);
//                    final Dialog dialog = new Dialog(mContext, R.style.UpdateAppDialog);
//                    View contentView = LayoutInflater.from(mContext).inflate(
//                            R.layout.lib_update_app_dialog, null);
//                    dialog.setContentView(contentView);
//                    dialog.setCanceledOnTouchOutside(true);
//                    TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
//                    if (TextUtils.isEmpty(apkversion)) {
//                        apkversion = "最新";
//                    }
//                    tv_title.setText("是否升级到" + apkversion + "版本？");
//
//                    LinearLayout ll_close = (LinearLayout) contentView.findViewById(R.id.ll_close);
//                    ll_close.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        dialog.cancel();
//                                                    }
//                                                }
//                    );
//                    Button btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
//                    btn_ok.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            InstallUtils.installAPK(mContext, archiveFilePath, mContext.getPackageName() + ".fileprovider", new InstallUtils.InstallCallBack() {
//                                @Override
//                                public void onSuccess() {
//                                    Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//
//                                @Override
//                                public void onFail(Exception e) {
//                                    Toast.makeText(mContext, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
//                                    dialog.cancel();
//                                }
//                            });
//
//                        }
//                    });
//                    TextView tv_update_info = (TextView) contentView.findViewById(R.id.tv_update_info);
//                    tv_update_info.setText("新版本大小 " + getFileSize(file) + "M \n \n" + describe);
//                    dialog.show();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    public void work() {
        File file = new File(archiveFilePath);
        final Dialog dialog = new Dialog(mContext, R.style.UpdateAppDialog);

        View contentView = null;
        String language = mContext.getResources().getConfiguration().locale.getLanguage();
        if (!language.equals("zh")) {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.lib_update_app_dialog2, null);
        } else {
            contentView = LayoutInflater.from(mContext).inflate(R.layout.lib_update_app_dialog, null);
        }

        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(true);
        TextView tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        if (TextUtils.isEmpty(apkversion)) {
            apkversion = mContext.getString(R.string.ad_ban_laster);
        }
        tv_title.setText(mContext.getString(R.string.ad_ban_if_update) + apkversion + mContext.getString(R.string.ad_ban_if_version));

        LinearLayout ll_close = (LinearLayout) contentView.findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.cancel();
                                        }
                                    }
        );
        Button btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstallUtils.installAPK(mContext, archiveFilePath, mContext.getPackageName() + ".fileprovider", new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, mContext.getString(R.string.ad_ban_install), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }

                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(mContext, mContext.getString(R.string.ad_ban_fail) + e.toString(), Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

            }
        });
        TextView tv_update_info = (TextView) contentView.findViewById(R.id.tv_update_info);

        if (!language.equals("zh")) {
            describe ="1. "+mContext.getString(R.string.ad_ban_update_system)+"\n"+"2. "+mContext.getString(R.string.ad_ban_jianron_system);
            tv_update_info.setText(mContext.getString(R.string.ad_ban_apksize) + getFileSize(file) + "M \n \n" + describe);
        } else {
            tv_update_info.setText(mContext.getString(R.string.ad_ban_apksize) + getFileSize(file) + "M \n \n" + describe);
        }

        dialog.show();
    }


    static class MyHandler extends Handler {
        WeakReference<UpUtil> mUp;

        public MyHandler(UpUtil upUtil) {
            mUp = new WeakReference<UpUtil>(upUtil);
        }

        @Override
        public void handleMessage(Message msg) {
            UpUtil upUtil = mUp.get();
            switch (msg.what) {
                case 1234:
                    upUtil.work();
                    break;
                default:
                    break;
            }
        }

    }


    public static double getFileSize(File file) {
        if (file == null) {
            return 0;
        }
        double size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.i("234qq",size+"");
        size = size * 1.0 / 1048576;
        double ss = (double) Math.round(size * 100) / 100;
//        Log.i("234qq",ss+"");
        return ss;
    }

    public static String getFilePath(Context context) {
        return InstallUtils.getCachePath(context) + File.separator + "update.apk";
    }


}




package com.banner.integrationsdk.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liyi.R;

import java.io.File;

public class UpActivity extends AppCompatActivity {
    Context mContext;
    LinearLayout ll_top, ll_close;
    String archiveFilePath;
    TextView tv_title, tv_update_info;
    Button btn_ok;
    File file;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lib_update_app_dialog);
        mContext = this;
        initview();
        work();
    }

    public void work() {
        archiveFilePath = UpUtil.getFilePath(mContext);//安装包路径
        SharedPreferences share = getSharedPreferences(UpUtil.ApkPreferences, Context.MODE_PRIVATE);
        //检测到apk包是完整的apk包
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            tv_title.setText("是否升级到" + info.versionName + "版本？");
        } else {
            tv_title.setText("是否升级到最新版本？");
        }
        file = new File(archiveFilePath);
        tv_update_info.setText("新版本大小 " + UpUtil.getFileSize(file) + "M \n \n" + share.getString("describe", ""));
    }

    public void initview() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_update_info = (TextView) findViewById(R.id.tv_update_info);
        ll_close = (LinearLayout) findViewById(R.id.ll_close);
        ll_close.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    }
        );
        ll_top = (LinearLayout) findViewById(R.id.ll_top);
        ll_top.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InstallUtils.installAPK(mContext, archiveFilePath, mContext.getPackageName() + ".fileprovider", new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(mContext, "正在安装程序", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onFail(Exception e) {
                        Toast.makeText(mContext, "安装失败:" + e.toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}

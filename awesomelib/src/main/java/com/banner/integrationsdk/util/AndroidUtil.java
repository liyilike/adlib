package com.banner.integrationsdk.util;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;


public class AndroidUtil {
    private static ProgressDialog progress;
    static Context context;
    static String toast;
    static View ToastView, viewin, viewout;
    static Drawable drawablee;
    static TextView handlertext;
    static Thread httpClientThread;

    public AndroidUtil(Activity loginActivity) {
        this.context = loginActivity;
    }

    /**
     * 线程安全运行包含ui主线程的toast,针对只需用一个toast,传入Activity可用
     **/
    public static void viewin(View view) {
        viewin = view;
        handler.obtainMessage(6).sendToTarget();
    }

    public static void viewout(View view) {
        viewout = view;
        handler.obtainMessage(7).sendToTarget();
    }

    public static void toast(View view, String string) {
        ToastView = view;
        toast = string;
        handler.obtainMessage(0).sendToTarget();
    }

    public static void setToast(Context view, String string) {
        context = view;
        toast = string;
        handler.obtainMessage(2).sendToTarget();
    }

    public static void setLongToast(Context view, String string) {
        context = view;
        toast = string;
        handler.obtainMessage(5).sendToTarget();
    }

    public static void setHandler(Runnable httpClientRunnable) {

        if (!httpClientThread.isAlive()) {   //有这短话就会能只运行一次线程
            httpClientThread = new Thread(httpClientRunnable);
            httpClientThread.start();
        }
    }


    public static void setMoreHandler(Runnable httpClientRunnable) {
        httpClientThread = new Thread(httpClientRunnable);
        httpClientThread.start();
    }

    /**
     * edit返回tostring
     **/
    public static String Str(EditText editText) {
        return editText.getText().toString();
    }

    /**
     * 单个跳转
     **/
    public static void Intent(Context beforeActivity, Class<?> afterActivity) {
        Intent intent5 = new Intent();
        intent5.setClass(beforeActivity, afterActivity);
        beforeActivity.startActivity(intent5);
    }

    /**
     * 线程更新文字图片,不绑定id
     **/
    public static void setText(String text, TextView textview) {
        handlertext = textview;
        Message msg = new Message();
        msg.obj = text;
        msg.what = 3;
        handler.sendMessage(msg);
    }

    public static void dissmess(ProgressDialog progress2) {
        progress = progress2;
        handler.obtainMessage(4).sendToTarget();
    }

    public static void dissprogerss() {
        handler.obtainMessage(4).sendToTarget();
    }

    public static void ShowProgerss(Activity activity, String toast2) {
        context = activity;
        toast = toast2;
        handler.obtainMessage(1).sendToTarget();
    }

    /**
     * 网络获取图片方法
     **/
    //获取网络图片,1个参数当前Activity,第二个参数绑定的id,第三个参数要获取图片的网络地址
    public static void getNetimage(final ImageView mImageView, final String IMAGE_URL) {
        new Thread(new Runnable() {
            public void run() {
                drawablee = loadImageFromNetwork(IMAGE_URL);
                mImageView.post(new Runnable() {   // post() 特别关键，就是到UI主线程去更新图片
                    public void run() {
                        mImageView.setImageDrawable(drawablee);
                    }
                });
            }

        }).start();
    }

    //获取网络图片,但没有线程,加上上面就可以了
    public static Drawable loadImageFromNetwork(String imageUrl) {
        Drawable drawable = null;
        try {
            // 可以在这里通过文件名来判断，是否本地有此图片
            drawable = Drawable.createFromStream(new URL(imageUrl).openStream(), "image.jpg");
        } catch (IOException e) {
        }
        if (drawable == null) {
        } else {
        }
        return drawable;
    }


    static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    progress = new ProgressDialog(context);
                    progress.setMessage(toast);
                    progress.setCanceledOnTouchOutside(false);
                    progress.setCancelable(false);
                    progress.show();
                }
                break;
                case 2: {
                    Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
                }
                break;
                case 3: {
                    handlertext.setText((String) msg.obj);
                }
                break;
                case 4: {
                    progress.dismiss();
                }
                break;
                case 5: {
                    Toast.makeText(context, toast, Toast.LENGTH_LONG).show();
                }
                break;
                case 6: {
                    viewin.setVisibility(View.VISIBLE);
                }
                break;
                case 7: {
                    viewout.setVisibility(View.GONE);
                }
                break;
                default:
                    break;
            }

        }

        ;
    };

}

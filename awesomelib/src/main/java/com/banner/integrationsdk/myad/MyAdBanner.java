package com.banner.integrationsdk.myad;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.banner.integrationsdk.Interface.JavaScriptinterface;
import com.banner.integrationsdk.util.AndroidUtil;
import com.liyi.R;
import com.liyi.fragmet.IndexOne;

import java.lang.ref.WeakReference;


public class MyAdBanner extends AppCompatActivity {
    WebView myweb;
    public static ProgressBar downProgress = null;
    TextView url, title;
    ImageView close, close2;
    ProgressBar progressBar = null;
    static Context mContext;
    SwipeRefreshLayout swipeRefreshLayout;
    public static String downUrl = null;
    public static LinearLayout downLin = null;
    public static TextView mun = null;
    String webURL;
    private Handler mHandler = new MyHandler(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myad);
        mContext = this;
        getBundle();
        initview();
    }

    public void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("url")) {
            webURL = bundle.getString("url");//读出数据
        } else {
            webURL = getString(R.string.apk_more_down);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        downProgress = null;
    }

    public void initview() {
        downLin = (LinearLayout) findViewById(R.id.downLin);
        mun = (TextView) findViewById(R.id.mun);
        downProgress = (ProgressBar) findViewById(R.id.downProgress);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.id_swipe_ly);
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myweb.loadUrl(myweb.getUrl());
            }
        });
        url = (TextView) findViewById(R.id.url);
        close = (ImageView) findViewById(R.id.close);
        title = (TextView) findViewById(R.id.title);
        close2 = (ImageView) findViewById(R.id.close2);
        myweb = (WebView) findViewById(R.id.myweb);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        myweb.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        myweb.clearCache(true);
        //js和网页交互让网页跳转android原生浏览器
        myweb.addJavascriptInterface(new JavaScriptinterface(this),"android");
        // 设置setWebChromeClient对象
        myweb.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    swipeRefreshLayout.setRefreshing(false);
                    if ((keyCode == KeyEvent.KEYCODE_BACK) && myweb.canGoBack()) {
                        myweb.goBack();
                        return true;
                    }

                }
                return false;
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (myweb.canGoBack()) {
                    myweb.goBack();
                } else {
                    finish();
                }
            }
        });
        close2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        myweb.getSettings().setJavaScriptEnabled(true);
        myweb.requestFocus();
        myweb.loadUrl(webURL);
        // 设置web视图客户端
        myweb.setWebViewClient(new MyWebViewClient());
        myweb.setDownloadListener(new MyWebViewDownLoadListener());
        myweb.setWebChromeClient(setting);
    }

    //标题设置
    WebChromeClient setting = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title2) {
            super.onReceivedTitle(view, title2);
            Message msg = new Message();
            msg.obj = title2;
            msg.what = 0;
            mHandler.sendMessage(msg);
        }

        public void onProgressChanged(WebView view, int newProgress) {
            url.setText(myweb.getUrl());
            if (newProgress == 100) {
                progressBar.setVisibility(View.INVISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            } else {

                if (View.INVISIBLE == progressBar.getVisibility()) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    //下载监听
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            if (MyAdService.runFlag == true) {
                AndroidUtil.setToast(mContext, getString(R.string.ad_ban_loading));
                return;
            }
            AndroidUtil.setToast(mContext, getString(R.string.ad_ban_start_down));
            downUrl = url;
//            stopService(new Intent(mContext, MyAdService.class));
            startService(new Intent(mContext, MyAdService.class));
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }

    }

    //内部类
    public class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
            view.loadUrl(url);
            // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
            return true;
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        public void onPageFinished(WebView view, String url) {
        }

        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
        }

    }


    static class MyHandler extends Handler {
        WeakReference<MyAdBanner> mActivity;

        public MyHandler(MyAdBanner activity) {
            mActivity = new WeakReference<MyAdBanner>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyAdBanner Activity = mActivity.get();
            switch (msg.what) {
                case 0:
                    Activity.title.setText((String) msg.obj);
                    break;
            }
        }

    }

//    Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0:
//                    title.setText((String) msg.obj);
//                    break;
//                case 1:
//                    break;
//                default:
//                    break;
//            }
//
//        }
//    };

}

package com.banner.integrationsdk.Interface;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * Created by gjz on 9/3/16.
 */
public class JavaScriptinterface {
    Context context;
    public JavaScriptinterface(Context c) {
        context= c;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void Intent (String url) {
//        Log.i("gg1","ggg");
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}

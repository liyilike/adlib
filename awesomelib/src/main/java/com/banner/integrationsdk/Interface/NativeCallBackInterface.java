package com.banner.integrationsdk.Interface;

import com.qq.e.ads.nativ.NativeAD;
import com.qq.e.ads.nativ.NativeADDataRef;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * Created by gjz on 9/3/16.
 */
public interface NativeCallBackInterface extends NativeAD.NativeAdListener {
    void onADLoaded(List<NativeADDataRef> var1);

    void onNoAD(AdError var1);

    void onADStatusChanged(NativeADDataRef var1);

    void onADError(NativeADDataRef var1, AdError var2);

}

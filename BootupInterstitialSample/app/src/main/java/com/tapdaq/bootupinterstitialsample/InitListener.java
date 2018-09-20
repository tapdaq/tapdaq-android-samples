package com.tapdaq.bootupinterstitialsample;

import android.app.Activity;
import android.util.Log;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.ads.TapdaqPlacement;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;

/**
 * Created by dominicroberts on 23/01/2017.
 */

public class InitListener extends TMInitListener {

    private Activity mActivity;

    public InitListener(Activity activity) {
        super();
        mActivity = activity;
    }

    @Override
    public void didInitialise() {
        super.didInitialise();
        Log.i("TAPDAQ-BOOTUP-SAMPLE", "didInitialise");
        Tapdaq.getInstance().loadInterstitial(mActivity, TapdaqPlacement.TDPTagDefault, new TMAdListener() {
            @Override
            public void didLoad() {
                super.didLoad();
                Tapdaq.getInstance().showInterstitial(mActivity, TapdaqPlacement.TDPTagDefault, new TMAdListener());
            }
        });
    }

    @Override
    public void didFailToInitialise(TMAdError error) {
        super.didFailToInitialise(error);
        Log.i("TAPDAQ-BOOTUP-SAMPLE", "didFailToInitialise" + error.getErrorMessage());
    }
}

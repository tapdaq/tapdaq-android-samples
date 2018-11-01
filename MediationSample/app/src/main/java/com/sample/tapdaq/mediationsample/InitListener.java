package com.sample.tapdaq.mediationsample;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMInitListener;

/**
 * Created by dominicroberts on 25/01/2017.
 */

public class InitListener extends TMInitListener {

    @Override
    public void didInitialise() {
        super.didInitialise();
        Log.i("MEDIATION-SAMPLE", "didInitialise");
    }

    @Override
    public void didFailToInitialise(TMAdError error) {
        super.didFailToInitialise(error);
        Log.i("MEDIATION-SAMPLE", "didFailToInitialise" + error.getErrorMessage());
    }
}

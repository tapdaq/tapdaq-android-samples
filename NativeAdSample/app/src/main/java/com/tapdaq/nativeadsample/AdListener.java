package com.tapdaq.nativeadsample;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;

import java.util.Locale;

/**
 * Created by dominicroberts on 25/01/2017.
 */

public class AdListener extends TMAdListener {
    @Override
    public void didLoad() {
        Log.i("NATIVE-SAMPLE", "didLoad");
    }

    @Override
    public void willDisplay() {
        Log.i("NATIVE-SAMPLE", "willDisplay");
    }

    @Override
    public void didDisplay() {
        Log.i("NATIVE-SAMPLE", "didDisplay");
    }

    @Override
    public void didClick() {
        Log.i("NATIVE-SAMPLE", "didClick");
    }

    @Override
    public void didClose() {
        Log.i("NATIVE-SAMPLE", "didClose");
    }

    @Override
    public void didFailToLoad(TMAdError tmAdError) {
        Log.i("NATIVE-SAMPLE", String.format(Locale.ENGLISH, "%s %s", "didFailToLoad", tmAdError.getErrorMessage()));
    }
}

package com.sample.tapdaq.mediationsample;

import android.util.Log;

import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.model.rewards.TDReward;

import java.util.Locale;
import java.util.Map;

/**
 * Created by dominicroberts on 10/11/2016.
 */

public class AdListener extends TMAdListener {

    @Override
    public void didRewardFail(TMAdError error) {
        super.didRewardFail(error);
        Log.i("MEDIATION-SAMPLE", "didRewardFail " + error.getErrorMessage());
    }

    @Override
    public void onUserDeclined() {
        Log.i("MEDIATION-SAMPLE", "onUserDeclined");
    }


    @Override
    public void didVerify(TDReward reward) {
        Log.i("MEDIATION-SAMPLE", String.format(Locale.ENGLISH, "didVerify %s %d %b %s", reward.getName(), reward.getValue(), reward.isValid(), reward.getCustom_json().toString()));
    }

    @Override
    public void didComplete() {
        Log.i("MEDIATION-SAMPLE", "didComplete");
    }

    @Override
    public void didEngagement() {
        Log.i("MEDIATION-SAMPLE", "didEngagement");
    }

    @Override
    public void didLoad() {
        Log.i("MEDIATION-SAMPLE", "didLoad");
    }

    @Override
    public void didRefresh() {
        Log.i("MEDIATION-SAMPLE", "didRefresh");
    }

    @Override
    public void willDisplay() {
        Log.i("MEDIATION-SAMPLE", "willDisplay");
    }

    @Override
    public void didDisplay() {
        Log.i("MEDIATION-SAMPLE", "didDisplay");
    }

    @Override
    public void didClick() {
        Log.i("MEDIATION-SAMPLE", "didClick");
    }

    @Override
    public void didClose() {
        Log.i("MEDIATION-SAMPLE", "didClose");
    }

    @Override
    public void didFailToLoad(TMAdError tmAdError) {
        Log.i("MEDIATION-SAMPLE", "didFailToLoad " + tmAdError.getErrorMessage());
    }
}

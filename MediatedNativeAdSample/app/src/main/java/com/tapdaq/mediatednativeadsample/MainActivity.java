package com.tapdaq.mediatednativeadsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.TapdaqPlacement;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdOptions;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.load_btn).setOnClickListener(new OnClickLoadAd());

        TLog.setLoggingLevel(TLogLevel.DEBUG);
        TapdaqConfig config = new TapdaqConfig();
        List<TapdaqPlacement> enabledPlacements = new ArrayList<TapdaqPlacement>();
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.NATIVE), TapdaqPlacement.TDPTagDefault));
        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", config, new TMInitListener() {
            @Override
            public void didInitialise() {
                super.didInitialise();
                findViewById(R.id.load_btn).setEnabled(true);
            }

            @Override
            public void didFailToInitialise(TMAdError error) {
                super.didFailToInitialise(error);
            }
        });
    }

    private class OnClickLoadAd implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            TDMediatedNativeAdOptions options = new TDMediatedNativeAdOptions(); //optional param
            Tapdaq.getInstance().loadMediatedNativeAd(MainActivity.this, TapdaqPlacement.TDPTagDefault, options, new AdListener());
        }
    }

    private class AdListener extends TMAdListener {
        @Override
        public void didLoad(TDMediatedNativeAd ad) {
            super.didLoad(ad);
            NativeAdLayout adlayout = findViewById(R.id.native_ad);
            adlayout.populate(ad);
        }

        @Override
        public void didDisplay() {
            super.didDisplay();
        }

        @Override
        public void didClick() {
            super.didClick();
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            super.didFailToLoad(error);
        }
    }
}

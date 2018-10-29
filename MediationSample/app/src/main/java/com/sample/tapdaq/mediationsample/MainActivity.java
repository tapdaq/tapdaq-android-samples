package com.sample.tapdaq.mediationsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;

import java.util.ArrayList;
import java.util.List;

/*
*
* Mediation Sample for Tapdaq Integration
*
* This app demonstrates how to initialise and show banners, static/video/rewarded interstitials
* using Tapdaq Mediation service, you should have set up an account with Tapdaq and retreived
* your AppID and Client Key in order to use this app.
*
 */

public class MainActivity extends AppCompatActivity {

    TMBannerAdView mBannerAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TLog.setLoggingLevel(TLogLevel.DEBUG);

        //Get Show Buttons from layout
        findViewById(R.id.sample_show_debug_btn).setOnClickListener(new ClickShowDebug());
        findViewById(R.id.sample_show_banner_btn).setOnClickListener(new ClickShowBanner());

        findViewById(R.id.sample_load_interstitial_btn).setOnClickListener(new ClickLoadInterstitial());
        findViewById(R.id.sample_load_video_btn).setOnClickListener(new ClickLoadVideo());
        findViewById(R.id.sample_load_rewarded_video_btn).setOnClickListener(new ClickLoadRewarded());

        findViewById(R.id.sample_show_interstitial_btn).setOnClickListener(new ClickShowInterstitial());
        findViewById(R.id.sample_show_video_btn).setOnClickListener(new ClickShowVideo());
        findViewById(R.id.sample_show_rewarded_btn).setOnClickListener(new ClickShowRewardedVideo());

        //Get Banner UI from layout
        mBannerAd = (TMBannerAdView)findViewById(R.id.sample_banner_ad);

        //Set Placements & Ad Types
        List<TapdaqPlacement> enabledPlacements = new ArrayList<>();

        //Configuration
        TapdaqConfig config = new TapdaqConfig();
        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        //Initialise Cardview app
        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", config, new InitListener());
    }

    private class ClickShowDebug implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /* Start Test Activity
            * Params Context (Current Activity), AdSize, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().startTestActivity(MainActivity.this);
        }
    }

    private class ClickShowBanner implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new ad
            * Params Context (Current Activity), AdSize, AdListener (optional, can be set to null)
            */
            mBannerAd.load(MainActivity.this, TMBannerAdSizes.STANDARD, new AdListener());
        }
    }

    private class ClickLoadInterstitial implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Static interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().loadInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickLoadVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Static interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().loadVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickLoadRewarded implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Static interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            Tapdaq.getInstance().loadRewardedVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickShowInterstitial implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Static interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            if (Tapdaq.getInstance().isInterstitialReady(MainActivity.this, TapdaqPlacement.TDPTagDefault)) {
                Tapdaq.getInstance().showInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
            } else {
                Log.i("MEDIATION-SAMPLE", "Interstitial ad not available, call Load first");
            }
        }
    }

    private class ClickShowVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Video interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            if (Tapdaq.getInstance().isVideoReady(MainActivity.this, TapdaqPlacement.TDPTagDefault)) {
                Tapdaq.getInstance().showVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
            } else {
                Log.i("MEDIATION-SAMPLE", "Video ad not available, call Load first");
            }
        }
    }

    private class ClickShowRewardedVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /*Load a new Rewarded Video interstitial
            * Params Activity (Current Activity), TapdaqPlacement, AdListener (optional, can be set to null)
            */
            if (Tapdaq.getInstance().isRewardedVideoReady(MainActivity.this, TapdaqPlacement.TDPTagDefault)) {
                Tapdaq.getInstance().showRewardedVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
            } else {
                Log.i("MEDIATION-SAMPLE", "Rewarded ad not available, call Load first");
            }
        }
    }
}

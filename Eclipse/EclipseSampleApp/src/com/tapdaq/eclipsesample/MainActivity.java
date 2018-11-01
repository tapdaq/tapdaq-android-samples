package com.tapdaq.eclipsesample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

import com.tapdaq.*;
import com.tapdaq.sdk.*;
import com.tapdaq.sdk.ads.TapdaqPlacement;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.*;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.demo_load_static_interstitial_btn).setOnClickListener(new ClickLoadStatic());
		findViewById(R.id.demo_static_interstitial_btn).setOnClickListener(new ClickShowStatic());
		findViewById(R.id.demo_load_video_interstitial_btn).setOnClickListener(new ClickLoadVideo());
		findViewById(R.id.demo_video_interstitial_btn).setOnClickListener(new ClickShowVideo());
		findViewById(R.id.demo_load_rewarded_video_interstitial_btn).setOnClickListener(new ClickLoadRewardedVideo());
		findViewById(R.id.demo_rewarded_video_interstitial_btn).setOnClickListener(new ClickShowRewardedVideo());
		
		Tapdaq.getInstance().registerAdapter(this, new TMAdMobAdapter(this));
		Tapdaq.getInstance().registerAdapter(this, new TMFacebookAdapter(this));
		Tapdaq.getInstance().registerAdapter(this, new TMUnityAdsAdapter(this));
		Tapdaq.getInstance().registerAdapter(this, new TMVungleAdapter(this));
		
		TLog.setLoggingLevel(TLogLevel.DEBUG);
		
		TapdaqConfig config = new TapdaqConfig(this);
		Tapdaq.getInstance().initialize(this, "56154428d10c645f2337687c", "3564a57a-9391-485b-ac01-83efea9b0599", config, new TMInitListener());
	}
	
	private class ClickLoadStatic implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().loadInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class ClickShowStatic implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().showInterstitial(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class ClickLoadVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().loadVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class ClickShowVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().showVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class ClickLoadRewardedVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().loadRewardedVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class ClickShowRewardedVideo implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
        	Tapdaq.getInstance().showRewardedVideo(MainActivity.this, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
	}
	
	private class AdListener extends TMAdListener {
		 @Override
	        public void didClose() {
	            TLog.debug("didClose");
	        }

	        @Override
	        public void didFailToLoad(TMAdError error) {
	            String str = String.format(Locale.ENGLISH, "didFailToLoad: %s", error.getErrorMessage());
	            TLog.debug(str);
	        }

	        @Override
	        public void didClick() {
	            TLog.debug("didClick");
	        }

	        @Override
	        public void willDisplay() {
	            TLog.debug("willDisplay");
	        }

	        @Override
	        public void didDisplay() {
	            TLog.debug("didDisplay");
	        }

	        @Override
	        public void didLoad() {
	            TLog.debug("didLoad");
	        }

	        @Override
	        public void didRewardFail(TMAdError error) {
	        	String str = String.format(Locale.ENGLISH, "didRewardFail: %s", error.getErrorMessage());
	            TLog.debug(str);
	        }

	        @Override
	        public void onUserDeclined() {
	            TLog.debug("onUserDeclined");
	        }

	        @Override
	        public void didVerify(String location, String reward, Double value) {
	            String str = String.format(Locale.ENGLISH, "didVerify: %s. %s %s", location, value, reward);
	            TLog.debug(str);
	        }

	        @Override
	        public void didComplete() {
	            TLog.debug("didComplete");
	        }

	        @Override
	        public void didEngagement() {
	            TLog.debug("didEngagement");
	        }
	}

}

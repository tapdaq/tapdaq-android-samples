package com.sample.tapdaq.mediationsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.tapdaq.sdk.*;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.common.TMAdType;
import com.tapdaq.sdk.common.TMBannerAdSizes;
import com.tapdaq.sdk.debug.TMDebugAdapter;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.model.TMAdSize;
import com.tapdaq.sdk.model.rewards.TDReward;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/*
*
* Mediation Sample for Tapdaq Integration
*
* This app demonstrates how to initialise and show banners, static/video/rewarded interstitials
* using Tapdaq Mediation service, you should have set up an account with Tapdaq and retrieved
* your AppID and Client Key in order to use this app.
*
 */

public class MainActivity extends AppCompatActivity {
    private String mAppId = "";
    private String mClientKey = "";

    private TMBannerAdView mBannerAd;
    private TextView mPlacementTagTextView;

    private TMDebugAdapter mLogListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TLog.setLoggingLevel(TLogLevel.DEBUG);

        //Get Show Buttons from layout
        findViewById(R.id.init_btn).setOnClickListener(new ClickInitialise());
        findViewById(R.id.show_debug_btn).setOnClickListener(new ClickShowDebug());

        findViewById(R.id.load_btn).setOnClickListener(new ClickLoadAd());
        findViewById(R.id.show_btn).setOnClickListener(new ClickShowAd());

        findViewById(R.id.load_banner_btn).setOnClickListener(new ClickLoadBanner());
        findViewById(R.id.destroy_banner_btn).setOnClickListener(new ClickDestroyBanner());

        //Logger
        mLogListAdapter = new TMDebugAdapter(this, new ArrayList<String>());
        ((ListView)findViewById(R.id.log_listview)).setAdapter(mLogListAdapter);

        //Get UI from layout
        mPlacementTagTextView = findViewById(R.id.placement_tag_textview);
        mPlacementTagTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateView();
            }
        });

        ((Spinner)findViewById(R.id.interstitial_spinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateView();
            }
        });

        mBannerAd = findViewById(R.id.banner_ad);
    }

    private String getPlacementTag() {
        return mPlacementTagTextView.getText().toString();
    }

    private void updateView() {
        String tag = getPlacementTag();

        boolean isReady = false;

        int type = TMAdType.getInt(((Spinner)findViewById(R.id.interstitial_spinner)).getSelectedItem().toString());
        switch (type) {
            case TMAdType.STATIC_INTERSTITIAL:
                isReady = Tapdaq.getInstance().isInterstitialReady(this, tag);
                break;
            case TMAdType.VIDEO_INTERSTITIAL:
                isReady = Tapdaq.getInstance().isVideoReady(this, tag);
                break;
            case TMAdType.REWARD_INTERSTITIAL:
                isReady = Tapdaq.getInstance().isRewardedVideoReady(this, tag);
                break;
            case TMAdType.OFFERWALL:
                isReady = Tapdaq.getInstance().isOfferwallReady(this);
                break;
        }

        if (isReady) {
            findViewById(R.id.show_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.show_btn).setVisibility(View.GONE);
        }
    }

    private class ClickInitialise implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            //Set Placements & Ad Types
            List<TapdaqPlacement> enabledPlacements = new ArrayList<>();
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE), TapdaqPlacement.TDPTagDefault));

            //Configuration
            TapdaqConfig config = new TapdaqConfig();
            config.setAutoReloadAds(true);
            config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

            //Initialise app
            mLogListAdapter.insert("Click Initialise", 0);
            ((Button)findViewById(R.id.init_btn)).setText(R.string.initialising_label);
            findViewById(R.id.init_btn).setEnabled(false);
            Tapdaq.getInstance().initialize(MainActivity.this, mAppId, mClientKey, config, new InitListener());
        }
    }

    private class ClickShowDebug implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            /* Start Test Activity
            * Params Activity (Current Activity)
            */
            Tapdaq.getInstance().startTestActivity(MainActivity.this);
        }
    }

    private class ClickLoadAd implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int type = TMAdType.getInt(((Spinner)findViewById(R.id.interstitial_spinner)).getSelectedItem().toString());

            mLogListAdapter.insert("Click Load " + TMAdType.getString(type), 0);
            switch (type) {
                case TMAdType.STATIC_INTERSTITIAL:
                {
                    Tapdaq.getInstance().loadInterstitial(MainActivity.this, getPlacementTag(), new AdListener(type));
                    break;
                }
                case TMAdType.VIDEO_INTERSTITIAL:
                {
                    Tapdaq.getInstance().loadVideo(MainActivity.this, getPlacementTag(), new AdListener(type));
                    break;
                }
                case TMAdType.REWARD_INTERSTITIAL:
                {
                    Tapdaq.getInstance().loadRewardedVideo(MainActivity.this, getPlacementTag(), new AdListener(type));
                    break;
                }
                case TMAdType.OFFERWALL:
                {
                    Tapdaq.getInstance().loadOfferwall(MainActivity.this, new AdListener(type));
                    break;
                }
                default:
                    break;
            }
        }
    }

    private class ClickShowAd implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int type = TMAdType.getInt(((Spinner)findViewById(R.id.interstitial_spinner)).getSelectedItem().toString());

            mLogListAdapter.insert("Click Show " + TMAdType.getString(type), 0);
            switch (type) {
                case TMAdType.STATIC_INTERSTITIAL:
                {
                    if (Tapdaq.getInstance().isInterstitialReady(MainActivity.this, getPlacementTag())) {
                        Tapdaq.getInstance().showInterstitial(MainActivity.this, getPlacementTag(), new AdListener(type));
                    } else {
                        Log.i("MEDIATION-SAMPLE", "Interstitial ad not available, call Load first");
                    }
                    break;
                }
                case TMAdType.VIDEO_INTERSTITIAL:
                {
                    if (Tapdaq.getInstance().isVideoReady(MainActivity.this, getPlacementTag())) {
                        Tapdaq.getInstance().showVideo(MainActivity.this, getPlacementTag(), new AdListener(type));
                    } else {
                        Log.i("MEDIATION-SAMPLE", "Video ad not available, call Load first");
                    }
                    break;
                }
                case TMAdType.REWARD_INTERSTITIAL:
                {
                    if (Tapdaq.getInstance().isRewardedVideoReady(MainActivity.this, getPlacementTag())) {
                        Tapdaq.getInstance().showRewardedVideo(MainActivity.this, getPlacementTag(), new AdListener(type));
                    } else {
                        Log.i("MEDIATION-SAMPLE", "Rewarded ad not available, call Load first");
                    }
                    break;
                }
                case TMAdType.OFFERWALL:
                {
                    if (Tapdaq.getInstance().isOfferwallReady(MainActivity.this)) {
                        Tapdaq.getInstance().showOfferwall(MainActivity.this, new AdListener(type));
                    } else {
                        Log.i("MEDIATION-SAMPLE", "Rewarded ad not available, call Load first");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    private class ClickLoadBanner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            TMAdSize size = TMBannerAdSizes.get((String)((Spinner)findViewById(R.id.banner_spinner)).getSelectedItem());
            mBannerAd.load(MainActivity.this, size, new AdListener(TMAdType.BANNER));
        }
    }

    private class ClickDestroyBanner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mBannerAd.destroy(MainActivity.this);
        }
    }

    private class InitListener extends TMInitListener
    {
        @Override
        public void didInitialise() {
            super.didInitialise();
            findViewById(R.id.init_btn).setVisibility(View.GONE);
            findViewById(R.id.content_view).setVisibility(View.VISIBLE);
            Log.i("MEDIATION-SAMPLE", "didInitialise");
            mLogListAdapter.insert("didInitialise", 0);
        }

        @Override
        public void didFailToInitialise(TMAdError error) {
            super.didFailToInitialise(error);
            ((Button)findViewById(R.id.init_btn)).setText(R.string.initialise_label);

            String str = String.format(Locale.ENGLISH, "didFailToInitialise: %d - %s", error.getErrorCode(), error.getErrorMessage());

            for (String key : error.getSubErrors().keySet()) {
                TMAdError value = error.getSubErrors().get(key);
                String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                str = str.concat("\n ");
                str = str.concat(subError);
            }

            Log.i("MEDIATION-SAMPLE", str);
            mLogListAdapter.insert(str, 0);
            findViewById(R.id.init_btn).setEnabled(true);
        }
    }

    public class AdListener extends TMAdListener {
        private int mType;
        AdListener(int type) {
            mType = type;
        }

        @Override
        public void didLoad() {
            updateView();
            Log.i("MEDIATION-SAMPLE", "didLoad " + TMAdType.getString(mType));
            mLogListAdapter.insert("didLoad " + TMAdType.getString(mType), 0);
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            updateView();

            String str = String.format(Locale.ENGLISH, "didFailToLoad %s: %d - %s", TMAdType.getString(mType), error.getErrorCode(), error.getErrorMessage());

            for (String key : error.getSubErrors().keySet()) {
                TMAdError value = error.getSubErrors().get(key);
                String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                str = str.concat("\n ");
                str = str.concat(subError);
            }

            Log.i("MEDIATION-SAMPLE", str);
            mLogListAdapter.insert(str, 0);
        }

        @Override
        public void didRefresh() {
            Log.i("MEDIATION-SAMPLE", "didRefresh " + TMAdType.getString(mType));
            mLogListAdapter.insert("didRefresh " + TMAdType.getString(mType), 0);
        }

        @Override
        public void willDisplay() {
            Log.i("MEDIATION-SAMPLE", "willDisplay " + TMAdType.getString(mType));
            mLogListAdapter.insert("willDisplay " + TMAdType.getString(mType), 0);
        }

        @Override
        public void didDisplay() {
            Log.i("MEDIATION-SAMPLE", "didDisplay " + TMAdType.getString(mType));
            mLogListAdapter.insert("didDisplay " + TMAdType.getString(mType), 0);
        }

        @Override
        public void didClick() {
            Log.i("MEDIATION-SAMPLE", "didClick " + TMAdType.getString(mType));
            mLogListAdapter.insert("didClick " + TMAdType.getString(mType), 0);
        }

        @Override
        public void didVerify(TDReward reward) {
            String str = String.format(Locale.ENGLISH, "didVerify %s: Reward name: %s. Value: %d. Valid: %b. Custom Json: %s", TMAdType.getString(mType), reward.getName(), reward.getValue(), reward.isValid(), reward.getCustom_json().toString());
            Log.i("MEDIATION-SAMPLE", str);
            mLogListAdapter.insert(str, 0);
        }

        @Override
        public void didClose() {
            updateView();
            Log.i("MEDIATION-SAMPLE", "didClose " + TMAdType.getString(mType));
            mLogListAdapter.insert("didClose " + TMAdType.getString(mType), 0);
        }
    }
}

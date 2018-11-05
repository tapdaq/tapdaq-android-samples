package com.tapdaq.moreappssample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.tapdaq.adapters.tapdaq.moreapps.TMMoreAppsConfig;
import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.TapdaqPlacement;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.debug.TMDebugAdapter;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "TAPDAQ";

    private String mAppId = "";
    private String mClientKey = "";

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

        //Logger
        mLogListAdapter = new TMDebugAdapter(this, new ArrayList<String>());
        ((ListView)findViewById(R.id.log_listview)).setAdapter(mLogListAdapter);
    }

    private TMMoreAppsConfig getCustomMoreAppsConfig() {
        TMMoreAppsConfig config = new TMMoreAppsConfig();
        config.setHeaderText("More Games");
        config.setInstalledButtonText("Play");

        config.setHeaderTextColor(Color.WHITE);
        config.setHeaderColor(Color.DKGRAY);
        config.setHeaderCloseButtonColor(Color.BLACK);
        config.setBackgroundColor(Color.GRAY);

        config.setAppNameColor(Color.BLACK);
        config.setAppButtonColor(Color.BLACK);
        config.setAppButtonTextColor(Color.WHITE);
        config.setInstalledAppButtonColor(Color.WHITE);
        config.setInstalledAppButtonTextColor(Color.BLACK);
        return config;
    }

    private void updateView() {
        if (Tapdaq.getInstance().isMoreAppsReady(this)) {
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
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-1"));
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-2"));
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-3"));
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-4"));
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-5"));
            enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-backfill"));

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
            Tapdaq.getInstance().loadMoreApps(MainActivity.this, getCustomMoreAppsConfig(), new AdListener());
        }
    }

    private class ClickShowAd implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            if (Tapdaq.getInstance().isMoreAppsReady(MainActivity.this)) {
                Tapdaq.getInstance().showMoreApps(MainActivity.this, new AdListener());
            }

            updateView();
        }
    }

    private class InitListener extends TMInitListener
    {
        @Override
        public void didInitialise() {
            super.didInitialise();
            findViewById(R.id.init_btn).setVisibility(View.GONE);
            findViewById(R.id.content_view).setVisibility(View.VISIBLE);
            Log.i(TAG, "didInitialise");
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

            Log.i(TAG, str);
            mLogListAdapter.insert(str, 0);
            findViewById(R.id.init_btn).setEnabled(true);
        }
    }


    public class AdListener extends TMAdListener {
        @Override
        public void didLoad() {
            updateView();
            Log.i(TAG, "didLoad");
            mLogListAdapter.insert("didLoad", 0);
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            updateView();

            String str = String.format(Locale.ENGLISH, "didFailToLoad: %d - %s", error.getErrorCode(), error.getErrorMessage());

            for (String key : error.getSubErrors().keySet()) {
                TMAdError value = error.getSubErrors().get(key);
                String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                str = str.concat("\n ");
                str = str.concat(subError);
            }

            Log.i(TAG, str);
            mLogListAdapter.insert(str, 0);
        }

        @Override
        public void willDisplay() {
            Log.i(TAG, "willDisplay");
            mLogListAdapter.insert("willDisplay", 0);
        }

        @Override
        public void didDisplay() {
            Log.i(TAG, "didDisplay");
            mLogListAdapter.insert("didDisplay", 0);
        }

        @Override
        public void didClick() {
            Log.i(TAG, "didClick");
            mLogListAdapter.insert("didClick", 0);
        }

        @Override
        public void didClose() {
            updateView();
            Log.i(TAG, "didClose");
            mLogListAdapter.insert("didClose", 0);
        }
    }
}

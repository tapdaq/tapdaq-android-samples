package com.tapdaq.bootupinterstitialsample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.TapdaqPlacement;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.debug.TMDebugAdapter;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.model.rewards.TDReward;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "TAPDAQ-SAMPLE";

    private String mAppId = "";
    private String mClientKey = "";

    private String mPlacementTag = TapdaqPlacement.TDPTagDefault;

    private TMDebugAdapter mLogListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Logger
        mLogListAdapter = new TMDebugAdapter(this, new ArrayList<String>());
        ((ListView)findViewById(R.id.log_listview)).setAdapter(mLogListAdapter);

        //Setup Buttons
        findViewById(R.id.show_debug_btn).setOnClickListener(new ClickShowDebug());

        //Initialise Tapdaq
        TapdaqConfig config = new TapdaqConfig();

        mLogListAdapter.insert("Initialise Tapdaq", 0);
        Tapdaq.getInstance().initialize(this, mAppId, mClientKey, config, new InitListener());
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

    private class InitListener extends TMInitListener
    {
        @Override
        public void didInitialise() {
            super.didInitialise();
            Log.i(TAG, "didInitialise");
            mLogListAdapter.insert("didInitialise", 0);

            Tapdaq.getInstance().loadInterstitial(MainActivity.this, new AdListener());
        }

        @Override
        public void didFailToInitialise(TMAdError error) {
            super.didFailToInitialise(error);

            String str = String.format(Locale.ENGLISH, "didFailToInitialise: %d - %s", error.getErrorCode(), error.getErrorMessage());

            for (String key : error.getSubErrors().keySet()) {
                for (TMAdError value : error.getSubErrors().get(key)) {
                    String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                    str = str.concat("\n ");
                    str = str.concat(subError);
                }
            }

            Log.i(TAG, str);
            mLogListAdapter.insert(str, 0);
        }
    }

    public class AdListener extends TMAdListener {
        @Override
        public void didLoad() {
            Log.i(TAG, "didLoad");
            mLogListAdapter.insert("didLoad", 0);

            Tapdaq.getInstance().showInterstitial(MainActivity.this, mPlacementTag, this);
        }

        @Override
        public void didFailToLoad(TMAdError error) {

            String str = String.format(Locale.ENGLISH, "didFailToLoad: %d - %s", error.getErrorCode(), error.getErrorMessage());

            for (String key : error.getSubErrors().keySet()) {
                for (TMAdError value : error.getSubErrors().get(key)) {
                    String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                    str = str.concat("\n ");
                    str = str.concat(subError);
                }
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
        public void didVerify(TDReward reward) {
            String str = String.format(Locale.ENGLISH, "didVerify: Reward name: %s. Value: %d. Valid: %b. Custom Json: %s", reward.getName(), reward.getValue(), reward.isValid(), reward.getCustom_json().toString());
            Log.i(TAG, str);
            mLogListAdapter.insert(str, 0);
        }

        @Override
        public void didClose() {
            Log.i(TAG, "didClose");
            mLogListAdapter.insert("didClose", 0);
        }
    }
}

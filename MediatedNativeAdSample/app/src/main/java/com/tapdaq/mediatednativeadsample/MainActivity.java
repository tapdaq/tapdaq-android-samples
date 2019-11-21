package com.tapdaq.mediatednativeadsample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAd;
import com.tapdaq.sdk.adnetworks.TDMediatedNativeAdOptions;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.debug.TMDebugAdapter;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMAdListener;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "TAPDAQ-SAMPLE";

    private String mAppId = "";
    private String mClientKey = "";

    private TextView mPlacementTagTextView;

    private TMDebugAdapter mLogListAdapter;

    private Map<String, TDMediatedNativeAd> mAd = new HashMap<>();

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
        findViewById(R.id.clear_btn).setOnClickListener(new ClickClearAd());

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
    }

    private String getPlacementTag() {
        return mPlacementTagTextView.getText().toString();
    }

    private void updateView() {
        if (mAd.containsKey(getPlacementTag())) {
            findViewById(R.id.show_btn).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.show_btn).setVisibility(View.GONE);
        }
    }

    private class ClickInitialise implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            //Configuration
            TapdaqConfig config = new TapdaqConfig();
            config.setAutoReloadAds(true);

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
        public void onClick(View view) {
            TDMediatedNativeAdOptions options = new TDMediatedNativeAdOptions(); //optional param
            Tapdaq.getInstance().loadMediatedNativeAd(MainActivity.this, getPlacementTag(), options, new AdListener());
        }
    }

    private class ClickShowAd implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            NativeAdLayout adlayout = findViewById(R.id.native_ad);
            adlayout.populate(mAd.get(getPlacementTag()));
            mAd.remove(getPlacementTag());
            findViewById(R.id.clear_btn).setVisibility(View.VISIBLE);
            updateView();
        }
    }

    private class ClickClearAd implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            NativeAdLayout adlayout = findViewById(R.id.native_ad);
            adlayout.clear();
            findViewById(R.id.clear_btn).setVisibility(View.GONE);
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
                for (TMAdError value : error.getSubErrors().get(key)) {
                    String subError = String.format(Locale.ENGLISH, "%s - %d: %s", key,  value.getErrorCode(), value.getErrorMessage());
                    str = str.concat("\n ");
                    str = str.concat(subError);
                }
            }

            Log.i(TAG, str);
            mLogListAdapter.insert(str, 0);
            findViewById(R.id.init_btn).setEnabled(true);
        }
    }

    private class AdListener extends TMAdListener {
        @Override
        public void didLoad(TDMediatedNativeAd ad) {
            super.didLoad(ad);
            mAd.put(getPlacementTag(), ad);
            updateView();
            Log.i(TAG, "didLoad");
            mLogListAdapter.insert("didLoad", 0);
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            updateView();

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
        public void didDisplay() {
            Log.i(TAG, "didDisplay");
            mLogListAdapter.insert("didDisplay", 0);
        }

        @Override
        public void didClick() {
            Log.i(TAG, "didClick");
            mLogListAdapter.insert("didClick", 0);
        }


    }
}

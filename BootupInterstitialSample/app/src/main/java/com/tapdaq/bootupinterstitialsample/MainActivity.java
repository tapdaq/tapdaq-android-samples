package com.tapdaq.bootupinterstitialsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.ads.TapdaqPlacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TapdaqConfig config = new TapdaqConfig();

        List<TapdaqPlacement> enabledPlacements = new ArrayList<TapdaqPlacement>();
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE), TapdaqPlacement.TDPTagDefault));

        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", config, new InitListener(this));
    }
}

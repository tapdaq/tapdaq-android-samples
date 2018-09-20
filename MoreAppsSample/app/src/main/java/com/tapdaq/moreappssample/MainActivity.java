package com.tapdaq.moreappssample;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.ads.TapdaqPlacement;
import com.tapdaq.sdk.common.TMAdError;
import com.tapdaq.sdk.listeners.TMInitListener;
import com.tapdaq.sdk.moreapps.TMMoreAppsConfig;
import com.tapdaq.sdk.moreapps.TMMoreAppsListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MoreAppsListener mMoreAppsListener = new MoreAppsListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_moreAppsBtn).setOnClickListener(new OnClickLoadMoreApps());
        findViewById(R.id.show_moreapps_btn).setOnClickListener(new OnClickShowMoreApps());

        String mApplicationId = "";
        String mClientKey = "";

        List<TapdaqPlacement> placements = new ArrayList<>();
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-1"));
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-2"));
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-3"));
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-4"));
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-5"));
        placements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_MEDIUM), "tray-position-backfill"));

        TapdaqConfig config = new TapdaqConfig();
        config.withPlacementTagSupport(placements.toArray(new TapdaqPlacement[placements.size()]));

        Tapdaq.getInstance().initialize(this, mApplicationId, mClientKey, config, new TMInitListener() {
            @Override
            public void didInitialise() {
                super.didInitialise();
                findViewById(R.id.load_moreAppsBtn).setVisibility(View.VISIBLE);
            }

            @Override
            public void didFailToInitialise(TMAdError error) {
                super.didFailToInitialise(error);
            }
        });
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

    private class OnClickLoadMoreApps implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Tapdaq.getInstance().loadMoreApps(MainActivity.this, getCustomMoreAppsConfig(), mMoreAppsListener);
        }
    }

    private class OnClickShowMoreApps implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if (Tapdaq.getInstance().isMoreAppsReady(MainActivity.this)) {
                Tapdaq.getInstance().showMoreApps(MainActivity.this, mMoreAppsListener);
            }
        }
    }

    private class MoreAppsListener extends TMMoreAppsListener {
        @Override
        public void didLoad() {
            super.didLoad();

            if (Tapdaq.getInstance().isMoreAppsReady(MainActivity.this)) {
                findViewById(R.id.show_moreapps_btn).setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void didFailToLoad(TMAdError error) {
            super.didFailToLoad(error);
            Toast.makeText(MainActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void willDisplay() {
            super.willDisplay();
        }

        @Override
        public void didDisplay() {
            super.didDisplay();
        }

        @Override
        public void didClose() {
            super.didClose();
        }
    }
}

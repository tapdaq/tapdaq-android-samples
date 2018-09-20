package com.tapdaq.nativeadsample;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.tapdaq.sdk.CreativeType;
import com.tapdaq.sdk.TMNativeAd;
import com.tapdaq.sdk.Tapdaq;
import com.tapdaq.sdk.TapdaqConfig;
import com.tapdaq.sdk.ads.TapdaqPlacement;
import com.tapdaq.sdk.helpers.TLog;
import com.tapdaq.sdk.helpers.TLogLevel;
import com.tapdaq.sdk.listeners.TMInitListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.load_native_btn).setOnClickListener(new ClickLoad());
        findViewById(R.id.show_native_btn).setOnClickListener(new ClickShow());

        TLog.setLoggingLevel(TLogLevel.DEBUG);

        //Creatives & Placements
        List<TapdaqPlacement> enabledPlacements = new ArrayList<TapdaqPlacement>();
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE, CreativeType.SQUARE_LARGE), TapdaqPlacement.TDPTagMainMenu));
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.INTERSTITIAL_PORTRAIT, CreativeType.INTERSTITIAL_LANDSCAPE), TapdaqPlacement.TDPTagLevelStart));
        enabledPlacements.add(TapdaqPlacement.createPlacement(Arrays.asList(CreativeType.SQUARE_LARGE), TapdaqPlacement.TDPTagGameOver));

        //Add placements to configuration
        TapdaqConfig config = new TapdaqConfig();
        config.withPlacementTagSupport(enabledPlacements.toArray(new TapdaqPlacement[enabledPlacements.size()]));

        //Initialise the SDK
        Tapdaq.getInstance().initialize(this, "<APP_ID>", "<CLIENT_KEY>", config, new TMInitListener());

    }

    private class ClickLoad implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Tapdaq.getInstance().loadNativeAdvert(MainActivity.this, CreativeType.SQUARE_LARGE, TapdaqPlacement.TDPTagDefault, new AdListener());
        }
    }

    private class ClickShow implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //Get Ad
            TMNativeAd ad = Tapdaq.getInstance().getNativeAdvert(MainActivity.this, CreativeType.SQUARE_LARGE, TapdaqPlacement.TDPTagDefault, new AdListener());

            if (ad != null) {
                ImageView img = (ImageView) findViewById(R.id.native_image);
                Bitmap bmp = ad.getImage(MainActivity.this); //get image
                if (bmp != null) {
                    img.setImageBitmap(bmp); // Set image view
                    img.setOnClickListener(new ClickAd(ad)); // Add click handler
                    ad.triggerDisplay(view.getContext()); //Send impression stat
                } else {
                    //Error - Image not available. Retry loading ad
                }
            } else {
                //Error - No Ad
            }
        }
    }

    private class ClickAd implements View.OnClickListener {

        private TMNativeAd mAd;

        private ClickAd(TMNativeAd ad) {
            mAd = ad;
        }
        @Override
        public void onClick(View view) {
            mAd.triggerClick(view.getContext()); // Handle click event (Send stat & open url)
        }
    }
}

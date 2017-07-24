package com.app.monitormymortgage;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import io.fabric.sdk.android.Fabric;
import io.intercom.android.sdk.Intercom;

/**
 * Created by Sangram on 22/04/16.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Iconify.with(new FontAwesomeModule());

        // Intercom
        Intercom.initialize(this, "android_sdk-cb75724a3cd71ff90e6a23d2b38c5701610767c1", "c12nxg29");
        Intercom.client().setInAppMessageVisibility(Intercom.Visibility.GONE);

//        Intercom.client().registerUnidentifiedUser();
//        Intercom.client().registerIdentifiedUser(Registration.create());


        Log.i("Intercom", "Initialized");
    }
}

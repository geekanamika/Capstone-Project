package com.example.android.udacitycapstoneproject;

import android.app.Application;
import android.content.Context;

import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public class MyApp extends Application {

    private static MyApp mInstance;
    public static Context getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = (MyApp) getApplicationContext();
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }
}

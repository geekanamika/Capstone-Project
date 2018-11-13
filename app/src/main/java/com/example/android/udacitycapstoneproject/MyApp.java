package com.example.android.udacitycapstoneproject;

import android.app.Application;
import android.content.Context;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.di.components.ApplicationComponent;
import com.example.android.udacitycapstoneproject.di.components.DaggerApplicationComponent;
import com.example.android.udacitycapstoneproject.di.modules.ApplicationModule;

import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public class MyApp extends Application {

    private static MyApp mInstance;
    protected ApplicationComponent applicationComponent;

    public static Context getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = (MyApp) getApplicationContext();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);
        if (BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public ApplicationComponent getComponent(){
        return applicationComponent;
    }
}

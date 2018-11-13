package com.example.android.udacitycapstoneproject.di.modules;

import android.app.Activity;
import android.content.Context;

import com.example.android.udacitycapstoneproject.di.qualifiers.ActivityContext;
import com.example.android.udacitycapstoneproject.ui.main.MainActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Module
public class MainActivityModule {
    private MainActivity mActivity;

    public MainActivityModule(MainActivity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }
}

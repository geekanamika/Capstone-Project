package com.example.android.udacitycapstoneproject.di.modules;

import android.content.Context;

import dagger.Module;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Module
public class ContextModule {
    Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context.getApplicationContext();
    }
}

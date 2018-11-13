package com.example.android.udacitycapstoneproject.di.components;

import android.app.Application;
import android.content.Context;

import com.example.android.udacitycapstoneproject.MyApp;
import com.example.android.udacitycapstoneproject.di.modules.ApplicationModule;
import com.example.android.udacitycapstoneproject.di.qualifiers.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(MyApp demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();

//    AppNewsRepository getNewsRespository();

//    @PreferenceInfo
//    SharedPreferences getPreferenceHelper();

//    AppDbHelper getDbHelper();
}

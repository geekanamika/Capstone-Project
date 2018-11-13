package com.example.android.udacitycapstoneproject.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.udacitycapstoneproject.data.local.AppDatabase;
import com.example.android.udacitycapstoneproject.data.local.AppDbHelper;
import com.example.android.udacitycapstoneproject.data.remote.WebService;
import com.example.android.udacitycapstoneproject.di.qualifiers.ApplicationContext;
import com.example.android.udacitycapstoneproject.di.qualifiers.DatabaseInfo;
import com.example.android.udacitycapstoneproject.di.qualifiers.PreferenceInfo;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.example.android.udacitycapstoneproject.utils.AppExecutors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application app) {
        mApplication = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    /**
     * local db dependencies
     */

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 1;
    }

    @Provides
    @Singleton
    AppDatabase provideAppDatabase(@DatabaseInfo String dbName, Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, dbName)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Singleton
    @Provides
    AppExecutors.MainThreadExecutor provideMainThreadExecutor() {
        return new AppExecutors.MainThreadExecutor();
    }

    @Singleton
    @Provides
    AppExecutors provideExecutors(AppExecutors.MainThreadExecutor mainThreadExecutor) {
        return new AppExecutors(Executors.newSingleThreadExecutor(),
                    mainThreadExecutor
                );
    }

    /**
     * shared preference dependencies
     */

    @Provides
    @PreferenceInfo
    SharedPreferences provideSharedPrefs() {
        return mApplication.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     *  network dependencies
     */

    @Provides
    @Singleton
    public WebService provideWebService(Retrofit retrofit) {
        return retrofit.create(WebService.class);
    }

    @Provides
    public Retrofit retrofit(GsonConverterFactory gsonConverterFactory, Gson gson){
        return new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build();
    }

    @Provides
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    public GsonConverterFactory gsonConverterFactory(Gson gson){
        return GsonConverterFactory.create(gson);
    }
}

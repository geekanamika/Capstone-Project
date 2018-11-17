package com.example.android.udacitycapstoneproject.utils;

import android.content.Context;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.AppDatabase;
import com.example.android.udacitycapstoneproject.data.local.AppDbHelper;
import com.example.android.udacitycapstoneproject.data.prefs.AppPrefHelper;
import com.example.android.udacitycapstoneproject.data.remote.AppNetworkSource;

/**
 * Created by Anamika Tripathi on 17/11/18.
 */
public class InjectorUtil {
    public static AppNewsRepository provideRepository(Context context) {
        // local db
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        AppDbHelper dbHelper = AppDbHelper.getInstance(database.favDao(), executors);
        // remote
        AppNetworkSource networkDataSource =
                AppNetworkSource.getInstance();
        // pref
        AppPrefHelper preferenceHelper = new AppPrefHelper(context, "news-pref");

        return AppNewsRepository.getInstance(networkDataSource, preferenceHelper, dbHelper);
    }
}

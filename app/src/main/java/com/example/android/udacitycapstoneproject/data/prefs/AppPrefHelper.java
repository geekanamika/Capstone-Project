package com.example.android.udacitycapstoneproject.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Singleton
public class AppPrefHelper {

    SharedPreferences sharedPreferences;

    @Inject
    AppPrefHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

}

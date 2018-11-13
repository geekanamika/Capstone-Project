package com.example.android.udacitycapstoneproject.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Singleton
public class AppPrefHelper implements IPrefHelper{

    private SharedPreferences sharedPreferences;
    private final String DEFAULT_CHANNEL_KEY = "DEFAULT-CHANNEL-KEY";

    @Inject
    AppPrefHelper(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getDefaultOrFavChannel() {
        return sharedPreferences.getString(DEFAULT_CHANNEL_KEY, "bbc-sport");
    }

    @Override
    public void setDefaultOrFavChannel(String channel) {
        sharedPreferences.edit().putString(DEFAULT_CHANNEL_KEY, channel).apply();
    }
}

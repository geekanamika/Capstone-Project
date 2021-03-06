package com.example.android.udacitycapstoneproject.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public class AppPrefHelper implements IPrefHelper{

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences prefs;
    private final String DEFAULT_CHANNEL_KEY = "select fav channel";
    private final String IS_FIRST_RUN_KEY = "is first run";
    private final String CURRENT_CHANNEL_KEY = "CURRENT-CHANNEL-KEY";
    public static final String LATEST_TOP_THREE_NEWS_KEY = "LATEST_TOP_THREE_NEWS_KEY";


    public AppPrefHelper(Context context, String prefFileName) {
        sharedPreferences = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public SharedPreferences getMySharedPrefences() {
        return sharedPreferences;
    }

    @Override
    public boolean isFirstRun() {
        return prefs.getBoolean(IS_FIRST_RUN_KEY, true);
    }

    @Override
    public void setFirstRun() {
        prefs.edit().putBoolean(IS_FIRST_RUN_KEY, false).apply();
    }

    @Override
    public String getDefaultOrFavChannel() {
        return prefs.getString(DEFAULT_CHANNEL_KEY, "bbc-sport");
    }

    @Override
    public String getCurrentChannel() {
        return sharedPreferences.getString(CURRENT_CHANNEL_KEY, "bbc-sport");
    }

    @Override
    public void setDefaultOrFavChannel(String channel) {
        sharedPreferences.edit().putString(DEFAULT_CHANNEL_KEY, channel).apply();
    }

    @Override
    public void setCurrentChannel(String channel) {
        sharedPreferences.edit().putString(CURRENT_CHANNEL_KEY, channel).apply();
    }

    @Override
    public String getTopThreeLatestNews() {
        return sharedPreferences.getString(LATEST_TOP_THREE_NEWS_KEY, "");
    }

    @Override
    public void setTopThreeLatestNews(String threeLatestNews) {
        sharedPreferences.edit().putString(LATEST_TOP_THREE_NEWS_KEY, threeLatestNews).apply();
    }
}

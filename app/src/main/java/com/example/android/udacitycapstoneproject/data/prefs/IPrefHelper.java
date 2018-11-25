package com.example.android.udacitycapstoneproject.data.prefs;

import android.content.SharedPreferences;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public interface IPrefHelper {
    String getDefaultOrFavChannel();
    String getCurrentChannel();
    void setDefaultOrFavChannel(String channel);
    void setCurrentChannel(String channel);
    String getTopThreeLatestNews();
    void setTopThreeLatestNews(String threeLatestNews);
    SharedPreferences getMySharedPrefences();
}

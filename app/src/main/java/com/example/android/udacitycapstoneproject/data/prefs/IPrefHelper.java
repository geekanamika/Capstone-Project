package com.example.android.udacitycapstoneproject.data.prefs;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public interface IPrefHelper {
    String getDefaultOrFavChannel();
    String getCurrentChannel();
    void setDefaultOrFavChannel(String channel);
    void setCurrentFavChannel(String channel);
    String getTopThreeLatestNews();
    void setTopThreeLatestNews(String threeLatestNews);
}

package com.example.android.udacitycapstoneproject.data;

import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;

import com.example.android.udacitycapstoneproject.data.local.AppDbHelper;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.prefs.AppPrefHelper;
import com.example.android.udacitycapstoneproject.data.remote.AppNetworkSource;

import java.util.List;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
public class AppNewsRepository implements INewsRepository {

    private AppDbHelper dbHelper;
    private AppPrefHelper prefHelper;
    private AppNetworkSource networkHelper;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNewsRepository sInstance;

    private AppNewsRepository(AppDbHelper dbHelper, AppPrefHelper helper, AppNetworkSource networkHelper) {
        this.dbHelper = dbHelper;
        this.prefHelper = helper;
        this.networkHelper = networkHelper;
    }


    public synchronized static AppNewsRepository getInstance(
            AppNetworkSource networkDataSource,
            AppPrefHelper preferenceHelper, AppDbHelper dbHelper) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNewsRepository(dbHelper,preferenceHelper,networkDataSource);
            }
        }
        return sInstance;
    }


    // db methods
    @Override
    public void insertFavouriteNews(Article fav) {
        dbHelper.insertFavouriteNews(fav);
    }

    @Override
    public void removeFromFavourite(String id) {
        dbHelper.removeFromFavourite(id);
    }

    @Override
    public LiveData<Integer> checkIfMovieIsFavourite(String title) {
        return dbHelper.checkIfMovieIsFavourite(title);
    }

    @Override
    public LiveData<List<Article>> getFavouriteArticles() {
        return dbHelper.getFavouriteArticles();
    }

    @Override
    public LiveData<Article> getFavouriteFirstArticle() {
        return dbHelper.getFavouriteFirstArticle();
    }

    /**
     * network related methods
     */
    @Override
    public LiveData<List<Article>> getTopNewsHeadlines() {
        return networkHelper.getDownloadedNewsArticles();
    }

    @Override
    public void startFetchingData(String sourceOfNews) {
        networkHelper.loadNewsArticles(sourceOfNews);
    }

    @Override
    public LiveData<Boolean> isLoadingData() {
        return networkHelper.getIsLoading();
    }

    @Override
    public String getDefaultOrFavChannel() {
        return prefHelper.getDefaultOrFavChannel();
    }

    @Override
    public String getCurrentChannel() {
        return prefHelper.getCurrentChannel();
    }

    @Override
    public void setDefaultOrFavChannel(String channel) {
        prefHelper.setDefaultOrFavChannel(channel);
    }

    @Override
    public void setCurrentChannel(String channel) {
        prefHelper.setCurrentChannel(channel);
    }

    @Override
    public String getTopThreeLatestNews() {
        return prefHelper.getTopThreeLatestNews();
    }

    @Override
    public void setTopThreeLatestNews(String threeLatestNews) {
        prefHelper.setTopThreeLatestNews(threeLatestNews);
    }

    @Override
    public SharedPreferences getMySharedPrefences() {
        return prefHelper.getMySharedPrefences();
    }

    @Override
    public boolean isFirstRun() {
        return prefHelper.isFirstRun();
    }

    @Override
    public void setFirstRun() {
        prefHelper.setFirstRun();
    }
}

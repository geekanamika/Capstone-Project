package com.example.android.udacitycapstoneproject.data;

import android.arch.lifecycle.LiveData;

import com.example.android.udacitycapstoneproject.data.local.AppDbHelper;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.prefs.AppPrefHelper;
import com.example.android.udacitycapstoneproject.data.remote.AppNetworkSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
@Singleton
public class AppNewsRepository implements INewsRepository {

    private AppDbHelper dbHelper;
    private AppPrefHelper prefHelper;
    private AppNetworkSource networkHelper;

    @Inject
    AppNewsRepository(AppDbHelper dbHelper, AppPrefHelper helper, AppNetworkSource networkHelper) {
        this.dbHelper = dbHelper;
        this.prefHelper = helper;
        this.networkHelper = networkHelper;
    }

    // db methods
    @Override
    public void insertFavouriteNews(Article fav) {
        dbHelper.insertFavouriteNews(fav);
    }

    @Override
    public void removeFromFavourite(int id) {
        dbHelper.removeFromFavourite(id);
    }

    @Override
    public LiveData<Integer> checkIfMovieIsFavourite(int id) {
        return dbHelper.checkIfMovieIsFavourite(id);
    }

    @Override
    public LiveData<List<Article>> getFavouriteArticles() {
        return dbHelper.getFavouriteArticles();
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
}

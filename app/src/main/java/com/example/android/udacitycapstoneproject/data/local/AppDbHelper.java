package com.example.android.udacitycapstoneproject.data.local;

import android.arch.lifecycle.LiveData;

import com.example.android.udacitycapstoneproject.data.local.dao.FavDao;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.AppExecutors;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class AppDbHelper implements IDbHelper {

    private final FavDao favDao;
    private final AppExecutors mExecutors;
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppDbHelper sInstance;

    private AppDbHelper(FavDao dao,
                        AppExecutors executors) {
        favDao = dao;
        mExecutors = executors;
    }

    public synchronized static AppDbHelper getInstance(
            FavDao favDao,
            AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppDbHelper(favDao,
                        executors);
            }
        }
        return sInstance;
    }


    @Override
    public void insertFavouriteNews(final Article fav) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                favDao.insertNewsMovie(fav);
            }
        };
        mExecutors.diskIO().execute(runnable);
    }

    @Override
    public void removeFromFavourite(final String id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                favDao.deleteArticleMovie(id);
            }
        };
        mExecutors.diskIO().execute(runnable);
    }

    @Override
    public LiveData<Integer> checkIfMovieIsFavourite(String id) {
        return favDao.isFavourite(id);
    }

    @Override
    public LiveData<List<Article>> getFavouriteArticles() {
        return favDao.getFavouritesNewsList();
    }
}

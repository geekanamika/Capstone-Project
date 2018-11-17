package com.example.android.udacitycapstoneproject.data.local;

import android.arch.lifecycle.LiveData;

import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.AppExecutors;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class AppDbHelper implements IDbHelper {

    private final AppDatabase mAppDatabase;
    private final AppExecutors executors;

    AppDbHelper(AppDatabase appDatabase, AppExecutors executors) {
        this.mAppDatabase = appDatabase;
        this.executors = executors;
    }

    @Override
    public void insertFavouriteNews(final Article fav) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favDao().insertNewsMovie(fav);
            }
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public void removeFromFavourite(final int id) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mAppDatabase.favDao().deleteArticleMovie(id);
            }
        };
        executors.diskIO().execute(runnable);
    }

    @Override
    public LiveData<Integer> checkIfMovieIsFavourite(int id) {
        return mAppDatabase.favDao().isFavourite(id);
    }

    @Override
    public LiveData<List<Article>> getFavouriteArticles() {
        return mAppDatabase.favDao().getFavouritesNewsList();
    }
}

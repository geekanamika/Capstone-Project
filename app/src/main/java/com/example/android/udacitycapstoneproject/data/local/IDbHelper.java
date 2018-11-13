package com.example.android.udacitycapstoneproject.data.local;

import android.arch.lifecycle.LiveData;

import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public interface IDbHelper {

    void insertFavouriteNews(Article fav);
    void removeFromFavourite(int id);
    LiveData<Integer> checkIfMovieIsFavourite(int id);
    LiveData<List<Article>> getFavouriteArticles();
}

package com.example.android.udacitycapstoneproject.data.local.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
@Dao
public interface FavDao {
    @Query("SELECT * FROM news_favourites")
    LiveData<List<Article>> getFavouritesNewsList();

    @Query("SELECT COUNT(*) FROM news_favourites")
    LiveData<Integer> getFavouritesNewsListSize();

    @Query("SELECT COUNT(id) FROM news_favourites WHERE title = :title")
    LiveData<Integer> isFavourite(String title);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNewsMovie(Article article);

    @Query("DELETE FROM news_favourites WHERE title = :movieTitle")
    void deleteArticleMovie(String movieTitle);

    @Query("DELETE FROM news_favourites")
    void deleteAllArticles();
}

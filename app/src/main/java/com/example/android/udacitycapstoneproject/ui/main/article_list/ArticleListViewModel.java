package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
class ArticleListViewModel extends ViewModel {

    private LiveData<List<Article>> newsNetworkLiveData;
    private LiveData<List<Article>> favouriteNewsData;
    AppNewsRepository repository;

    public ArticleListViewModel() {
        newsNetworkLiveData = repository.getTopNewsHeadlines();
        favouriteNewsData = repository.getFavouriteArticles();
        startFetchingData(repository.getDefaultOrFavChannel());
    }

    //Todo get current channel instead of bbc-news always
    String getCurrentChannel() {
        return repository.getDefaultOrFavChannel();
    }

    void startFetchingData(String source) {
        repository.startFetchingData(source);
    }

    LiveData<List<Article>> getNewsNetworkLiveData() {
        return newsNetworkLiveData;
    }

    public LiveData<List<Article>> getFavouriteNewsData() {
        return favouriteNewsData;
    }
}

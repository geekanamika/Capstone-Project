package com.example.android.udacitycapstoneproject.ui.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
class MainActivityViewModel extends ViewModel {

    private LiveData<List<Article>> newsNetworkLiveData;
    private LiveData<List<Article>> favouriteNewsData;
    @Inject
    AppNewsRepository repository;

    public MainActivityViewModel() {
        newsNetworkLiveData = repository.getTopNewsHeadlines();
        favouriteNewsData = repository.getFavouriteArticles();
        startFetchingData(repository.getDefaultOrFavChannel());
    }

    private void startFetchingData(String source) {
        repository.startFetchingData(source);
    }

    public LiveData<List<Article>> getNewsNetworkLiveData() {
        return newsNetworkLiveData;
    }

    public LiveData<List<Article>> getFavouriteNewsData() {
        return favouriteNewsData;
    }
}

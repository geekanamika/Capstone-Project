package com.example.android.udacitycapstoneproject.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;

import java.util.List;

import timber.log.Timber;


/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class SharedViewModel extends AndroidViewModel {

    private AppNewsRepository repository;
    private LiveData<List<Article>> newsNetworkLiveData;
    private final MutableLiveData<String> channel;
    private final MutableLiveData<Article> articleMutableLiveData;

    public SharedViewModel(@NonNull Application application) {
        super(application);
        repository = InjectorUtil.provideRepository(application.getApplicationContext());
        newsNetworkLiveData = repository.getTopNewsHeadlines();
        channel = new MutableLiveData<>();
        articleMutableLiveData = new MutableLiveData<>();
        channel.postValue(repository.getCurrentChannel());
    }

    public LiveData<Article> getArticleMutableLiveData() {
        return articleMutableLiveData;
    }

    public void setArticleMutableLiveData(Article article) {
        articleMutableLiveData.postValue(article);
    }

    public void startFetchingData(String source) {
        repository.startFetchingData(source);
    }

    public LiveData<List<Article>> getNewsNetworkLiveData() {
        return newsNetworkLiveData;
    }

    public LiveData<String> getChannel() {
        return channel;
    }

    void setChannel(String channel) {
        this.channel.postValue(channel);
        Timber.d("channel in view-model is " + channel);
    }

    String getDefaultOrFavChannel() {
        return repository.getDefaultOrFavChannel();
    }

    public void saveFavouriteNews(Article article) {
        repository.insertFavouriteNews(article);
    }

    public LiveData<Integer> isFavourite(String title) {
        return repository.checkIfMovieIsFavourite(title);
    }

    public void removeFromFav(String title) {
        repository.removeFromFavourite(title);
    }

    public LiveData<Article> getFirstFavArticle() {
        return repository.getFavouriteFirstArticle();
    }

    public String getFavouriteChannel() {
        return repository.getDefaultOrFavChannel();
    }

    public void setCurrentChannel(String channel) {
        repository.setCurrentChannel(channel);
    }
}

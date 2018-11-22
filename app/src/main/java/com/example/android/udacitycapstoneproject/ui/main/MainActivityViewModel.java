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


/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private AppNewsRepository repository;
    private LiveData<List<Article>> newsNetworkLiveData;
    private final MutableLiveData<String> channel;
    private final MutableLiveData<Article> articleMutableLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = InjectorUtil.provideRepository(application.getApplicationContext());
        newsNetworkLiveData = repository.getTopNewsHeadlines();
        channel = new MutableLiveData<>();
        articleMutableLiveData = new MutableLiveData<>();
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

    public void setChannel(String channel) {
        this.channel.postValue(channel);
    }

    String getDefaultOrFavChannel() {
        return repository.getDefaultOrFavChannel();
    }
}

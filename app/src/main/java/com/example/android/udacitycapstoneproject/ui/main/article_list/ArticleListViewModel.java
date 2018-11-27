package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class ArticleListViewModel extends AndroidViewModel {

    private LiveData<List<Article>> newsNetworkLiveData;
    private AppNewsRepository repository;
    private final MutableLiveData<String> currentChannel;

    public ArticleListViewModel(@NonNull Application application) {
        super(application);

        repository = InjectorUtil.provideRepository(application.getApplicationContext());
        newsNetworkLiveData = repository.getTopNewsHeadlines();
        currentChannel = new MutableLiveData<>();
        setCurrentChannel(getChannelFromRepo());
    }

    void startFetchingData(String source) {
        repository.startFetchingData(source);
    }

    LiveData<List<Article>> getNewsNetworkLiveData() {
        return newsNetworkLiveData;
    }

    public LiveData<String> getCurrentChannel() {
        return currentChannel;
    }

    public void setCurrentChannel(String channel) {
        this.currentChannel.postValue(channel);
    }
    public String getFavouriteChannel() {
        return repository.getDefaultOrFavChannel();
    }

    public String getChannelFromRepo(){
        return repository.getCurrentChannel();
    }

    public SharedPreferences getSharedPreferences(){
        return repository.getMySharedPrefences();
    }

    public boolean isFirstRun() {
        return repository.isFirstRun();
    }

    public void setFirstRun() {
        repository.setFirstRun();
    }
}

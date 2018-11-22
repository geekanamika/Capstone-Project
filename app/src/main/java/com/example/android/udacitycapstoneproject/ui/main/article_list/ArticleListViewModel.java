package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;

import java.util.List;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
class ArticleListViewModel extends AndroidViewModel {

    private LiveData<List<Article>> newsNetworkLiveData;
    private AppNewsRepository repository;
    private String channel;

    public ArticleListViewModel(@NonNull Application application) {
        super(application);

        repository = InjectorUtil.provideRepository(application.getApplicationContext());
        newsNetworkLiveData = repository.getTopNewsHeadlines();
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
        startFetchingData(channel);
    }
}

package com.example.android.udacitycapstoneproject.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class AppNetworkSource {

    private WebService webService;
    // mutable list which contains values from network source
    private final MutableLiveData<List<Article>> mDownloadedNewsArticles;
    // checks about loading status & helps in loading indicator
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();


    AppNetworkSource(WebService webService) {
        this.webService = webService;
        mDownloadedNewsArticles = new MutableLiveData<>();
    }

    public LiveData<List<Article>> getDownloadedNewsArticles() {
        return mDownloadedNewsArticles;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadNewsArticles(String source) {
        isLoading.postValue(true);
        Call<NewsResponse> newsResponse = webService.loadTopHeadlines(source, BuildConfig.NewsApiKey);
        newsResponse.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful()) {
                    // posting value to the live data
                    mDownloadedNewsArticles.postValue(response.body().getArticles());
                    isLoading.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Timber.e("onFailure called inside retrofit loading");
            }
        });

    }
}

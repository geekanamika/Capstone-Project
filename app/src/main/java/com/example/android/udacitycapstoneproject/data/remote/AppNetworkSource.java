package com.example.android.udacitycapstoneproject.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class AppNetworkSource {

    private WebService webService;
    // mutable list which contains values from network source
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNetworkSource sInstance;

    // mutable list which contains values from network source
    private final MutableLiveData<List<Article>> mDownloadedNewsArticles;
    // checks about loading status & helps in loading indicator
    private MutableLiveData<Boolean> isLoading;


    private AppNetworkSource() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webService = retrofit.create(WebService.class);
        mDownloadedNewsArticles = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    /**
     * Get the singleton for this class
     */
    public static AppNetworkSource getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNetworkSource();
            }
        }
        return sInstance;
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

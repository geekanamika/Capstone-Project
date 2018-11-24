package com.example.android.udacitycapstoneproject.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.MyApp;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.example.android.udacitycapstoneproject.utils.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static com.example.android.udacitycapstoneproject.utils.AppConstants.BASE_URL;
import static com.example.android.udacitycapstoneproject.utils.AppConstants.HEADER_CACHE_CONTROL;
import static com.example.android.udacitycapstoneproject.utils.AppConstants.HEADER_PRAGMA;

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
    private Retrofit retrofit;


    private AppNetworkSource() {
        if(retrofit == null) {
            retrofit = getRetrofit();
        }

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
                    for (int i = 0; i < response.body().getArticles().size(); i++) {
                        Timber.d(i + " " + response.body().getArticles().get(i).getTitle());
                    }
                    isLoading.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Timber.e("onFailure called inside retrofit loading");
            }
        });

    }

    private Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                CacheControl cacheControl;
                if (NetworkUtil.isNetworkAvailable()) {
                    cacheControl = new CacheControl.Builder()
                            .maxAge(0, TimeUnit.SECONDS)
                            .build();
                } else {
                    cacheControl = new CacheControl.Builder()
                            .maxStale(14, TimeUnit.DAYS)
                            .build();
                }

                return originalResponse.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!NetworkUtil.isNetworkAvailable()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }

    private Cache provideCache() {
        Cache cache = null;

        try {
            cache = new Cache(new File(MyApp.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
           Timber.e("Could not create Cache!");
        }

        return cache;
    }

    public Retrofit getRetrofit() {
        // Add all interceptors you want (headers, URL, logging, stetho logs)
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache());

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                // Add your adapter factory to handler Errors
                .client(httpClient.build())
                .build();
    }

    public void loadFromCache() {

    }
}

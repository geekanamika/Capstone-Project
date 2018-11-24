package com.example.android.udacitycapstoneproject.worker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;
import com.example.android.udacitycapstoneproject.data.remote.AppNetworkSource;
import com.example.android.udacitycapstoneproject.data.remote.WebService;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 23/11/18.
 */
public class SyncNewsWorker extends Worker {
    public SyncNewsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Retrofit retrofit = AppNetworkSource.getInstance().getRetrofit();
        WebService webService = retrofit.create(WebService.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String channel = preferences.getString("select fav channel", "bbc-sport");
        Timber.d(channel);
        Call<NewsResponse> newsResponseCall = webService.loadTopHeadlines(channel, BuildConfig.NewsApiKey);
        try {
            Response<NewsResponse> myNewsResponse = newsResponseCall.execute();
            if (myNewsResponse.code() == 200) {
                NewsResponse data = myNewsResponse.body();
                List<Article> articleList = data.getArticles();
                for (int i = 0; i < articleList.size(); i++) {
                    Timber.d(articleList.get(i).getTitle());
                }
            } else {
                return Result.RETRY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }
}

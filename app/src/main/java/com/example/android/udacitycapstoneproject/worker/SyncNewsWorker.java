package com.example.android.udacitycapstoneproject.worker;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.example.android.udacitycapstoneproject.BuildConfig;
import com.example.android.udacitycapstoneproject.MyApp;
import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;
import com.example.android.udacitycapstoneproject.data.remote.AppNetworkSource;
import com.example.android.udacitycapstoneproject.data.remote.WebService;
import com.example.android.udacitycapstoneproject.ui.widgets.NewsWidget;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

import static com.example.android.udacitycapstoneproject.utils.AppConstants.LABEL_NEWS;

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
        String channel;
        AppNewsRepository newsRepository = InjectorUtil.provideRepository(MyApp.getInstance());
        channel = newsRepository.getDefaultOrFavChannel();
        Timber.d(channel);
        Call<NewsResponse> newsResponseCall = webService.loadTopHeadlines(channel, BuildConfig.NewsApiKey);
        try {
            Response<NewsResponse> myNewsResponse = newsResponseCall.execute();
            if (myNewsResponse.code() == 200) {
                NewsResponse data = myNewsResponse.body();
                String latestTopThreeNews = getTopThreeLatestNews(data.getArticles());
                newsRepository.setTopThreeLatestNews(latestTopThreeNews);
                updateWidgetMethod(latestTopThreeNews);
            } else {
                return Result.RETRY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }


    private String getTopThreeLatestNews(List<Article> articleList) {
        StringBuilder sb = new StringBuilder("Latest News");
        for (int i = 0; i < articleList.size() && i<3 ; i++) {
            sb.append("\n");
            sb.append(articleList.get(i).getTitle());
        }
        return sb.toString();
    }

    private void updateWidgetMethod(String topThreeNews) {

        AppWidgetManager manager = AppWidgetManager.getInstance(MyApp.getInstance());
        RemoteViews remoteViews = new RemoteViews(MyApp.getInstance().getPackageName(),
                R.layout.latest_news_widget);
        ComponentName componentName = new ComponentName(MyApp.getInstance(), NewsWidget.class);
        remoteViews.setTextViewText(R.id.news_text, topThreeNews);
        manager.updateAppWidget(componentName, remoteViews);
    }


}

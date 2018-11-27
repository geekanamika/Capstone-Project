package com.example.android.udacitycapstoneproject.sync;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.AsyncTask;
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
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 27/11/18.
 */
public class MyJobService extends JobService {
    private AsyncTask mBackgroundTask;
    private AppNewsRepository newsRepository;

    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Retrofit retrofit = AppNetworkSource.getInstance().getRetrofit();
                WebService webService = retrofit.create(WebService.class);
                String channel;
                newsRepository = InjectorUtil.provideRepository(MyApp.getInstance());
                channel = newsRepository.getDefaultOrFavChannel();
                Timber.d(channel);
                Call<NewsResponse> newsResponseCall = webService.loadTopHeadlines(channel, BuildConfig.NewsApiKey);
                Response<NewsResponse> myNewsResponse;
                try {
                    Timber.d("inside on start job");
                    myNewsResponse = newsResponseCall.execute();
                    if (myNewsResponse.code() == 200) {
                        Timber.d("job successful");
                        return myNewsResponse.body();
                    } else {
                        Timber.d("job unsuccessful");
                    }
                } catch (IOException e) {
                    Timber.e("IO-exception for retrofit call");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                if(o == null) {
                    jobFinished(jobParameters, true);
                } else {
                    NewsResponse data = (NewsResponse) o;
                    jobFinished(jobParameters, false);
                    String latestTopThreeNews = getTopThreeLatestNews(data.getArticles());
                    newsRepository.setTopThreeLatestNews(latestTopThreeNews);
                    updateWidgetMethod(latestTopThreeNews);
                }
            }
        };

        mBackgroundTask.execute();
        return true;
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
        Timber.d("updated widget");
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }
}

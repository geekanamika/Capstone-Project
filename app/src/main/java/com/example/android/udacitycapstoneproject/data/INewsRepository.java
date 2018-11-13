package com.example.android.udacitycapstoneproject.data;

import android.arch.lifecycle.LiveData;

import com.example.android.udacitycapstoneproject.data.local.IDbHelper;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.data.prefs.IPrefHelper;

import java.util.List;

/**
 * Created by Anamika Tripathi on 12/11/18.
 */
interface INewsRepository extends IDbHelper, IPrefHelper {
    LiveData<List<Article>> getTopNewsHeadlines();
    void startFetchingData(String sourceOfNews);
    public LiveData<Boolean> isLoadingData();


}

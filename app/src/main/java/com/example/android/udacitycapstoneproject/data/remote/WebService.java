package com.example.android.udacitycapstoneproject.data.remote;

import com.example.android.udacitycapstoneproject.data.local.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public interface WebService {

    //top-headlines?sources=bbc-sport&apiKey=
    //everything?sources=bbc-sport&apiKey=
    @GET("top-headlines")
    Call<NewsResponse> loadTopHeadlines(@Query("sources") String source,
                                        @Query("api_key") String api_key);
}

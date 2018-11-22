package com.example.android.udacitycapstoneproject.ui.favourites;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;

import java.util.List;

/**
 * Created by Anamika Tripathi on 22/11/18.
 */
public class FavouriteViewmodel extends AndroidViewModel {

    private AppNewsRepository repository;
    public FavouriteViewmodel(@NonNull Application application) {
        super(application);
        repository = InjectorUtil.provideRepository(application.getApplicationContext());

    }

    public LiveData<List<Article>> getFavouriteMovies() {
        return repository.getFavouriteArticles();
    }

}

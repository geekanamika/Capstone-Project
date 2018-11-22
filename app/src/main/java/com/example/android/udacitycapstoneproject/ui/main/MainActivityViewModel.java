package com.example.android.udacitycapstoneproject.ui.main;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;
import com.example.android.udacitycapstoneproject.utils.InjectorUtil;


/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private AppNewsRepository repository;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = InjectorUtil.provideRepository(application.getApplicationContext());
    }

    String getDefaultOrFavChannel() {
        return repository.getDefaultOrFavChannel();
    }
}

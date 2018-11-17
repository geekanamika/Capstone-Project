package com.example.android.udacitycapstoneproject.ui.main;

import android.arch.lifecycle.ViewModel;

import com.example.android.udacitycapstoneproject.data.AppNewsRepository;


/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class MainActivityViewModel extends ViewModel {
    //@Inject
    AppNewsRepository repository;
    MainActivityViewModel(){

    }
}

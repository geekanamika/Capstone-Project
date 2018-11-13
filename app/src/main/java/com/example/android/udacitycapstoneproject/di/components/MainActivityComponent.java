package com.example.android.udacitycapstoneproject.di.components;

import com.example.android.udacitycapstoneproject.di.modules.MainActivityModule;
import com.example.android.udacitycapstoneproject.di.qualifiers.MainActivityScope;
import com.example.android.udacitycapstoneproject.ui.main.MainActivity;

import dagger.Component;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
@MainActivityScope
@Component(modules = MainActivityModule.class, dependencies = ApplicationComponent.class)
public interface MainActivityComponent {
    void injectMainActivity(MainActivity mainActivity);
}

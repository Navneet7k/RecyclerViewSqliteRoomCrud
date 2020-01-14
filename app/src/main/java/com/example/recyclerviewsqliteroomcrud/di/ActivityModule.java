package com.example.recyclerviewsqliteroomcrud.di;

import com.example.recyclerviewsqliteroomcrud.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}

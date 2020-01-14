package com.example.recyclerviewsqliteroomcrud.di;

import androidx.lifecycle.ViewModel;

import com.example.recyclerviewsqliteroomcrud.view.MainActivityVM;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityVM.class)
    abstract ViewModel bindMainActivityVM(MainActivityVM mainActivityVM);
}

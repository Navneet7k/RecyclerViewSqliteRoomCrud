package com.example.recyclerviewsqliteroomcrud.di;

import android.app.Application;
import android.content.Context;

import com.example.recyclerviewsqliteroomcrud.persistance.NotesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ViewModelModule.class})
public class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    NotesRepository provideNotesRepository(Context context){
        return new NotesRepository(context);
    }
}

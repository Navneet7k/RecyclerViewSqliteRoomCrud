package com.example.recyclerviewsqliteroomcrud;

import android.app.Application;

import com.example.recyclerviewsqliteroomcrud.di.ActivityModule;
import com.example.recyclerviewsqliteroomcrud.di.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityModule.class}
)
public interface AppComponent {

    void inject(BaseApplication app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

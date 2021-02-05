package zingmyorder.kitchen.mobile.di;

import android.app.Application;

import zingmyorder.kitchen.mobile.ApplicationHome;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        ActivityModule.class,AppModule.class,ActivityModule.class,NetworkModule.class}
)
public interface AppComponent {

    void inject(ApplicationHome app);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

package zingmyorder.kitchen.mobile.di;

import android.app.Application;
import android.content.Context;

import zingmyorder.kitchen.mobile.ApiInterface;
import zingmyorder.kitchen.mobile.AppConstants;
import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.base.BaseSchedulers;
import zingmyorder.kitchen.mobile.persistance.AppDataManager;
import zingmyorder.kitchen.mobile.persistance.AppPreferencesHelper;
import zingmyorder.kitchen.mobile.persistance.PreferenceInfo;
import zingmyorder.kitchen.mobile.persistance.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = {ViewModelModule.class})
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    BaseSchedulers provideScheduler() {
        return new SchedulerProvider();
    }

    @Provides
    @Singleton
    ApiInterface provideApi(Retrofit retrofit) {
        return retrofit.create(ApiInterface.class);
    }

    @Provides
    @Singleton
    AppDataManager provideAppDataManager(AppPreferencesHelper helper) {
        return new AppDataManager(helper);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(ApiInterface api, BaseSchedulers scheduler, AppDataManager appDataManager) {
        return new DataManager(api, scheduler, appDataManager);
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }
}

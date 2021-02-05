package zingmyorder.kitchen.mobile.di;


import zingmyorder.kitchen.mobile.view.RegistrationFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RegistrationModuleProvider {

    @ContributesAndroidInjector(modules = RegistrationModule.class)
    abstract RegistrationFragment provideLoginFragmentFactory();


}
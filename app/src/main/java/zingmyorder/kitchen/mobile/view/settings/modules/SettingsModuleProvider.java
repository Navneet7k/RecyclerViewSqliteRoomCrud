package zingmyorder.kitchen.mobile.view.settings.modules;

import zingmyorder.kitchen.mobile.view.settings.SettingsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SettingsModuleProvider {
    @ContributesAndroidInjector(modules = SettingsModule.class)
    abstract SettingsFragment provideSettingsFragment();
}

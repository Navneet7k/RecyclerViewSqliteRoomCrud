package zingmyorder.kitchen.mobile.view.settings.modules;

import androidx.lifecycle.ViewModelProvider;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.di.ViewModelProviderFactory;
import zingmyorder.kitchen.mobile.view.settings.SettingsFragmentVM;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsModule {
    @Provides
    SettingsFragmentVM settingsFragmentVM(DataManager dataManager) {
        return new SettingsFragmentVM(dataManager);
    }
    @Provides
    ViewModelProvider.Factory provideSettingsFragmentVM(SettingsFragmentVM settingsFragmentVM) {
        return new ViewModelProviderFactory<>(settingsFragmentVM);
    }
}

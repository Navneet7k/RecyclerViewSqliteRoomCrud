package zingmyorder.kitchen.mobile.di;

import androidx.lifecycle.ViewModelProvider;


import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.view.RegistrationViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class RegistrationModule {
    @Provides
    RegistrationViewModel registrationViewModel(DataManager dataManager) {
        return new RegistrationViewModel(dataManager);
    }
    @Provides
    ViewModelProvider.Factory provideRegistrationViewNodel(RegistrationViewModel registrationViewModel) {
        return new ViewModelProviderFactory<>(registrationViewModel);
    }

}

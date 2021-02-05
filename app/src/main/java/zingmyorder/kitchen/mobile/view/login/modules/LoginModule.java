package zingmyorder.kitchen.mobile.view.login.modules;

import androidx.lifecycle.ViewModelProvider;


import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.di.ViewModelProviderFactory;
import zingmyorder.kitchen.mobile.view.login.vm.LoginViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class LoginModule {
    @Provides
    LoginViewModel loginViewModel(DataManager dataManager) {
        return new LoginViewModel(dataManager);
    }
    @Provides
    ViewModelProvider.Factory provideLoginViewModel(LoginViewModel loginViewModel) {
        return new ViewModelProviderFactory<>(loginViewModel);
    }
}

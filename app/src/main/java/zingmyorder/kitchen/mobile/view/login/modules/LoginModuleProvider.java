package zingmyorder.kitchen.mobile.view.login.modules;

import zingmyorder.kitchen.mobile.view.login.LoginFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class LoginModuleProvider {
    @ContributesAndroidInjector(modules = LoginModule.class)
    abstract LoginFragment provideLoginFragmentFactory();
}

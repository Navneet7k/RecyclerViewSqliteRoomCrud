package zingmyorder.kitchen.mobile.di;


import zingmyorder.kitchen.mobile.view.login.LoginFragment;
import zingmyorder.kitchen.mobile.view.RegistrationFragment;
import zingmyorder.kitchen.mobile.view.login.modules.LoginModuleProvider;
import zingmyorder.kitchen.mobile.view.order_details.OrderDetailsFragment;
import zingmyorder.kitchen.mobile.view.order_details.modules.OrderDetailsModuleProvider;
import zingmyorder.kitchen.mobile.view.orders.OrdersListFragment;
import zingmyorder.kitchen.mobile.view.settings.SettingsFragment;
import zingmyorder.kitchen.mobile.view.settings.modules.SettingsModuleProvider;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {

    @ContributesAndroidInjector(modules = LoginModuleProvider.class)
    abstract LoginFragment contributeLoginFragment();

    @ContributesAndroidInjector(modules = RegistrationModuleProvider.class)
    abstract RegistrationFragment contributeRegistrationFragment();

    @ContributesAndroidInjector(modules = RegistrationModuleProvider.class)
    abstract OrdersListFragment contributeOrdersListFragment();

    @ContributesAndroidInjector(modules = OrderDetailsModuleProvider.class)
    abstract OrderDetailsFragment contributeOrderDetailsFragment();

    @ContributesAndroidInjector(modules = SettingsModuleProvider.class)
    abstract SettingsFragment contributeSettingsFragment();

}

package zingmyorder.kitchen.mobile.view.orders.modules;


import zingmyorder.kitchen.mobile.di.RegistrationModule;
import zingmyorder.kitchen.mobile.view.orders.OrdersListFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrdersListModuleProvider {
    @ContributesAndroidInjector(modules = RegistrationModule.class)
    abstract OrdersListFragment provideOrdersListFragmentFactory();
}

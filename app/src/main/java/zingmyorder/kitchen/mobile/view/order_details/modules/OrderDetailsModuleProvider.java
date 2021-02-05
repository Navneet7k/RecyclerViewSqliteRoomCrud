package zingmyorder.kitchen.mobile.view.order_details.modules;

import zingmyorder.kitchen.mobile.view.order_details.OrderDetailsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class OrderDetailsModuleProvider {
    @ContributesAndroidInjector(modules = OrderDetailsModule.class)
    abstract OrderDetailsFragment provideOrderDetailsFragment();
}

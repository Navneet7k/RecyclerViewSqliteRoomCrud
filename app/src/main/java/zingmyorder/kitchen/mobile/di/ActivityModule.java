package zingmyorder.kitchen.mobile.di;

import zingmyorder.kitchen.mobile.MainActivity;
import zingmyorder.kitchen.mobile.view.SignupAndSigninActivity;
import zingmyorder.kitchen.mobile.view.order_details.OrderDetailsActivity;
import zingmyorder.kitchen.mobile.view.orders.OrdersListActivity;
import zingmyorder.kitchen.mobile.view.settings.AppSettingsActivity;
import zingmyorder.kitchen.mobile.view.settings.DashboardSettingsActivity;
import zingmyorder.kitchen.mobile.view.settings.PrinterSettingsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract MainActivity contributeWelcomeActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract SignupAndSigninActivity contributeSignupActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract OrdersListActivity contributeOrdersListActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract OrderDetailsActivity contributeOrderDetailsActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract AppSettingsActivity contributeAppSettingsActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract PrinterSettingsActivity contributePrinterSettingsActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract DashboardSettingsActivity contributeDashboardSettingsActivity();

}

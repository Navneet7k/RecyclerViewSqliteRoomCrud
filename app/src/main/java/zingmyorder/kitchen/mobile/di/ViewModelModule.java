package zingmyorder.kitchen.mobile.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import zingmyorder.kitchen.mobile.view.login.vm.LoginViewModel;
import zingmyorder.kitchen.mobile.view.RegistrationViewModel;
import zingmyorder.kitchen.mobile.view.SignupViewModel;
import zingmyorder.kitchen.mobile.view.order_details.vm.OrderDetailsActivityVM;
import zingmyorder.kitchen.mobile.view.order_details.vm.OrderDetailsFragmentVM;
import zingmyorder.kitchen.mobile.view.orders.vm.OrdersListActivityVM;
import zingmyorder.kitchen.mobile.view.orders.vm.OrdersListVM;
import zingmyorder.kitchen.mobile.view.settings.AppSettingsActivityVM;
import zingmyorder.kitchen.mobile.view.settings.DashboardSettingsVM;
import zingmyorder.kitchen.mobile.view.settings.PrinterSettingsActivityVM;
import zingmyorder.kitchen.mobile.view.settings.SettingsFragmentVM;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@SuppressWarnings("WeakerAccess")
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SignupViewModel.class)
    abstract ViewModel bindSignupViewModel(SignupViewModel fragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationViewModel.class)
    abstract ViewModel bindregistrationViewModel(RegistrationViewModel fragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel.class)
    abstract ViewModel bindloginViewModel(LoginViewModel fragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersListVM.class)
    abstract ViewModel bindOrdersListVM(OrdersListVM fragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(OrdersListActivityVM.class)
    abstract ViewModel bindOrdersListActivityVM(OrdersListActivityVM ordersListActivityVM);

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailsFragmentVM.class)
    abstract ViewModel bindOrderDetailsFragmentVM(OrderDetailsFragmentVM orderDetailsFragmentVM);

    @Binds
    @IntoMap
    @ViewModelKey(SettingsFragmentVM.class)
    abstract ViewModel bindSettingsFragmentVM(SettingsFragmentVM settingsFragmentVM);

    @Binds
    @IntoMap
    @ViewModelKey(OrderDetailsActivityVM.class)
    abstract ViewModel bindOrderDetailsActivityVM(OrderDetailsActivityVM orderDetailsActivityVM);

    @Binds
    @IntoMap
    @ViewModelKey(AppSettingsActivityVM.class)
    abstract ViewModel bindAppSettingsActivityVM(AppSettingsActivityVM appSettingsActivityVM);

    @Binds
    @IntoMap
    @ViewModelKey(PrinterSettingsActivityVM.class)
    abstract ViewModel bindPrinterSettingsActivityVM(PrinterSettingsActivityVM printerSettingsActivityVM);

    @Binds
    @IntoMap
    @ViewModelKey(DashboardSettingsVM.class)
    abstract ViewModel bindDashboardSettingsVM(DashboardSettingsVM dashboardSettingsVM);


    @Binds
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);
}

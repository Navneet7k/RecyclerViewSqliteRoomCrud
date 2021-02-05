package zingmyorder.kitchen.mobile.view.orders.modules;

import androidx.lifecycle.ViewModelProvider;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.di.ViewModelProviderFactory;
import zingmyorder.kitchen.mobile.view.orders.vm.OrdersListVM;

import dagger.Module;
import dagger.Provides;

@Module
public class OrdersListModule {
    @Provides
    OrdersListVM ordersListVM(DataManager dataManager) {
        return new OrdersListVM(dataManager);
    }
    @Provides
    ViewModelProvider.Factory provideOrdersListVM(OrdersListVM ordersListVM) {
        return new ViewModelProviderFactory<>(ordersListVM);
    }
}

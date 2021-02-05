package zingmyorder.kitchen.mobile.view.order_details.modules;

import androidx.lifecycle.ViewModelProvider;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.di.ViewModelProviderFactory;
import zingmyorder.kitchen.mobile.view.order_details.vm.OrderDetailsFragmentVM;

import dagger.Module;
import dagger.Provides;

@Module
public class OrderDetailsModule {
    @Provides
    OrderDetailsFragmentVM orderDetailsFragmentVM(DataManager dataManager) {
        return new OrderDetailsFragmentVM(dataManager);
    }
    @Provides
    ViewModelProvider.Factory provideOrderDetailsFragmentVM(OrderDetailsFragmentVM orderDetailsFragmentVM) {
        return new ViewModelProviderFactory<>(orderDetailsFragmentVM);
    }
}
